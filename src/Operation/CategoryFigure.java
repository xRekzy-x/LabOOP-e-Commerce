package Operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Controller.DataControl;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import Model.Product;
import javafx.application.Application;

public class CategoryFigure extends Application {
    public static void showChart() {
        Application.launch(CategoryFigure.class);
    }
    public void start(Stage stage){
        stage.setTitle("Products by Category");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Category");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Products");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Number of Products per Category");

        Map<String, Integer> categoryCount = new HashMap<>();
        List<Product> products = DataControl.readAllProducts();
        for (Product p : products) {
            if(categoryCount.get(p.getProCategory())==null)
                categoryCount.put(p.getProCategory(),1);
            else
                categoryCount.put(p.getProCategory(),categoryCount.get(p.getProCategory())+1);
        }
        List<Map.Entry<String,Integer>> entryList = DataControl.descendingSort(categoryCount);
        //assign the value to the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Category Count");
        for (Map.Entry<String, Integer> entry : entryList) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChart.getData().add(series);
        StackPane root = new StackPane(barChart);
        DataControl.showGraph(root, stage);
        DataControl.saveGraph("category_figure", stage.getScene());
    }
}
