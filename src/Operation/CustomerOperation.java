package Operation;
import java.util.List;

import Controller.DataControl;
import IOInterface.IOInterface;
import Model.Customer;
import Model.CustomerListResult;
import Model.User;


public class CustomerOperation {
    UserOperation userOp=UserOperation.getInstance();
    IOInterface io = IOInterface.getInstance();
    private static CustomerOperation instance;
    private CustomerOperation(){}
    public synchronized static CustomerOperation getInstance(){
        if(instance==null) instance = new CustomerOperation();
        return instance;
    }
    public boolean validateEmail(String userEmail){
        if(userEmail!=null&&userEmail.matches("^(\\w){1,}(@)[a-z]{2,}(\\.)[a-z]{2,}$")) return true;
        //\\w means any words(a-zA-Z0-9_)
        else return false;
    }
    public boolean validateMobile(String userMobile){
        if(userMobile!=null&&userMobile.matches("^0(3|4)[0-9]{8,}$")) return true;
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
                String userRegisterTime = DataControl.getCurrentTime();
                try{
                    Customer customer = new Customer(userId, userName, userOp.encryptPassword(userPassword), userRegisterTime, userEmail, userMobile);
                    DataControl.addLine("users",customer);
                }
                catch(IllegalArgumentException e){
                    io.printErrorMessage("registerCustomer", e.getMessage());
                }
               
                return true;
            }
        }
        return false;
    }
    public boolean updateProfile(String attributeName,String value,Customer customerObject){
        switch (attributeName) {
            case "userName":
                List<String>allUsers=DataControl.readAllUsersPart("name");
                if(allUsers.contains(value)) return false;
                else{
                    try{
                        customerObject.setUserName(value);
                        return true;
                    }catch(IllegalArgumentException e){
                        io.printErrorMessage("updateProfile", e.getMessage());
                        return false;
                    }
                }
            case "userPassword":
                    try{
                        customerObject.setUserPassword(value);
                        return true;
                    }catch(IllegalArgumentException e){
                        io.printErrorMessage("updateProfile", e.getMessage());
                        return false;
                    }
            case "userId":
                try{
                    customerObject.setUserID(value);
                    return true;
                }catch(IllegalArgumentException e){
                    io.printErrorMessage("updateProfile", e.getMessage());
                    return false;
                }
            case "userEmail":
                try{
                    customerObject.setUserEmail(value);
                    return true;
                }catch(IllegalArgumentException e){
                    io.printErrorMessage("updateProfile", e.getMessage());
                    return false;
                }
            case "userMobile":
                try{
                    customerObject.setUserMobile(value);
                    return true;
                }catch(IllegalArgumentException e){
                    io.printErrorMessage("updateProfile", e.getMessage());
                    return false;
                }
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
        List<User> allAdmins= DataControl.readAllUsers();
        for(User user:allAdmins){
            if(user.getUserRole()=="customer"){
                deleteCustomer(user.getUserID());
            }
        }
        //DataControl.deleteAll("users");
    }
}
