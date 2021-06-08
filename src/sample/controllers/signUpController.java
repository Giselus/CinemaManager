package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Main;
import sample.QueryExecutor;

import java.io.IOException;
import java.util.Objects;

public class signUpController {

    @FXML
    TextField nameField;
    @FXML
    TextField surnameField;
    @FXML
    TextField loginField;
    @FXML
    TextField passwordField;

    public void backToPreviousScene(){
        Main.setLastScene();
    }

    public void register(){
        String name = nameField.getText();
        String surname = surnameField.getText();
        String login = loginField.getText();
        String password = passwordField.getText();
        String query = "INSERT INTO klient VALUES(NULL,'" + name + "','" + surname+"','"+login + "','" + password + "');";
        try{
            QueryExecutor.executeQuery(query);
            backToPreviousScene();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
