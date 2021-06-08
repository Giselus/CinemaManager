package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main extends Application {
    public static Stage mainStage;
    public static Object controller;
    private static ArrayList<Pair<String, String>> previousScenes = new ArrayList<>();
    private static String currentScene, currentStyle;

    public static int ID;
    public static boolean logged = false;
    public static void setScene(String tmp,String styleCss){
        if(currentScene != null)
            previousScenes.add(new Pair<>(currentScene,currentStyle));
        currentScene = tmp;
        currentStyle = styleCss;
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

    public static void setLastScene(){
        if(previousScenes.isEmpty())
            return;
        currentScene = previousScenes.get(previousScenes.size()-1).getKey();
        currentStyle = previousScenes.get(previousScenes.size()-1).getValue();
        previousScenes.remove(previousScenes.size()-1);
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(Main.class.getResource(currentScene));
            Parent root= loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Main.class.getResource(currentStyle).toExternalForm());
            mainStage.setScene(scene);
            mainStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void initializeDatabase(){
        try {
            String query = Files.readString(Paths.get(Main.class.getResource("/sample/SQL/clear.sql").toURI()));
            QueryExecutor.executeQuery(query);
            query = Files.readString(Paths.get(Main.class.getResource("/sample/SQL/new_kino.sql").toURI()));
            QueryExecutor.executeQuery(query);
            query = Files.readString(Paths.get(Main.class.getResource("/sample/SQL/wypelnienie.sql").toURI()));
            QueryExecutor.executeQuery(query);
            query = Files.readString(Paths.get(Main.class.getResource("/sample/SQL/random.sql").toURI()));
            QueryExecutor.executeQueryWithoutReturns(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setResizable(false);
        mainStage=primaryStage;
        setScene("fxml/firstScene.fxml","/sample/style/styleLogIn.css");

    }
    public static void main(String[] args) {
        launch(args);
    }
}
