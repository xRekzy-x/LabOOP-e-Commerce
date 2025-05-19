package Model;

import java.util.ArrayList;
import java.util.List;

import Controller.DataControl;

public class OrderListResult {
    private List<Order> OrderEachPage = new ArrayList<>();
    private String customerId;
    public OrderListResult(String customerId){
        this.customerId=customerId;
    }
    public String toString(){
        StringBuilder content= new StringBuilder();
        for(int i =0;i<OrderEachPage.size();i++){
            content.append("\n ("+(i+1)+") "+OrderEachPage.get(i).toString());
        }
        //content.append("\n"+getCurrentPage()+"/"+getTotalPages());
        return content.toString();
    }
    public Order getOrder(String orderId){
        for(Order order : OrderEachPage){
            if(order.getOrderID().equals(orderId))
                return order;
        }//ITERATOR
        return null;
    }
    public List<Order> getAllOrdersEachPage(){
        return OrderEachPage;
    }
    public int getCurrentPage(){
        List<Order> allOrders = DataControl.readAllOrdersOfACustomer(customerId);
        for(int i =0;i<allOrders.size();i++){
            if((allOrders.get(i).getOrderID()).equals((OrderEachPage.get(0).getOrderID()))){
                return i/10+1;
            }
        }
        return 0;
    }
    public int getTotalPages(){
        int size = DataControl.readAllOrdersOfACustomer(customerId).size();
        if(size%10==0) return (size/10);
        else return (size/10+1);
    }
}
