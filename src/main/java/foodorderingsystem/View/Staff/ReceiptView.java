package foodorderingsystem.View.Staff;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.Model.Staff.Order;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.Optional;

public class ReceiptView {

    private final Controller controller;
    private final int tableNumber;

    public ReceiptView(Controller controller, int tableNumber) {
        this.controller = controller;
        this.tableNumber = tableNumber;
    }

    public void start(Stage primaryStage) {
        // Create a VBox layout for the receipt page
        VBox receiptBox = new VBox(20);
        receiptBox.setAlignment(Pos.TOP_CENTER);
        receiptBox.setPadding(new Insets(20, 20, 20, 20));
        receiptBox.setStyle("-fx-background-color: white; -fx-border-color: lightgray; -fx-border-width: 2px;");

        // Create a header label
        Label headerLabel = new Label("Receipt for Table " + tableNumber);
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        receiptBox.getChildren().add(headerLabel);

        // Retrieve orders from the database for the selected table
        List<Order> orders = controller.getOrdersForTable(tableNumber);

        double totalSum = 0.0;

        if (orders != null && !orders.isEmpty()) {
            int itemNumber = 1;
            for (Order order : orders) {
                Label orderLabel = new Label(itemNumber + ". " + order.toString());
                orderLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
                receiptBox.getChildren().add(orderLabel);
                totalSum += order.getPrice();
                itemNumber++;
            }

            // Add some spacing before the total sum label
            Region spacer = new Region();
            spacer.setPrefHeight(20);
            receiptBox.getChildren().add(spacer);

            // Display the total sum
            Label totalLabel = new Label("Total: RM" + String.format("%.2f", totalSum));
            totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
            receiptBox.getChildren().add(totalLabel);
        } else {
            Label noOrdersLabel = new Label("No orders found for this table.");
            noOrdersLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
            receiptBox.getChildren().add(noOrdersLabel);
        }

        // Create a "Paid" button
        Button paidButton = new Button("Paid");
        paidButton.setFont(new Font("Arial", 16));
        paidButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 4;");
        paidButton.setPrefSize(150, 50);
        paidButton.setOnAction(e -> showConfirmationDialog(primaryStage));
        receiptBox.getChildren().add(paidButton);

        // Create a "Back" button
        Button backButton = new Button("Back");
        backButton.setFont(new Font("Arial", 16));
        backButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 4;");
        backButton.setPrefSize(150, 50);
        backButton.setOnAction(e -> {
            StaffTableSelectionView tableSelectionView = new StaffTableSelectionView(controller);
            tableSelectionView.start(primaryStage);
        });
        receiptBox.getChildren().add(backButton);

        // Create a BorderPane to add a white frame around the receipt
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(receiptBox);
        borderPane.setStyle("-fx-background-color: lightgray; -fx-padding: 20px;");

        // Create a scene with the receipt page
        Scene receiptScene = new Scene(borderPane);

        // Set the scene to the primary stage
        primaryStage.setScene(receiptScene);
        primaryStage.setTitle("Receipt");

        // Maximize the window size to fit the screen dimensions
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.show();
    }

    private void showConfirmationDialog(Stage primaryStage) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Payment Confirmation");
        alert.setContentText("Are you sure the payment has been made?");

        ButtonType buttonTypeNo = new ButtonType("No");
        ButtonType buttonTypeConfirm = new ButtonType("Confirm");

        alert.getButtonTypes().setAll(buttonTypeNo, buttonTypeConfirm);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeConfirm) {
            // Remove orders for the table
            controller.removeOrdersForTable(tableNumber);

            // Navigate back to the StaffView page
            StaffView staffView = new StaffView(controller);
            staffView.start(primaryStage);
        }
    }
}