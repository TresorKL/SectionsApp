// order class  
package za.ac.tut.order;
 
public class Order {
  int orderId;
  String dishName;
  int quantity;
  
  public Order(){}
  
  public Order(int orderId,String dishName,int quantity){
    this.orderId =orderId;
    this.dishName = dishName;
    this.quantity =quantity;
  }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
   
   public double determineAmountDue(){
     
       double amountDue=0;
       
       if(getDishName().compareTo("DISH A")==0){
           
            amountDue= getQuantity()*80;
       }else if(getDishName().compareTo("DISH B")==0){
           
            amountDue= getQuantity()*50;
       }else if(getDishName().compareTo("DISH C")==0){
           
             amountDue= getQuantity()*100;
       }
        return amountDue;
   }
  
}
