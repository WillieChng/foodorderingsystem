package foodorderingsystem.View.Customer;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.Model.MenuItem;
import foodorderingsystem.View.UI;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Map;

public class CartView extends UI {

    private final Image[] images;

    public CartView(Controller controller, Image[] images) {
        super(controller);
        this.images = images;
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
        Label headerLabel = new Label("Cart");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        VBox headerBox = new VBox(headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        GridPane.setConstraints(headerBox, 0, 0, 2, 1);

        // Create a VBox for the ListView and buttons
        VBox listViewWithButtons = new VBox(10);
        listViewWithButtons.setAlignment(Pos.CENTER);

        // Retrieve cart items from the controller
        Map<MenuItem, Integer> cartItems = controller.getCartItems();

        if (cartItems != null && !cartItems.isEmpty()) {
            for (Map.Entry<MenuItem, Integer> entry : cartItems.entrySet()) {
                MenuItem item = entry.getKey();
                int quantity = entry.getValue();

                GridPane itemGrid = new GridPane();
                itemGrid.setHgap(10);
                itemGrid.setVgap(10);
                itemGrid.setPadding(new Insets(10, 10, 10, 10));
                itemGrid.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-width: 1px;");

                ImageView imageView = new ImageView(images[item.getID() - 1]);
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

                Label quantityLabel = new Label("Quantity: " + quantity);
                GridPane.setConstraints(quantityLabel, 1, 3);

                Button addButton = new Button("+");
                addButton.setOnAction(e -> {
                    controller.addItemToCart(item);
                    start(primaryStage); // Refresh the view
                });

                Button removeButton = new Button("-");
                removeButton.setOnAction(e -> {
                    controller.removeItemFromCart(item);
                    start(primaryStage); // Refresh the view
                });

                HBox buttonBox = new HBox(5, addButton, removeButton); // HBox with 5px spacing
                GridPane.setConstraints(buttonBox, 1, 4, 2, 1);

                itemGrid.getChildren().addAll(imageView, nameLabel, descriptionLabel, priceLabel, quantityLabel, buttonBox);
                listViewWithButtons.getChildren().add(itemGrid);
            }
        } else {
            Label noItemsLabel = new Label("No Items In Cart :(");
            noItemsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            listViewWithButtons.getChildren().add(noItemsLabel);
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

        // Create a "Checkout" button
        Button checkoutButton = new Button("Checkout");
        checkoutButton.setFont(new Font("Arial", 16));
        checkoutButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 4;");
        checkoutButton.setPrefSize(150, 50);
        GridPane.setConstraints(checkoutButton, 1, 2);

        // Add event handlers to switch views
        backButton.setOnAction(e -> {
            MenuView menuView = new MenuView(controller);
            menuView.start(primaryStage);
        });
        checkoutButton.setOnAction(e -> {
            // Handle checkout logic here
            System.out.println("Proceeding to checkout...");
        });

        // Add all components to the grid
        grid.getChildren().addAll(headerBox, scrollPane, backButton, checkoutButton);

        // Update the scene's root node
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            scene = new Scene(grid, 800, 600);
            primaryStage.setScene(scene);
        } else {
            scene.setRoot(grid);
        }

        primaryStage.setTitle("Food Ordering System - Cart");
        primaryStage.show();
    }
}