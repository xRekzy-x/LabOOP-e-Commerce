package Operation;
import java.util.List;

import Controller.DataControl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
public class DiscountFigure extends Application {
    @Override
    public void start(Stage stage){
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
        
    }
    public static void showChart() {
        Application.launch(DiscountFigure.class);
    }
}
