package Operation;

import Controller.DataControl;
import Model.Customer;
import Model.Order;
import Model.OrderListResult;
import Model.Product;
import javafx.application.Platform;
import javafx.scene.chart.BarChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderOperation {
    private Stage stage=null;
    private static OrderOperation instance;
    private OrderOperation(){}
    public static OrderOperation getInstance(){
        if(instance==null){
            instance = new OrderOperation();
        }
        return instance;
    }
    public String generateUniqueOrderId(){
        String generatedId;
        Random random = new Random();
        do {
            long randomNum = random.nextLong(100000);
            generatedId = String.format("o_%05d", randomNum);
        } while (DataControl.readAllOrdersPart("order_id").contains(generatedId));
        return generatedId;
    }
    public boolean createAnOrder(String customerId,String productId,String createTime){
        if(createTime==null) createTime = DataControl.getCurrentTime();
        if(customerId!=null&&productId!=null){
            String orderId= generateUniqueOrderId();
            Order order = new Order(orderId, customerId, productId, createTime);
            DataControl.addLine("orders", order);
            return true;
        }
        return false;
    }
    public boolean deleteOrder(String orderId){
        return DataControl.deleteOneLine("orders", orderId);
    }
    public OrderListResult getOrderList(String customerId,int pageNumber){
        OrderListResult orderList = new OrderListResult(customerId);
        List<Order> allOrdersOfACustomer;
        if(customerId==null) allOrdersOfACustomer = DataControl.readAllOrders();
        else allOrdersOfACustomer = DataControl.readAllOrdersOfACustomer(customerId);
        int initialIndex = (pageNumber-1)*10;
        int endIndex = Math.min(initialIndex+10,allOrdersOfACustomer.size());
        orderList.getAllOrdersEachPage().clear();
        if(pageNumber>orderList.getTotalPages()) return null;
        for(int i = initialIndex;i<endIndex;i++){
            orderList.getAllOrdersEachPage().add(allOrdersOfACustomer.get(i));
        }
        return orderList;
    }
    public void generateTestOrderData(){
        UserOperation userOp = UserOperation.getInstance();
        OrderOperation orderOp = OrderOperation.getInstance();
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String CHARACTERSforNAME=letters+"_";
        String CHARACTERSforPASSWORD=letters+numbers;
        String CHARACTERSforEMAIL=letters;
        Random random = new Random();
        int length=0;
        String userName;
        String userPassword;
        String userMobile;
        String userEmail;
        String userRegisterTime;
        String userId;
        StringBuilder name = new StringBuilder(10);
        StringBuilder password = new StringBuilder(10);
        StringBuilder email = new StringBuilder(17);
        StringBuilder mobile = new StringBuilder(10);
        int numberOfOrdersEachUser=0;
        List<String> allProIDs= DataControl.readAllProductsPart("id");
        for(int i =0;i<10;i++){
            name.setLength(0);
            password.setLength(0);
            email.setLength(0);
            mobile.setLength(0);
            int nameLength= random.nextInt(6)+5;
            int passwordLength= random.nextInt(6)+5;
            do{
                name.append(CHARACTERSforNAME.charAt(random.nextInt(CHARACTERSforNAME.length())));
                userName=name.toString();
                length++;
            }while(userOp.checkUsernameExist(userName)||length<nameLength);
            length=0;
            password.append(letters.charAt(random.nextInt(letters.length())));
            password.append(numbers.charAt(random.nextInt(numbers.length())));
            for(int j=2;j<passwordLength;j++){
                password.append(CHARACTERSforPASSWORD.charAt(random.nextInt(CHARACTERSforPASSWORD.length())));
            }
            userPassword=password.toString();
            for(int a=0;a<5;a++){
                email.append(CHARACTERSforEMAIL.charAt(random.nextInt(CHARACTERSforEMAIL.length())));
            }
            email.append("@");
            for(int b=0;b<3;b++){
                email.append(CHARACTERSforEMAIL.toLowerCase().charAt(random.nextInt(CHARACTERSforEMAIL.length())));
            }
            email.append(".");
            for(int c=0;c<3;c++){
                email.append(CHARACTERSforEMAIL.toLowerCase().charAt(random.nextInt(CHARACTERSforEMAIL.length())));
            }
            userEmail=email.toString();
            mobile.append("0");
            mobile.append( String.valueOf(random.nextInt(2)+3));
            
            for(int d=2;d<10;d++){
                mobile.append(numbers.charAt(random.nextInt(numbers.length())));
            }  
            userMobile=mobile.toString();   
            userRegisterTime= DataControl.generateRandomTimeInYear("before", "01-06-2024_01:01:01",2024);       
            userId = userOp.generateUniqueUserId();
            Customer customer = new Customer(userId, userName, userOp.encryptPassword(userPassword),userRegisterTime, userEmail, userMobile);
            DataControl.addLine("users", customer.toString());
            numberOfOrdersEachUser=random.nextInt(16)+5;
            for(int e=0;e<numberOfOrdersEachUser;e++){
                Order order = new Order(orderOp.generateUniqueOrderId(), userId, allProIDs.get(random.nextInt(allProIDs.size())), DataControl.generateRandomTimeInYear("after",userRegisterTime,2024));
                DataControl.addLine("orders", order.toString());
            }
            
        }
    }
    public void generateSingleCustomerConsumptionFigure(String customerId) {
        Platform.runLater(() -> {
            stage = new Stage();
            stage.setTitle("Consumption");
            String title;
            if(customerId==null) title="Consumption of every customers";
            else title = "Consumption each customer";
            BarChart<String, Number> barChart = DataControl.creataBarChart(title, "Month", "consumption");
            List<Double> arr = new ArrayList<>();
            List<Order> orders = DataControl.readAllOrders();
            List<String> months = new ArrayList<>();
            
            DataControl.readAllProducts();
            for(int j=0;j<12;j++){
                months.add(String.format("%02d", j+1));
                arr.add(0.0);
            }
            for(Order order : orders){
                String[] parts = order.getOrderTime().split("-");
                if(order.getUserID().equals(customerId)||customerId==null){
                    for(String month: months ){
                        if(parts[1].equals(month))
                            arr.set(Integer.parseInt(month)-1,arr.get(Integer.parseInt(month)-1)+Product.getRegistryByID(order.getProductID()).getProCurrentPrice());
                    }
                }
            }
            barChart.getData().clear();
            barChart.getData().add(DataControl.assignValueToBarChart("Money spent on purchasing",months,arr,null,0));
            StackPane root = new StackPane(barChart);
            DataControl.showGraph(root, stage);
            if(customerId==null) DataControl.saveGraph("Consumption_Figure_For_All", stage.getScene());
            else DataControl.saveGraph("Consumption_Figure_For_Single", stage.getScene());
            stage.setOnCloseRequest(e -> stage= null);
        });
    }
    public void generateAllCustomersConsumptionFigure() {
        generateSingleCustomerConsumptionFigure(null);
    }
    public void generateAllTop10BestSellersFigure() {
        Platform.runLater(() -> {
            stage = new Stage();
            stage.setTitle("BestSeller");
            BarChart<String, Number> barChart = DataControl.creataBarChart("Top 10 best-selling products", "Product", "Number of products");
            List<Integer> arr = new ArrayList<>();
            List<Order> orders = DataControl.readAllOrders();
            List<String> productName = new ArrayList<>();
            
            DataControl.readAllProducts();
            for(int j=0;j<orders.size();j++){
                arr.add(1);
            }
            for(Order order : orders){ 
                productName.add(order.getProductID());
            }
            barChart.getData().clear();
            barChart.getData().add(DataControl.assignValueToBarChart("Products",productName,arr,"descending",10));
            StackPane root = new StackPane(barChart);
            DataControl.showGraph(root, stage);
            DataControl.saveGraph("Best_Sellers", stage.getScene());
            stage.setOnCloseRequest(e -> stage= null);
        });
    }
    public void deleteAllOrders(){
        DataControl.deleteAll("orders");
    }
    
}
