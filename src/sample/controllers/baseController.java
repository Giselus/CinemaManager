package sample.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import sample.Main;
import sample.QueryExecutor;

import java.sql.ResultSet;

public class baseController {

    @FXML
    Button baseButton;
    @FXML
    Button repertoireButton;
    @FXML
    Button loginButton;
    @FXML
    Button signupButton;

    @FXML
    public void initialize(){
        refreshLook();
    }

    private void refreshLook(){
        refreshUpper();
        refreshMovies();
    }

    private void refreshUpper(){
        if(!Main.Logged){
            loginButton.setText("Log in");
            loginButton.setOnAction((e) -> Main.setScene("/sample/fxml/logIn.fxml","/sample/style/styleLogIn.css"));
            signupButton.setText("Sign up");
            signupButton.setOnAction((e) -> Main.setScene("/sample/fxml/signUp.fxml","/sample/style/styleSignUp.css"));
        }else{
            //TODO: set Buttons actions
            loginButton.setText("Account");
            signupButton.setText("Log out");
        }
    }

    private enum FilterType{
        ScoreUp,ScoreDown,AlphabeticalUp,AlphabeticalDown;
    }
    public static FilterType filter = FilterType.ScoreDown;

    @FXML
    ChoiceBox<String> filterChoice;

    @FXML
    private void applyFilter(){
        //do something
        switch(filterChoice.getSelectionModel().getSelectedItem()){
            case "Score from highest":
                filter = FilterType.ScoreDown;
                break;
            case "Score from lowest":
                filter = FilterType.ScoreUp;
                break;
            case "From A to Z":
                filter = FilterType.AlphabeticalDown;
                break;
            case "From Z to A":
                filter = FilterType.AlphabeticalUp;
                break;
        }

        refreshMovies();
    }

    private int page = 1;

    private void refreshMovies(){
        // filters
        filterChoice.setItems(FXCollections.observableArrayList(
                "Score from highest", "Score from lowest" , "From A to Z", "From Z to A"));
        switch(filter){
            case ScoreDown:
                filterChoice.getSelectionModel().select(0);
                break;
            case ScoreUp:
                filterChoice.getSelectionModel().select(1);
                break;
            case AlphabeticalDown:
                filterChoice.getSelectionModel().select(2);
                break;
            case AlphabeticalUp:
                filterChoice.getSelectionModel().select(3);
                break;
        }
        // movies
        String query = "SELECT * FROM film_filtry ";
        switch(filter){
            case ScoreDown:
                query += "ORDER BY score DESC";
                break;
            case ScoreUp:
                query += "ORDER BY score ASC";
                break;
            case AlphabeticalDown:
                query += "ORDER BY tytul DESC";
                break;
            case AlphabeticalUp:
                query += "ORDER BY tytul ASC";
                break;
        }
        query += " LIMIT 10 OFFSET ";
        query += String.valueOf((page-1) * 10);
        try {
            ResultSet result = QueryExecutor.executeSelect(query);
            while (result.next()) {
                System.out.println(result);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        // pages
    }
}
