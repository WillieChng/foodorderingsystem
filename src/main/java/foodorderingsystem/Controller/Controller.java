package foodorderingsystem.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.ByteArrayInputStream;

import foodorderingsystem.Model.Customer.Cart;
import foodorderingsystem.Model.Customer.MenuItem;
import foodorderingsystem.Model.Staff.Order;
import foodorderingsystem.Others.DatabaseUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller {
    private Order order;
    private Connection connection;
    private Cart cart;
    private DatabaseUtil databaseUtil;

    public Controller(Order order, Connection connection) {
        this.order = order;
        this.connection = connection != null ? connection : createMockConnection();
        this.cart = new Cart(); // Initialize the cart object
        this.databaseUtil = DatabaseUtil.getInstance(); // Get the Singleton instance
    }

    private Connection createMockConnection() {
        // Create a mock connection or handle it appropriately
        return null;
    }

    public Order getOrder() {
        return order;
    }

    public void setTableNumber(int tableNumber) {
        order.setTableNumber(tableNumber);
    }

    public int getTableNumber() {
        return order.getTableNumber();
    }

    // Add more business logic methods here

    public void insertMenuItem(int id, String name, String description, double price, String imagePath) {
        try {
            byte[] imageBytes = loadImage(imagePath);
            MenuItem menuItem = new MenuItem(id, name, description, price, imageBytes);
            databaseUtil.insertMenuItem(menuItem);
            System.out.println("Menu item inserted: " + name);
        } catch (SQLException | IOException e) {
            // Handle the exception more gracefully
            System.err.println("Error inserting menu item: " + e.getMessage());
        }
    }

    private byte[] loadImage(String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            try (FileInputStream fis = new FileInputStream(imageFile)) {
                return fis.readAllBytes();
            }
        } else {
            System.err.println("Image not found: " + imagePath);
            return new byte[0];
        }
    }

    public List<MenuItem> getMenuItems() {
        try {
            return databaseUtil.getMenuItems();
        } catch (SQLException e) {
            // Handle the exception more gracefully
            System.err.println("Error retrieving menu items: " + e.getMessage());
            return null;
        }
    }

    public void addItemToCart(MenuItem item) {
        cart.addItem(item);
    }

    public void removeItemFromCart(MenuItem item) {
        cart.removeItem(item);
        // Optionally, remove the item from the database if needed
        // databaseUtil.removeMenuItem(item);
    }

    public Map<MenuItem, Integer> getCartItems() {
        return cart.getItems();
    }

    public void clearCart() {
        cart.clear();
    }

    public List<Order> getOrders() {
        try {
            return databaseUtil.getOrders();
        } catch (SQLException e) {
            // Handle the exception more gracefully
            System.err.println("Error retrieving orders: " + e.getMessage());
            return null;
        }
    }

    public Map<Integer, List<Order>> getItemsFromOrders() {
        Map<Integer, List<Order>> orders = new HashMap<>();
        String sql = "SELECT table_number, name, quantity, price, image FROM orders";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int tableNumber = resultSet.getInt("table_number");
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");
                byte[] image = resultSet.getBytes("image");

                Order order = new Order(tableNumber, name, quantity, price, image);

                orders.computeIfAbsent(tableNumber, k -> new ArrayList<>()).add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving items from orders: " + e.getMessage());
        }
        return orders;
    }

    public void removeItemFromOrders(int tableNumber, String name) {
        String sql = "DELETE FROM orders WHERE table_number = ? AND name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, tableNumber);
            preparedStatement.setString(2, name);
            int rowsDeleted = preparedStatement.executeUpdate();
            System.out.println("Debug: Removed " + rowsDeleted + " item(s) from orders table for table number " + tableNumber + " and name " + name);
        } catch (SQLException e) {
            System.err.println("Error removing item from orders: " + e.getMessage());
        }
    }

    public void checkout() {
        try {
            String sql = "INSERT INTO orders (table_number, name, quantity, price, image) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (Map.Entry<MenuItem, Integer> entry : cart.getItems().entrySet()) {
                    MenuItem item = entry.getKey();
                    int quantity = entry.getValue();
                    preparedStatement.setInt(1, order.getTableNumber());
                    preparedStatement.setString(2, item.getName());
                    preparedStatement.setInt(3, quantity);
                    preparedStatement.setDouble(4, item.getPrice() * quantity);
                    preparedStatement.setBytes(5, item.getImage());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                System.out.println("Checkout completed: Order saved to database.");
            }
        } catch (SQLException e) {
            System.err.println("Error during checkout: " + e.getMessage());
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // New method to get orders for a specific table number
    public List<Order> getOrdersForTable(int tableNumber) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT table_number, name, quantity, price, image FROM orders WHERE table_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, tableNumber);
            System.out.println("Debug: Executing query: " + preparedStatement);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int quantity = resultSet.getInt("quantity");
                    double price = resultSet.getDouble("price");
                    byte[] image = resultSet.getBytes("image");
                    Order order = new Order(tableNumber, name, quantity, price, image);
                    orders.add(order);
                    System.out.println("Debug: Retrieved order: " + order);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving orders for table: " + e.getMessage());
        }
        return orders;
    }

    // New method to remove orders for a specific table number
    public void removeOrdersForTable(int tableNumber) {
        String sql = "DELETE FROM orders WHERE table_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, tableNumber);
            int rowsDeleted = preparedStatement.executeUpdate();
            System.out.println("Debug: Removed " + rowsDeleted + " order(s) for table number " + tableNumber);
        } catch (SQLException e) {
            System.err.println("Error removing orders for table: " + e.getMessage());
        }
    }

    // New method to remove a menu item
    public void removeMenuItem(MenuItem menuItem) {
        String sql = "DELETE FROM menu WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, menuItem.getID());
            int rowsDeleted = preparedStatement.executeUpdate();
            System.out.println("Debug: Removed " + rowsDeleted + " item(s) from menu table with id " + menuItem.getID());
        } catch (SQLException e) {
            System.err.println("Error removing menu item: " + e.getMessage());
        }
    }

     // New method to display a pop-up form for updating a menu item
     public void showUpdateMenuItemForm(MenuItem menuItem) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Menu Item");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Image
        ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(menuItem.getImage())));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        grid.add(new Label("Image:"), 0, 0);
        grid.add(imageView, 1, 0);

        // Button to choose a new image
        Button chooseImageButton = new Button("Choose Image");
        grid.add(chooseImageButton, 2, 0);

        // Name
        TextField nameField = new TextField(menuItem.getName());
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);

        // Description
        TextField descriptionField = new TextField(menuItem.getDescription());
        grid.add(new Label("Description:"), 0, 2);
        grid.add(descriptionField, 1, 2);

        // Price
        TextField priceField = new TextField(String.valueOf(menuItem.getPrice()));
        grid.add(new Label("Price:"), 0, 3);
        grid.add(priceField, 1, 3);

        // File chooser for selecting a new image
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        // Store the new image bytes
        final byte[][] newImageBytes = {null};

        chooseImageButton.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                try {
                    newImageBytes[0] = loadImage(selectedFile.getAbsolutePath());
                    imageView.setImage(new Image(new ByteArrayInputStream(newImageBytes[0])));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Update Button
        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            String newName = nameField.getText();
            String newDescription = descriptionField.getText();
            double newPrice = Double.parseDouble(priceField.getText());

            menuItem.setName(newName);
            menuItem.setDescription(newDescription);
            menuItem.setPrice(newPrice);

            // Update the image if a new one was selected
            if (newImageBytes[0] != null) {
                menuItem.setImage(newImageBytes[0]);
            }

            updateMenuItem(menuItem);
            stage.close();
        });
        grid.add(updateButton, 1, 4);

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.showAndWait();
    }

    // New method to update a menu item in the database
    public void updateMenuItem(MenuItem menuItem) {
        String sql = "UPDATE menu SET name = ?, description = ?, price = ?, image = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, menuItem.getName());
            preparedStatement.setString(2, menuItem.getDescription());
            preparedStatement.setDouble(3, menuItem.getPrice());
            preparedStatement.setBytes(4, menuItem.getImage());
            preparedStatement.setInt(5, menuItem.getID());
            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println("Debug: Updated " + rowsUpdated + " item(s) in menu table with id " + menuItem.getID());
        } catch (SQLException e) {
            System.err.println("Error updating menu item: " + e.getMessage());
        }
    }

    // New method to display a pop-up form for adding a new menu item
