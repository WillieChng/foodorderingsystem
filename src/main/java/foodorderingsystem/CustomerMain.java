package foodorderingsystem;

import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.Model.Staff.Order;
import foodorderingsystem.Others.DatabaseUtil;
import foodorderingsystem.View.Customer.TableSelectionView;

public class CustomerMain extends Application {

    private Controller controller;
    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        // Open the foodorderingsystem.Model package to javafx.base at runtime
        ModuleLayer.boot().findModule("foodorderingsystem").ifPresent(module -> {
            module.addOpens("foodorderingsystem.Model.Customer", ModuleLayer.boot().findModule("javafx.base").orElseThrow());
            module.addOpens("foodorderingsystem.Model.Staff", ModuleLayer.boot().findModule("javafx.base").orElseThrow());
        });

        // Create an Order object
        Order order = new Order(0, null, 0, 0);

        try {
            // Create a Database Connection
            connection = DatabaseUtil.getInstance().getConnection();

            // Create tables if they do not exist
            DatabaseUtil.getInstance().createTables();

            // Create a Controller object with the Order and Database Connection
            controller = new Controller(order, connection);

            // Create a TableSelectionView object with the Controller
            TableSelectionView tableSelectionView = new TableSelectionView(controller);
            tableSelectionView.start(primaryStage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        // Close the connection when the application exits
        if (controller != null) {
            controller.closeConnection();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}