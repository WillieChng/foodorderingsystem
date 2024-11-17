package foodorderingsystem.View.Staff;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.Model.Customer.MenuItem;
import foodorderingsystem.View.UI;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;

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
        Button addItemButton = createAddItemButton(primaryStage);

        // Add all components to the grid
        grid.getChildren().addAll(createHeader("Manage Menu"), scrollPane, backButton, addItemButton);

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

        Button removeButton = new Button("Remove From Menu");
        removeButton.setOnAction(e -> showRemoveConfirmationDialog(item, primaryStage));
        GridPane.setConstraints(removeButton, 1, 3);

        Button updateButton = new Button("Update Information");
        updateButton.setOnAction(e -> {
            controller.showUpdateMenuItemForm(item);
            start(primaryStage); // Refresh the view
        });
        GridPane.setConstraints(updateButton, 1, 4);

        itemGrid.getChildren().addAll(imageView, nameLabel, descriptionLabel, priceLabel, removeButton, updateButton);
        return itemGrid;
    }

    private void showRemoveConfirmationDialog(MenuItem item, Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Remove Item");
        alert.setContentText("Are you sure you want to remove " + item.getName() + " from the menu?");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        alert.showAndWait().ifPresent(type -> {
            if (type == buttonTypeYes) {
                controller.removeMenuItem(item);
                start(primaryStage); // Refresh the view
                System.out.println("Removed from menu: " + item.getName());
            }
        });
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

    private Button createAddItemButton(Stage primaryStage) {
        Button addItemButton = new Button("Add Item To Menu");
        addItemButton.setFont(new Font("Arial", 16));
        addItemButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 4;");
        addItemButton.setPrefSize(200, 50);
        GridPane.setConstraints(addItemButton, 1, 2);

        addItemButton.setOnAction(e -> {
            controller.showAddMenuItemForm();
            start(primaryStage); // Refresh the view
        });

        return addItemButton;
    }
}