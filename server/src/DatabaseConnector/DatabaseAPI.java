package DatabaseConnector;

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

        statement = connection.createStatement();
        preparedStatement = connection.prepareStatement(" SELECT username, password FROM login WHERE username = ? AND password = ? ");
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,password);
        preparedStatement.execute();

        if( preparedStatement == null)
            return false;
        else
            return true;
    }
}
