package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.util.Objects;

public class logInController {
    public void backToPreviousScene(ActionEvent event) throws IOException {
        Main.setLastScene();
    }

}
