/*
 * http://www.zentus.com/sqlitejdbc/
 * sqlitejdbc-v056.jar
 */
package ru.spbau.cheque.server.ofx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author sam
 */
public class SqliteDB {

    public static void main(String[] args) {

        Connection connection = null;
        ResultSet resultSet = null;
        Statement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:D:\\ChequeRecognizer.sqlite");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT name FROM cheque");
            while (resultSet.next()) {
                System.out.println("EMPLOYEE NAME:"
                        + resultSet.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
