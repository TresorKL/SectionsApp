// SectionThread class 
package za.ac.tut.sectionthread;
 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import za.ac.tut.order.Order;
import za.ac.tut.sectionmanager.SectionManager;

public class SectionThread extends Thread{

Socket socket;
String sectionName;
List<SectionThread>sections;
SectionManager manager = new SectionManager();
   // this thread has sectionName, socket, sections list and the section manager as parameters
   public SectionThread(String sectionName,Socket socket,List<SectionThread>sections,SectionManager manager){
       this.sectionName = sectionName;
       this.socket = socket;
       this.sections =sections;
       this.manager=manager;
   }
  // this method will help when seaching the the collection section in the list of the sections
    public String getSectionName() {
        return sectionName;
    }
   
   
  //this method help to display the amount due and ask for a correct payment  
  public void makePayement(Socket socket,LinkedList<Order>queue){
      /*because of the FIFO logic we first serve the first order in queue of orders 
       that why we get it amount due here*/
      double amountDue = queue.get(0).determineAmountDue();
      int payment;
      boolean isValid=true;
      double change=0;
      // payment process
      do{
        payment=Integer.parseInt(JOptionPane.showInputDialog(null,"THE AMOUNT DUE IS R"+amountDue+"\n"+ "PLEASE MAKE PAYMENT"));
        
        if(payment >= amountDue){
            isValid = true;
           change = payment - amountDue;
           JOptionPane.showMessageDialog(null,"YOUR CHANGE IS R"+change);
        }else{
         isValid = false;
         JOptionPane.showMessageDialog(null,"INVALID PAYMENT");
        }
      
      }while(!isValid); 
      
   }
 // once an order is correctly paid this method method will help to send that order to collection section 
  public void sendOrderTOTheCollectionSection(Socket socket,LinkedList<Order>queue,SectionManager manager){
   //get the order respecting the FIFO logic
   Order order = queue.get(0);
   
   // Order data that will be sent as String to the collection side/section
   String orderStr= order.getOrderId()+"-"+order.getDishName()+"-"+order.getQuantity()+"-"+order.determineAmountDue();
    
   //BoadcastOrder to the collection
   manager.broadcastOrder(sections, orderStr);
    
   // revome the order from the queue of orders after it's sent to the collection section
   queue.remove(order);
   
  }
  
  // this method is called in the boadcast method of sections manager class
  public void sendOrder( String orderStr){
   PrintWriter out = null;
   try{
         out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
         // send order
         out.println(orderStr);
         System.out.println("sent for collection...");
          
       }catch(Exception ex){
          System.out.println(ex);
       }
  
  }
  // this method receive new orders and put them in the queue of orders
  public void addNewOrderToTheQueue(Socket socket,LinkedList<Order>queue){
    BufferedReader in=null;
    try{
          in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
       // receive and split the order
         String[] orderToken =in.readLine().split("#");
         
         // get correct fields for the order object that is goig to be create
         int orderId = Integer.parseInt(orderToken[0]);
         String dishName = orderToken[1];
         int quantity = Integer.parseInt(orderToken[2]);
         
         // instanciate the order object
         Order order = new Order(orderId,dishName,quantity);
         
         // store the order to the queue of orders
         queue.add(order);
    }catch(Exception ex){
        System.out.println(ex);
    }
  }
  // menu of the payement section
  public int menu(){
     int option=0;
     option =Integer.parseInt(JOptionPane.showInputDialog(null,"1: LET CUSTOMER PAY"+"\n"+"2 WAIT & RECEIVE NEW ORDER"+"\n"+"3 CLOSE THE PROGRAM",
                                                               "PAYMENT SECTION",JOptionPane.INFORMATION_MESSAGE));
     
     return option;
  }
  
  public void run(){
      int option =0;
      BufferedReader in=null;
       LinkedList<Order>queue= new LinkedList();
    
   while(true){
       //receive new order
       addNewOrderToTheQueue(socket, queue);
       try{
         // option from the menu
         option = menu();
         while(option!=3){
            // if option =1 this means user want to make the payment 
            if(option==1){
            // paument
            makePayement( socket,queue);
            // sending order for collection
            sendOrderTOTheCollectionSection(socket,queue,manager);
            /*if option =2 this means the user who is managing the payment section
            wants to wait and receive new orders*/
            }else if(option==2){
            addNewOrderToTheQueue(socket, queue);
            }
            
             option = menu();
            
        }   
       }catch(Exception ex){
          System.out.println(ex);
       }
   }
  }
}
