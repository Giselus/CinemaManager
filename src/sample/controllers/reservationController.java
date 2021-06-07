package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sample.Main;
import sample.QueryExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class reservationController {

    @FXML public Button buyButton;
    @FXML public Button reserveButton;
    @FXML public AnchorPane panePane;
    int counter = 10;
    int start_counter=10;
    public static boolean isBuying = true;
    public static int amountOfTickets = 0;
    public static ArrayList<Integer> rows;
    public static ArrayList<Integer> columns;
    int number_of_rows = 8;
    int number_of_columns = 15;
    Rectangle[][] table;
    char[][] taken_table;
    Text[] column_names;
    Text[] row_names;
    public void initialize(){
        String query = "SELECT wolne_miejsca("+repertoireController.seans+");";
        try{
            ResultSet resultSet=QueryExecutor.executeSelect(query);
            while(resultSet.next()){
                start_counter = resultSet.getInt(1);
            }
            start_counter = Math.min(start_counter, 10);
            counter = start_counter;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        query="SELECT * from sala WHERE id="+repertoireController.sala+";";
        try {
            ResultSet resultSet= QueryExecutor.executeSelect(query);
            while(resultSet.next()){
                number_of_rows=resultSet.getInt(2);
                number_of_columns=resultSet.getInt(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        table = new Rectangle[number_of_rows][number_of_columns];
        taken_table = new char[number_of_rows][number_of_columns];
        for(int i=0; i<number_of_rows; i++){
            for(int j=0; j<number_of_columns; j++){
                taken_table[i][j] = 0;
            }
        }
        query = "select b.numer_rzedu, b.numer_miejsca from bilet b join zamowienie z on b.id_zamowienia " +
                "= z.id join seans s ON z.id_seansu = s.id WHERE s.id = "+repertoireController.seans+";";
        try {
            ResultSet resultSet= QueryExecutor.executeSelect(query);
            while(resultSet.next()){
                int x=resultSet.getInt(1);
                int y=resultSet.getInt(2);
                taken_table[x][y] = 2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int width = number_of_columns*50;
        int height = number_of_rows*50;
        int startX = (1280 - width)/2;
        int startY = (720 - height)/2;
        column_names = new Text[number_of_columns];
        for(int i=0; i<number_of_columns; i++){
            column_names[i] = new Text();
            column_names[i].setText(String.valueOf(i+1));
            column_names[i].setX(startX + 50*i + 15);
            column_names[i].setY(startY + 50* number_of_rows + 25);
            column_names[i].resize(50, 50);
            panePane.getChildren().add(column_names[i]);
        }
        row_names = new Text[number_of_rows];
        for(int i=0; i<number_of_rows; i++){
            row_names[i] = new Text();
            row_names[i].setText(String.valueOf(i+1));
            row_names[i].setX(startX - 30);
            row_names[i].setY(startY + 50*i + 30);
            row_names[i].resize(50, 50);
            panePane.getChildren().add(row_names[i]);
        }
        Line line = new Line();
        line.setStartX(startX + 3*50);
        line.setStartY(startY - 100);
        line.setEndX(startX + number_of_columns*50 - 150);
        line.setEndY(startY - 100);
        line.setStrokeWidth(3.5);
        panePane.getChildren().add(line);
        for(int i=0; i<number_of_rows; i++){
            for(int j=0; j<number_of_columns; j++){
                table[i][j] = new Rectangle();
                table[i][j].setWidth(50);
                table[i][j].setHeight(50);
                table[i][j].setX(startX + 50*j);
                table[i][j].setY(startY + 50*i);
                table[i][j].setStrokeWidth(1);
                table[i][j].setStroke(Color.BLACK);

                int finalI = i+1;
                int finalJ = j+1;
                table[i][j].setOnMousePressed(e->{
                    if(e.getButton() == MouseButton.PRIMARY){
                        if((counter == 0 && taken_table[finalI-1][finalJ-1] == 0) || (taken_table[finalI-1][finalJ-1] == 2)){
                            return;
                        }
                        taken_table[finalI-1][finalJ-1] = (char) (1 - (int)taken_table[finalI-1][finalJ-1]);
                        if(taken_table[finalI-1][finalJ-1] == 1){
                            counter--;
                        } else {
                            counter++;
                        }
                    }
                    updateButton();
                });
                table[i][j].setOnMouseEntered(e->{
                    if(taken_table[finalI-1][finalJ-1] == 1){
                        table[finalI-1][finalJ-1].setFill(Color.DARKORANGE);
                    } else if(taken_table[finalI-1][finalJ-1] == 0){
                        table[finalI-1][finalJ-1].setFill(Color.DARKGREEN);
                    } else {
                        table[finalI-1][finalJ-1].setFill(Color.DARKRED);
                    }
                });
                table[i][j].setOnMouseExited(e->{
                    if(taken_table[finalI-1][finalJ-1] == 1){
                        table[finalI-1][finalJ-1].setFill(Color.ORANGE);
                    } else if(taken_table[finalI-1][finalJ-1] == 0){
                        table[finalI-1][finalJ-1].setFill(Color.GREEN);
                    } else {
                        table[finalI-1][finalJ-1].setFill(Color.RED);
                    }
                });
                panePane.getChildren().add(table[i][j]);
            }
        }
        updateButton();
    }
    public void updateButton(){
        for(int i=0; i<number_of_rows; i++){
            for(int j=0; j<number_of_columns; j++){
                if(taken_table[i][j] == 1){
                    table[i][j].setFill(Color.ORANGE);
                } else if(taken_table[i][j] == 0) {
                    table[i][j].setFill(Color.GREEN);
                } else {
                    table[i][j].setFill(Color.RED);
                }
            }
        }
    }
    @FXML public void useBuyButton(){
        isBuying = true;
        amountOfTickets = start_counter - counter;
        if(amountOfTickets == 0){
            return;
        }
        addSeats();
        Main.setScene("/sample/fxml/ticketNumber.fxml", "/sample/style/styleReservation.css");
    }
    @FXML public void useReserveButton(){
        isBuying = false;
        amountOfTickets = start_counter-counter;
        if(amountOfTickets == 0){
            return;
        }
        addSeats();
        Main.setScene("/sample/fxml/ticketNumber.fxml", "/sample/style/styleReservation.css");
    }
    public void addSeats(){
        rows = new ArrayList<>();
        columns = new ArrayList<>();
        for(int i=0; i<number_of_rows; i++){
            for(int j=0; j<number_of_columns; j++){
                if(taken_table[i][j] == 1){
                    rows.add(i);
                    columns.add(j);
                }
            }
        }
    }
}
