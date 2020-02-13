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


    @Override
    public final String encryptCardNumber(String username, String cardNumber) throws RemoteException {
        sb.setLength(0); // clear stringBuilder

        Privileges privilege = userCredentials.get(username).getPrivileges(); // get username privilege
        if (privilege.equals(Privileges.GUEST)) // check if user has functionality for encryption method
        {
            return sb.append("Guests don't have encryption functionality!").toString(); // return encrypted card number
        }
        if (!Utils.verifyCardNumber(cardNumber) | !Utils.verifyLuhn(cardNumber)) {// check if card number is valid
            return  sb.append("Invalid bank card! Enter information again.").toString();
        }

        int count = countEncryptions(cardNumber); // check number of occurrences of current card number

        if( count == 0){ // first occurrence of card number
            EncryptCard encryptCard = new EncryptCard(INITIAL_OFFSET);
            String result = encryptCard.encrypt(cardNumber);
            encryptions.put(result,cardNumber);
            writeSortedByCardNumber();
            writeSortedByEncryption();
        } else if( count >=1 && count <= 10){

        }

    }

    // decryption of card Number
    @Override
    public final String decryptCardNumber(String username, String cardNumber) throws RemoteException {

        Privileges privilege = userCredentials.get(username).getPrivileges(); // get user privileges
        if (!privilege.equals(Privileges.GUEST)) // user has privileges for decryption method
        {
            return cipher.decrypt(cardNumber); // return decrypted card number
        } else { // user has no rights for the decryption method
            return null;
        }
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