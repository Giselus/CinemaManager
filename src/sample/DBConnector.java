package sample;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnector {

    //public static String URL = "jdbc:postgresql://localhost/IdProjekt?allowMultiQueries=true";
    public static String USER = "postgres";
    public static String PASSWORD = "projektid";
    public static String PORT = "projektid";
    public static String HOST = "projektid";
    public static String DATABASE = "projektid";
    public static String URL=null;
    public static Connection connect(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }catch(Exception e){
            e.printStackTrace();
        }
        return connection;
    }

}
