import Controller.DataControl;
import Operation.*;
import javafx.application.Platform;

public class App {
    public static void main(String[] args) throws Exception {
        UserOperation userOp = UserOperation.getInstance();
        CustomerOperation cusOp = CustomerOperation.getInstance();
        AdminOperation adminOp = AdminOperation.getInstance();
        ProductOperation proOp = ProductOperation.getInstance();
        // //User.getAllUsers().clear();
        // Customer.getAllCustomers().clear();
        // Product.getAllProducts().clear();
        // proOp.deleteAllProducts();
        // cusOp.deleteAllCustomer();
        // adminOp.registerAdmin();

        // Customer user3 = new Customer("u_1234512345", "UserZero", "@!aA2",
        // "01-01-2000_00:00:00", "p1@kk.kk", "0303030301");

        // System.out.println( "user3"+cusOp.registerCustomer(user3.getUserName(),
        // user3.getUserPassword(), user3.getUserEmail(), user3.getUserMobile()));
        // System.out.println( cusOp.registerCustomer("userOne","@!aA2", "p1@kk.kk",
        // "0303030301"));
        // System.out.println( cusOp.registerCustomer("userTwo","@!aA2", "p2@kk.kk",
        // "0303030302"));
        // System.out.println( cusOp.registerCustomer("userThree","@!aA2", "p3@kk.kk",
        // "0303030304"));
        // System.out.println( cusOp.registerCustomer("userFour","@!aA2", "p4@kk.kk",
        // "0303030305"));
        // System.out.println( cusOp.registerCustomer("userFive","@!aA2", "p5@kk.kk",
        // "0303030306"));
        // System.out.println( cusOp.registerCustomer("userSix","@!aA2", "p6@kk.kk",
        // "0303030307"));
        // System.out.println( cusOp.registerCustomer("userSeven","@!aA2", "p7@kk.kk",
        // "0303030308"));
        // System.out.println( cusOp.registerCustomer("userEight","@!aA2", "p8@kk.kk",
        // "0303030309"));
        // System.out.println( cusOp.registerCustomer("userNine","@!aA2", "p9@kk.kk",
        // "0303030310"));
        // System.out.println( cusOp.registerCustomer("userTen","@!aA2", "p10@kk.kk",
        // "0303030311"));
        // System.out.println( cusOp.registerCustomer("userEleven","@!aA2", "p10@kk.kk",
        // "0303030311"));
        // System.out.println("yes");
        // //System.out.println(cusOp.deleteCustomer("u_1234512345"));
        // //System.out.println(cusOp.getCustomerList(2));
        // //System.out.println(admin1);
        // //System.out.println(Customer.getAllCustomers().size());

        // //proOp.extractProductsFromFile();
        // //cusOp.deleteAllCustomer();
        System.out.println("woo");
        // System.out.println(DataControl.ReadAllUsers());
        // System.out.println(DataControl.ReadAllUsersPart("password"));
        // System.out.println(DataControl.readUsersInRange(1, 3));
        // String result = String.join(" \n ", DataControl.readUsersInRange(1, 4));
        // System.out.println(result);
        // System.out.println("Next");
        // result = String.join(" \n ", DataControl.readCustomersInRange(1,4));
        // System.out.println(result);
        // System.out.println("Next");
        // result = String.join(" \n ", DataControl.readAllCustomers());
        // System.out.println(result);
        // System.out.println(DataControl.readAllUsersPart("mobile"));
        // User.clearData();
        // System.out.println(DataControl.readAllUsers());
        // System.out.println(DataControl.readAllUsersPart("id"));
        // DataControl.readAllUsers();
        // System.out.println(DataControl.readAllUsers().get(0));
        System.out.println(
                cusOp.registerCustomer("userMuoiBay", userOp.encryptPassword("kkk12A"), "kkk@kkk.kkk", "0312345678"));
        // cusOp.registerCustomer("userMuoiHai","kkk12A","kkk@kkk.kkk","0312345678");

        // Customer user3 = new Customer("u_1234512345", "UserZero", "@!aA2",
        // "01-01-2000_00:00:00", "p1@kk.kk", "0303030301");

        System.out.println(cusOp.deleteCustomer("u_3131157014"));
        // proOp.extractProductsFromFile();
        // System.out.println(proOp.getProductList(1));
        // System.out.println(cusOp.getCustomerList(1));
        // System.out.println(proOp.deleteProduct("p021"));
        System.out.println(proOp.getProductById("p006"));

        //proOp.generateCategoryFigure();
        // proOp.generateDiscountFigure();
        // proOp.generateLikesCountFigure();
        // proOp.generateDiscountLikesCountFigure();

    }
}
