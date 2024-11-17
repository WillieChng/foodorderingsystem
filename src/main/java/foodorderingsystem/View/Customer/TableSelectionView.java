package foodorderingsystem.View.Customer;

import foodorderingsystem.Controller.Controller;
import foodorderingsystem.View.UI;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.*;
import javafx.stage.*;

public class TableSelectionView extends UI {

    public TableSelectionView(Controller controller) {
        super(controller);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = createGridPane();
        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, 1);
        spinner.setValueFactory(valueFactory);

        VBox tableSelectionBox = createTableSelectionBox("Select Table Number:", "Proceed", spinner, () -> {
            int selectedTable = spinner.getValue();
            controller.setTableNumber(selectedTable);
            MenuView menuView = new MenuView(controller);
            menuView.start(primaryStage);
        });

        // Add all components to the grid
        grid.getChildren().addAll(createHeader("Table Selection"), tableSelectionBox);

        // Update the scene's root node
        updateScene(primaryStage, grid);
    }
}