module pl.mikomak.plaguesimulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens pl.mikomak.plaguesimulator to javafx.fxml;
    exports pl.mikomak.plaguesimulator;
}