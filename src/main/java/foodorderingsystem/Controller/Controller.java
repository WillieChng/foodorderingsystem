package foodorderingsystem.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import foodorderingsystem.Model.Customer.Cart;
import foodorderingsystem.Model.Customer.MenuItem;
import foodorderingsystem.Model.Staff.Order;
import foodorderingsystem.Others.DatabaseUtil;

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

    public void insertMenuItem(int id, String name, String description, double price) {
        try {
            MenuItem menuItem = new MenuItem(id, name, description, price);
            databaseUtil.insertMenuItem(menuItem);
            System.out.println("Menu item inserted: " + name);
        } catch (SQLException e) {
            // Handle the exception more gracefully
            System.err.println("Error inserting menu item: " + e.getMessage());
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
        String sql = "SELECT table_number, name, quantity FROM orders";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int tableNumber = resultSet.getInt("table_number");
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");

                Order order = new Order(tableNumber, name, quantity);

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
            String sql = "INSERT INTO orders (table_number, name, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (Map.Entry<MenuItem, Integer> entry : cart.getItems().entrySet()) {
                    MenuItem item = entry.getKey();
                    int quantity = entry.getValue();
                    preparedStatement.setInt(1, order.getTableNumber());
                    preparedStatement.setString(2, item.getName());
                    preparedStatement.setInt(3, quantity);
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
}