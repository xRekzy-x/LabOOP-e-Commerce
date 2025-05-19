package Operation;

import java.text.SimpleDateFormat;
import java.util.Date;

import Controller.DataControl;
import Model.Admin;

public class AdminOperation {
    private static AdminOperation instance;
    private AdminOperation(){}
    public static AdminOperation getInstance(){
        if(instance == null){
            instance = new AdminOperation();
        }
        return instance;
    }
public void registerAdmin(){
    UserOperation userOp = UserOperation.getInstance();
    String userRegisterTime = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new Date());
    Admin admin1 = new Admin("u_0000000001", "ADMIN", userOp.encryptPassword("dangtoicao123"), userRegisterTime);
    DataControl.addLine("users",admin1);
    // try (BufferedWriter writer = new BufferedWriter(new FileWriter("data\\users.txt", true))) { 
    //     writer.write(admin1.toString());  // Convert user object to string and write
    //     writer.newLine();        // Add a new line after each user's data     
    // }
    // catch(IOException e){
    //     e.printStackTrace();
    // }
    // UserOperation userOp = UserOperation.getInstance();
    // if(userOp.validateUsername(userName)&&
    //     userOp.validatePassword(userPassword)&&
    //     User.getUser(userName)==null){
    //         String userId = userOp.generateUniqueUserId();
    //         String userRegisterTime = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new Date());
    //         try (BufferedReader reader = new BufferedReader(new FileReader("data/users.txt"))) {
    //             String line;
    //             while ((line = reader.readLine()) != null) {
    //                 String[] parts = line.split("\"");
    //                 if (parts[7].trim().equals(userName)) {
    //                     return false;
    //                 }
    //             }
    //             Admin user = new Admin(userId,userName,userPassword,userRegisterTime);
    //             String userData = user.toString();
    //             try (BufferedWriter writer = new BufferedWriter(new FileWriter("data\\users.txt", true))) { 
    //                 writer.write(userData);  // Convert user object to string and write
    //                 writer.newLine();        // Add a new line after each user's data
    //             } catch (IOException e) {
    //                 e.printStackTrace();
    //             }   
    //         }
    //         catch(IOException e){
    //             e.printStackTrace();
    //         }
    //         return true;
    //     }
    //     return false; 
    // }
    }
}
