package foodorderingsystem.View.Staff;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.Model.Staff.Order;
import foodorderingsystem.View.UI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

public class CartManagementView extends UI {
    private Timeline timeline;
    private boolean isActive;

    public CartManagementView(Controller controller) {
        super(controller);
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Debug: Starting CartManagementView");
        isActive = true;

        GridPane grid = createGridPane();
        VBox listViewWithButtons = createListViewWithButtons(primaryStage);
        ScrollPane scrollPane = createScrollPane(listViewWithButtons);
        Button backButton = createBackButton(primaryStage);

        // Add all components to the grid
        grid.getChildren().addAll(createHeader("Cart Management"), scrollPane, backButton);

        // Update the scene's root node
        updateScene(primaryStage, grid);

        // Start the refresh timeline
        startRefreshTimeline(primaryStage);
    }

    private void startRefreshTimeline(Stage primaryStage) {
        timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            if (isActive) {
                refresh(primaryStage);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void stopRefreshTimeline() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    private VBox createListViewWithButtons(Stage primaryStage) {
        VBox listViewWithButtons = new VBox(10);
        listViewWithButtons.setAlignment(Pos.CENTER);

        Map<Integer, List<Order>> orders = controller.getItemsFromOrders();
        if (orders == null) {
            System.err.println("Error: orders is null");
            return listViewWithButtons;
        }
        System.out.println("Debug: Retrieved cart items from controller: " + orders);

        if (!orders.isEmpty()) {
            for (Map.Entry<Integer, List<Order>> entry : orders.entrySet()) {
                int tableNumber = entry.getKey();
                List<Order> orderList = entry.getValue();

                Label tableNumberLabel = new Label("Table Number: " + tableNumber);
                tableNumberLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
                listViewWithButtons.getChildren().add(tableNumberLabel);

                for (Order order : orderList) {
                    listViewWithButtons.getChildren().add(createItemGrid(order, primaryStage));
                }
            }
        } else {
            Label noItemsLabel = new Label("No Items In Cart :(");
            noItemsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            listViewWithButtons.getChildren().add(noItemsLabel);
        }

        return listViewWithButtons;
    }

    private GridPane createItemGrid(Order order, Stage primaryStage) {
        GridPane itemGrid = new GridPane();
        itemGrid.setHgap(10);
        itemGrid.setVgap(10);
        itemGrid.setPadding(new Insets(10, 10, 10, 10));
        itemGrid.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-width: 1px;");

        ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(order.getImage())));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        GridPane.setConstraints(imageView, 0, 0, 1, 4);

        Label nameLabel = new Label(order.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        GridPane.setConstraints(nameLabel, 1, 0);

        Label quantityLabel = new Label("Quantity: " + order.getQuantity());
        GridPane.setConstraints(quantityLabel, 1, 1);

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> {
            controller.markOrderAsServed(order);
            refresh(primaryStage); // Refresh the view
        });
        GridPane.setConstraints(clearButton, 1, 2);

        itemGrid.getChildren().addAll(imageView, nameLabel, quantityLabel, clearButton);
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
            stopRefreshTimeline();
            isActive = false;
            StaffView staffView = new StaffView(controller);
            staffView.start(primaryStage);
        });

        return backButton;
    }

    private void refresh(Stage primaryStage) {
        start(primaryStage);
    }

    public void stop() {
        stopRefreshTimeline();
        isActive = false;
    }
}