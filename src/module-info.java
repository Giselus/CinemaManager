module projektID {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens sample;
    opens sample.controllers;
    exports sample.controllers;
}
