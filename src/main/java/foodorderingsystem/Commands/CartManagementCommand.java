package foodorderingsystem.Commands;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.View.Staff.CartManagementView;
import javafx.stage.Stage;

public class CartManagementCommand implements Command {
    private Controller controller;
    private Stage primaryStage;

    public CartManagementCommand(Controller controller, Stage primaryStage) {
        this.controller = controller;
        this.primaryStage = primaryStage;
    }

    @Override
    public void execute() {
        CartManagementView cartManagementView = new CartManagementView(controller);
        //cartManagementView.setController(controller);
        cartManagementView.start(primaryStage);
    }
}