public void showAddMenuItemForm() {
    Stage stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setTitle("Add Menu Item");

    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setPadding(new Insets(10, 10, 10, 10));
    grid.setHgap(10);
    grid.setVgap(10);

    // Image
    ImageView imageView = new ImageView();
    imageView.setFitWidth(100);
    imageView.setFitHeight(100);
    grid.add(new Label("Image:"), 0, 0);
    grid.add(imageView, 1, 0);

    // Button to choose a new image
    Button chooseImageButton = new Button("Choose Image");
    grid.add(chooseImageButton, 2, 0);

    // Name
    TextField nameField = new TextField();
    grid.add(new Label("Name:"), 0, 1);
    grid.add(nameField, 1, 1);

    // Description
    TextField descriptionField = new TextField();
    grid.add(new Label("Description:"), 0, 2);
    grid.add(descriptionField, 1, 2);

    // Price
    TextField priceField = new TextField();
    grid.add(new Label("Price:"), 0, 3);
    grid.add(priceField, 1, 3);

    // File chooser for selecting a new image
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

    // Store the new image bytes
    final byte[][] newImageBytes = {null};

    chooseImageButton.setOnAction(e -> {
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                newImageBytes[0] = loadImage(selectedFile.getAbsolutePath());
                imageView.setImage(new Image(new ByteArrayInputStream(newImageBytes[0])));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    });

    // Add Button
    Button addButton = new Button("Add");
    addButton.setOnAction(e -> {
        String name = nameField.getText();
        String description = descriptionField.getText();
        String priceText = priceField.getText();

        if (name.isEmpty() || description.isEmpty() || priceText.isEmpty() || newImageBytes[0] == null) {
            // Show an error message if any field is empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("All fields must be filled!");
            alert.showAndWait();
        } else {
            double price = Double.parseDouble(priceText);
            MenuItem menuItem = new MenuItem(0, name, description, price, newImageBytes[0]);
            try {
                databaseUtil.insertMenuItem(menuItem);
                System.out.println("Menu item added: " + name);
                stage.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    });
    grid.add(addButton, 1, 4);

    Scene scene = new Scene(grid, 400, 300);
    stage.setScene(scene);
    stage.showAndWait();
}
}