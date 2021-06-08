package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import sample.Main;
import sample.QueryExecutor;

import java.sql.ResultSet;

public class movieController {

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
        refreshMovie();
        refreshComments();
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
            loginButton.setText("Account");
            loginButton.setOnAction(e -> Main.setScene("/sample/fxml/account.fxml","/sample/style/style.css"));
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

    public static int movieID;

    @FXML
    Text titleText;
    @FXML
    Text scoreText;
    @FXML
    ImageView moviePoster;
    @FXML
    VBox movieCast;
    @FXML
    VBox infoBox;
    private void refreshMovie(){
        try {
            String query = "SELECT * FROM film WHERE id = " + String.valueOf(movieID) + ";";
            ResultSet movieInfo = QueryExecutor.executeSelect(query);
            if (movieInfo.next()) {
                titleText.setText(movieInfo.getString("tytul"));
                titleText.setFont(Font.font ("Verdana", 30));
                String scoreQuery = "SELECT score(" + movieID + ");";
                ResultSet scoreResult = QueryExecutor.executeSelect(scoreQuery);
                if(scoreResult.next()){
                    scoreText.setText(String.valueOf(Math.round(scoreResult.getFloat(1)*100)/100f));
                    scoreText.setFont(Font.font ("Verdana", 30));
                }
            }
            query = String.format("SELECT DISTINCT studio FROM wytwornia WHERE id IN (SELECT id_wytwornia FROM" +
                    " film_wytwornia WHERE id_filmu = %d)",movieID);
            ResultSet result = QueryExecutor.executeSelect(query);
            String wytwornie = "Companies: ";
            boolean tmp = false;
            while(result.next()){
                if(tmp)
                    wytwornie += ", ";
                tmp = true;
                wytwornie += result.getString("studio");
            }
            Text wytwornieText = new Text();
            wytwornieText.setText(wytwornie);
            wytwornieText.setFont(Font.font ("Verdana", 20));

            query = String.format("SELECT DISTINCT rodzaj FROM gatunek WHERE id IN (SELECT id_gatunku FROM" +
                    " film_gatunek WHERE id_filmu = %d)",movieID);
            result = QueryExecutor.executeSelect(query);
            String gatunki = "Genres: ";
            tmp = false;
            while(result.next()){
                if(tmp)
                    gatunki += ", ";
                tmp = true;
                gatunki += result.getString("rodzaj");
            }
            Text gatunkiText = new Text();
            gatunkiText.setText(gatunki);
            gatunkiText.setFont(Font.font ("Verdana", 20));
            query = String.format("SELECT DISTINCT jezyk_oryginalu FROM jezyk WHERE id IN (SELECT id_jezyk FROM" +
                    " film_jezyk WHERE id_filmu = %d)",movieID);
            result = QueryExecutor.executeSelect(query);
            String jezyki = "Languages: ";
            tmp = false;
            while(result.next()){
                if(tmp)
                    jezyki += ", ";
                tmp = true;
                jezyki += result.getString("jezyk_oryginalu");
            }
            Text jezykiText = new Text();
            jezykiText.setText(jezyki);
            jezykiText.setFont(Font.font ("Verdana", 20));
            query = String.format("SELECT DISTINCT kraj FROM kraj WHERE id IN (SELECT id_kraj FROM" +
                    " film_kraj WHERE id_filmu = %d)",movieID);
            result = QueryExecutor.executeSelect(query);
            String kraje = "Production places: ";
            tmp = false;
            while(result.next()){
                if(tmp)
                    kraje += ", ";
                tmp = true;
                kraje += result.getString("kraj");
            }
            Text krajeText = new Text();
            krajeText.setText(kraje);
            krajeText.setFont(Font.font ("Verdana", 20));
            infoBox.getChildren().clear();
            gatunkiText.setWrappingWidth(600);
            krajeText.setWrappingWidth(600);
            jezykiText.setWrappingWidth(600);
            wytwornieText.setWrappingWidth(600);
            infoBox.getChildren().add(gatunkiText);
            infoBox.getChildren().add(krajeText);
            infoBox.getChildren().add(jezykiText);
            infoBox.getChildren().add(wytwornieText);

            movieCast.getChildren().clear();
            query = String.format("SELECT * FROM film JOIN produkcja ON id = id_filmu AND id = %d",movieID);
            ResultSet castInfo = QueryExecutor.executeSelect(query);
            while(castInfo.next()){
                String name_surname = null;
                String position_name = null;
                int id_pozycji = castInfo.getInt("id_pozycja");
                int id_osoby = castInfo.getInt("id_osoba");
                String tmpQuery = String.format("SELECT * FROM osoby WHERE id = %d",id_osoby);
                ResultSet tmpResult = QueryExecutor.executeSelect(tmpQuery);
                if(tmpResult.next()){
                    name_surname = tmpResult.getString("imie") + " " + tmpResult.getString("nazwisko");
                }
                tmpQuery = String.format("SELECT * FROM pozycja WHERE id = %d",id_pozycji);
                tmpResult = QueryExecutor.executeSelect(tmpQuery);
                if(tmpResult.next()){
                    position_name = tmpResult.getString("nazwa");
                }
                AnchorPane castPane = new AnchorPane();
                Text nameText = new Text();
                nameText.setText(name_surname);
                Text positonText = new Text();
                positonText.setText(position_name);
                castPane.getChildren().add(nameText);
                castPane.getChildren().add(positonText);
                nameText.setLayoutY(20);
                positonText.setLayoutY(40);
                nameText.setLayoutX(80);
                positonText.setLayoutX(80);
                nameText.setFont(Font.font ("Verdana", 20));
                positonText.setFont(Font.font ("Verdana", 17));
                Button button = new Button();
                button.setOnAction(e -> {
                    personController.personID = id_osoby;
                    Main.setScene("/sample/fxml/person.fxml","/sample/style/stylePerson.css");
                });
                button.setLayoutY(0);
//                button.setPrefSize(10,10);
//                button.setMaxSize(10,10);
                button.setText("Check");
                castPane.getChildren().add(button);
                movieCast.getChildren().add(castPane);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    TextArea commentArea;
    @FXML
    TextField scoreField;

    @FXML
    public void publishComment(){
        if(!Main.logged){
            System.out.println("You are not logged :c");
            return;
        }
        String addCommentQuery = String.format("INSERT INTO historia_ocen" +
                        " VALUES(%d, %d, %d, current_date,'%s');",
                movieID,Main.ID,Integer.valueOf(scoreField.getText()),commentArea.getText());
        QueryExecutor.executeQuery(addCommentQuery);
        commentArea.setText("");
        refreshLook();
    }

    @FXML
    Text commentsPageText;

    @FXML
    TextField commentsPageField;

    @FXML
    VBox commentsBox;

    private static int commentsPage = 1;

    @FXML
    private void applyCommentsFilter(){
        commentsPage = Integer.parseInt(commentsPageField.getText());
        refreshComments();
    }

    private void refreshComments(){
        try{
            String noOfCommentsQuery = String.format("SELECT COUNT(*) FROM film_komentarze WHERE id = %d;",movieID);
            ResultSet noOfCommentsResult = QueryExecutor.executeSelect(noOfCommentsQuery);
            if(noOfCommentsResult.next()){
                commentsPageText.setText(String.format("Comments page(1-%d):",(noOfCommentsResult.getInt(1)-1)/10 +1));
            }

            commentsPageField.setText(String.valueOf(commentsPage));

            String commentsQuery = String.format("SELECT * FROM film_komentarze WHERE id = %d;",movieID);
            ResultSet comments = QueryExecutor.executeSelect(commentsQuery);
            commentsBox.getChildren().clear();
            while(comments.next()){
                AnchorPane commentPane = new AnchorPane();
                commentPane.setPrefWidth(1280);
                Text comment = new Text();

                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(1280);
                rectangle.setHeight(140);
                rectangle.setFill(Color.valueOf("#c8cccb"));

                comment.setLayoutY(25);
                comment.setLayoutX(20);
                comment.wrappingWidthProperty().set(600);
                comment.setText(comments.getString("komentarz"));
                comment.setFont(Font.font ("Verdana", 20));
                Text nameSurname = new Text();
                nameSurname.setLayoutX(610);
                nameSurname.setLayoutY(25);
                nameSurname.setText(comments.getString("imie") + " " + comments.getString("nazwisko"));
                nameSurname.setFont(Font.font ("Verdana", 20));
                Text score = new Text();
                score.setLayoutX(610);
                score.setLayoutY(50);
                score.setText("Score given: " + comments.getString("ocena") + "*");
                score.setFont(Font.font ("Verdana", 20));
                Text date = new Text();
                date.setLayoutX(610);
                date.setLayoutY(75);
                date.setText(comments.getString("data_wystawienia"));
                date.setFont(Font.font ("Verdana", 20));
                commentPane.getChildren().add(rectangle);
                commentPane.getChildren().add(comment);
                commentPane.getChildren().add(nameSurname);
                commentPane.getChildren().add(score);
                commentPane.getChildren().add(date);
                commentsBox.getChildren().add(commentPane);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
