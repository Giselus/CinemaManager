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
    @FXML
    public TextField hostField;
    @FXML
    public TextField databaseField;
    @FXML
    public TextField portField;

    @FXML public void confirm(){
        DBConnector.USER=loginField.getText();
        DBConnector.PASSWORD=passwordField.getText();
        DBConnector.HOST=hostField.getText();
        DBConnector.DATABASE=databaseField.getText();
        DBConnector.PORT=portField.getText();
        DBConnector.URL=String.format("jdbc:postgresql://%s:%s/%s",DBConnector.HOST,DBConnector.PORT,DBConnector.DATABASE);
        Main.initializeDatabase();
        Main.setScene("fxml/base.fxml","/sample/style/style.css");
    }
}
