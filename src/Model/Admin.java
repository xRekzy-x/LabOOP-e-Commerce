package Model;


public class Admin extends User {
    public Admin(String userId,String userName, String userPassword,String userRegisterTime){
        super(userId,userName,userPassword,userRegisterTime,"admin");
    }
    public Admin(){
        super();
    }
}
