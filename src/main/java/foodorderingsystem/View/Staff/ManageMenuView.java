package foodorderingsystem.View.Staff;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.Model.Customer.MenuItem;
import foodorderingsystem.View.UI;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ManageMenuView extends UI {

    public ManageMenuView(Controller controller) {
        super(controller);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = createGridPane();
        VBox listViewWithButtons = createListViewWithButtons(primaryStage);
        ScrollPane scrollPane = createScrollPane(listViewWithButtons);
        Button backButton = createBackButton(primaryStage);

        // Add all components to the grid
        grid.getChildren().addAll(createHeader("Manage Menu"), scrollPane, backButton);

        // Update the scene's root node
        updateScene(primaryStage, grid);
    }

    private VBox createListViewWithButtons(Stage primaryStage) {
        VBox listViewWithButtons = new VBox(10);
        listViewWithButtons.setAlignment(Pos.CENTER);

        List<MenuItem> menuItems = controller.getMenuItems();

        if (menuItems != null && !menuItems.isEmpty()) {
            for (MenuItem item : menuItems) {
                listViewWithButtons.getChildren().add(createItemGrid(item, primaryStage));
            }
        } else {
            Label noItemsLabel = new Label("No Items Available :(");
            noItemsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            listViewWithButtons.getChildren().add(noItemsLabel);
        }

        return listViewWithButtons;
    }

    private GridPane createItemGrid(MenuItem item, Stage primaryStage) {
        GridPane itemGrid = new GridPane();
        itemGrid.setHgap(10);
        itemGrid.setVgap(10);
        itemGrid.setPadding(new Insets(10, 10, 10, 10));
        itemGrid.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-width: 1px;");

        ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(item.getImage())));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        GridPane.setConstraints(imageView, 0, 0, 1, 4);

        Label nameLabel = new Label(item.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        GridPane.setConstraints(nameLabel, 1, 0);

        Label descriptionLabel = new Label(item.getDescription());
        GridPane.setConstraints(descriptionLabel, 1, 1);

        Label priceLabel = new Label("RM" + item.getPrice());
        GridPane.setConstraints(priceLabel, 1, 2);

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            // Implement update functionality
        });
        GridPane.setConstraints(updateButton, 1, 3);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            controller.removeMenuItem(item);
            start(primaryStage); // Refresh the view
        });
        GridPane.setConstraints(deleteButton, 1, 4);

        itemGrid.getChildren().addAll(imageView, nameLabel, descriptionLabel, priceLabel, updateButton, deleteButton);
        return itemGrid;
    }

    private ScrollPane createScrollPane(VBox listViewWithButtons) {
        ScrollPane scrollPane = new ScrollPane(listViewWithButtons);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600);
        GridPane.setConstraints(scrollPane, 0, 1, 2, 1);
        return scrollPane;
    }

    private Button createBackButton(Stage primaryStage) {
        Button backButton = new Button("Back");
        backButton.setFont(new Font("Arial", 16));
        backButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 4;");
        backButton.setPrefSize(150, 50);
        GridPane.setConstraints(backButton, 0, 2);

        backButton.setOnAction(e -> {
            StaffView staffView = new StaffView(controller);
            staffView.start(primaryStage);
        });

        return backButton;
    }
}