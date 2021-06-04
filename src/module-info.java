module projektID {
    requires javafx.controls;
    requires javafx.fxml;
    opens sample;
    opens sample.controllers;
    exports sample.controllers;
}
