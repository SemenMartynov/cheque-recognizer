/*
 * http://www.zentus.com/sqlitejdbc/
 * sqlitejdbc-v056.jar
 */
package ru.spbau.cheque.server.ofx;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ru.spbau.cheque.server.recognition.BlueObject;
import ru.spbau.cheque.server.recognition.Cheque;

/**
 *
 * @author sam
 */
public class SqliteDB {

    public SqliteDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private String url = "jdbc:sqlite:D:\\tmp\\NetBeansProjects\\cheque-recognizer\\db\\ChequeRecognizer.sqlite";
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public List<ChequeString> getChequeStrings(int userId) {
        List<ChequeString> ChequeStrings = new ArrayList<ChequeString>();
        
        try {
            resultSet = statement.executeQuery("SELECT * FROM cheque WHERE user = " + userId + ";");
            while (resultSet.next()) {
                ChequeStrings.add( new ChequeString( resultSet.getInt("id"),
                                                     resultSet.getDate("timestamp"),
                                                     resultSet.getString("name"),
                                                     resultSet.getFloat("quantity"),
                                                     resultSet.getFloat("price"),
                                                     resultSet.getString("memo") ) );
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ChequeStrings;
    }

    public void putCheque(Cheque myCheque) {
        try {

            for (BlueObject element : myCheque.getTable()) {
                resultSet = statement.executeQuery("INSERT INTO 'cheque' ('user', 'timestamp', 'name', 'quantity', 'price', 'memo') VALUES"
                        + " (" + myCheque.getUsrId() + ", DATETIME('now'), '" + element.getName() + "', " + element.getCount() + ", " + element.getPrice() + ", " + myCheque.getCompany() + ");");
            }
            if (!resultSet.rowInserted()) {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}