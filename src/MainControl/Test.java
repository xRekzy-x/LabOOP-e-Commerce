package MainControl;
import java.util.List;
import Controller.DataControl;
import IOInterface.IOInterface;
import Model.Admin;
import Model.Customer;
import Model.CustomerListResult;
import Model.Order;
import Model.OrderListResult;
import Model.Product;
import Model.ProductListResult;
import Model.User;
import Operation.*;

public class Test  {
    public static void main(String[] args) {
        new Thread(() -> AllStatiscalFigure.launch()).start();
        DataControl.sleep(1);
        DataControl.readAllUsers();
        DataControl.readAllOrders();
        mainInterface();
        }
        
    
    private static void mainInterface(){
        CustomerOperation cusOp = CustomerOperation.getInstance();
        UserOperation userOp = UserOperation.getInstance();
        IOInterface io = IOInterface.getInstance();
        io.mainMenu();
        String[] inputMainMenu=io.getUserInput("Please choose from 1 to 3", 1);
        switch (inputMainMenu[0]) {
            case "1":
                User user=null;
                while(user==null){
                    String [] informationLog = io.getUserInput("Please enter username and password", 2);
                    user = userOp.login(informationLog[0], informationLog[1]);
                    if(user==null) System.out.println("wrong name or password!");
                    else io.printMessage("login successfully! Welcome,"+user.getUserName());
                    if(user!=null&&user.getUserRole().equals("admin")){
                        adminInterface((Admin) user);
                    }
                    if(user!=null&&user.getUserRole().equals("customer")){
                        customerInterface((Customer) user);
                    }
                }
                break;
            case "2":
                boolean isReg=false;
                String [] informationReg = io.getUserInput("Please enter username,password,email,mobile", 4);
                isReg=cusOp.registerCustomer(informationReg[0], informationReg[1], informationReg[2], informationReg[3]);
                if(isReg){
                    customerInterface((Customer)DataControl.readAllCustomers().get(DataControl.readAllCustomers().size()-1));
                    io.printMessage("Your account is now available!");
                }
                else{
                    io.printMessage("fail to register");
                    mainInterface();
                }
                break;
            case "3":
                io.printMessage("bye");
                System.exit(0);
        }
    }
    private static void customerInterface(Customer user){
        DataControl.readAllUsers();
        DataControl.readAllOrders();
        IOInterface io = IOInterface.getInstance();
        CustomerOperation cusOp = CustomerOperation.getInstance();
        UserOperation userOp = UserOperation.getInstance();
        OrderOperation orderOp = OrderOperation.getInstance();
        io.customerMenu();
        String[] inputCustomerMenu = io.getUserInput("choose from 1 to 6", 2);
        if (inputCustomerMenu[0].equals("3") &&("keyword".equals(inputCustomerMenu[1]) || inputCustomerMenu[1] == null)) {
            showProduct(user);
        }
        switch (inputCustomerMenu[0]) {
            case "1":
                List<User> allCus = DataControl.readAllCustomers();
                for(User cus:allCus){
                    if(cus.getUserName().equals(user.getUserName())){ 
                        io.printMessage("=======================");
                        io.printMessage(cus.toString());
                        io.printMessage("=======================");
                    }
                }
                DataControl.sleep(2);
                customerInterface(user);
                break;
            case "2":
                boolean isChanged=false;
                String[] choice2_5 = new String[1];
                choice2_5[0] = "n";
                while(choice2_5[0].equalsIgnoreCase("n")){
                    String[] choice2 = io.getUserInput("(1) Update username\n"+
                                                        "(2) Update password\n"+
                                                        "(3) Update email\n"+
                                                        "(4) Update mobile", 1);
                    switch (choice2[0]) {
                        case "1":
                            String[] choice2_1 = io.getUserInput("Enter new name: ", 1);
                            isChanged=cusOp.updateProfile("userName", choice2_1[0], user);
                            if(isChanged) io.printMessage("Successfully changed your name to "+user.getUserName());
                            else io.printMessage("Fail! Your name is duplicated or invalid");
                            break;
                        case "2":
                            String[] choice2_2 = io.getUserInput("Enter new password: ", 1);
                            isChanged=cusOp.updateProfile("userPassword", choice2_2[0], user);
                            if(isChanged){
                                io.printMessage("Successfully changed your password to "+user.getUserPassword());
                                user.setUserPassword(userOp.encryptPassword(choice2_2[0]));
                            } 
                            else io.printMessage("Fail! Your password is invalid");
                            break;
                        case "3":
                            String[] choice2_3 = io.getUserInput("Enter new email: ", 1);
                            isChanged=cusOp.updateProfile("userEmail", choice2_3[0], user);
                            if(isChanged) io.printMessage("Successfully changed your email to "+user.getUserEmail());
                            else io.printMessage("Fail! Your email is invalid");
                            break;
                        case "4":
                            String[] choice2_4 = io.getUserInput("Enter new mobile: ", 1);
                            isChanged=cusOp.updateProfile("userMobile", choice2_4[0], user);
                            if(isChanged) io.printMessage("Successfully changed your mobile to "+user.getUserMobile());
                            else io.printMessage("Fail! Your mobile is invalid");
                            break;
                        default:
                            break;
                        }
                    cusOp.deleteCustomer(user.getUserID());
                    DataControl.addLine("users", user.toString());
                    choice2_5 = io.getUserInput("Press 'n' for a next update,'b' to go back", 1);
                    if(choice2_5[0].equalsIgnoreCase("b")) customerInterface(user);
                }
                break;
            case "4"://show history order
                int pageNum=1;
                OrderListResult orderList;
                String[] choice4 = new String[1];
                choice4[0]="n";
                do{
                    orderList = orderOp.getOrderList(user.getUserID(),pageNum);
                    if(orderList==null){
                        io.printMessage("====The list is empty!====");
                        customerInterface(user);
                    }
                    else{
                        List<Order> showOrder = orderList.getAllOrdersEachPage();
                        if(choice4[0].equalsIgnoreCase("n")||choice4[0].equalsIgnoreCase("p"))
                            io.showList("admin", "order", showOrder, orderList.getCurrentPage(), orderList.getTotalPages());
                    }
                    choice4 = io.getUserInput("Enter 'n' for next page, 'p' for previous page, 'b' to go back", 1);
                    pageNum=afterGeneration(pageNum, orderList.getTotalPages(),choice4[0],user);
                }while(!choice4[0].equalsIgnoreCase("b"));
                break;
            case "5":
                orderOp.generateAllCustomersConsumptionFigure();
                orderOp.generateSingleCustomerConsumptionFigure(user.getUserID());
                orderOp.generateAllTop10BestSellersFigure();
                io.printMessage("sucessfully generated 3 charts");
                DataControl.sleep(2);
                customerInterface(user);
                break;
            case "6":
                mainInterface();
                break;
            default:
                break;
        }
    }
    
