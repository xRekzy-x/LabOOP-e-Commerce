package Model;
import java.util.HashMap;
import java.util.Map;

public class Product {
    private String proId;
    private String proModel;
    private String proCategory;
    private String proName;
    private double proCurrentPrice;
    private double proRawPrice;
    private double proDiscount;
    private int proLikesCount;
    private static final Map<String,Product> registry = new HashMap<>();
    public Product(String proId, String proModel, String proCategory, String proName, Double proCurrentPrice, Double proRawPrice, Double proDiscount,int proLikesCount) {
        if(registry.containsKey(proId)){
            throw new IllegalArgumentException("Product ID must be unique. Duplicate found: " + proId);
        }
        this.proId=proId;
        //existingProductIds.add(proId);
        this.proModel=proModel;
        this.proCategory=proCategory;
        this.proName=proName;
        this.proCurrentPrice=proCurrentPrice;
        this.proRawPrice=proRawPrice;
        this.proDiscount=proDiscount;
        this.proLikesCount=proLikesCount;
        registry.put(proId, this);
        
    }
    // public static Product getRegistryByName(String productName){
    //     for(User user: registry.values()){
    //         if(user.getUserName().equals(userName)) return user;
    //     }
    //     return null;
    // }
    public static Product getRegistryByID(String proID){
        return registry.get(proID);
    }
      public static void clearData(){
        registry.clear();
    }
    public String getProId() {
        return proId;
    }
    public String getProModel() {
        return proModel;
    }
    public String getProCategory() {
        return proCategory;
    }
    public String getProName() {
        return proName;
    }
    public double getProCurrentPrice() {
        return proCurrentPrice;
    }
    public double getProRawPrice() {
        return proRawPrice;
    }
    public double getProDiscount() {
        return proDiscount;
    }
    public int getProLikesCount() {
        return proLikesCount;
    }
    public String toString(){
        return "\"pro_id\":\""+proId+"\",\"pro_model\":\""+proModel+"\",\"pro_category\":\""+proCategory+"\",\"pro_name:\":\""+proName+"\",\"pro_current_price\":\""+proCurrentPrice+"\",\"pro_raw_price\":\""+proRawPrice+"\",\"pro_discount\":\""+proDiscount+"\",\"pro_likes_counts\":\""+proLikesCount+"\"}";
    }
    
}
