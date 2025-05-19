package Model;
import java.util.HashMap;
import java.util.Map;

import Controller.DataControl;
import Operation.UserOperation;

public abstract class User{
    private String userId;
    private String userName;
    private String userPassword;
    private String userRegisterTime;
    private String userRole;
    UserOperation userOp = UserOperation.getInstance();
    private static final Map<String, User> registry = new HashMap<>();
    public User(String userId,String userName, String userPassword,String userRegisterTime,String userRole){
        if (userId == null || !userId.matches("^u_\\d{10}$")) {
            throw new IllegalArgumentException("Invalid userId format. Must be 'u_' followed by exactly 10 digits.");
        }
        if (registry.containsKey(userId)) {
            throw new IllegalArgumentException("UserId must be unique. Duplicate found: " + userId);
        }
        //existingUserIds.add(userId);
        //^ bắt đầu chuỗi (ko cho ký tự nào đứng trước nó)
        //u_ phải có u_
        // \\ là 1 \
        // d{10} phải có chính xác 10 digits
        // $ kết thúc chuỗi, không được có kí tự nào đứng sau
        this.userId=userId;
        if(userOp.checkUsernameExist(userName)) {
            throw new IllegalArgumentException("Username is already existed");
        }
        if(!userOp.validateUsername(userName)){
            throw new IllegalArgumentException(" The name should only contain letters or\r\n" + //
                                " * underscores, and its length should be at least 5 characters.");
        }
        this.userName=userName;
        if(!userOp.validatePassword(userOp.encryptPassword(userPassword))){
            throw new IllegalArgumentException(" The password should contain at least\r\n" + //
                                " * one letter (uppercase or lowercase) and one number. The length \r\n" + //
                                "* must be greater than or equal to 5 characters");
        }
        this.userPassword=userPassword;
        if (userRegisterTime == null || !userRegisterTime.matches("^([0-2][0-9]|3[01])-(0[1-9]|1[0-2])-\\d{4}_([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$"))
            throw new IllegalArgumentException("Invalid userRegisterTime format. Must be DD-MM-YYYY_HH:MM:SS");
        this.userRegisterTime=userRegisterTime;
        if(!userRole.equals("customer")&&!userRole.equals("admin")){
            throw new IllegalArgumentException("Invalid user role");
        }
        this.userRole=userRole;
        registry.put(userId, this);
    }
    public User(){
        this.userRole = "customer";
        this.userId = "u_0123456789";
        this.userName = "Default User";
        this.userPassword = "default";
        this.userRegisterTime = "01-01-2000_00:00:00";
    }
    public String getUserID(){return userId;}
    public String getUserName(){return userName;}
    public String getUserPassword(){return userPassword;}
    public String getUserRegisterTime(){return userRegisterTime;}
    public String getUserRole(){return userRole;}
    public void setUserName(String userName){
        if(!userOp.validateUsername(userName)) throw new IllegalArgumentException("The name should only contain letters or\r\n" + //
                        " * underscores, and its length should be at least 5 characters");
        this.userName=userName;
    }
    public void setUserPassword(String userPassword){
        if(!userOp.validatePassword(userPassword)) throw new IllegalArgumentException("The password should contain at least\r\n" + //
                        " * one letter (uppercase or lowercase) and one number. The length \r\n" + //
                        "* must be greater than or equal to 5 characters.");
        this.userPassword=userPassword;
    }
    public void setUserID(String userId){
        if (userId == null || !userId.matches("^u_\\d{10}$")) {
            throw new IllegalArgumentException("Invalid userId format. Must be 'u_' followed by exactly 10 digits.");
        }
        if (DataControl.readAllUsersPart("id").contains(userId)) {
            throw new IllegalArgumentException("UserId must be unique. Duplicate found: " + userId);
        }
        this.userId=userId;
    }
    public static void clearData(){
        registry.clear();
    }
    public static Map<String,User> getRegistry(){
        return registry;
    }
    public static User getRegistryByName(String userName){
        for(User user: registry.values()){
            if(user.getUserName().equals(userName)) return user;
        }
        return null;
    }
    public static User getRegistryByID(String userID){
        return registry.get(userID);
    }

    // public void setUserID(String userId){
    //     if (userId == null || !userId.matches("^u_\\d{10}$")) 
    //         throw new IllegalArgumentException("Invalid userId format. Must be 'u_' followed by exactly 10 digits.");
    //     this.userId=userId;
    // }
    // public void setUserName(String userName){this.userName=userName;}
    // public void setUserPassword(String userPassword){this.userPassword=userPassword;}
    // public void setUserRegisterTime(String userRegisterTime){
    //     if (userRegisterTime == null || !userRegisterTime.matches("^([0-2][0-9]|3[01])-(0[1-9]|1[0-2])-\\d{4}_([01][0-9]|2[0-3]):([0-5][0-9]:([0-5][0-9])$"))
    //         throw new IllegalArgumentException("Invalid userRegisterTime format. Must be DD-MM-YYYY_HH:MM:SS");
    //     this.userRegisterTime=userRegisterTime;
    // }
    // public void saveUser(){
    //     String userData = this.toString();
    //     try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\rosto\\practice\\tutorial\\loopex\\javalab\\e_commerce\\data\\users.txt", true))) {  // true for appending data
    //             writer.write(userData);  
    //             writer.newLine();             
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
    // public void extractProductsFromFile(){
    //     String content=null;
    //    // Gson gson = new Gson();
    //     String line;
        
    //      try(BufferedReader reader =  new BufferedReader(new FileReader("data/kkk.txt"))){
    //         while ((line = reader.readLine()) != null) {
    //         String[] parts = line.split("\"");
    //         String proId = parts[3].trim();
    //         String proModel=parts[7].trim();
    //         String proCategory=parts[11].trim();
    //         String proName = parts[15].trim();
           
    //         content = parts[18].trim().replaceAll(":", "");
    //         content = content.replaceAll(",","");
    //         Double proCurrentPrice = Double.parseDouble(content);
            
    //         //String proRawPrice = parts[20].replaceAll(":", "");
    //         content = parts[20].trim().replaceAll(":", "");
    //         content = content.replaceAll(",","");
    //         Double proRawPrice = Double.parseDouble(content);

    //         //String proDiscount = parts[22].replaceAll(":", "");
    //         content = parts[22].trim().replaceAll(":", "");
    //         content = content.replaceAll(",","");
    //         Double proDiscount = Double.parseDouble(content);
    //        // String proLikesCount = parts[24].replaceAll(":", "");
    //         content = parts[24].trim().replaceAll(":", "");
    //         content = content.replaceAll("}","");
    //         int proLikesCount = Integer.parseInt(content);
            
    //         Product product = new Product(proId, proModel, proCategory, proName, proCurrentPrice, proRawPrice, proDiscount, proLikesCount);
    //         }
          
    //     }
    //     catch(IOException e){
    //         e.printStackTrace();
    //     }
    // }
    public String toString(){
        return "{\"user_id\":\""+userId+"\",\"user_name\":\""+userName+"\",\"user_password\":\""+userPassword+"\",\"user_register_time\":\""+userRegisterTime+"\",\"user_role\":\""+userRole+"\"";
    }
}