package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Main;
import sample.QueryExecutor;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Objects;

public class logInController {

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    public void backToPreviousScene(){
        Main.setLastScene();
    }

    public void login(){
        String login = loginField.getText();
        String password = passwordField.getText();
        String query = "SELECT id FROM klient WHERE login = '" + login + "' AND haslo = '" + password + "';";
        try{
            ResultSet result = QueryExecutor.executeSelect(query);
            if(result.next()){
                Main.ID = result.getInt("id");
                Main.logged = true;
            }else{
                throw new Exception("wrong login or password");
            }
            backToPreviousScene();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
