package foodorderingsystem.View.Staff;

import foodorderingsystem.Controller.Controller;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StaffView extends Application {
    private Controller controller;

    public StaffView() {
        // No-argument constructor required by JavaFX
    }

    public StaffView(Controller controller) {
        this.controller = controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void start(Stage primaryStage) {
        // Create buttons
        Button cartManagementButton = new Button("Cart Management");
        Button generateReceiptButton = new Button("Generate Receipt");
        Button manageMenuButton = new Button("Manage Menu");

        // Set button styles to make them bigger
        cartManagementButton.setStyle("-fx-font-size: 20px; -fx-padding: 15px;");
        generateReceiptButton.setStyle("-fx-font-size: 20px; -fx-padding: 15px;");
        manageMenuButton.setStyle("-fx-font-size: 20px; -fx-padding: 15px;");

        // Add event handler to the "Cart Management" button
        cartManagementButton.setOnAction(e -> {
            CartManagementView cartManagementView = new CartManagementView();
            cartManagementView.setController(controller);
            cartManagementView.start(primaryStage);
        });

        // Add event handler to the "Generate Receipt" button
        generateReceiptButton.setOnAction(e -> {
            StaffTableSelectionView tableSelectionView = new StaffTableSelectionView(controller);
            tableSelectionView.start(primaryStage);
        });

        // Add event handler to the "Manage Menu" button
        manageMenuButton.setOnAction(e -> {
            ManageMenuView manageMenuView = new ManageMenuView(controller);
            manageMenuView.start(primaryStage);
        });

        // Create VBox and set alignment and spacing
        VBox vbox = new VBox(20, cartManagementButton, generateReceiptButton, manageMenuButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: beige;");

        // Create scene and set stage
        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Staff Main Page");
        primaryStage.show();
    }
}