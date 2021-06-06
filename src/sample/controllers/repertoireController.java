package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import sample.Main;
import sample.QueryExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class repertoireController {
    @FXML public VBox movieBox;
    @FXML public AnchorPane myPane;
    public static int sala=0;
    public static int cena = 0;
    public void initialize() {
        String query = "SELECT * FROM seans JOIN film ON seans.id_filmu = film.id ORDER BY data_rozpoczecia;";
        try {
            ResultSet result = QueryExecutor.executeSelect(query);
            movieBox.getChildren().clear();
            while (result.next()) {
                Timestamp date = result.getTimestamp(3);
                String movieTitle = result.getString(10);
                int duration = result.getInt(12);
                int tmpSala = result.getInt(4);
                int tmpCena = result.getInt(6);
                AnchorPane moviePane = new AnchorPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(1080);
                rectangle.setHeight(200);
                rectangle.setFill(Color.rgb(169, 167, 167));
                Text title = createTitle(movieTitle);
                Text dur = createDuration(duration);
                Text dates = createDate(date);
                Button buyTickets = new Button();
                buyTickets.setLayoutX(800);
                buyTickets.setLayoutY(120);
                buyTickets.resize(140, 60);
                buyTickets.setText("BUY");
                buyTickets.setOnAction(event -> {
                    sala = tmpSala;
                    cena = tmpCena;
                    Main.setScene("/sample/fxml/ticketNumber.fxml", "/sample/style/styleReservation.css");
                });
                Button infoButton = new Button();
                infoButton.setLayoutX(870);
                infoButton.setLayoutY(120);
                infoButton.resize(140, 60);
                infoButton.setText("INFO");
                infoButton.setOnAction(event -> {
                    sala = tmpSala;
                    cena = tmpCena;
                    Main.setScene("/sample/fxml/reservation.fxml", "/sample/style/styleReservation.css");
                });
                moviePane.getChildren().add(rectangle);
                moviePane.getChildren().add(title);
                moviePane.getChildren().add(dur);
                moviePane.getChildren().add(dates);
                moviePane.getChildren().add(buyTickets);
                moviePane.getChildren().add(infoButton);
                movieBox.getChildren().add(moviePane);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Text createTitle(String movieTitle){
        Text title = new Text();
        title.setText(movieTitle);
        title.setFont(Font.font("Arial", 30));
        double text_width = title.getLayoutBounds().getWidth();
        title.setLayoutX((1080 - text_width) / 2);
        title.setLayoutY(45);
        return title;
    }
    public Text createDuration(int duration){
        Text dur = new Text();
        dur.setText("Duration: "+ duration + " min");
        dur.setFont(Font.font("Arial", 20));
        dur.setLayoutX(5);
        dur.setLayoutY(100);
        return dur;
    }
    public Text createDate(Timestamp date){
        Text dates = new Text();
        dates.setText(date.toString());
        dates.setFont(Font.font("Arial", 20));
        dates.setLayoutX(5);
        dates.setLayoutY(77);
        return dates;
    }
}
