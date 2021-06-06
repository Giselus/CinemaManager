package sample.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sample.Main;
import sample.QueryExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

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
        baseButton.setOnAction(e -> Main.setScene("/sample/fxml/base.fxml","/sample/style/style.css"));
        repertoireButton.setOnAction(e -> Main.setScene("/sample/fxml/repertoire.fxml","/sample/style/style.css"));
        if(!Main.logged){
            loginButton.setText("Log in");
            loginButton.setOnAction((e) -> Main.setScene("/sample/fxml/logIn.fxml","/sample/style/styleLogIn.css"));
            signupButton.setText("Sign up");
            signupButton.setOnAction((e) -> Main.setScene("/sample/fxml/signUp.fxml","/sample/style/styleSignUp.css"));
        }else{
            //TODO: set Buttons actions
            loginButton.setText("Account");
            loginButton.setOnAction(e -> Main.setScene("/sample/fxml/account.fxml","/sample/style/style.css"));
            signupButton.setText("Log out");
            signupButton.setOnAction(e -> {
                Main.logged = false;
                refreshLook();
            });
        }
    }

    private enum FilterType{
        ScoreUp,ScoreDown,AlphabeticalUp,AlphabeticalDown;
    }
    public static FilterType filter = FilterType.ScoreDown;

    @FXML
    ChoiceBox<String> filterChoice;

    @FXML
    Text pageText;

    @FXML
    TextField pageField;

    @FXML
    private void applyFilter(){
        switch (filterChoice.getSelectionModel().getSelectedItem()) {
            case "Score from highest" -> filter = FilterType.ScoreDown;
            case "Score from lowest" -> filter = FilterType.ScoreUp;
            case "From A to Z" -> filter = FilterType.AlphabeticalUp;
            case "From Z to A" -> filter = FilterType.AlphabeticalDown;
        }
        page = Integer.parseInt(pageField.getText());
        refreshMovies();
    }

    private static int page = 1;

    @FXML
    VBox movieBox;

    private void refreshMovies(){
        // filters
        //TODO: add filter for genre
        filterChoice.setItems(FXCollections.observableArrayList(
                "Score from highest", "Score from lowest" , "From A to Z", "From Z to A"));
        switch (filter) {
            case ScoreDown -> filterChoice.getSelectionModel().select(0);
            case ScoreUp -> filterChoice.getSelectionModel().select(1);
            case AlphabeticalDown -> filterChoice.getSelectionModel().select(2);
            case AlphabeticalUp -> filterChoice.getSelectionModel().select(3);
        }
        pageField.setText(String.valueOf(page));
        String moviesNo = "SELECT COUNT(*) FROM film";
        ResultSet moviesNoResult = QueryExecutor.executeSelect(moviesNo);
        try {
            if(moviesNoResult.next()) {
                int moviesNoInt = moviesNoResult.getInt(1);
                pageText.setText("Page(1-"+String.valueOf((moviesNoInt-1)/10+1) + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // movies
        String query = "SELECT * FROM film_filtry ";
        switch (filter) {
            case ScoreDown -> query += "ORDER BY score DESC";
            case ScoreUp -> query += "ORDER BY score ASC";
            case AlphabeticalDown -> query += "ORDER BY tytul DESC";
            case AlphabeticalUp -> query += "ORDER BY tytul ASC";
        }
        query += " LIMIT 10 OFFSET ";
        query += String.valueOf((page-1) * 10);
        try {
            ResultSet result = QueryExecutor.executeSelect(query);
            movieBox.getChildren().clear();
            while (result.next()) {
                int id = result.getInt(1);
                String title = result.getString(3);
                float score = result.getFloat(2);
                score = Math.round(score * 100)/100f;
                //Todo: just generate movies and so
                AnchorPane moviePane = new AnchorPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(1080);
                rectangle.setHeight(200);
                rectangle.setFill(Paint.valueOf("DODGERBLUE"));
                Text titleText = new Text();
                titleText.setLayoutX(50);
                titleText.setLayoutY(20);
                Text scoreText = new Text();
                scoreText.setLayoutY(60);
                titleText.setText(title);
                scoreText.setText(String.valueOf(score));
                Button button = new Button();
                button.setOnAction(e -> goToMovie(id));
                moviePane.getChildren().add(rectangle);
                moviePane.getChildren().add(titleText);
                moviePane.getChildren().add(scoreText);
                moviePane.getChildren().add(button);
                movieBox.getChildren().add(moviePane);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        // pages

    }

    private void goToMovie(int id){
        movieController.movieID = id;
        Main.setScene("/sample/fxml/movie.fxml","/sample/style/style.css");

    }
}
