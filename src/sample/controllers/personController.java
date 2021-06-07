package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import sample.Main;
import sample.QueryExecutor;

import java.sql.ResultSet;

public class personController {

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
        refreshInfo();
    }

    public static int personID;

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

    @FXML
    Text nameText;

    @FXML
    Text sexText;

    @FXML
    Text birthText;

    @FXML
    Text countryText;

    @FXML
    Text pageText;

    @FXML
    TextField pageField;

    private int page = 1;

    @FXML
    private void apply(){
        page = Integer.parseInt(pageField.getText());
        refreshLook();
    }

    @FXML
    VBox roleBox;

    private void refreshInfo(){
        try {
            String query = String.format("SELECT * FROM osoby WHERE id = %d;", personID);
            ResultSet result = QueryExecutor.executeSelect(query);
            if(result.next()){
                nameText.setText(result.getString("imie") + " " + result.getString("nazwisko"));
                if(result.getString("plec").equals("mezczyzna")){
                    sexText.setText("male");
                }else{
                    sexText.setText("female");
                }
                birthText.setText("Born: " + result.getString("data_urodzenia"));
                String tmpQuery = String.format("SELECT * FROM kraj WHERE id = %d;",result.getInt("miejsce_urodzenia"));
                ResultSet tmpResult = QueryExecutor.executeSelect(tmpQuery);
                if(tmpResult.next()){
                    countryText.setText("Birth place: " + tmpResult.getString("kraj"));
                }
            }
            String noOfPagesQuery = String.format("SELECT COUNT(*) FROM role_osoby WHERE id = %d",personID);
            ResultSet noOfPagesResult = QueryExecutor.executeSelect(noOfPagesQuery);
            if(noOfPagesResult.next())
                pageText.setText(String.format("Page(1-%d)",(noOfPagesResult.getInt(1)-1)/10 + 1));
            query = String.format("SELECT * FROM role_osoby WHERE id = %d ORDER BY data_premiery " +
                    "LIMIT 10 OFFSET %d",personID,(page-1)*10);
            result = QueryExecutor.executeSelect(query);

            roleBox.getChildren().clear();
            while(result.next()){
                Text movieName = new Text();
                Text movieDate = new Text();
                Text roleName = new Text();
                roleName.setText(result.getString("nazwa"));
                movieDate.setText(result.getString("data_premiery"));
                movieName.setText(result.getString("tytul"));
                HBox box = new HBox();
                box.setSpacing(20);
                box.getChildren().add(movieName);
                box.getChildren().add(roleName);
                box.getChildren().add(movieDate);
                Button button = new Button();
                button.setText("");
                Integer p = result.getInt("id_filmu");
                button.setOnAction(e -> {
                    movieController.movieID = p;
                    Main.setScene("/sample/fxml/movie.fxml","/sample/style/style.css");
                });
                box.getChildren().add(button);
                roleBox.getChildren().add(box);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }



}
