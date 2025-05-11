import java.util.HashSet;
import java.util.Set;

public class Product {
    private String proId;
    private String proModel;
    private String proCategory;
    private String proName;
    private double proCurrentPrice;
    private double proRawPrice;
    private double proDiscount;
    private int proLikesCount;
    private static final Set<String>  existingProductIds = new HashSet<>();
    public Product(String proId, String proModel, String proCategory, String proName, Double proCurrentPrice, Double proRawPrice, Double proDiscount,int proLikesCount) {
        if(existingProductIds.contains(proId)){
            throw new IllegalArgumentException("Product ID must be unique. Duplicate found: " + proId);
        }
        this.proId=proId;
        existingProductIds.add(proId);
        this.proModel=proModel;
        this.proCategory=proCategory;
        this.proName=proName;
        this.proCurrentPrice=proCurrentPrice;
        this.proRawPrice=proRawPrice;
        this.proDiscount=proDiscount;
        this.proLikesCount=proLikesCount;
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
        return proId;
    }
    
}
