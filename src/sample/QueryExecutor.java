package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class QueryExecutor {

    public static ResultSet executeSelect(String selectQuery){
        Connection connection = DBConnector.connect();
        try {
            try {

                Statement statement = connection.createStatement();
                return statement.executeQuery(selectQuery);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }finally {
                connection.close();
            }

        }catch ( Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void executeQuery(String query){
        Connection connection = DBConnector.connect();
        try {
            try {
                Statement statement = connection.createStatement();
                statement.execute(query);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }finally {
                connection.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void executeQueryWithoutReturns(String query){
        String T[] = query.split(";");
        Connection connection = DBConnector.connect();
        try {
            for (String str : T) {
                try {
                    Statement statement = connection.createStatement();
                    statement.execute(str);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
