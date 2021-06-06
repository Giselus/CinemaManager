package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sample.Main;
import sample.QueryExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class repertoireController {
    @FXML public VBox movieBox;
    @FXML public AnchorPane myPane;
    public static int sala=0;
    public void initialize() {
        String query = "SELECT * FROM seans ORDER BY data_rozpoczecia limit 10;";
        try{
            ResultSet result= QueryExecutor.executeSelect(query);
            movieBox.getChildren().clear();
            while(result.next()){
                int id = result.getInt(1);
                int idFilm=result.getInt(2);
                AnchorPane moviePane = new AnchorPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(1080);
                rectangle.setHeight(200);
                rectangle.setFill(Paint.valueOf("DODGERBLUE"));
                Text title=new Text();
                title.setText(String.valueOf(id)+" "+String.valueOf(idFilm));
                title.setLayoutX(50);
                title.setLayoutY(20);
                int tmpSala=result.getInt(4);
                rectangle.setOnMouseClicked(event -> {
                    sala=tmpSala;
                    Main.setScene("/sample/fxml/reservation.fxml","/sample/style/styleReservation.css");
                    //goto nowa scena

                });
                moviePane.getChildren().add(rectangle);
                moviePane.getChildren().add(title);
                movieBox.getChildren().add(moviePane);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
