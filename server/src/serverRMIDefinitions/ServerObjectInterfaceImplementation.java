package serverRMIDefinitions;

import substitutionCypher.Cipher;
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
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ServerObjectInterfaceImplementation extends UnicastRemoteObject implements ServerObjectInterface {

    private Map<String, User> userCredentials; // store username and password
    private Cipher cipher; // cypher for encryption and decryption
    private XStream xStream; // used for reading and writing to XML configuration file
    private String pathToCredentialsFile; // path to XML file with information about users

    public ServerObjectInterfaceImplementation() throws RemoteException, IOException {
        cipher = new Cipher(5); // create cipher with offset 5
        pathToCredentialsFile = "D:\\encryptionProject\\server\\src\\serverData\\data.xml"; // set XML config file location
        userCredentials = new HashMap<>();
        xStream = new XStream(new DomDriver());
        Utils.initXStream(xStream); // initialize xStream
        initializeMap(); // fill map with users
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

    @Override
    public final String encryptCardNumber(String username, String cardNumber) throws RemoteException {
     //   sb.setLength(0); // clear stringBuilder

        Privileges privilege = userCredentials.get(username).getPrivileges(); // get username privilege
        if (privilege.equals(Privileges.GUEST) || privilege.equals(Privileges.USER) || privilege.equals(Privileges.ADMIN)) // the user has functionality for encryption method
        {
            return cipher.encrypt(cardNumber); // return encrypted card number
        } else { // user has no rights for the encryption method
            return null;
        }
    }

    // decryption of card Number
    @Override
    public final String decryptCardNumber(String username, String cardNumber) throws RemoteException {

        Privileges privilege = userCredentials.get(username).getPrivileges(); // get user privileges
        if (privilege.equals(Privileges.USER) || privilege.equals(Privileges.ADMIN)) // user has privileges for decryption method
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