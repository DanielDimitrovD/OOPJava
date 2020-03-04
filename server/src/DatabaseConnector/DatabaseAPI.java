package DatabaseConnector;

import userPackage.Privileges;

import java.sql.*;

public class DatabaseAPI {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;

    public DatabaseAPI() {
        try{
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankdatabase","root","");
    } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // method to validate if user is registered in database
    public boolean validateUserLogin(String username,String password) throws SQLException {

      //  statement = connection.createStatement();
        preparedStatement = connection.prepareStatement(" SELECT username, password FROM login WHERE username = ? AND password = ? ");
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,password);

        resultSet = preparedStatement.executeQuery();
        connection.close();

        if( resultSet.next())
            return true;
        else
            return false;
    }

    public boolean addUserInDatabase(String username, String password, Privileges privileges) throws SQLException {

       // statement = connection.createStatement();
        preparedStatement = connection.prepareStatement("SELECT username FROM login WHERE username = ?");
        preparedStatement.setString(1,username);
        resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){ // if there is an account with this username already in the database show alert message
            return false;
        }
        else { // prepare statement to update database information
            preparedStatement.close();
            preparedStatement = connection.prepareStatement("INSERT INTO login (username,password,type) VALUES (?,?,?)");
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            preparedStatement.setString(3, String.valueOf(privileges));

            preparedStatement.executeUpdate();
            return true;
        }
    }
}
