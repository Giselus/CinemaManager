package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage mainStage;
    public static Object controller;

    public static void setScene(String tmp,String styleCss){
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(Main.class.getResource(tmp));
            Parent root= loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Main.class.getResource(styleCss).toExternalForm());
            mainStage.setScene(scene);
            mainStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setResizable(false);
        mainStage=primaryStage;
        setScene("fxml/sample.fxml","/sample/style/style.css");
    }
    public void logIn(Stage primaryStage) throws Exception{
        primaryStage.setResizable(false);
        mainStage=primaryStage;
        setScene("fxml/logLn.fxml","/sample/style/style.css");
    }
    public static void main(String[] args) {
        launch(args);
    }
}
