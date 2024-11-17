package foodorderingsystem;

import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.Model.Staff.Order;
import foodorderingsystem.Others.DatabaseUtil;
import foodorderingsystem.View.Staff.StaffView;

public class StaffMain extends Application {
    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        // Create an Order object
        Order order = new Order(0, null, 0, 0, null, false);

        // Create a Controller object with the Order and Database Connection
        try {
            connection = DatabaseUtil.getInstance().getConnection();
            // Create tables if they do not exist and import menu data if the table is empty
            DatabaseUtil.getInstance().createTables();

            Controller controller = new Controller(order, connection);

            // Create a StaffView object with the Controller
            StaffView staffView = new StaffView(controller);
            staffView.start(primaryStage);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        // Close the connection when the application exits
        try {
            DatabaseUtil.getInstance().closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}