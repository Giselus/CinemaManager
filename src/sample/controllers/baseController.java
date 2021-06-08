package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import sample.Main;
import sample.QueryExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

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
        baseButton.setOnAction(e -> Main.setScene("/sample/fxml/base.fxml","/sample/style/styleBase.css"));
        repertoireButton.setOnAction(e -> Main.setScene("/sample/fxml/repertoire.fxml","/sample/style/style.css"));
        if(!Main.logged){
            loginButton.setText("Log in");
            loginButton.setOnAction((e) -> Main.setScene("/sample/fxml/logIn.fxml","/sample/style/styleLogIn.css"));
            signupButton.setText("Sign up");
            signupButton.setOnAction((e) -> Main.setScene("/sample/fxml/signUp.fxml","/sample/style/styleSignUp.css"));
            //testCode.setOnAction((e) -> Main.setScene("/sample/fxml/reservation.fxml","/sample/style/styleReservation.css"));
        }else{

            loginButton.setText("Account");
            loginButton.setOnAction(e -> Main.setScene("/sample/fxml/account.fxml","/sample/style/styleAccount.css"));
            signupButton.setText("Log out");
            signupButton.setOnAction(e -> {
                Main.logged = false;
                refreshLook();
            });
        }
    }

    @FXML
    private void backToPreviousPage(){
        Main.setLastScene();
    }

    private enum FilterType{
        ScoreUp,ScoreDown,AlphabeticalUp,AlphabeticalDown;
    }
    public static FilterType filter = FilterType.ScoreDown;

    @FXML
    ChoiceBox<String> genreChoice;

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
        genreFilter = genreChoice.getSelectionModel().getSelectedItem();
        refreshMovies();
    }

    private static int page = 1;

    @FXML
    VBox movieBox;

    String genreFilter = "Every genre";

    private void refreshMovies() {
        // filters
        ObservableList<String> genres = FXCollections.observableArrayList();
        genres.add("Every genre");
        try {
            String genresQuery = "SELECT * FROM gatunek";
            ResultSet genresResult = QueryExecutor.executeSelect(genresQuery);
            while (genresResult.next()){
                genres.add(genresResult.getString("rodzaj"));
            }
            genreChoice.setItems(genres);

        }catch (Exception e){
            e.printStackTrace();
        }
        filterChoice.setItems(FXCollections.observableArrayList(
                "Score from highest", "Score from lowest" , "From A to Z", "From Z to A"));
        switch (filter) {
            case ScoreDown -> filterChoice.getSelectionModel().select(0);
            case ScoreUp -> filterChoice.getSelectionModel().select(1);
            case AlphabeticalUp -> filterChoice.getSelectionModel().select(2);
            case AlphabeticalDown -> filterChoice.getSelectionModel().select(3);
        }
        genreChoice.getSelectionModel().select(genreFilter);
        pageField.setText(String.valueOf(page));
        String moviesNo = "SELECT COUNT(*) FROM film";
        if(genreFilter != "Every genre")
            moviesNo = String.format("SELECT COUNT(*) FROM film WHERE id IN (SELECT id_filmu " +
                    "FROM gatunek JOIN film_gatunek ON id = id_gatunku WHERE rodzaj = '%s');",genreFilter);
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
        if(genreFilter != "Every genre")
            query = String.format("SELECT * FROM film_filtry WHERE id IN (SELECT id_filmu " +
                    "FROM gatunek JOIN film_gatunek ON id = id_gatunku WHERE rodzaj = '%s') ",genreFilter);
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
                int id = result.getInt("id");
                String title = result.getString("tytul");
                float score = result.getFloat("score");
                Timestamp premiere_date = result.getTimestamp("data_premiery");
                score = Math.round(score * 100)/100f;

                AnchorPane moviePane = new AnchorPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(1280);
                rectangle.setHeight(200);
                //rectangle.setId("rectangleId");
                //rectangle.setStyle("-fx-fill: red; -fx-stroke: black; -fx-stroke-width: 525;");
                rectangle.setFill(Paint.valueOf("#c8cccb"));
                Text titleText = new Text();
                titleText.setFont(Font.font("Arial", 30));
                titleText.setText(title);
                double text_width = titleText.getLayoutBounds().getWidth();
                titleText.setLayoutX((1280 - text_width) / 2);
                titleText.setLayoutY(40);

                Text marks=new Text();
                marks.setLayoutY(50);
                marks.setLayoutX(1150);
                marks.setText("Marks");
                Text scoreText = new Text();
                scoreText.setLayoutY(80);
                scoreText.setLayoutX(1160);

                scoreText.setText(String.valueOf(score));
                Button button = new Button();
                button.setOnAction(e -> goToMovie(id));
                button.setId("test");
                button.setText("Info");
                button.setLayoutX(565);
                button.setLayoutY(155);
                Button button2=new Button();
                button2.setOnAction(e -> goToRepertoire(title));
                button2.setId("test");
                button2.setText("Seanse");
                button2.setLayoutX(650);
                button2.setLayoutY(155);

                Text date_premiere = createDate(premiere_date);
                Text genre_text = createGenre(id);
                Text languages = createLanguage(id);

                moviePane.getChildren().add(rectangle);
                moviePane.getChildren().add(titleText);
                moviePane.getChildren().add(scoreText);
                moviePane.getChildren().add(marks);
                moviePane.getChildren().add(button);
                moviePane.getChildren().add(button2);
                moviePane.getChildren().add(date_premiere);
                moviePane.getChildren().add(genre_text);
                moviePane.getChildren().add(languages);
                movieBox.getChildren().add(moviePane);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        // pages
    }


    private void goToMovie(int id){
        movieController.movieID = id;
        Main.setScene("/sample/fxml/movie.fxml","/sample/style/stylePerson.css");
    }
    private void goToRepertoire(String name){
        repertoireController.fromBase = true;
        repertoireController.movie_name = name;
        Main.setScene("/sample/fxml/repertoire.fxml","/sample/style/style.css");
    }

    private Text createDate(Timestamp date){
        Text dates = new Text();
        dates.setText("Premiere: " + date.toString().substring(0, 10));
        dates.setFont(Font.font("Arial", 20));
        dates.setLayoutX(5);
        dates.setLayoutY(77);
        return dates;
    }

    private Text createGenre(int id){
        Text result = new Text();
        String query = "SELECT g.rodzaj FROM film JOIN film_gatunek fg ON film.id = fg.id_filmu " +
                "JOIN gatunek g ON g.id = fg.id_gatunku where film.id = " + id + ";";
        String solve = "";
        try{
            ResultSet resultSet = QueryExecutor.executeSelect(query);
            while(resultSet.next()){
                solve += resultSet.getString(1) + ", ";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(solve.isEmpty()){
            result.setText("Genres: unknown");
        } else {
            solve = solve.substring(0, solve.length()-2);
            result.setText("Genres: " + solve);
        }
        result.setLayoutX(5);
        result.setLayoutY(100);
        result.setFont(Font.font("Arial", 20));
        return result;
    }

    private Text createLanguage(int id){
        Text result = new Text();
        String query = "SELECT g.jezyk_oryginalu FROM film JOIN film_jezyk fg ON film.id = fg.id_filmu " +
                "JOIN jezyk g ON g.id = fg.id_jezyk where film.id = " + id + ";";
        String solve = "";
        try{
            ResultSet resultSet = QueryExecutor.executeSelect(query);
            while(resultSet.next()){
                solve += resultSet.getString(1) + ", ";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(solve.isEmpty()){
            result.setText("Languages: unknown");
        } else {
            solve = solve.substring(0, solve.length()-2);
            result.setText("Languages: " + solve);
        }
        result.setLayoutX(5);
        result.setLayoutY(123);
        result.setFont(Font.font("Arial", 20));
        return result;
    }
}