package foodorderingsystem.View;

import foodorderingsystem.Controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public abstract class UI {
    protected Controller controller;

    public UI(Controller controller) {
        this.controller = controller;
    }

    public abstract void start(Stage primaryStage);

    protected GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-background-color: beige;");
        return grid;
    }

    protected VBox createHeader(String title) {
        Label headerLabel = new Label(title);
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        VBox headerBox = new VBox(headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        GridPane.setConstraints(headerBox, 0, 0, 2, 1);
        return headerBox;
    }

    protected void updateScene(Stage primaryStage, GridPane grid) {
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            scene = new Scene(grid, 800, 600);
            primaryStage.setScene(scene);
        } else {
            scene.setRoot(grid);
        }
        primaryStage.show();
    }

    protected VBox createTableSelectionBox(String labelText, String buttonText, Spinner<Integer> spinner, Runnable onProceed) {
        VBox tableSelectionBox = new VBox(10);
        tableSelectionBox.setAlignment(Pos.CENTER);

        Label tableNumberLabel = new Label(labelText);
        tableNumberLabel.setFont(new Font("Arial", 20));
        tableNumberLabel.setTextFill(Color.BLACK);

        spinner.setStyle("-fx-font-size: 50;");

        Button proceedButton = new Button(buttonText);
        proceedButton.setFont(new Font("Arial", 16));
        proceedButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 4;");
        proceedButton.setPrefSize(150, 50);
        proceedButton.setOnAction(e -> onProceed.run());

        tableSelectionBox.getChildren().addAll(tableNumberLabel, spinner, proceedButton);
        GridPane.setConstraints(tableSelectionBox, 0, 1, 2, 1);
        return tableSelectionBox;
    }
}