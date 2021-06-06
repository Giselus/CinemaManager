package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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

    public static int movieID;

    @FXML
    Text titleText;
    @FXML
    Text scoreText;
    @FXML
    ImageView moviePoster;
    @FXML
    VBox movieCast;

    private void refreshMovie(){
        try {
            String query = "SELECT * FROM film WHERE id = " + String.valueOf(movieID) + ";";
            ResultSet movieInfo = QueryExecutor.executeSelect(query);
            if (movieInfo.next()) {
                titleText.setText(movieInfo.getString("tytul"));
                String scoreQuery = "SELECT score(" + movieID + ");";
                ResultSet scoreResult = QueryExecutor.executeSelect(scoreQuery);
                if(scoreResult.next()){
                    scoreText.setText(String.valueOf(Math.round(scoreResult.getFloat(1)*100)/100f));
                }
            }
            //TODO: add cast


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
        String addCommentQuery = String.format("INSERT INTO historia_ocen(id_filmu, id_klienta, ocena, komentarz)" +
                        " VALUES(%d, %d, %d, '%s');",
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
            String noOfCommentsQuery = String.format("SELECT COUNT(*) FROM historia_ocen WHERE id_filmu = %d;",movieID);
            ResultSet noOfCommentsResult = QueryExecutor.executeSelect(noOfCommentsQuery);
            if(noOfCommentsResult.next()){
                commentsPageText.setText(String.format("Comments page(1-%d):",(noOfCommentsResult.getInt(1)-1)/10 +1));
            }

            commentsPageField.setText(String.valueOf(commentsPage));

            String commentsQuery = String.format("SELECT * FROM historia_ocen " +
                    "WHERE id_filmu = %d ORDER BY data_wystawienia DESC LIMIT 10 OFFSET %d;",movieID, (commentsPage-1)*10);
            ResultSet comments = QueryExecutor.executeSelect(commentsQuery);
            commentsBox.getChildren().clear();
            while(comments.next()){
                AnchorPane commentPane = new AnchorPane();
                commentPane.setPrefWidth(1080);
                Text comment = new Text();
                comment.wrappingWidthProperty().set(600);
                comment.setText(comments.getString("komentarz"));
                commentPane.getChildren().add(comment);
                commentsBox.getChildren().add(commentPane);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
