package semestertest4order;
 
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JOptionPane;

public class SemesterTest4Order {
   
  // an order has an order number or order id so I decided to randomly generate the order id 
    public static int generateRandomOrderId(){
       int orderId=0;
       int max = 999;
       int min= 100;
       int temp = max-min;
        
       orderId= (int)(Math.random()*temp)+min;
       
       return orderId;
    }
   // this method will accept order description from user and send to the server side  
    public static void makeOrder(Socket socket){
       PrintWriter out=null;
       String order="";
       String dishName="";
       int orderId= generateRandomOrderId();
       
       // input dish choice
      int dishChoice = Integer.parseInt(JOptionPane.showInputDialog(null,"1: DISH A R80"+"\n"+"2: DISH B R50"+"\n"+"3: DISH C R100"));
     // input quntity 
     int quantity = Integer.parseInt(JOptionPane.showInputDialog(null,"PLEASE ENTER QUANTITY"));
       
       // assigning the correct dishName according to the dishChoice
       if(dishChoice==1){
           dishName="DISH A";
       }else if(dishChoice==2){
           dishName="DISH B";
       }else if(dishChoice==3){
           dishName="DISH C";
       }
       // arranging order description that will be sent to the payment side
       order= orderId+"#"+dishName+"#"+quantity;
       
       try{
         out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
         // send order
         out.println(order);
         
         // comfirmation message
         JOptionPane.showMessageDialog(null,"ORDER SUCCESSFULLY MADE"+"\n"+"YOUR ORDER No IS"+orderId);
       }catch(Exception ex){
          System.out.println(ex);
       }
    }
    
    public static int menu(){
    int option = 0;
    
    option = Integer.parseInt(JOptionPane.showInputDialog(null,"1: MAKE ORDER "+"\n"+"2 CLOSE THE SYSTEM","ORDER SECTION",JOptionPane.INFORMATION_MESSAGE));
    
    return option;
    }
 
    public static void main(String[] args) {
       int option=0;
       InetAddress adds = null;
       Socket socket = null;
       PrintWriter out=null;
       /*thesection name: will be sent to sever so that the server can be able to 
         identify the role of each section/client PC taht is connected */
       String sectionName="order";
       
    try{
        adds=InetAddress.getByName("localhost");
        socket = new Socket(adds,9191);
       out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
       // send the section
       out.println(sectionName);
       
       option=menu();
        while(option!=2){
            if(option ==1){
            makeOrder(socket);
        }
          option=menu();
        }
       
     }catch(Exception ex){
       System.out.println(ex);
     }
    }
    
}
