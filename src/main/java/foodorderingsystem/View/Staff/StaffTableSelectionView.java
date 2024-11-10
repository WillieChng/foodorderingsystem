package foodorderingsystem.View.Staff;

import foodorderingsystem.Controller.Controller;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StaffTableSelectionView {

    private final Controller controller;

    public StaffTableSelectionView(Controller controller) {
        this.controller = controller;
    }

    public void start(Stage primaryStage) {
        // Create a GridPane layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-background-color: beige;");

        // Create a header label
        Label headerLabel = new Label("Table Selection");
        headerLabel.setFont(new Font("Arial", 20));
        headerLabel.setTextFill(Color.BLACK);
        GridPane.setConstraints(headerLabel, 0, 0);

        // Create a Spinner for table selection
        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, 1);
        spinner.setValueFactory(valueFactory);
        spinner.setStyle("-fx-font-size: 50;");
        GridPane.setConstraints(spinner, 0, 1);

        // Create a "Proceed" button
        Button proceedButton = new Button("Proceed");
        proceedButton.setFont(new Font("Arial", 16));
        proceedButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 4;");
        proceedButton.setPrefSize(150, 50);
        GridPane.setConstraints(proceedButton, 0, 2);

        // Add event handler to switch to ReceiptView
        proceedButton.setOnAction(e -> {
            int selectedTable = spinner.getValue();
            ReceiptView receiptView = new ReceiptView(controller, selectedTable);
            receiptView.start(primaryStage);
        });

        // Add all components to the grid
        grid.getChildren().addAll(headerLabel, spinner, proceedButton);

        // Update the scene's root node
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            scene = new Scene(grid, 400, 300);
            primaryStage.setScene(scene);
        } else {
            scene.setRoot(grid);
        }

        primaryStage.setTitle("Table Selection");
        primaryStage.show();
    }
}