package serverRMIDefinitions;

import DatabaseConnector.DatabaseAPI;
import filesOperations.BankCardByCardNumberStream;
import filesOperations.BankCardByEncryptionStream;
import filesOperations.XMLSerialization;
import CypherForEncryption.SubstitutionCard;
import userPackage.Privileges;
import userPackage.User;
import userPackage.Users;
import Utilities.Utils;


import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.*;

// implementation of the RMI service interface
public class ServerObjectInterfaceImplementation extends UnicastRemoteObject implements ServerObjectInterface {

    private static final int INITIAL_OFFSET = 5; // initial offset for the Cipher

    private Registry registry; // reference to the object
    private Map<String, User> userCredentials; // store username and password
    private XMLSerialization xmlSerialization; // used for reading and writing to XML configuration file
    private String pathToCredentialsFile; // path to XML file with information about users
    private TreeMap<String,String> encryptions;  // pair ( encryption number) -> ( bank account number)
    private BankCardByCardNumberStream streamByCard; // used for I/O operations with data sorted by card number
    private BankCardByEncryptionStream streamByEncryption; // used for I/O operations with data sorted by encryption number
    private StringBuilder sb; // string builder for manipulation of strings


    // UPDATE
    private DatabaseAPI databaseConnection;
    private SubstitutionCard substitutionCard;


    private boolean isEmpty;

    public ServerObjectInterfaceImplementation() throws RemoteException, IOException {
        streamByCard = new BankCardByCardNumberStream(); // initialize I/O for data sorted by card number
        streamByEncryption = new BankCardByEncryptionStream(); // initialize I/O for data sorted by encryption number
        xmlSerialization = new XMLSerialization( pathToCredentialsFile); // initialize xml I/O instance
        encryptions = new TreeMap<>(); // initialize treeMap
        // UPDATE

        databaseConnection = new DatabaseAPI();
        substitutionCard = new SubstitutionCard(INITIAL_OFFSET);
    }



    @Override
    public  String encryptCardNumber(String username, String cardNumber) throws RemoteException, SQLException {
        String encryptedCardNumber = substitutionCard.encrypt(cardNumber);
        if(databaseConnection.insertCardNumberEncryptionInDatabase(username,cardNumber,encryptedCardNumber))
           return encryptedCardNumber;
        else
            return "User already encrypted this credit card number";
    }
    // decryption of card Number
    @Override
    public final String decryptCardNumber(String username, String encryptionNumber) throws RemoteException, SQLException {
        String result = "";

        Privileges privilege = databaseConnection.getUserPrivilegesFromDatabase(username);
        if (privilege.equals(Privileges.GUEST)) // user doesn't have privileges for decryption method
        {
            result = "No permissions for decryption functionality";
            return result; // return decrypted card number
        }


        return databaseConnection.getCardNumberByEncryptionNumberFromDatabase(username,encryptionNumber);
    }

    // validate if a client can continue to the main client functionality
    @Override
    public final boolean validateUser(String username, String password) throws RemoteException {
        try {
            if(databaseConnection.validateUserLogin(username, password))
                return true;
            else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // add account to database
    @Override
    public final boolean addUser(String username, String password, Privileges privileges) throws RemoteException {
     try {
         if(databaseConnection.addUserInDatabase(username, password, privileges))
             return true;
         else
             return false;
     } catch (SQLException e) {
         e.printStackTrace();
     }
        return false;
    } // method addUser end
} // class end