package foodorderingsystem;

import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.Model.Order;
import foodorderingsystem.Others.DatabaseUtil;
import foodorderingsystem.View.Staff.StaffView;

public class StaffMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create an Order object
        Order order = new Order(0, null);

        // Create a Controller object with the Order and Database Connection
        try (Connection connection = DatabaseUtil.getConnection()) {
            Controller controller = new Controller(order, connection);

            // Create a StaffView object with the Controller
            StaffView staffView = new StaffView(controller);
            staffView.start(primaryStage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}