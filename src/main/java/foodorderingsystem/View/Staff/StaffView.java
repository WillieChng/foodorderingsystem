package foodorderingsystem.View.Staff;

import foodorderingsystem.Commands.CartManagementCommand;
import foodorderingsystem.Commands.Command;
import foodorderingsystem.View.UI;
import foodorderingsystem.Commands.GenerateReceiptCommand;
import foodorderingsystem.Commands.Invoker;
import foodorderingsystem.Commands.ManageMenuCommand;
import foodorderingsystem.Controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StaffView extends UI {

    public StaffView(Controller controller) {
        super(controller);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = createGridPane();
        VBox buttonBox = createButtonBox(primaryStage);

        // Add all components to the grid
        grid.getChildren().addAll(createHeader("Staff View"), buttonBox);

        // Update the scene's root node
        updateScene(primaryStage, grid);
    }

    private VBox createButtonBox(Stage primaryStage) {
        VBox buttonBox = new VBox(20);
        buttonBox.setAlignment(Pos.CENTER);

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

        // Create an invoker
        Invoker invoker = new Invoker();

        // Set button actions
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

        buttonBox.getChildren().addAll(cartManagementButton, generateReceiptButton, manageMenuButton);
        GridPane.setConstraints(buttonBox, 0, 1, 2, 1);
        return buttonBox;
    }
}