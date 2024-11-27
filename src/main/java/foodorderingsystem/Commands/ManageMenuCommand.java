package foodorderingsystem.Commands;

import foodorderingsystem.View.Staff.ManageMenuView;
import javafx.stage.Stage;

public class ManageMenuCommand implements Command {
    private ManageMenuView manageMenuView;
    private Stage primaryStage;

    public ManageMenuCommand(ManageMenuView manageMenuView, Stage primaryStage) {
        this.manageMenuView = manageMenuView;
        this.primaryStage = primaryStage;
    }

    @Override
    public void execute() {
        manageMenuView.start(primaryStage);
    }
}