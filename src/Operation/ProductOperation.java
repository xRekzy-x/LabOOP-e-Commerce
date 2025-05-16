package Operation;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


//import com.google.gson.Gson;
import java.util.Map;

import javax.xml.crypto.Data;

import Controller.DataControl;

import Model.Product;
import Model.ProductListResult;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class ProductOperation {
    private static ProductOperation instance;
    private ProductOperation(){}
    public static ProductOperation getInstance(){
        if(instance==null){
            instance= new ProductOperation();
        }
        return instance;
    }
    public void extractProductsFromFile(){
        String content=null;
       // Gson gson = new Gson();
        String line;
        
         try(BufferedReader reader =  new BufferedReader(new FileReader("data/kkk.txt"))){
            while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\"");
            String proId = parts[3].trim();
            String proModel=parts[7].trim();
            String proCategory=parts[11].trim();
            String proName = parts[15].trim();
           
            content = parts[18].trim().replaceAll(":", "");
            content = content.replaceAll(",","");
            Double proCurrentPrice = Double.parseDouble(content);
            
            //String proRawPrice = parts[20].replaceAll(":", "");
            content = parts[20].trim().replaceAll(":", "");
            content = content.replaceAll(",","");
            Double proRawPrice = Double.parseDouble(content);

            //String proDiscount = parts[22].replaceAll(":", "");
            content = parts[22].trim().replaceAll(":", "");
            content = content.replaceAll(",","");
            Double proDiscount = Double.parseDouble(content);
           // String proLikesCount = parts[24].replaceAll(":", "");
            content = parts[24].trim().replaceAll(":", "");
            content = content.replaceAll("}","");
            int proLikesCount = Integer.parseInt(content);
            Product product = new Product(proId, proModel, proCategory, proName, proCurrentPrice, proRawPrice, proDiscount, proLikesCount);
            DataControl.addLine("products", product);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public ProductListResult getProductList(int pageNumber) {
        ProductListResult proList = new ProductListResult();
        List<Product> allPro=  DataControl.readAllProducts();
        int initialIndex = (pageNumber-1)*10;
        int endIndex = Math.min(initialIndex+10,allPro.size());
        proList.getAllProductsEachPage().clear();
        if(pageNumber>proList.getTotalPages()) return null;
        for(int i = initialIndex;i<endIndex;i++){
            proList.getAllProductsEachPage().add(allPro.get(i));
        }
        return proList;
    }
    public boolean deleteProduct(String proId){
        return DataControl.deleteOneLine("products", proId);
    }
    public void deleteAllProducts() {
       DataControl.deleteAll("products");
    }
    public List<Product> getProductListByKeyword(String keyword) {
        List<Product> proKey = new ArrayList<>();
        List<Product> allPro = DataControl.readAllProducts();
        List<String> allProName = DataControl.readAllProductsPart("name");
        for(int i = 0;i<allProName.size();i++){
            if(allProName.get(i).toLowerCase().contains(keyword.toLowerCase())){
                proKey.add(allPro.get(i));
            }
        }
        return proKey;
    }
    public Product getProductById(String productId) {
        List<String> allProID = DataControl.readAllProductsPart("id");
        int index=-1;
        for(int i=0;i<allProID.size();i++){
            if(allProID.get(i).equals(productId)){
                index=i;
            }
        }
        if(index==-1) return null;
        return DataControl.readAllProducts().get(index);
    }
    public void generateCategoryFigure() {
        CategoryFigure.showChart();
    }
    public void generateDiscountFigure() {
        DiscountFigure.showChart();
    }
    public void generateLikesCountFigure() {
        LikesCountFigure.showChart();
    }
    public void generateDiscountLikesCountFigure(){
        DiscountLikeChart.showChart();
    }
}
