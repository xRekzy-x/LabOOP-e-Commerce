import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
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
            validateMobile(userMobile)&&
            User.getUser(userName)==null){
                String userId = userOp.generateUniqueUserId();
                String userRegisterTime = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new Date());
                Customer user = new Customer(userId,userName,userPassword,userRegisterTime,userEmail,userMobile);
                String userData = user.toString();
                try (BufferedReader reader = new BufferedReader(new FileReader("data/users.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("\"");
                        if (parts[7].trim().equals(userName)) {
                            return false;
                        }
                    }
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("data\\users.txt", true))) { 
                        writer.write(userData);  // Convert user object to string and write
                        writer.newLine();        // Add a new line after each user's data
                    } catch (IOException e) {
                        e.printStackTrace();
                    }   
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return true;
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
}
