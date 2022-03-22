/*Sections manager*/
package za.ac.tut.sectionmanager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import za.ac.tut.sectionthread.SectionThread;
 

public class SectionManager {
 // each section/pc will need to have its own thread
    List<SectionThread> sections = new ArrayList<>();
    public SectionManager(){
    
    }
    
    // the execute method will be called in the main class 
    public void execute(){
    ServerSocket s=null;
    Socket socket = null;
     BufferedReader in=null;
     SectionManager manager = new SectionManager(); 
    
    
 
    try{
    s = new ServerSocket(9191);
    while(true){
    socket = s.accept();
    in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
    // it receive the section name of each pc that is connected
    String connectedSection=in.readLine();
    
    
    System.out.println(connectedSection+" SECTION IS CONNECTED");
    
    // create a thread for each connected section
    SectionThread sectionThread = new SectionThread(connectedSection,socket,sections,manager);
     // store the created sectionThread to a list of sectionThread
     sections.add(sectionThread);
     
    // start the created section Thread 
     sectionThread.start();
    }
    }catch(Exception ex){
    System.out.println(ex);
    }
 
    }
    
    /*This method will be called in the sectionThread it helps to send the paid order
    to the collection side only nowhere else*/
    public void broadcastOrder(List<SectionThread>sections,String orderStr){
      // search a thread that has 'collection' as its section name  
      for(int i=0; i<sections.size();i++){
       //once it's found 
       if(sections.get(i).getSectionName().compareTo("collection")==0){
           //get the section thread at index and call a method from the thread class 
           sections.get(i).sendOrder(orderStr);
        }
      
      }
    
    }
}
