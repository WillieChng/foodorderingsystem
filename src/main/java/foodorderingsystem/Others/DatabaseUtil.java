package foodorderingsystem.Others;

import foodorderingsystem.Model.MenuItem;
import foodorderingsystem.Model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/fos_db";
    private static final String USER = "root";
    private static final String PASSWORD = "li852456";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void createTables(Connection connection) throws SQLException {
        String createMenuTable = "CREATE TABLE IF NOT EXISTS menu (" +
                                   "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                   "name VARCHAR(255), " +
                                   "description VARCHAR(255), " +
                                   "price DOUBLE)";
        String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders (" +
                                   "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                   "table_number INT, " +
                                   "details VARCHAR(255))";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createMenuTable);
            statement.execute(createOrdersTable);
        }
    }

    public static void insertMenuItem(MenuItem menuItem) throws SQLException {
        String insertMenuItemSQL = "INSERT INTO menu (name, description, price) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertMenuItemSQL)) {
            preparedStatement.setString(1, menuItem.getName());
            preparedStatement.setString(2, menuItem.getDescription());
            preparedStatement.setDouble(3, menuItem.getPrice());
            preparedStatement.executeUpdate();
        }
    }

    public static List<MenuItem> getMenuItems() throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM menu";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");

                MenuItem menuItem = new MenuItem(id, name, description, price);
                menuItems.add(menuItem);
            }
        }
        return menuItems;
    }

    public static List<Order> getOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int tableNumber = resultSet.getInt("table_number");
                String details = resultSet.getString("details");
                orders.add(new Order(tableNumber, details));
            }
        }
        return orders;
    }
}