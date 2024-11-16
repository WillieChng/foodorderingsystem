package foodorderingsystem.View.Staff;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.Model.Customer.MenuItem;
import foodorderingsystem.View.UI;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
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
        // Create a GridPane layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-background-color: beige;");

        // Create a header label and center it
        Label headerLabel = new Label("Manage Menu");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        VBox headerBox = new VBox(headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        GridPane.setConstraints(headerBox, 0, 0, 2, 1);

        // Create a VBox for the ListView and buttons
        VBox listViewWithButtons = new VBox(10);
        listViewWithButtons.setAlignment(Pos.CENTER);

        // Retrieve menu items from the database
        List<MenuItem> menuItems = controller.getMenuItems();

        if (menuItems != null) {
            for (MenuItem item : menuItems) {
                GridPane itemGrid = new GridPane();
                itemGrid.setHgap(10);
                itemGrid.setVgap(10);
                itemGrid.setPadding(new Insets(10, 10, 10, 10));
                itemGrid.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-width: 1px;");

                // Retrieve the image from the database
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
                removeButton.setOnAction(e -> {
                    // Display confirmation dialog
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Remove Item");
                    alert.setContentText("Are you sure you want to remove " + item.getName() + " from the menu?");

                    ButtonType buttonTypeYes = new ButtonType("Yes");
                    ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                    alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                    alert.showAndWait().ifPresent(type -> {
                        if (type == buttonTypeYes) {
                            // Remove the item from the menu
                            controller.removeMenuItem(item);
                            start(primaryStage); // Refresh the view
                            System.out.println("Removed from menu: " + item.getName());
                        }
                    });
                });
                GridPane.setConstraints(removeButton, 1, 3);

                Button updateButton = new Button("Update Information");
                updateButton.setOnAction(e -> {
                    // Show the update form for the item
                    controller.showUpdateMenuItemForm(item);
                    start(primaryStage); // Refresh the view
                    System.out.println("Update item: " + item.getName());
                });
                GridPane.setConstraints(updateButton, 1, 4);

                itemGrid.getChildren().addAll(imageView, nameLabel, descriptionLabel, priceLabel, removeButton, updateButton);
                listViewWithButtons.getChildren().add(itemGrid);
            }
        }

        // Create a ScrollPane to hold the listViewWithButtons
        ScrollPane scrollPane = new ScrollPane(listViewWithButtons);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600);
        GridPane.setConstraints(scrollPane, 0, 1, 2, 1);

        // Create a "Back" button
        Button backButton = new Button("Back");
        backButton.setFont(new Font("Arial", 16));
        backButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 4;");
        backButton.setPrefSize(150, 50);
        GridPane.setConstraints(backButton, 0, 2);

        // Add event handler to switch views
        backButton.setOnAction(e -> {
            StaffView staffView = new StaffView(controller);
            staffView.start(primaryStage);
        });

        // Create an "Add Item To Menu" button
        Button addItemButton = new Button("Add Item To Menu");
        addItemButton.setFont(new Font("Arial", 16));
        addItemButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 4;");
        addItemButton.setPrefSize(200, 50);
        GridPane.setConstraints(addItemButton, 1, 2);

        // Add event handler to show the add item form
        addItemButton.setOnAction(e -> {
            controller.showAddMenuItemForm();
            start(primaryStage); // Refresh the view
        });

        // Add all components to the grid
        grid.getChildren().addAll(headerBox, scrollPane, backButton, addItemButton);

        // Update the scene's root node
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            scene = new Scene(grid, 800, 600);
            primaryStage.setScene(scene);
        } else {
            scene.setRoot(grid);
        }

        primaryStage.setTitle("Food Ordering System - Manage Menu");
        primaryStage.show();
    }
}