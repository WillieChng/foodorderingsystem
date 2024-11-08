package foodorderingsystem.View;

import foodorderingsystem.Controller.Controller;
import javafx.stage.Stage;

public abstract class UI {
    protected Controller controller;

    public UI(Controller controller) {
        this.controller = controller;
    }

    public abstract void start(Stage primaryStage);
}