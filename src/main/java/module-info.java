module foodorderingsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.sql;
    requires java.desktop;

    opens foodorderingsystem to javafx.fxml;
    exports foodorderingsystem;
}
