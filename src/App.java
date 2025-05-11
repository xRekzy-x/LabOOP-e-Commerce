public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Customer user = new Customer();
        Customer user2 = new Customer();
        Customer user3 = new Customer("u_1234512345", "xrekzy", "123456", "01-01-2000_00:00:00", "222@gmai.com", "000202");
        Customer user4 = new Customer("u_1234512344", "xrekzy", "123456", "01-01-2000_00:00:00", "222@gmai.com", "000202");    
        System.out.println(user);
        System.out.println(user2);
        // user.saveUser();
        UserOperation userOp= UserOperation.getInstance();
        CustomerOperation cusOp= CustomerOperation.getInstance();

        System.out.println(userOp.generateUniqueUserId());
        System.out.println(userOp.encryptPassword("ANH_YEU_EM"));
        System.out.println(userOp.decryptPassword(userOp.encryptPassword("ANH_YEU_EM")));
        //System.out.println(userOp.login("xrekzy", "123456"));
        System.out.println(user3);
        System.out.println(user4);
        System.out.println( cusOp.registerCustomer("_xRekzy","@!aA2", "kk@kk.kk", "0303030303"));
        // System.out.println(userOp.checkUsernameExist("xrekzy"));
        // System.out.println(userOp.validatePassword("@@@@@!aA2"));
        // System.out.println(userOp.validateUsername("aAaa_"));
        // System.out.println(cusOp.validateEmail("2@ma.mbn"));
        // System.out.println(cusOp.validateMobile("0123456787"));
        System.out.println(cusOp.updateProfile("userEmail", "k22k@kfas.com", user4));
        System.out.println(user4);
    }
}
