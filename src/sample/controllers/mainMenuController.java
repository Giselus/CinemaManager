package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.util.Objects;

public class mainMenuController {
    @FXML ImageView firstImageView;
    Image firstImage=new Image(getClass().getResourceAsStream("/sample/image/harry1.jpeg"));
    int valueOfRating=10;
    public void displayImage(){
        firstImageView.setImage(firstImage);
    }
    //change scene
    public void switchToSceneLogiIn(){
        Main.setScene("/sample/fxml/logIn.fxml","/sample/style/styleLogIn.css");
        //Main.setScene("/sample/fxml/reservation.fxml","/sample/style/styleReservation.css");
    }
    public void switchToSceneSignUp(){
        Main.setScene("/sample/fxml/signUp.fxml","/sample/style/styleSignUp.css");
    }
}