    private static void adminInterface(Admin admin){
        DataControl.readAllUsers();
        DataControl.readAllOrders();
        IOInterface io = IOInterface.getInstance();
        CustomerOperation cusOp = CustomerOperation.getInstance();
        OrderOperation orderOp = OrderOperation.getInstance();
        ProductOperation proOp = ProductOperation.getInstance(); 
        int pageNum=1;
        io.adminMenu();
        String[] inputAdminMenu = io.getUserInput("choose from 1 to 8", 1);
        switch(inputAdminMenu[0]){
            case "1":
                showProduct(admin);
                break;
            case "2"://add customer
                boolean isSuccess= false;
                String[] choice2 = new String[1];
                choice2[0]="n";
                while(isSuccess==false){
                    if(choice2[0].equalsIgnoreCase("n")){
                        String[] newCus = io.getUserInput("Enter customer's name,password,email and mobile respectively:\n(Enter b to go back) ", 4);
                        isSuccess=cusOp.registerCustomer(newCus[0], newCus[1], newCus[2], newCus[3]);
                        if(isSuccess==false){
                            if(!newCus[0].equalsIgnoreCase("b")) io.printErrorMessage("addCustomer", "cannot add customer because of invalid information");
                            choice2[0]=newCus[0];
                        }
                        else{
                            List<String> allNames = DataControl.readAllUsersPart("name");
                            io.printMessage("Successfully added "+allNames.get(allNames.size()-1));
                            choice2[0]="s";
                        }
                    }
                    if(choice2[0].equalsIgnoreCase("b")) adminInterface(admin);
                    else if(choice2[0]!="n"){
                        choice2 = io.getUserInput("Enter 'n' to add next customer,'b' to go back ", 1);
                        if(choice2[0].equalsIgnoreCase("b")) adminInterface(admin);
                        else isSuccess=false;
                    }
                }
                break;
            case "3"://show customers
                CustomerListResult cusList = new CustomerListResult();
                String[] choice3 = new String[1];
                
                choice3[0]="n";
                do{
                    cusList = cusOp.getCustomerList(pageNum);
                    if(cusList==null) {
                        io.printMessage("====The list is empty!====");
                        adminInterface(admin);
                    }
                    else{
                        List<User> showCus = cusList.getAllCustomersEachPage();
                        if(choice3[0].equalsIgnoreCase("n")||choice3[0].equalsIgnoreCase("p"))
                            io.showList("admin", "customer", showCus, cusList.getCurrentPage(), cusList.getTotalPages());
                    }
                    choice3 = io.getUserInput("Enter 'n' for next page, 'p' for previous page, 'b' to go back, or a customer ID to view detail", 1);
                    pageNum=afterGeneration(pageNum, cusList.getTotalPages(),choice3[0],admin);
                    if(choice3[0].contains("u_")){
                        boolean foundUser=false;
                        List<User> allUsers = DataControl.readAllCustomers();
                        for(int i =0;i<allUsers.size();i++){
                            if(allUsers.get(i).getUserID().equals(choice3[0])){
                                System.out.println("\n"+allUsers.get(i)+"\n");
                                foundUser=true;
                                pageNum=1;
                            }
                        }
                        if(foundUser==false){
                            io.printMessage("cannot find user with that ID");
                        }
                    }
                }while(!choice3[0].equalsIgnoreCase("b"));
                break;
            case "4"://show orders
                String customerID=null;
                OrderListResult orderList;
                String[] choice4 = new String[1];
                choice4[0]="n";
                do{
                    orderList = orderOp.getOrderList(customerID,pageNum);
                    if(orderList==null){
                        io.printMessage("====The list is empty!====");
                        adminInterface(admin);
                    }
                    else{
                    List<Order> showOrder = orderList.getAllOrdersEachPage();
                        if(choice4[0].equalsIgnoreCase("n")||choice4[0].equalsIgnoreCase("p"))
                            io.showList("admin", "order", showOrder, orderList.getCurrentPage(), orderList.getTotalPages());
                    }
                    choice4 = io.getUserInput("Enter 'n' for next page, 'p' for previous page, 'b' to go back, or a customer ID to view detail", 1);
                    pageNum=afterGeneration(pageNum, orderList.getTotalPages(),choice4[0],admin);
                    if(choice4[0].contains("u_")){
                        customerID=choice4[0];
                        pageNum=1;
                        choice4[0]="n";
                    }     
                }while(!choice4[0].equalsIgnoreCase("b"));
                break;
            case "5":
                String[] choice5 = new String[1];
                choice5[0]="n";
                do{
                    if(choice5[0].equalsIgnoreCase("n")){
                         orderOp.generateTestOrderData();
                        io.printMessage("successfully genereated test data!");
                    }
                    choice5=io.getUserInput("Enter 'n' to generate again, 'b' to go back", 1);
                    if(choice5[0].equalsIgnoreCase("b")){
                        adminInterface(admin);
                    }
                }while(choice5[0]!="b");
                break;
            case "6":
                proOp.generateDiscountFigure();
                proOp.generateCategoryFigure();
                proOp.generateDiscountLikesCountFigure();
                proOp.generateLikesCountFigure();
                io.printMessage("successfully generated 4 graphs!");
                adminInterface(admin);
                break;
            case "7":
                //io.printMessage("(1) Remove one customer\n(2) Remove all customers\n(3) Remove one product\n(4) Remove all products\n(5) Remove one order\n(6) Remove all orders");
                String[] choice7 = new String[1];
                boolean isRemoved = false;
                do{
                    choice7 = io.getUserInput("(1) Remove one customer\n" + //
                                            "(2) Remove all customers\n" + //
                                            "(3) Remove one product\n" + //
                                            "(4) Remove all products\n" + //
                                            "(5) Remove one order\n" + //
                                            "(6) Remove all orders\n" +
                                            "(7) Go back", 1);
                    switch (choice7[0]) {
                        case "1":
                            String choice7_1[] = io.getUserInput("Enter customerID: ", 1);
                            isRemoved=cusOp.deleteCustomer(choice7_1[0]);
                            showMsgForDeleting(isRemoved,choice7_1[0]);
                            isRemoved=false;
                            break;
                        case "2":
                            cusOp.deleteAllCustomer();
                            io.printMessage("Sucessfully deleted all customers");
                            break;
                        case "3":
                            String choice7_3[] = io.getUserInput("Enter product ID: ", 1);
                            isRemoved=proOp.deleteProduct(choice7_3[0]);
                            showMsgForDeleting(isRemoved,choice7_3[0]);
                            isRemoved=false;
                            break;
                        case "4":
                            proOp.deleteAllProducts();
                            io.printMessage("Sucessfully deleted all products");
                            break;
                        case "5":
                            String choice7_5[] = io.getUserInput("Enter order ID: ", 1);
                            isRemoved=orderOp.deleteOrder(choice7_5[0]);
                            showMsgForDeleting(isRemoved,choice7_5[0]);
                            isRemoved=false;
                            break;
                        case "6":
                            orderOp.deleteAllOrders();
                            io.printMessage("Sucessfully deleted all products");
                            break;
                        case "7":
                            adminInterface(admin);
                            break;
                        default:
                            break;
                    }
                }while(isRemoved==false);
            case "8":
                mainInterface();                     
                break;
            default:
                io.printMessage("Invalid choice! please only choose from 1 to 8");
                adminInterface(admin);
                break;

        }
    }
    private static void showMsgForDeleting(boolean isRemoved,String nameOfDeletedItem){
        IOInterface io = IOInterface.getInstance();
        if(isRemoved){
            io.printMessage("Sucessfully deleted "+nameOfDeletedItem);
        } 
        else{
            io.printMessage("the ID is not found");
        }
    }
    private static int afterGeneration(int pageNum,int totalPages,String choice,User user){
        if(choice.equalsIgnoreCase("n")){
            if(pageNum<totalPages) pageNum++;
            else pageNum=1;
        } 
        else if(choice.equalsIgnoreCase("p")){
            if(pageNum>1) pageNum--;
            else pageNum=totalPages;
        }
        else if(choice.equalsIgnoreCase("b")){
            pageNum=1;
            returnToInterface(user);
        }
        return pageNum;
    }
    private static void showProduct(User user){
        int pageNum=1;
        ProductOperation proOp = ProductOperation.getInstance(); 
        IOInterface io = IOInterface.getInstance();
        ProductListResult proList = new ProductListResult();
        String[] choice = new String[1];
        choice[0]="n";
        do{
            proList=proOp.getProductList(pageNum);
            if(proList==null){
                io.printMessage("====The list is empty!====");
                returnToInterface(user);
            } 
            else{
                List<Product> showPro = proList.getAllProductsEachPage();
                if(choice[0].equalsIgnoreCase("n")||choice[0].equalsIgnoreCase("p"))
                    io.showList("admin", "product", showPro, proList.getCurrentPage(), proList.getTotalPages());
            }
            choice = io.getUserInput("Enter 'n' for next page, 'p' for previous page, 'b' to go back", 1);
            pageNum=afterGeneration(pageNum, proList.getTotalPages(),choice[0],user); 
        }while(!choice[0].equalsIgnoreCase("b"));
    }
    private static void returnToInterface(User user){
        if(user.getUserRole().equals("admin")) adminInterface((Admin)user);
        if(user.getUserRole().equals("customer")) customerInterface((Customer) user);
    }
    
    
}
