package foodorderingsystem.View.Customer;

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

public class MenuView extends UI {

    public MenuView(Controller controller) {
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
        Label headerLabel = new Label("Menu");
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

                Button addButton = new Button("Add");
                addButton.setOnAction(e -> {
                    // Add the item to the cart
                    controller.addItemToCart(item);
                    System.out.println("Added to cart: " + item.getName());
                });
                GridPane.setConstraints(addButton, 1, 3);

                itemGrid.getChildren().addAll(imageView, nameLabel, descriptionLabel, priceLabel, addButton);
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

        // Create a "Proceed to Cart" button
        Button proceedToCartButton = new Button("Proceed to Cart");
        proceedToCartButton.setFont(new Font("Arial", 16));
        proceedToCartButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 4;");
        proceedToCartButton.setPrefSize(150, 50);
        GridPane.setConstraints(proceedToCartButton, 1, 2);

        // Add event handlers to switch views
        backButton.setOnAction(e -> {
            TableSelectionView tableSelectionView = new TableSelectionView(controller);
            tableSelectionView.start(primaryStage);
        });
        proceedToCartButton.setOnAction(e -> {
            CartView cartView = new CartView(controller);
            cartView.start(primaryStage);
        });

        // Add all components to the grid
        grid.getChildren().addAll(headerBox, scrollPane, backButton, proceedToCartButton);

        // Update the scene's root node
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            scene = new Scene(grid, 800, 600);
            primaryStage.setScene(scene);
        } else {
            scene.setRoot(grid);
        }

        primaryStage.setTitle("Food Ordering System - Menu");
        primaryStage.show();
    }
}