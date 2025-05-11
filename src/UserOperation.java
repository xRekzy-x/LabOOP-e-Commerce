import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
public class UserOperation {
    private static UserOperation instance;
    Random random = new Random();
    private UserOperation(){

    }
    public synchronized static UserOperation getInstance(){
        if(instance==null){
            instance= new UserOperation();
        }
        return instance;
    }
    public String generateUniqueUserId(){
        String generatedId;
        do {
            long randomNum = random.nextLong(10000000000L);
            generatedId = String.format("u_%010d", randomNum);
        } while (User.getUserByID(generatedId)!=null);
        return generatedId;
    }
    public String encryptPassword(String password){
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder(password.length()*3+4);//StringBuilder is a method which allows us to modify the string easily
        for(int i=0;i<password.length()*2;i++){
            int index = random.nextInt(CHARACTERS.length());
            randomString.append(CHARACTERS.charAt(index));//charat: a character at position index... append ==> add 1 more character at the end
        }
        int index=2;
        for(int i=0;i<password.length();i++){
            randomString.insert(index,password.charAt(i));//character sẽ được insert ở sau index đó
            index+=3;//cộng 2 cộng thêm cả ký tự mới thêm vô là 3
        }
        randomString.insert(0,"^^");
        randomString.append("$$");    
        return randomString.toString();
    }
    public String decryptPassword(String encrypyedPassword){
        StringBuilder password = new StringBuilder((encrypyedPassword.length()-4)/3);
        int index=4;
        for(int i=0;i<(encrypyedPassword.length()-4)/3;i++){
            password.append(encrypyedPassword.charAt(index));
            index+=3;
        }
        return password.toString();
    }
    public boolean checkUsernameExist(String userName) {
        if(User.getUser(userName)!=null) return true;
        else return false;
    }
    public boolean validateUsername(String userName) {
        if(!userName.matches("^[a-zA-Z_]{5,}$")) return false;//so sánh 5 ký tự xem có a-zA-Z_ ko
        else return true;
    }
    public boolean validatePassword(String userPassword) {
        if(!userPassword.matches("(?=.*[a-zA-Z])(?=.*[0-9]).{5,}")) return false;
        //có ^ và $ để kiểm tra toàn bộ chuỗi
        //.* để kiểm tra toàn bộ kí tự ở đằng sau
        //?= lookahead assertion
        else return true;
    }
    public User login(String userName, String userPassword){
        User user = User.getUser(userName);
        if(user.getUserPassword().equals(userPassword))
            return user;
        return null;
    }
   
}
