package serverRMIDefinitions;

import filesOperations.BankCardByCardNumberStream;
import filesOperations.BankCardByEncryptionStream;
import substitutionCypher.EncryptCard;
import userPackage.Privileges;
import userPackage.User;
import userPackage.Users;
import Utilities.Utils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerObjectInterfaceImplementation extends UnicastRemoteObject implements ServerObjectInterface {

    private Registry registry; // reference to the object
    private Map<String, User> userCredentials; // store username and password
    private XStream xStream; // used for reading and writing to XML configuration file
    private String pathToCredentialsFile; // path to XML file with information about users
    private TreeMap<String,String> encryptions;  // pair ( encryption number) -> ( bank account number)
    private BankCardByCardNumberStream streamByCard; // used for I/O operations with data sorted by card number
    private BankCardByEncryptionStream streamByEncryption; // used for I/O operations with data sorted by encryption number
    private StringBuilder sb; // string builder for manipulation of strings
    private int numberOfEncryptions; // counts number of encryptions of single Bank number

    private static final int INITIAL_OFFSET = 5; // initial offset for the Cipher

    public ServerObjectInterfaceImplementation() throws RemoteException, IOException {
        pathToCredentialsFile = "D:\\encryptionProject\\server\\src\\serverData\\data.xml"; // set XML config file location
        userCredentials = new HashMap<>(); // initialize hashMap
        encryptions = new TreeMap<>(); // initialize treeMap
        streamByCard = new BankCardByCardNumberStream(); // initialize I/O for data sorted by card number
        streamByEncryption = new BankCardByEncryptionStream(); // initialize I/O for data sorted by encryption number
        xStream = new XStream(new DomDriver()); // initialize xStream
        Utils.initXStream(xStream); // set up xStream
        initializeMap(); // fill map with users

    }

    public void setRegistry(Registry r){
        registry = r;
    }

    // initialize userCredentials
    private final void initializeMap() throws IOException, RemoteException {
        userCredentials = new HashMap<>();

        Path path = Paths.get(pathToCredentialsFile); // get file Path
        if (Files.notExists(path)) { // if file doesn't exist create it
            Files.createFile(path);
            return;
        } else if (Files.size(Paths.get(pathToCredentialsFile)) == 0) {
            return; // if file is created but doesn't have anything written to it exit method
        } else {  // file exists and has data written to it
            try (FileReader reader = new FileReader(pathToCredentialsFile)) { // read from file

                Users data = (Users) xStream.fromXML(reader);  // serialize XML to Users class

                for (User i : data.getUsers()) {    // fill HashMap with User values
                    userCredentials.put(i.getUsername(), new User(i.getUsername(), i.getPassword(), i.getPrivileges()));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // function that counts number of errors for Bank card number
    private int countEncryptions(String cardNumber){
        int counter = 0;

        Set<Map.Entry<String,String>> entry = encryptions.entrySet();

        for ( Map.Entry<String,String> i : entry){
            if(i.getValue().equals(cardNumber)){
                counter++;
            }
        }
        return counter;
    }
    // write cards and their encryption's in file, sorted by card number
    public void writeSortedByCardNumber(){
        TreeSet<String> bankCards = new TreeSet<>();
        Set<Map.Entry<String,String>> cards = encryptions.entrySet();

        for( Map.Entry<String,String> i: cards){
            bankCards.add(i.getValue());
        }
        streamByCard.writeToFile(bankCards,cards);
    }
    // write cards and their encryption's in file, sorted by encryption number
    public void writeSortedByEncryption(){
        Set<Map.Entry<String,String>> cards = encryptions.entrySet();
        streamByEncryption.writeToFile(cards);
    }


    public String findCardNumber( String bankCard){
        String result = "";

        Set<Map.Entry<String,String>> entry = encryptions.entrySet();

        for( Map.Entry<String,String> card : entry){
            if(card.getKey().equals(bankCard)){
                result = card.getValue();
                break;
            }
        }
        return result;
    }

    @Override
    public  String encryptCardNumber(String username, String cardNumber) throws RemoteException {
        String result = "";
        Privileges privilege = userCredentials.get(username).getPrivileges(); // get username privilege
        if (privilege.equals(Privileges.GUEST)) // check if user has functionality for encryption method
        {
            result = "Guests don't have encryption functionality!";
            return result;
        }
        if (!Utils.verifyCardNumber(cardNumber) | !Utils.verifyLuhn(cardNumber)) {// check if card number is valid
            result = "Invalid bank card! Enter information again.";
            return result;
        }

        int count = countEncryptions(cardNumber); // check number of occurrences of current card number

        EncryptCard encryptCard; // declare encryption class

        if(count > 12) { // limitations for occurrences of card numbers in data
            result = "No more than 11 times can you encrypt the same card";
            return result;
        }
        else { // count <= 11
            if( count == 0)
                encryptCard = new EncryptCard(INITIAL_OFFSET);
            else if( count >= 1 && count <= 10)
                encryptCard = new EncryptCard(INITIAL_OFFSET + count);
            else { // count == 11
                encryptCard = new EncryptCard(INITIAL_OFFSET - 1);  // (INITIAL_OFFSET + 11 == 16 ) % 16 equals = 0 which gives no offset
            } // end else count == 11
            result = encryptCard.encrypt(cardNumber); // encrypt card number
            encryptions.put(result,cardNumber); // add (encrypted card number, bank card number) pair to map
            writeSortedByCardNumber(); // write to file sorted by card number
            writeSortedByEncryption(); // write to file sorted by encryption number
            return result;
        }

            // first occurrence of card number
            //   encryptCard = new EncryptCard(INITIAL_OFFSET); // initialize encryption class
      /*      result = encryptCard.encrypt(cardNumber); // get result
            encryptions.put(result,cardNumber); // put result in encryption's map
            writeSortedByCardNumber(); // write to file sorted by card number
            writeSortedByEncryption(); // write to file sorted by encryption number
            return  result;
        } else if( count >=1 && count <= 10){ // occurence in range [1-10]
            encryptCard = new EncryptCard(INITIAL_OFFSET + count);
            result = encryptCard.encrypt(cardNumber);
            encryptions.put(result,cardNumber);
            writeSortedByCardNumber(); // write to file sorted by card number
            writeSortedByEncryption();
            return  result;
        } else if( count  == 11 ){ // final occurence ( INTIAL_OFFSET + count = 16 ) % 16 = 0 there is no offset in this case
            // that's why we substract 1 from the INITIAL_OFFSET
            encryptCard = new EncryptCard(INITIAL_OFFSET -1);
            result  = encryptCard.encrypt(cardNumber);
            encryptions.put(result,cardNumber);
            writeSortedByCardNumber();
            writeSortedByEncryption();
            return  result;
        } else if ( count > 11) {
            result = "No more than 11 times can you encrypt the same card";
            return  result;
        }*/
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
        if (userCredentials.containsKey(username)) {  // check if username is registered in Credentials
            if (userCredentials.get(username).getPassword().equals(password)) // check if username has specified password
                return true; // client is in database
        }
        return false; // client isn't registered in database
    }

    // add account to database
    @Override
    public final void addUser(String username, String password, Privileges privileges) throws RemoteException {
        XStream stream = new XStream(new DomDriver());
        Utils.initXStream(stream);

        try {
            Users users = Utils.readFromXML(new File(pathToCredentialsFile)); // extract Users from XML file
            users.addUser(new User(username, password, privileges));  // put new User in Users collection

            userCredentials.put(username, new User(username, password, privileges)); // put new User in credentials table
            Utils.writeInXML(new File(pathToCredentialsFile), users); // write back new information to XML file
        } catch (IOException e) {
            e.printStackTrace();
        } // catch end
    } // method addUser end
} // class end