package Model;

import java.util.ArrayList;
import java.util.List;

import Controller.DataControl;

public class CustomerListResult {
    private static List<User> CustomerEachPage = new ArrayList<>();
    private int currentPage;
    private int totalPages;
    public CustomerListResult(){}
    public String toString(){
        StringBuilder content= new StringBuilder();
        for(int i =0;i<CustomerEachPage.size();i++){
            content.append("\n ("+(i+1)+") "+CustomerEachPage.get(i).toString());
        }
        content.append("\n"+getCurrentPage()+"/"+getTotalPages());
        return content.toString();
    }
    public static User getCustomer(String userName){
        for(User customer : CustomerEachPage){
            if(customer.getUserName().equals(userName))
                return customer;
        }//ITERATOR
        return null;
    }
    public static User getCustomerByID(String userId){
        for(User customer : CustomerEachPage){
            if(customer.getUserID().equals(userId))
                return customer;
        }//ITERATOR
        return null;
    }
    public List<User> getAllCustomersEachPage(){
        return CustomerEachPage;
    }
    public int getCurrentPage(){
        List<User> allCustomers = DataControl.readAllCustomers();
        for(int i =0;i<DataControl.readAllCustomers().size();i++){
            if((allCustomers.get(i).getUserName()).equals((CustomerEachPage.get(0).getUserName()))){
                return i/10+1;
            }
        }
        return 0;
    }
    public int getTotalPages(){
        int size = DataControl.readAllCustomers().size();
        if(size%10==0) return (size/10);
        else return (size/10+1);
    }
    
    
}
