package DatabaseConnector;

import userPackage.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DatabaseAPI {
    private Connection connection;
    private PreparedStatement preparedStatement;
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

        if( resultSet.next())
            return true;
        else
            return false;
    }

    public boolean addUserInDatabase(String username, String password, Privileges privileges) throws SQLException {

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

    public Privileges getUserPrivilegesFromDatabase(String username) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT type FROM login WHERE username = ?");
        preparedStatement.setString(1,username);

        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String resultPrivileges = resultSet.getString(1);

        if( resultPrivileges.equals(Privileges.GUEST))
            return Privileges.GUEST;
        else if( resultPrivileges.equals(Privileges.USER))
            return Privileges.USER;
        else
            return Privileges.ADMIN;
    }

    public boolean insertCardNumberEncryptionInDatabase(String username,String cardNumber,String encryptionNumber) throws SQLException {

        preparedStatement = connection.prepareStatement("SELECT userAccount,cardNumber FROM credentials WHERE userAccount = ? AND cardNumber = ?");
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,cardNumber);

        resultSet = preparedStatement.executeQuery();

        if( resultSet.next()){
            return false;
        }

        else {

            preparedStatement = connection.prepareStatement("INSERT INTO credentials (userAccount,cardNumber,encryptionNumber) VALUES (?,?,?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, cardNumber);
            preparedStatement.setString(3, encryptionNumber);

            preparedStatement.executeUpdate();
            return true;
        }
    }

    public String listUsersFromDatabase() throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT username,password,type FROM login");
        resultSet = preparedStatement.executeQuery();

        Users usersWrapperClass = new Users();

        while(resultSet.next()){
            User currentUser = new User(resultSet.getString("username"),resultSet.getString("password"),Privileges.valueOf(resultSet.getString("type")));
            usersWrapperClass.addUser(currentUser);
        }

        String resultUsernameDataString = usersWrapperClass.getUsers()
                .stream()
                .map(User::toString)
                .collect(Collectors.joining("\n"));

        return resultUsernameDataString;
    }

    public ArrayList<Person> getPeopleDataFromDatabase() throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT * FROM login");
        resultSet = preparedStatement.executeQuery();

        ArrayList<Person> personList = new ArrayList<>();

        while( resultSet.next()){
            personList.add(new Person(resultSet.getString("username"),resultSet.getString("password"),resultSet.getString("type")));
        }

        return personList;
    }

    public ArrayList<UserCardNumber> getUserCardNumberDataFromDatabase() throws SQLException{
        preparedStatement = connection.prepareStatement("SELECT * FROM credentials");
        resultSet = preparedStatement.executeQuery();

        ArrayList<UserCardNumber> cardNumberList = new ArrayList<>();

        while( resultSet.next()){
            cardNumberList.add(new UserCardNumber(resultSet.getString("userAccount"),resultSet.getString("cardNumber"),resultSet.getString("encryptionNumber")));
        }

        return cardNumberList;
    }
}

