package foodorderingsystem.View.Customer;

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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.ByteArrayInputStream;
import java.util.Map;

public class CartView extends UI {
    private VBox listViewWithButtons;

    public CartView(Controller controller) {
        super(controller);
    }

    @Override
    public void start(Stage primaryStage) {
        // Set fixed size for the primary stage
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);

        GridPane grid = createGridPane();
        listViewWithButtons = createListViewWithButtons(primaryStage);
        ScrollPane scrollPane = createScrollPane(listViewWithButtons);
        Button backButton = createBackButton(primaryStage);

        // Add all components to the grid
        grid.getChildren().addAll(createHeader("Cart"), scrollPane, backButton);

        // Conditionally add the checkout button if there are items in the cart
        if (controller.getCartItems() != null && !controller.getCartItems().isEmpty()) {
            Button checkoutButton = createCheckoutButton(primaryStage);
            grid.getChildren().add(checkoutButton);
        }

        // Update the scene's root node
        updateScene(primaryStage, grid);
    }

    private VBox createListViewWithButtons(Stage primaryStage) {
        VBox listViewWithButtons = new VBox(10);
        listViewWithButtons.setAlignment(Pos.TOP_CENTER); // Align items to the top
        listViewWithButtons.setPrefSize(800, 600); // Set preferred size to match MenuView

        updateListViewWithButtons(listViewWithButtons, primaryStage);

        return listViewWithButtons;
    }

    private void updateListViewWithButtons(VBox listViewWithButtons, Stage primaryStage) {
        listViewWithButtons.getChildren().clear();

        Map<MenuItem, Integer> cartItems = controller.getCartItems();

        if (cartItems != null && !cartItems.isEmpty()) {
            for (Map.Entry<MenuItem, Integer> entry : cartItems.entrySet()) {
                MenuItem item = entry.getKey();
                int quantity = entry.getValue();
                listViewWithButtons.getChildren().add(createItemGrid(item, quantity, primaryStage));
            }
        } else {
            Label noItemsLabel = new Label("No Items In Cart :(");
            noItemsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            listViewWithButtons.getChildren().add(noItemsLabel);
        }
    }

    private GridPane createItemGrid(MenuItem item, int quantity, Stage primaryStage) {
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

        Text descriptionText = new Text(item.getDescription());
        TextFlow descriptionTextFlow = new TextFlow(descriptionText);
        descriptionTextFlow.setPrefWidth(700); // Set preferred width to ensure wrapping
        GridPane.setConstraints(descriptionTextFlow, 1, 1);

        Label priceLabel = new Label("RM" + String.format("%.2f", item.getPrice()));
        GridPane.setConstraints(priceLabel, 1, 2);

        Label quantityLabel = new Label("Quantity: " + quantity);
        GridPane.setConstraints(quantityLabel, 1, 3);

        Button addButton = new Button("+");
        addButton.setOnAction(e -> {
            controller.addItemToCart(item);
            refresh(primaryStage); // Refresh the view
        });

        Button removeButton = new Button("-");
        removeButton.setOnAction(e -> {
            controller.removeItemFromCart(item);
            refresh(primaryStage); // Refresh the view
        });

        HBox buttonBox = new HBox(5, addButton, removeButton); // HBox with 5px spacing
        GridPane.setConstraints(buttonBox, 1, 4, 2, 1);

        itemGrid.getChildren().addAll(imageView, nameLabel, descriptionTextFlow, priceLabel, quantityLabel, buttonBox);
        return itemGrid;
    }

    private ScrollPane createScrollPane(VBox listViewWithButtons) {
        ScrollPane scrollPane = new ScrollPane(listViewWithButtons);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600); // Set preferred height to match MenuView
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
            MenuView menuView = new MenuView(controller);
            menuView.start(primaryStage);
        });

        return backButton;
    }

    private Button createCheckoutButton(Stage primaryStage) {
        Button checkoutButton = new Button("Checkout");
        checkoutButton.setFont(new Font("Arial", 16));
        checkoutButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 4;");
        checkoutButton.setPrefSize(150, 50);
        GridPane.setConstraints(checkoutButton, 1, 2);

        checkoutButton.setOnAction(e -> {
            controller.checkout();
            System.out.println("Proceeding to checkout...");
            ThankYouView thankYouView = new ThankYouView(controller);
            thankYouView.start(primaryStage);
        });

        return checkoutButton;
    }

    private void refresh(Stage primaryStage) {
        updateListViewWithButtons(listViewWithButtons, primaryStage);
    }
}