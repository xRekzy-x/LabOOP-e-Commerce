public class User{
    private static User instance;
    private User(){}
    public static synchronized User getInstance(){
        if(instance==null) instance = new User(); 
        return instance;
    }
}