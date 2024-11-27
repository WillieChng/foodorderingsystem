package foodorderingsystem.Commands;

import foodorderingsystem.View.Staff.StaffTableSelectionView;
import javafx.stage.Stage;

public class GenerateReceiptCommand implements Command {
    private StaffTableSelectionView staffTableSelectionView;
    private Stage primaryStage;

    public GenerateReceiptCommand(StaffTableSelectionView staffTableSelectionView, Stage primaryStage) {
        this.staffTableSelectionView = staffTableSelectionView;
        this.primaryStage = primaryStage;
    }

    @Override
    public void execute() {
        staffTableSelectionView.start(primaryStage);
    }
}