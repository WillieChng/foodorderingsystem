package foodorderingsystem.Commands;

import foodorderingsystem.View.Staff.CartManagementView;
import javafx.stage.Stage;

public class CartManagementCommand implements Command {
    private CartManagementView cartManagementView;
    private Stage primaryStage;

    public CartManagementCommand(CartManagementView cartManagementView, Stage primaryStage) {
        this.cartManagementView = cartManagementView;
        this.primaryStage = primaryStage;
    }

    @Override
    public void execute() {
        cartManagementView.start(primaryStage);
    }
}