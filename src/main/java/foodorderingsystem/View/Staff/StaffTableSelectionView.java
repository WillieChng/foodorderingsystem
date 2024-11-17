package foodorderingsystem.View.Staff;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.View.UI;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.text.Font;

public class StaffTableSelectionView extends UI {

    public StaffTableSelectionView(Controller controller) {
        super(controller);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = createGridPane();
        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, 1);
        spinner.setValueFactory(valueFactory);

        VBox tableSelectionBox = createTableSelectionBox("Select Table Number:", "Proceed", spinner, () -> {
            int selectedTable = spinner.getValue();
            ReceiptView receiptView = new ReceiptView(controller, selectedTable);
            receiptView.start(primaryStage);
        });

        // Create a "Back" button
        Button backButton = createBackButton(primaryStage);

        // Add the "Back" button to the VBox
        tableSelectionBox.getChildren().add(backButton);

        // Add all components to the grid
        grid.getChildren().addAll(createHeader("Table Selection"), tableSelectionBox);

        // Update the scene's root node
        updateScene(primaryStage, grid);
    }

    private Button createBackButton(Stage primaryStage) {
        Button backButton = new Button("Back");
        backButton.setFont(new Font("Arial", 16));
        backButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 4;");
        backButton.setPrefSize(150, 50);

        backButton.setOnAction(e -> {
            StaffView staffView = new StaffView(controller);
            staffView.start(primaryStage);
        });

        return backButton;
    }
}