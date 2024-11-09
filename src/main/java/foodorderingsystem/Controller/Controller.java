package foodorderingsystem.Controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import foodorderingsystem.Model.Cart;
import foodorderingsystem.Model.MenuItem;
import foodorderingsystem.Model.Order;
import foodorderingsystem.Others.DatabaseUtil;

public class Controller {
    private Order order;
    private Connection connection;
    private Cart cart;

    public Controller(Order order, Connection connection) {
        this.order = order;
        this.connection = connection != null ? connection : createMockConnection();
        this.cart = new Cart(); // Initialize the cart object
    }

    private Connection createMockConnection() {
        // Create a mock connection or handle it appropriately
        return null;
    }

    public void updateOrderDetails(String details) {
        order.setDetails(details);
        // Use the connection to update the database
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
            DatabaseUtil.insertMenuItem(menuItem);
            System.out.println("Menu item inserted: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MenuItem> getMenuItems() {
        try {
            return DatabaseUtil.getMenuItems();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addItemToCart(MenuItem item) {
        cart.addItem(item);
    }

    public void removeItemFromCart(MenuItem item) {
        cart.removeItem(item);
    }

    public Map<MenuItem, Integer> getCartItems() {
        return cart.getItems();
    }

    public void clearCart() {
        cart.clear();
    }

    public List<Order> getOrders() {
        try {
            return DatabaseUtil.getOrders();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}