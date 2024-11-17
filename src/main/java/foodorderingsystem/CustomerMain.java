package foodorderingsystem;

import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.Model.Staff.Order;
import foodorderingsystem.Others.DatabaseUtil;
import foodorderingsystem.View.Customer.TableSelectionView;

public class CustomerMain extends Application {

    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        // Open the foodorderingsystem.Model package to javafx.base at runtime
        ModuleLayer.boot().findModule("foodorderingsystem").ifPresent(module -> {
            module.addOpens("foodorderingsystem.Model.Customer", ModuleLayer.boot().findModule("javafx.base").orElseThrow());
            module.addOpens("foodorderingsystem.Model.Staff", ModuleLayer.boot().findModule("javafx.base").orElseThrow());
        });

        try {
            initializeDatabase();
            Controller controller = createController();
            startTableSelectionView(primaryStage, controller);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeDatabase() throws SQLException, IOException {
        connection = DatabaseUtil.getInstance().getConnection();
        DatabaseUtil.getInstance().createTables();
    }

    private Controller createController() {
        Order order = new Order(0, null, 0, 0, null, false);
        return new Controller(order, connection);
    }

    private void startTableSelectionView(Stage primaryStage, Controller controller) {
        // Set fixed size for the primary stage
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);

        TableSelectionView tableSelectionView = new TableSelectionView(controller);
        tableSelectionView.start(primaryStage);
    }

    @Override
    public void stop() {
        closeDatabaseConnection();
    }

    private void closeDatabaseConnection() {
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