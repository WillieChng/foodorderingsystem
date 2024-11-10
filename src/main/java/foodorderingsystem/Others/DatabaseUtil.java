package foodorderingsystem.Others;

import foodorderingsystem.Model.Customer.MenuItem;
import foodorderingsystem.Model.Staff.Order;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
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

    public void createTables() throws SQLException, IOException {
        String createMenuTable = "CREATE TABLE IF NOT EXISTS menu (" +
                                   "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                   "name VARCHAR(255), " +
                                   "description VARCHAR(255), " +
                                   "price DOUBLE, " +
                                   "image MEDIUMBLOB)";
        String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders (" +
                                   "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                   "table_number INT, " +
                                   "name VARCHAR(255), " +
                                   "quantity INT, " +
                                   "price DOUBLE)";

        try (Statement statement = getConnection().createStatement()) {
            statement.execute(createMenuTable);
            statement.execute(createOrdersTable);
        }

        // Check if the menu table is empty before importing data
        if (isTableEmpty("menu")) {
            importMenuData();
        }
    }

    private boolean isTableEmpty(String tableName) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName;
        try (Statement statement = getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt(1) == 0;
            }
        }
        return false;
    }

    public void insertMenuItem(MenuItem menuItem) throws SQLException {
        String insertMenuItemSQL = "INSERT INTO menu (name, description, price, image) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(insertMenuItemSQL)) {
            preparedStatement.setString(1, menuItem.getName());
            preparedStatement.setString(2, menuItem.getDescription());
            preparedStatement.setDouble(3, menuItem.getPrice());
            preparedStatement.setBytes(4, menuItem.getImage());
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
                byte[] image = resultSet.getBytes("image");

                MenuItem menuItem = new MenuItem(id, name, description, price, image);
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
                double price = resultSet.getDouble("price");
                orders.add(new Order(tableNumber, name, quantity, price));
            }
        }
        return orders;
    }

    public static void importMenuData() throws SQLException, IOException {
        String insertSQL = "INSERT INTO menu (name, description, price, image) VALUES (?, ?, ?, ?)";
        
        String csvFile = "src/main/java/foodorderingsystem/Others/menu.csv";
        String line;
        boolean firstLine = true;
    
        try (Connection conn = getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL);
             BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
    
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
    
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                
                pstmt.setString(1, data[0].trim());
                pstmt.setString(2, data[1].trim().replace("\"", ""));
                pstmt.setDouble(3, Double.parseDouble(data[2].trim()));
    
                // Improved image loading with proper resource management
                String imagePath = "src/main/resources/" + data[3].trim();
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    try (FileInputStream fis = new FileInputStream(imageFile)) {
                        pstmt.setBinaryStream(4, fis, imageFile.length());
                        pstmt.executeUpdate();
                    }
                } else {
                    System.out.println("Image not found: " + imagePath);
                    pstmt.setBytes(4, new byte[0]);
                    pstmt.executeUpdate();
                }
            }
        }
    }
}