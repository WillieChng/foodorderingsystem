package foodorderingsystem.View.Staff;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.Model.Staff.Order;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CartManagementView extends Application {
    private Controller controller;
    private Map<String, Image> nameToImageMap;

    public CartManagementView() {
        // No-argument constructor required by JavaFX
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setControllerAndImages(Controller controller, Map<String, Image> nameToImageMap) {
        this.controller = controller;
        this.nameToImageMap = nameToImageMap;
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Debug: Starting CartManagementView");

        // Create a GridPane layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-background-color: beige;");
        System.out.println("Debug: Created GridPane layout");

        // Create a header label and center it
        Label headerLabel = new Label("Cart Management");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        VBox headerBox = new VBox(headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        GridPane.setConstraints(headerBox, 0, 0, 2, 1);
        System.out.println("Debug: Created header label");

        // Create a VBox for the ListView and buttons
        VBox listViewWithButtons = new VBox(10);
        listViewWithButtons.setAlignment(Pos.CENTER);
        System.out.println("Debug: Created VBox for ListView and buttons");

        // Retrieve cart items from the controller
        Map<Integer, List<Order>> orders = controller.getItemsFromOrders();
        System.out.println("Debug: Retrieved cart items from controller: " + orders);

        if (orders != null && !orders.isEmpty()) {
            for (Map.Entry<Integer, List<Order>> entry : orders.entrySet()) {
                int tableNumber = entry.getKey();
                List<Order> orderList = entry.getValue();

                Label tableNumberLabel = new Label("Table Number: " + tableNumber);
                tableNumberLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
                listViewWithButtons.getChildren().add(tableNumberLabel);

                for (Order order : orderList) {
                    String name = order.getName().trim();
                    String normalizedName = name.replaceAll("\\s+", "");
                    int quantity = order.getQuantity();
                    System.out.println("Debug: Displaying item: " + name + ", Quantity: " + quantity);

                    GridPane itemGrid = new GridPane();
                    itemGrid.setHgap(10);
                    itemGrid.setVgap(10);
                    itemGrid.setPadding(new Insets(10, 10, 10, 10));
                    itemGrid.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-width: 1px;");
                    System.out.println("Debug: Created item grid for item: " + name);

                    // Ensure valid image for menu item
                    System.out.println("Debug: Looking for image with name: " + normalizedName);
                    Image image = nameToImageMap.get(normalizedName);
                    if (image == null) {
                        System.out.println("Debug: Image not found for item: " + normalizedName + ", using default image.");
                        image = new Image("file:src/main/resources/images/default.png");
                    } else {
                        System.out.println("Debug: Found image for item: " + normalizedName);
                    }
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    GridPane.setConstraints(imageView, 0, 0, 1, 4);
                    System.out.println("Debug: Created image view for item: " + name);

                    Label nameLabel = new Label(name);
                    nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                    GridPane.setConstraints(nameLabel, 1, 0);
                    System.out.println("Debug: Created name label for item: " + name);

                    Label quantityLabel = new Label("Quantity: " + quantity);
                    GridPane.setConstraints(quantityLabel, 1, 1);
                    System.out.println("Debug: Created quantity label for item: " + name);

                    Button clearButton = new Button("Clear");
                    clearButton.setOnAction(e -> {
                        // Remove the item from the orders table and refresh the view
                        controller.removeItemFromOrders(tableNumber, name);
                        System.out.println("Debug: Removed item: " + name + " from table number: " + tableNumber);
                        start(primaryStage); // Refresh the view
                    });
                    GridPane.setConstraints(clearButton, 1, 2);
                    System.out.println("Debug: Created clear button for item: " + name);

                    itemGrid.getChildren().addAll(imageView, nameLabel, quantityLabel, clearButton);
                    listViewWithButtons.getChildren().add(itemGrid);
                    System.out.println("Debug: Added item grid to listViewWithButtons for item: " + name);
                }
            }
        } else {
            System.out.println("Debug: No items in cart.");
            Label noItemsLabel = new Label("No Items In Cart :(");
            noItemsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            listViewWithButtons.getChildren().add(noItemsLabel);
        }

        // Create a ScrollPane to hold the listViewWithButtons
        ScrollPane scrollPane = new ScrollPane(listViewWithButtons);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600);
        GridPane.setConstraints(scrollPane, 0, 1, 2, 1);
        System.out.println("Debug: Created ScrollPane");

        // Create a "Back" button
        Button backButton = new Button("Back");
        backButton.setFont(new Font("Arial", 16));
        backButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 4;");
        backButton.setPrefSize(150, 50);
        GridPane.setConstraints(backButton, 0, 2);
        System.out.println("Debug: Created Back button");

        // Add event handler to switch views
        backButton.setOnAction(e -> {
            StaffView staffView = new StaffView(controller);
            staffView.start(primaryStage);
            System.out.println("Debug: Switched to StaffView");
        });

        // Add all components to the grid
        grid.getChildren().addAll(headerBox, scrollPane, backButton);
        System.out.println("Debug: Added all components to the grid");

        // Update the scene's root node
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            scene = new Scene(grid, 800, 600);
            primaryStage.setScene(scene);
        } else {
            scene.setRoot(grid);
        }
        System.out.println("Debug: Updated the scene's root node");

        primaryStage.setTitle("Cart Management");
        primaryStage.show();
        System.out.println("Debug: Showed primary stage");
    }

    public Map<String, Image> loadImages() {
        Map<String, Image> nameToImageMap = new HashMap<>();
        nameToImageMap.put("ClassicMargheritaPizza", new Image("file:src/main/resources/images/ClassicMargheritaPizza.png"));
        nameToImageMap.put("VeganBuddhaBowl", new Image("file:src/main/resources/images/VeganBuddhaBowl.png"));
        nameToImageMap.put("BerryAcaiSmoothieBowl", new Image("file:src/main/resources/images/BerryAcaiSmoothieBowl.png"));
        nameToImageMap.put("SpicyChickenTacos", new Image("file:src/main/resources/images/SpicyChickenTacos.png"));
        nameToImageMap.put("BeefRamenNoodleBowl", new Image("file:src/main/resources/images/BeefRamenNoodleBowl.png"));
        // Add more mappings as needed
        return nameToImageMap;
    }

    public static void main(String[] args) {
        launch(args);
    }
}