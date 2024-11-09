package foodorderingsystem.View.Staff;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.Model.Order;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class CartManagementView extends Application {
    private Controller controller;

    public CartManagementView(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void start(Stage primaryStage) {
        // Retrieve the list of orders
        List<Order> orders = controller.getOrders();

        // Create a VBox to hold the order details
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: beige;");

        // Add a label for each order
        for (Order order : orders) {
            Label orderLabel = new Label("Table " + order.getTableNumber() + ": " + order.getDetails());
            orderLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
            vbox.getChildren().add(orderLabel);
        }

        // Create a scene and set the stage
        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cart Management");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}