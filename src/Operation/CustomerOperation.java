package Operation;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Controller.DataControl;
import Model.Customer;
import Model.CustomerListResult;
import Model.User;

import java.text.SimpleDateFormat;

public class CustomerOperation {
    UserOperation userOp=UserOperation.getInstance();
    private static CustomerOperation instance;
    private CustomerOperation(){}
    public synchronized static CustomerOperation getInstance(){
        if(instance==null) instance = new CustomerOperation();
        return instance;
    }
    public boolean validateEmail(String userEmail){
        if(userEmail.matches("^(\\w){1,}(@)[a-z]{2,}(\\.)[a-z]{2,}$")) return true;
        //\\w means any words(a-zA-Z0-9_)
        else return false;
    }
    public boolean validateMobile(String userMobile){
        if(userMobile.matches("^0(3|4)[0-9]{8,}$")) return true;
        else return false;
    }
    public boolean registerCustomer(String userName,String userPassword,String userEmail,String userMobile){
        UserOperation userOp = UserOperation.getInstance();
        if(userOp.validateUsername(userName)&&
            userOp.validatePassword(userPassword)&&
            validateEmail(userEmail)&&
            validateMobile(userMobile)){
            if(DataControl.readAllUsersPart("name").contains(userName)){
                return false;
            }
            else{
                String userId=userOp.generateUniqueUserId();
                String userRegisterTime = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new Date());
                Customer customer = new Customer(userId, userName, userPassword, userRegisterTime, userEmail, userMobile);
                DataControl.addLine("users",customer);
                return true;
            }
        }
        return false;
    }
    public boolean updateProfile(String attributeName,String value,Customer customerObject){
        switch (attributeName) {
            case "userName":
                customerObject.setUserName(value);
                return true;
            case "userPassword":
                customerObject.setUserPassword(value);
                return true;
            case "userId":
                customerObject.setUserID(value);
                return true;
            case "userEmail":
                customerObject.setUserEmail(value);
                return true;
            case "userMobile":
                customerObject.setUserMobile(value);
                return true;
            default:
                break;
        }
        return false;
    }
    public boolean deleteCustomer (String customerId){
        return DataControl.deleteOneLine("users", customerId);
    }
    public CustomerListResult getCustomerList(int pageNumber) {
        CustomerListResult cusList = new CustomerListResult();
        List<User> allCus=  DataControl.readAllCustomers();
        int initialIndex = (pageNumber-1)*10;
        int endIndex = Math.min(initialIndex+10,allCus.size());
        cusList.getAllCustomersEachPage().clear();
        if(pageNumber>cusList.getTotalPages()) return null;
        for(int i = initialIndex;i<endIndex;i++){
            cusList.getAllCustomersEachPage().add(allCus.get(i));
        }
        return cusList;
    }
    public void deleteAllCustomer(){
        DataControl.deleteAll("users");
    }
}
