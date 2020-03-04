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


    // initialize userCredentials
    private final void initializeMap() throws IOException, RemoteException {
        userCredentials = new HashMap<>();

        // Path path = Paths.get(pathToCredentialsFile); // get file Path
        File f = new File(pathToCredentialsFile);
        if (!f.exists()) { // if file doesn't exist create it
            f.createNewFile(); // create file
            isEmpty = true;
            return;
        } else {  // file exists and has data written to it

            if (f.length() != 0) {

                Users data = xmlSerialization.readXML();  // serialize XML to Users class

                for (User i : data.getUsers()) {    // fill HashMap with User values
                    userCredentials.put(i.getUsername(), new User(i.getUsername(), i.getPassword(), i.getPrivileges()));
                }
            }
        }
    }

    // write cards and their encryption's in file, sorted by card number
    public void writeSortedByCardNumber(){
        TreeSet<String> bankCards = new TreeSet<>();
        Set<Map.Entry<String,String>> cards = encryptions.entrySet();

        // iterate through entrySet and get credit cards
        for( Map.Entry<String,String> i: cards){
            bankCards.add(i.getValue());
        }
        // use stream to handle writing to file
        streamByCard.writeToFile(bankCards,cards);
    }
    // write cards and their encryption's in file, sorted by encryption number
    public void writeSortedByEncryption(){
        Set<Map.Entry<String,String>> cards = encryptions.entrySet();
        streamByEncryption.writeToFile(cards);
    }

    // method to find if a card number exists in the data
    public String findCardNumber( String bankCard){
        String result = "";

        Set<Map.Entry<String,String>> entry = encryptions.entrySet();
        // iterate and check if bank card is equal to the given argument
        for( Map.Entry<String,String> card : entry){
            if(card.getKey().equals(bankCard)){
                result = card.getValue();
                break;
            }
        }
        return result;
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
    public final String decryptCardNumber(String username, String cardNumber) throws RemoteException {
        String result = "";

        Privileges privilege = userCredentials.get(username).getPrivileges(); // get user privileges
        if (privilege.equals(Privileges.GUEST)) // user doesn't have privileges for decryption method
        {
            result = "No permissions for decryption functionality";
            return result; // return decrypted card number
        }

        result = findCardNumber(cardNumber);

        if( result.equals("")){
            result = "Wrong encryption number. Try again with another number";
        }

        return result;
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