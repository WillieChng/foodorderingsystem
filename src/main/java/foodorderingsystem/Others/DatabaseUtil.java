package foodorderingsystem.Others;

import foodorderingsystem.Model.Customer.MenuItem;
import foodorderingsystem.Model.Staff.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/fos_db";
    private static final String USER = "root";
    private static final String PASSWORD = "li852456";
    private static DatabaseUtil instance;
    private Connection connection;

    private DatabaseUtil() {
        // private constructor to prevent instantiation
    }

    public static DatabaseUtil getInstance() {
        if (instance == null) {
            synchronized (DatabaseUtil.class) {
                if (instance == null) {
                    instance = new DatabaseUtil();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void createTables() throws SQLException {
        String createMenuTable = "CREATE TABLE IF NOT EXISTS menu (" +
                                   "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                   "name VARCHAR(255), " +
                                   "description VARCHAR(255), " +
                                   "price DOUBLE)";
        String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders (" +
                                   "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                   "table_number INT, " +
                                   "name VARCHAR(255), " +
                                   "quantity INT)";

        try (Statement statement = getConnection().createStatement()) {
            statement.execute(createMenuTable);
            statement.execute(createOrdersTable);
        }
    }

    public void insertMenuItem(MenuItem menuItem) throws SQLException {
        String insertMenuItemSQL = "INSERT INTO menu (name, description, price) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(insertMenuItemSQL)) {
            preparedStatement.setString(1, menuItem.getName());
            preparedStatement.setString(2, menuItem.getDescription());
            preparedStatement.setDouble(3, menuItem.getPrice());
            preparedStatement.executeUpdate();
        }
    }

    public List<MenuItem> getMenuItems() throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM menu";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
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

    public List<Order> getOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int tableNumber = resultSet.getInt("table_number");
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");
                orders.add(new Order(tableNumber, name, quantity));
            }
        }
        return orders;
    }
}