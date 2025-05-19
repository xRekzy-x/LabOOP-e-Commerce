package Operation;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//import com.google.gson.Gson;


import Controller.DataControl;

import Model.Product;
import Model.ProductListResult;
import javafx.application.Platform;
import javafx.scene.chart.BarChart;
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
        //CategoryFigure.showChart();
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("Products by Category");
            BarChart<String,Number> barChart = DataControl.creataBarChart("Number of Products per Category", "Category", "Number of Products");

            List<Integer> arr = new ArrayList<>();
            for(int i =0;i<DataControl.readAllProducts().size();i++){
                arr.add(1);
            }
            barChart.getData().add(DataControl.assignValueToBarChart("Category Count", DataControl.readAllProductsPart("category"), arr, "descending",0));
            StackPane root = new StackPane(barChart);
            DataControl.showGraph(root, stage);
            DataControl.saveGraph("category_figure", stage.getScene());
        });
    }
    public void generateDiscountFigure() {
        //DiscountFigure.showChart();
        Platform.runLater(() -> {
            Stage stage = new Stage();
            int low = 0;
            int medium = 0;
            int high = 0;
            List<String> discountString = DataControl.readAllProductsPart("discount");
            for(String dis : discountString){
                Double discount = Double.parseDouble(dis);
                if(discount<30) low++;
                else if(discount<60) medium++;
                else high++;
            }
            PieChart pieChart = new PieChart();
            pieChart.setTitle("Discount distribution");
            if(low>0) pieChart.getData().add(new PieChart.Data("< 30%", low));
            if(medium>0) pieChart.getData().add(new PieChart.Data("30% - 60%", medium));
            if(high>0) pieChart.getData().add(new PieChart.Data("> 60%", high));
            StackPane root = new StackPane(pieChart);
            DataControl.showGraph(root, stage);
            DataControl.saveGraph("Discount_Distribution", stage.getScene());
        });
    }
    public void generateLikesCountFigure() {
        // LikesCountFigure.showChart();
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("Likes count of each category");
            BarChart<String, Number> barChart = DataControl.creataBarChart("Number of likes per category", "category", "number of likes");
        
            List<Integer> arr = new ArrayList<>();
            List<String> like = DataControl.readAllProductsPart("likes_counts");
            for(int i=0;i<DataControl.readAllProducts().size();i++){
                arr.add(Integer.parseInt(like.get(i)));
            }
            barChart.getData().add(DataControl.assignValueToBarChart("Likes count",DataControl.readAllProductsPart("category"),arr,"ascending",0));
            StackPane root = new StackPane(barChart);
            DataControl.showGraph(root, stage);
            DataControl.saveGraph("Likes_count_figure", stage.getScene());
        });
    }
    public void generateDiscountLikesCountFigure(){
        // DiscountLikeChart.showChart();
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("Discount vs Likes Count");

            NumberAxis xAxis = new NumberAxis();
            xAxis.setLabel("Discount (%)");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Likes count");

            ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
            scatterChart.setTitle("Discount vs Likes Count");
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Discount vs Likes");

            List<Product> products = DataControl.readAllProducts();
            for (Product p : products) {
                series.getData().add(new XYChart.Data<>(p.getProDiscount(), p.getProLikesCount()));
            }
            scatterChart.getData().add(series);
            StackPane root = new StackPane(scatterChart);
            DataControl.showGraph(root, stage);
            DataControl.saveGraph("discount_vs_likes", stage.getScene());
        });
    }
}
