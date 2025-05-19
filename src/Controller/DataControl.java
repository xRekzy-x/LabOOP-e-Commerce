package Controller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import IOInterface.IOInterface;
import Model.Admin;
import Model.Customer;
import Model.Order;
import Model.Product;
import Model.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DataControl {
    private DataControl instance;
    private static IOInterface io = IOInterface.getInstance();
    private DataControl(){}
    public DataControl getInstance(){
        if(instance==null){
            instance = new DataControl();
        }
        return instance;
    }
    private static String deleteUnrelatedThings(String line){
        line = line.replaceAll("\"", "");
        line = line.replaceAll("\\{", "");
        line = line.replaceAll("\\}", "");
        return line;
    }
    public static List<String> transformLines(String file){
        List<String> allUsers = new ArrayList<>();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader("data/"+file+".txt"))) {
            while((line=reader.readLine())!=null){
                line = deleteUnrelatedThings(line);
                allUsers.add(line);
            }
        }
        catch(IOException e){
            io.printErrorMessage("transformLines in DataControl class","the format is inappropriate!"+e.getMessage());
        }
        return allUsers;
    } 
    private static String delete(String content,String delete){
        return content.replace(delete, "").trim();
    }
    public static List<User> readAllUsers(){
        User.clearData();
        List<User> allUsers = new ArrayList<>();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(Paths.get("data", "users.txt").toFile()))) {
            while((line=reader.readLine())!=null){
                line = deleteUnrelatedThings(line);
                String[] parts = line.split(",");
                if(parts.length<5) continue;
                String userName=delete(parts[1],"user_name:");
                String userID = delete(parts[0],"user_id:");
                String userPassword = delete(parts[2],"user_password:");
                String userRegisterTime = delete(parts[3],"user_register_time:");
                String userRole = delete(parts[4],"user_role:");
                if(userRole.equals("admin")){
                    allUsers.add(new Admin(userID, userName, userPassword, userRegisterTime));
                }
                else if(userRole.equals("customer")){
                    String userEmail = delete(parts[5],"user_email:");
                    String userMobile = delete(parts[6],"user_mobile:");
                    allUsers.add(new Customer(userID, userName, userPassword, userRegisterTime, userEmail, userMobile));
                }
            }
        }
        catch(IOException e){
            io.printErrorMessage("readAllUsers in DataControl", e.getMessage());
        }
        return allUsers;
    }
    
    public static List<String> readAllUsersPart(String part){
        List<String> allUserPart = new ArrayList<>();
        List<String> allUsers = transformLines("users");
        String content;
        for(String user : allUsers){
            String[] parts = user.split(",");
            if(parts.length<5) continue;
            if(part=="name"){
                content=delete(parts[1],"user_name:");
                allUserPart.add(content);
            }
            else if(part=="id"){
                content=delete(parts[0],"user_id:");
                allUserPart.add(content);
            }
            else if(part=="password"){
                content=delete(parts[2],"user_password:");
                allUserPart.add(content);
            }
            else if(part=="time"){
                content=delete(parts[3],"user_register_time:");
                allUserPart.add(content);
            }
            else if(part=="role"){
                content=delete(parts[4],"user_role:");
                allUserPart.add(content);    
            }
            else{
                io.printErrorMessage("readAllUserParts in DataControl", "invalid part");
            }
        }
        return allUserPart;
    }
    public static List<User> readUsersInRange(int start,int end){
        List<User> users = new ArrayList<>();
        List<User> allUsers = readAllUsers();
        for(int i= start;i<=end;i++){
            users.add(allUsers.get(i-1));
        }
        return users;
    }
    public static List<User> readAllCustomers(){
        List<User> customers= readAllUsers();
        for(int i =0; i <customers.size();i++){
            if(customers.get(i).toString().matches(".*\"user_role\":\"admin\".*")) customers.remove(i);
        }
        return customers;
    }
    public static <T> void addLine(String file,T line){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("data/"+file+".txt",true))){
            writer.newLine(); 
            writer.write(line.toString());     
        }   
        catch(IOException e){
            io.printErrorMessage("addLine in DataControl", e.getMessage());
        }
    }
    public static void deleteAll(String file){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("data/"+file+".txt",false))){
            writer.write("");     
        }   
        catch(IOException e){
            io.printErrorMessage("deleteAll in DataControl", e.getMessage());
        }
    }
    public static boolean deleteOneLine (String file,String id){
        List <String> updatedLines = new ArrayList<>();
        boolean isDeleted=false;
        try (BufferedReader reader = new BufferedReader(new FileReader("data/"+file+".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\"");
                if(parts.length<4) continue;
                if (parts[3].trim().equals(id)) {
                    if(file=="users"){
                        if(parts[19].trim().equals("customer")){
                            isDeleted=true;
                            continue;
                        }
                    }
                    else if(file=="products"||file=="orders"){
                        isDeleted=true;
                        continue;
                    }
                    
                    else{
                        io.printErrorMessage("deleteOneLine in DataControl", "invalid part");
                    }
            }
                updatedLines.add(line);
                    }
                    if(isDeleted){
                        Files.write(Paths.get("data/"+file+".txt"),updatedLines);
                    }
                    
         }
        catch(IOException e){
            io.printErrorMessage("deleteOneLine", e.getMessage());
        }
        return isDeleted;
    }
    public static List<Product> readAllProducts(){
        Product.clearData();
        List<Product> allProducts = new ArrayList<>();
        String line;
        try(BufferedReader reader= new BufferedReader(new FileReader("data/products.txt"))){
            while((line=reader.readLine())!=null){
                line = deleteUnrelatedThings(line);
                String[] parts = line.split(",");
                if(parts.length<8) continue;
                String proModel=delete(parts[1],"pro_model:");
                String proId = delete(parts[0],"pro_id:");
                String proCategory = delete(parts[2],"pro_category:");
                String proName = delete(parts[3],"pro_name:");
    
                Double proCurrentPrice = Double.parseDouble(delete(parts[4],"pro_current_price:"));
                Double proRawPrice = Double.parseDouble(delete(parts[5],"pro_raw_price:"));
                Double proDiscount = Double.parseDouble(delete(parts[6],"pro_discount:"));
                int proLikesCount = Integer.parseInt(delete(parts[7],"pro_likes_counts:"));
                Product product = new Product(proId, proModel, proCategory, proName, proCurrentPrice, proRawPrice, proDiscount, proLikesCount);
                allProducts.add(product);
            }
        }
        catch(IOException e){
            io.printErrorMessage("readAllProducts", e.getMessage());
        }
        return allProducts;
    }
    public static List<String> readAllProductsPart(String part){
        List<String> allProductPart = new ArrayList<>();
        List<String> allProdcuts = transformLines("products");
        String content;
        for(String product : allProdcuts){
            String[] parts = product.split(",");
            if(parts.length<8) continue;
            if(part=="model"){
                content=delete(parts[1],"pro_model:");
                allProductPart.add(content);
            }
            else if(part=="id"){
                content=delete(parts[0],"pro_id:");
                allProductPart.add(content);
            }
            else if(part=="category"){
                content=delete(parts[2],"pro_category:");
                allProductPart.add(content);
            }
            else if(part=="name"){
                content=delete(parts[3],"pro_name:");
                allProductPart.add(content);
            }
            else if(part=="current_price"){
                content=delete(parts[4],"pro_current_price:");
                allProductPart.add(content);    
            }
            else if(part=="raw_price"){
                content=delete(parts[5],"pro_raw_price:");
                allProductPart.add(content);    
            }
            else if(part=="discount"){
                content=delete(parts[6],"pro_discount:");
                allProductPart.add(content);    
            }
            else if(part=="likes_counts"){
                content=delete(parts[7],"pro_likes_counts:");
                allProductPart.add(content);    
            }
            else{
                io.printErrorMessage("readAllProductsPart in DataControl", "invalid part");
            }
        }
        return allProductPart;
    }
    public static Map<String,Integer> assignValueToMap(String type, int[] increment,int columns){
        List<String> allElements = DataControl.readAllProductsPart(type);
        Map<String,Integer> elementsCount = new HashMap<>();
        int i =0;
        for(String element: allElements){
            if(i<columns||columns==0){
                if(elementsCount.get(element)==null) elementsCount.put(element, 0);
                if(element==null) element="unknown";
                
                else if(elementsCount.containsKey(element)||element.trim().isEmpty()){
                    elementsCount.put(element,elementsCount.get(element)+increment[i]);
                }
                i++;
            }
        }
        i=0;
        return elementsCount;
    }
    public static <T extends Number> List<Map.Entry<String, T>> Sorting(Map<String, T> categoriesCount,String DescendingOrAscending) {
        List<Map.Entry<String, T>> entryList = new ArrayList<>(categoriesCount.entrySet());
        //create a new arrayList and fill instantly with categoriesCount.entrySet()
        //entrySet là 1 phần tử của map
        entryList.sort(new Comparator<Map.Entry<String, T>>() {
        @Override
        public int compare(Map.Entry<String, T> e1, Map.Entry<String, T> e2) {
            //return e2.getValue().doubleValue().compareTo(e1.getValue().doubleValue());
            if(DescendingOrAscending.equalsIgnoreCase("descending")) return Double.compare(e2.getValue().doubleValue(), e1.getValue().doubleValue());
            else return Double.compare(e1.getValue().doubleValue(), e2.getValue().doubleValue());
            //cái trước > cái sau (e2>e1) thì dương, bằng thì 0, bé hơn thì âm
            //nếu âm thì không làm gì cả
            //nếu dương thì đảo vị trí
        }
        });
        return entryList;
    }
    public static BarChart<String,Number> creataBarChart(String title,String x,String y){
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(x);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(y);

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(title);
        return barChart;
    }
    public static void showGraph(StackPane root ,Stage stage){
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
    public static void saveGraph(String output,Scene scene){
        WritableImage image = scene.snapshot(null);
        File file = new File("data/figure/"+output+".png");
        file.getParentFile().mkdirs(); // create folder if not exist
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            io.printErrorMessage("saveGraph", "cannot save graph"+e.getMessage());
        }
    }
    public static List<Order> readAllOrders(){
        Order.clearData();
        List<Order> allOrders = new ArrayList<>();
        String line;
        try(BufferedReader reader= new BufferedReader(new FileReader("data/orders.txt"))){
            while((line=reader.readLine())!=null){
                line = deleteUnrelatedThings(line);
                String[] parts = line.split(",");
                if(parts.length<4) continue;
                String orderId=delete(parts[0],"order_id:");
                String userId = delete(parts[1],"user_id:");
                String proId = delete(parts[2],"pro_id:");
                String orderTime = delete(parts[3],"order_time:");
                Order order = new Order(orderId, userId, proId, orderTime);
                allOrders.add(order);
            }
        }
        catch(IOException e){
            io.printErrorMessage("readAllOrders in DataControl", e.getMessage());
        }
        return allOrders;
    }
    public static List<String> readAllOrdersPart(String part){
        List<String> allOrdersPart = new ArrayList<>();
        List<String> allOrders = transformLines("orders");
        String content;
        for(String order : allOrders){
            String[] parts = order.split(",");
            if(parts.length<4) continue;
            else if(part=="order_id"){
                content=delete(parts[0],"order_id:");
                allOrdersPart.add(content);
            }
            else if(part=="user_id"){
                content=delete(parts[1],"user_id:");
                allOrdersPart.add(content);
            }
            else if(part=="pro_id"){
                content=delete(parts[2],"pro_id:");
                allOrdersPart.add(content);
            }
            else if(part=="order_time"){
                content=delete(parts[3],"order_time:");
                allOrdersPart.add(content);
            }
            else{
                io.printErrorMessage("readAllOrdersPart in DataControl", "invalid part");
            }
        }
        return allOrdersPart;
    }
    public static String getCurrentTime(){
        return new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new Date());
    }
    public static List<Order> readAllOrdersOfACustomer(String customerId){
        List<Order> allOrders = readAllOrders();
        List<Order> allOrdersOfACustomer = new ArrayList<>();
        for(Order order : allOrders){
            if(order.getUserID().equals(customerId)||customerId==null){ 
                allOrdersOfACustomer.add(order);
            }
        }
        return allOrdersOfACustomer;
    }
    public static String generateRandomTimeInYear(String before_Or_after,String Milestone,int year){
        Milestone=Milestone.replace("_", "-");
        String[] date = Milestone.split("-");
        String[] clock = date[3].split(":");
        LocalDateTime start;
        LocalDateTime end;
        if(before_Or_after.equals("after")){
            start = LocalDateTime.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]),Integer.parseInt(clock[0]),Integer.parseInt(clock[1]));
            end = LocalDateTime.of(year, 12, 31, 23, 59);
        }
        else{
            end = LocalDateTime.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]),Integer.parseInt(clock[0]),Integer.parseInt(clock[1]));
            start = LocalDateTime.of(year, 1, 1, 1, 1);
        }
        long startEpoch = start.toEpochSecond(ZoneOffset.UTC);
        long endEpoch = end.toEpochSecond(ZoneOffset.UTC);

        Random random = new Random();
        long randomEpoch = startEpoch + (long) (random.nextDouble() * (endEpoch - startEpoch));
        //random.nextDouble sinh ra số từ 0 tới 1 (luôn dưới hoặc bằng 100%)
        return LocalDateTime.ofEpochSecond(randomEpoch, 0, ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss"));
    }
    public static <T extends Number> XYChart.Series<String,Number> assignValueToBarChart(String nameOfData,List<String> x,List<T> y,String DescendingOrAscending,int columns){
        Map<String, Double> data = new LinkedHashMap<>();

        double valueNeedToAdd;
        double currentValue;
        for (int i =0;i<x.size();i++) {
            valueNeedToAdd=y.get(i).doubleValue();
            if(data.get(x.get(i))==null)
                data.put(x.get(i),valueNeedToAdd);
            else{
                currentValue=data.get(x.get(i)).doubleValue();
                data.put(x.get(i), (currentValue+valueNeedToAdd));
            }
        }
        
        List<Map.Entry<String,Double>> entryList;
        if(DescendingOrAscending==null) entryList = new ArrayList<>(data.entrySet());
        else entryList = Sorting(data, DescendingOrAscending); 
        if(columns>0){
            for(int i=entryList.size()-1;i>=columns;i--){
                entryList.remove(i);
            }
        }
        //assign the value to the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(nameOfData);
        for (Map.Entry<String, Double> entry : entryList) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        return series;
    }
    public static void sleep(int second){
         try {
            Thread.sleep(1000*second);
        } catch (InterruptedException e) {
            io.printErrorMessage("sleep in DataControl", e.getMessage());
        }
    }
}
