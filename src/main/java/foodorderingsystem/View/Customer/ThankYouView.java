package foodorderingsystem.View.Customer;

import foodorderingsystem.Controller.Controller;
import javafx.geometry.*;
import foodorderingsystem.View.UI;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ThankYouView extends UI {

    public ThankYouView(Controller controller) {
        super(controller);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a VBox layout for the thank you page
        VBox thankYouBox = new VBox(20);
        thankYouBox.setAlignment(Pos.CENTER);
        thankYouBox.setPadding(new Insets(20, 20, 20, 20));
        thankYouBox.setStyle("-fx-background-color: beige;");

        // Create thank you labels
        Label thankYouLabel = new Label("Thank You For Ordering");
        thankYouLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        Label waitLabel = new Label("Please Wait For Your Order :)");
        waitLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));

        // Create an "OK" button
        Button okButton = new Button("OK");
        okButton.setFont(new Font("Arial", 16));
        okButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 4;");
        okButton.setPrefSize(150, 50);
        okButton.setOnAction(e -> {
            // Navigate back to the TableSelectionView page
            TableSelectionView tableSelectionView = new TableSelectionView(controller);
            tableSelectionView.start(primaryStage);
        });

        // Add labels and button to the VBox
        thankYouBox.getChildren().addAll(thankYouLabel, waitLabel, okButton);

        // Create a scene with the thank you page
        Scene thankYouScene = new Scene(thankYouBox);

        // Set the scene to the primary stage
        primaryStage.setScene(thankYouScene);
        primaryStage.setTitle("Thank You");

        // Maximize the window size to fit the screen dimensions
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.show();
    }
}