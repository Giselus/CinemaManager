package sample;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnector {

    private static String URL = "jdbc:postgresql://localhost/IdProjekt";
    private static String USER = "postgres";
    private static String PASSWORD = "projektid";

    public static Connection connect(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected successfully");
        }catch(Exception e){
            e.printStackTrace();
        }
        return connection;
    }

}
