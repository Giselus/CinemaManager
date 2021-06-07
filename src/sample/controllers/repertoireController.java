package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
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
import java.time.LocalDate;
import java.util.ArrayList;

public class repertoireController {
    @FXML public VBox movieBox;
    @FXML public AnchorPane myPane;
    @FXML public ChoiceBox<String> filterChoice;
    public static String movie_name = "";
    public static boolean fromBase = false;
    public static int sala=0;
    public static int cena = 0;
    public static int seans = 0;
    @FXML public DatePicker callendar;

    public void initialize() {
        callendar.setValue(LocalDate.now());
        String query;
        query = "SELECT DISTINCT(f.tytul) FROM film f join seans s ON s.id_filmu = f.id" +
                " WHERE data_rozpoczecia > current_date";
        filterChoice.getItems().add("All");
        try{
            ResultSet result = QueryExecutor.executeSelect(query);
            while(result.next()){
                String temp_name = result.getString(1);
                filterChoice.getItems().add(temp_name);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        filterChoice.setValue("All");
        if(!fromBase) {
            query = "SELECT * FROM seans JOIN film ON seans.id_filmu = film.id" +
                    " WHERE data_rozpoczecia > current_date ORDER BY data_rozpoczecia;";
            setUpView(query);
        } else {
            fromBase = false;
            filterChoice.setValue(movie_name);
            setUpView(createQueryFromChoiceBox(filterChoice));
        }
    }
    @FXML public void applyFilter(){
        setUpView(createQueryFromChoiceBox(filterChoice));
    }

    public String createQueryFromChoiceBox(ChoiceBox<String> choiceBox){
        String boxItem = choiceBox.getValue();
        LocalDate calendar = callendar.getValue();

        String query = "SELECT * FROM seans JOIN film ON seans.id_filmu = film.id" +
                " WHERE data_rozpoczecia > '" + calendar.toString()+"'";
        if(boxItem.equals("All")){
            query += " ORDER BY data_rozpoczecia;";
            return query;
        }
        query += "and film.tytul = '" + boxItem + "' ORDER BY data_rozpoczecia;";
        return query;
    }
    public void setUpView(String query){
        movieBox.getChildren().clear();
        try {
            ResultSet result = QueryExecutor.executeSelect(query);
            while (result.next()) {
                Timestamp date = result.getTimestamp(3);
                String movieTitle = result.getString(10);
                int seansID = result.getInt(1);
                int duration = result.getInt(12);
                int tmpSala = result.getInt(4);
                int tmpCena = result.getInt(6);
                AnchorPane moviePane = new AnchorPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(1280);
                rectangle.setHeight(200);
                rectangle.setFill(Color.rgb(169, 167, 167));
                Text title = createTitle(movieTitle);
                Text dur = createDuration(duration);
                Text dates = createDate(date);
                Text seats = availableSeat(seansID);
                Text price = createPrice(seansID);
                Text sound = createSound(seansID);
                Button buyTickets = new Button();
                buyTickets.setLayoutX(1000);
                buyTickets.setLayoutY(120);
                buyTickets.resize(140, 60);
                buyTickets.setText("BUY");
                buyTickets.setOnAction(event -> {
                    seans = seansID;
                    sala = tmpSala;
                    cena = tmpCena;
                    Main.setScene("/sample/fxml/reservation.fxml", "/sample/style/styleReservation.css");
                });
                Button infoButton = new Button();
                infoButton.setLayoutX(1070);
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
                moviePane.getChildren().add(seats);
                moviePane.getChildren().add(price);
                moviePane.getChildren().add(sound);
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
        title.setLayoutX((1280 - text_width) / 2);
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
    public Text availableSeat(int id_seans){
        String query = "SELECT wolne_miejsca("+id_seans+");";
        try{
        ResultSet result = QueryExecutor.executeSelect(query);
        while (result.next()) {
            int sss = result.getInt(1);
            Text seats = new Text();
            seats.setText("Available seats: "+sss);
            seats.setFont(Font.font("Arial", 20));
            seats.setLayoutX(5);
            seats.setLayoutY(123);
            return seats;
        }
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Text();
    }
    public Text createPrice(int id_seans){
        Text price = new Text();
        String query = "SELECT cena FROM seans WHERE id = " + id_seans+";";
        try{
            ResultSet result = QueryExecutor.executeSelect(query);
            int pln = 0;
            while (result.next()){
                pln = result.getInt(1);
            }
            price.setText("Price: "+ pln + " pln");
            price.setFont(Font.font("Arial", 20));
            price.setLayoutX(5);
            price.setLayoutY(169);
            return price;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Text();
    }
    public Text createSound(int id_seans){
        Text price = new Text();
        String query = "SELECT dzwiek FROM seans WHERE id = " + id_seans+";";
        try{
            ResultSet result = QueryExecutor.executeSelect(query);
            String pln = "";
            while (result.next()){
                pln = result.getString(1);
            }
            if(pln.equals("oryginal")){
                pln = "original";
            } else if(pln.equals("dubbing")){
                pln = "dubbing";
            } else {
                pln = "lector";
            }
            price.setText("Sound: "+ pln);
            price.setFont(Font.font("Arial", 20));
            price.setLayoutX(5);
            price.setLayoutY(146);
            return price;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Text();
    }
}
