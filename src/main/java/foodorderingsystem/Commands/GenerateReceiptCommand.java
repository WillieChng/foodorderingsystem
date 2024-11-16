package foodorderingsystem.Commands;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.View.Staff.StaffTableSelectionView;
import javafx.stage.Stage;

public class GenerateReceiptCommand implements Command {
    private Controller controller;
    private Stage primaryStage;

    public GenerateReceiptCommand(Controller controller, Stage primaryStage) {
        this.controller = controller;
        this.primaryStage = primaryStage;
    }

    @Override
    public void execute() {
        StaffTableSelectionView tableSelectionView = new StaffTableSelectionView(controller);
        tableSelectionView.start(primaryStage);
    }
}