package foodorderingsystem;

import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.Model.Order;
import foodorderingsystem.Others.DatabaseUtil;
import foodorderingsystem.View.TableSelectionView;

public class FoodOrderingSystem extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Open the foodorderingsystem.Model package to javafx.base at runtime
        ModuleLayer.boot().findModule("foodorderingsystem").ifPresent(module -> {
            module.addOpens("foodorderingsystem.Model", ModuleLayer.boot().findModule("javafx.base").orElseThrow());
        });
        
        // Create an Order object
        Order order = new Order(0, null);

        // Create a Controller object with the Order and Database Connection
        try (Connection connection = DatabaseUtil.getConnection()) {
            // Create tables if they do not exist
            DatabaseUtil.createTables(connection);
            
            Controller controller = new Controller(order, connection);

            // Create a TableSelectionView object with the Controller
            TableSelectionView tableSelectionView = new TableSelectionView(controller);
            tableSelectionView.start(primaryStage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}