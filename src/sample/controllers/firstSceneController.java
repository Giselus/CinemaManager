package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.DBConnector;
import sample.Main;

public class firstSceneController {
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;

    @FXML public void confirm(){
        DBConnector.USER=loginField.getText();
        DBConnector.PASSWORD=passwordField.getText();
        Main.setScene("fxml/base.fxml","/sample/style/style.css");
    }
}
