package foodorderingsystem.View.Customer;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.View.UI;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.stage.*;

public class TableSelectionView extends UI {

    public TableSelectionView(Controller controller) {
        super(controller);
    }

    @Override
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

        // Add event handler to switch to MenuView
        proceedButton.setOnAction(e -> {
            int selectedTable = spinner.getValue();
            controller.setTableNumber(selectedTable);

            MenuView menuView = new MenuView(controller);
            menuView.start(primaryStage);
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

        primaryStage.setTitle("Food Ordering System");
        primaryStage.show();
    }
}