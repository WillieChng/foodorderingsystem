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

import java.io.ByteArrayInputStream;
import java.util.List;

public class MenuView extends UI {

    public MenuView(Controller controller) {
        super(controller);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = createGridPane();
        VBox listViewWithButtons = createListViewWithButtons(primaryStage);
        ScrollPane scrollPane = createScrollPane(listViewWithButtons);
        Button backButton = createBackButton(primaryStage);
        Button proceedToCartButton = createProceedToCartButton(primaryStage);

        // Add all components to the grid
        grid.getChildren().addAll(createHeader("Menu"), scrollPane, backButton, proceedToCartButton);

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

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            controller.addItemToCart(item);
            System.out.println("Added to cart: " + item.getName());
        });
        GridPane.setConstraints(addButton, 1, 3);

        itemGrid.getChildren().addAll(imageView, nameLabel, descriptionLabel, priceLabel, addButton);
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
            TableSelectionView tableSelectionView = new TableSelectionView(controller);
            tableSelectionView.start(primaryStage);
        });

        return backButton;
    }

    private Button createProceedToCartButton(Stage primaryStage) {
        Button proceedToCartButton = new Button("Proceed to Cart");
        proceedToCartButton.setFont(new Font("Arial", 16));
        proceedToCartButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 4;");
        proceedToCartButton.setPrefSize(150, 50);
        GridPane.setConstraints(proceedToCartButton, 1, 2);

        proceedToCartButton.setOnAction(e -> {
            CartView cartView = new CartView(controller);
            cartView.start(primaryStage);
        });

        return proceedToCartButton;
    }
}