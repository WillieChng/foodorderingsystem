package foodorderingsystem.View.Customer;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.View.UI;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ThankYouView extends UI {

    public ThankYouView(Controller controller) {
        super(controller);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = createGridPane();
        VBox thankYouBox = createThankYouBox(primaryStage);

        // Add all components to the grid
        grid.getChildren().addAll(createHeader("Thank You!"), thankYouBox);

        // Update the scene's root node
        updateScene(primaryStage, grid);
    }

    private VBox createThankYouBox(Stage primaryStage) {
        VBox thankYouBox = new VBox(10);
        thankYouBox.setAlignment(Pos.CENTER);

        Label thankYouLabel = new Label("Please wait patiently for your order :)");
        thankYouLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button backButton = new Button("Okay");
        backButton.setFont(new Font("Arial", 16));
        backButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 4;");
        backButton.setPrefSize(150, 50);
        backButton.setOnAction(e -> {
            TableSelectionView tableSelectionView = new TableSelectionView(controller);
            tableSelectionView.start(primaryStage);
        });

        thankYouBox.getChildren().addAll(thankYouLabel, backButton);
        GridPane.setConstraints(thankYouBox, 0, 1, 2, 1);
        return thankYouBox;
    }
}