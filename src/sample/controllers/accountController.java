package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import sample.Main;
import sample.QueryExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class accountController {
    int comment;
    String name;
    String surname;
    @FXML
    Text commentLabel;
    @FXML
    Text nameLabel;
    @FXML
    Text surnameLabel;
    public void initialize(){
        String query="SELECT * FROM klient WHERE id="+ Main.ID +";";
        try{
            ResultSet resultSet= QueryExecutor.executeSelect(query);
            while(resultSet.next()){
                name=resultSet.getString(2);
                surname=resultSet.getString(3);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        query="SELECT count(komentarz) FROM historia_ocen WHERE id_klienta="+Main.ID+"";
        try {
            ResultSet resultSet=QueryExecutor.executeSelect(query);
            while(resultSet.next()){
                comment=resultSet.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        commentLabel.setText(String.valueOf(comment));
        nameLabel.setText(String.valueOf(name));
        surnameLabel.setText(String.valueOf(surname));
    }
}
