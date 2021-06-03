package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class logInController {
    public void switchToSceneMenu(ActionEvent event) throws IOException {
        System.out.println("ZMIANA");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass()
                .getResource("/sample/fxml/sample.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        String css=this.getClass().getResource("/sample/style/style.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.show();
    }

}
