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
    public void switchToSceneLogiIn(ActionEvent event) throws IOException {
        System.out.println("ZMIANA");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass()
                .getResource("/sample/fxml/logIn.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        String css=this.getClass().getResource("/sample/style/styleLogIn.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.show();
    }
    public void switchToSceneSignUp(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass()
                .getResource("/sample/fxml/signUp.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        String css=this.getClass().getResource("/sample/style/styleSignUp.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.show();
    }
}
