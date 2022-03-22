// COLLECTION SECTION MAIN CLASS 
package semestertest4collection;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import za.ac.tut.collectionlistener.CollectionListener;

 
public class SemesterTest4Collection {

    public static void main(String[] args) {
         PrintWriter out =null;
         InetAddress adds=null;
         Socket socket=null;
         // this section's name
         String sectionName="collection";
   
         try{
           adds= InetAddress.getByName("localhost");
           socket = new Socket(adds,9191);
            
           out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
           // first send its name
           out.println(sectionName);
           
           // start Collection Listener thread
            new CollectionListener(socket).start();;
         }catch(Exception ex){
           System.out.println(ex);
         }
         
    }
    
}
