package Operation;

import java.util.List;

import Controller.DataControl;
import Model.Product;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;

public class DiscountLikeChart extends Application{
    @Override
     public void start(Stage stage){
        stage.setTitle("Discount vs Likes Count");

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Likes Count");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Discount (%)");

        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle("Discount vs Likes Count");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Discount vs Likes");

        List<Product> products = DataControl.readAllProducts();
        for (Product p : products) {
            series.getData().add(new XYChart.Data<>(p.getProLikesCount(), p.getProDiscount()));
        }
        scatterChart.getData().add(series);
        StackPane root = new StackPane(scatterChart);
        DataControl.showGraph(root, stage);
        DataControl.saveGraph("discount_vs_likes", stage.getScene());
    }
    public static void showChart() {
        Application.launch(DiscountLikeChart.class);
    }
}
