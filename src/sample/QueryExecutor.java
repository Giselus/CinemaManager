package sample;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class QueryExecutor {

    public static ResultSet executeSelect(String selectQuery){
        try{
            Connection connection = DBConnector.connect();
            Statement statement = connection.createStatement();
            return statement.executeQuery(selectQuery);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void executeQuery(String query){
        try{
            Connection connection = DBConnector.connect();
            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
