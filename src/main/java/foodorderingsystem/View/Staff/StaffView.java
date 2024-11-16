package foodorderingsystem.View.Staff;

import foodorderingsystem.Commands.CartManagementCommand;
import foodorderingsystem.Commands.Command;
import foodorderingsystem.Commands.GenerateReceiptCommand;
import foodorderingsystem.Commands.Invoker;
import foodorderingsystem.Commands.ManageMenuCommand;
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

        // Create command objects
        Command cartManagementCommand = new CartManagementCommand(controller, primaryStage);
        Command generateReceiptCommand = new GenerateReceiptCommand(controller, primaryStage);
        Command manageMenuCommand = new ManageMenuCommand(controller, primaryStage);

        // Create an Invoker
        Invoker invoker = new Invoker();

        // Add event handlers to the buttons
        cartManagementButton.setOnAction(e -> {
            invoker.setCommand(cartManagementCommand);
            invoker.executeCommand();
        });
        generateReceiptButton.setOnAction(e -> {
            invoker.setCommand(generateReceiptCommand);
            invoker.executeCommand();
        });
        manageMenuButton.setOnAction(e -> {
            invoker.setCommand(manageMenuCommand);
            invoker.executeCommand();
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