// Collection listener is a thread that WIL be listening to new orders that are sent for collection 
package za.ac.tut.collectionlistener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
 
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

public class CollectionListener extends Thread {

    Socket socket;

    public CollectionListener(Socket socket) {
        this.socket = socket;
    }

     
    //generate current time
    public String getCurrentTime(){
     LocalDateTime now = null;
     //setting the format
     DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
     
     now = LocalDateTime.now();
      
     return dtf.format(now);
    }

    //Store orders to the database
    public void storeOrderToTheDabase(String[] orderStr) {

        String URL = "jdbc:mysql://localhost:3306/order_db";
        String USER = "root";
        String PASSWORD = "87654321";
      try {
        // get connection to the database
            Connection connect = DriverManager.getConnection(URL, USER, PASSWORD);
        // get values of an order
        int orderId = Integer.parseInt(orderStr[0]);
        String dishName = orderStr[1];
        int quantity = Integer.parseInt(orderStr[2]);
        double amountDue = Double.parseDouble(orderStr[3]);
        //the cuurent timestamp will be stired as String 
        String timestamp= getCurrentTime();
           

            // query to be executed
            String sql = "insert into orders_tbl values(?,?,?,?,?)";

            PreparedStatement insert = connect.prepareStatement(sql);
           
            insert.setInt(1, orderId);
         
            insert.setString(2, dishName);
            
            insert.setInt(3, quantity);
           
            insert.setDouble(4, amountDue);
            
            insert.setString(5, timestamp);
           
             //update
            insert.executeUpdate();
            
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void displayOrder(String[] orderStr) {

        JOptionPane.showMessageDialog(null, "ORDER ID: " + orderStr[0] + "\n\n" + "DISH NAME: " + orderStr[1] + "\n"
                + "QUANTITY: " + orderStr[2] + "\n" + "TOT AMOUNT DUE: " + orderStr[3] + "\n\n"
                + "TIMESTEMP: " +getCurrentTime(), "COLLECTION SECTION", JOptionPane.INFORMATION_MESSAGE);
    }

    public void run() {
        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception ex) {
            System.out.println(ex);
        }

        while (!socket.isClosed()) {

            try {

                String[] orderStr = in.readLine().split("-");
              
               
                displayOrder(orderStr);
                  storeOrderToTheDabase(orderStr);
            } catch (Exception ex) {
                System.out.println(ex);
            }

        }

    }
}
