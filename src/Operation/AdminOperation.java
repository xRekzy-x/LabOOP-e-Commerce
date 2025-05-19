package Operation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Controller.DataControl;
import Model.Admin;
import Model.User;

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
    
    List<User> allusers = DataControl.readAllUsers();
    long[] i = new long[allusers.size()];
    int j=0;
    int iden=0;
    for(User user: allusers){
        String[] userId = user.getUserID().split("_");
        i[j] = Long.parseLong(userId[1]);
        j++;
    }
    UserOperation userOp = UserOperation.getInstance();
    String userRegisterTime = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new Date());
    for(int k=0;k<j;k++){
        for(int g=0;g<j;g++){
            if(k==(i[g]-1)){
                break;
            }
            if(g==(j-1)){
                iden=k+1;
            }
        }
        if(iden==k+1) break;
        iden = j+1;
    }
    char text = (char)('a'+iden);
    Admin admin1 = new Admin(String.format("u_%010d", iden), "ADMIN"+text, userOp.encryptPassword("dangtoicao123"), userRegisterTime);
    DataControl.addLine("users",admin1);
    }
}
