package foodorderingsystem.Commands;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.View.Staff.ManageMenuView;
import javafx.stage.Stage;

public class ManageMenuCommand implements Command {
    private Controller controller;
    private Stage primaryStage;

    public ManageMenuCommand(Controller controller, Stage primaryStage) {
        this.controller = controller;
        this.primaryStage = primaryStage;
    }

    @Override
    public void execute() {
        ManageMenuView manageMenuView = new ManageMenuView(controller);
        manageMenuView.start(primaryStage);
    }
}