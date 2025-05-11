public class Customer extends User {
    String userEmail;
    String userMobile;
    CustomerOperation cusOp = CustomerOperation.getInstance();
    public Customer(String userId,String userName, String userPassword,String userRegisterTime,String userEmail,String userMobile){
        super(userId,userName,userPassword,userRegisterTime,"customer");
        this.userEmail=userEmail;
        this.userMobile=userMobile;
    }
    public Customer(){
        super();
        this.userEmail="default@gmail.com";
        this.userMobile="911";
    }
    public String getUserEmail(){return userEmail;}
    public String getUserMobile(){return userMobile;}
    public void setUserEmail(String userEmail){
        if(!cusOp.validateEmail(userEmail)) throw new IllegalArgumentException(". An email address\r\n" + //
                        " * consists of username@domain.extension format");
        this.userEmail=userEmail;
    }
    public void setUserMobile(String userMobile){
        if(!cusOp.validateMobile(userMobile)) throw new IllegalArgumentException(" The mobile number\r\n" + //
                        " * should be exactly 10 digits long, consisting only of numbers,\r\n" + //
                        " * and starting with either '04' or '03'.");
        this.userMobile=userMobile;
    }
    public String toString(){
        return super.toString()+",\"user email\":\""+userEmail+"\",\"user_mobile\":\""+userMobile+"\"}";
    }
}
