package Controller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import Model.Admin;
import Model.Customer;
import Model.Product;
import Model.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DataControl {
    private DataControl instance;
    private DataControl(){
    }
    public DataControl getInstance(){
        if(instance==null){
            instance = new DataControl();
        }
        return instance;
    }
    public static List<String> readAllUsersString(){
        List<String> users = new ArrayList<>();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(Paths.get("data", "users.txt").toFile()))) {
            while((line=reader.readLine())!=null){
                users.add(line);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return users;
    }
    public static List<String> transformLines(String file){
        List<String> allUsers = new ArrayList<>();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader("data/"+file+".txt"))) {
            while((line=reader.readLine())!=null){
                line = line.replaceAll("\"", "");
                line = line.replaceAll("\\{", "");
                line = line.replaceAll("\\}", "");
                line = line.replaceAll("\":\"", "");
                allUsers.add(line);
            }
        }
        catch(IOException e){
            e.printStackTrace();
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
                System.out.println(line);
                line = line.replaceAll("\"", "");
                line = line.replaceAll("\\{", "");
                line = line.replaceAll("\\}", "");
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
            e.printStackTrace();
        }
        return allUsers;
    }
    
    public static List<String> readAllUsersPart(String part){
        List<String> allUserPart = new ArrayList<>();
        List<String> allUsers = transformLines("users");
        String content;
        for(String user : allUsers){
            String[] parts = user.split(",");
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
    public static List<User> readCustomersInRange(int start, int end){
        List<User> allCustomers = readAllCustomers();
        List<User> customers = new ArrayList<>();
        for(int i = start;i<=end;i++){
            customers.add(allCustomers.get(i-1));
        }
        return customers;
    }
    public static <T> void addLine(String file,T line){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("data/"+file+".txt",true))){
            writer.write(line.toString());     // JSON string from toString()
            writer.newLine(); 
        }   
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void deleteAll(String file){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("data/"+file+".txt",true))){
            writer.write("");     
        }   
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public static boolean deleteOneLine (String file,String id){
        List <String> updatedLines = new ArrayList<>();
        boolean isDeleted=false;
        try (BufferedReader reader = new BufferedReader(new FileReader("data/"+file+".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\"");
                if (parts[3].trim().equals(id)) {
                    if(file=="users"){
                        if(parts[19].trim().equals("customer")){
                            isDeleted=true;
                            continue;
                        }
                    }
                    else if(file=="products"){
                        isDeleted=true;
                        continue;
                    }
            }
                updatedLines.add(line);
                    }
                    if(isDeleted){
                        Files.write(Paths.get("data/"+file+".txt"),updatedLines);
                    }
                    
         }
        catch(IOException e){
            e.printStackTrace();
        }
        return isDeleted;
    }
    public static List<Product> readAllProducts(){
        Product.clearData();
        List<Product> allProducts = new ArrayList<>();
        String line;
        try(BufferedReader reader= new BufferedReader(new FileReader("data/products.txt"))){
            while((line=reader.readLine())!=null){
                line = line.replaceAll("\"", "");
                line = line.replaceAll("\\{", "");
                line = line.replaceAll("\\}", "");
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
            e.printStackTrace();
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
        }
        return allProductPart;
    }
    public static Map<String,Integer> assignValueToMap(String type, int[] increment){
        List<String> allElements = DataControl.readAllProductsPart(type);
        Map<String,Integer> elementsCount = new HashMap<>();
        int i =0;
        for(String element: allElements){
            if(elementsCount.get(element)==null) elementsCount.put(element, 0);
            if(element==null) element="unknown";
            
            else if(elementsCount.containsKey(element)||element.trim().isEmpty()){
                elementsCount.put(element,elementsCount.get(element)+increment[i]);
            }
            i++;
        }
        i=0;
        return elementsCount;
    }
    public static List<Map.Entry<String,Integer>> descendingSort(Map<String,Integer> categoriesCount){
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(categoriesCount.entrySet());
        //create a new arrayList and fill instantly with categoriesCount.entrySet()
        //entrySet là 1 phần tử của map
        entryList.sort(new Comparator<Map.Entry<String, Integer>>() {
        @Override
        public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
            return e2.getValue().compareTo(e1.getValue());
            //cái trước > cái sau (e2>e1) thì dương, bằng thì 0, bé hơn thì âm
            //nếu âm thì không làm gì cả
            //nếu dương thì đảo vị trí
        }
        });
        return entryList;
    }
    public static List<Map.Entry<String,Integer>> ascendingSort(Map<String,Integer> categoriesCount){
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(categoriesCount.entrySet());
        entryList.sort(new Comparator<Map.Entry<String, Integer>>() {
        @Override
        public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
            return e1.getValue().compareTo(e2.getValue());
        }
        });
        return entryList;
    }
    // public static JFreeChart createBarChart(List<Map.Entry<String,Integer>> entryList,String title,String x_axis,String y_axis){
    //     DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    //     for (Map.Entry<String, Integer> entry : entryList) {
    //         dataset.addValue(entry.getValue(), "products", entry.getKey());
    //     }
    //     JFreeChart barChart = ChartFactory.createBarChart(
    //             title,
    //             x_axis,
    //             y_axis,
    //             dataset
    //     );
    //     return barChart;
    // }
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
            e.printStackTrace();
        }
    }
}
