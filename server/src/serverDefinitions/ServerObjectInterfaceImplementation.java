package serverDefinitions;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

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

public class ServerObjectInterfaceImplementation extends UnicastRemoteObject implements ServerObjectInterface  {

    private Map<String,User> userCredentials; // store username and password
    private StringBuilder sb; // used to build result of encrypted card number
    private int offset; // define offset of algorithm
    private String pathToCredentialsFile; // path to XML file with information about users
    public ServerObjectInterfaceImplementation() throws RemoteException,IOException {
        sb = new StringBuilder();
        offset = 5;
        pathToCredentialsFile = "D:\\encryptionProject\\server\\src\\serverData\\data.xml";
        userCredentials = new HashMap<>();
        initializeMap();
    }

    // initialize userCredentials
    private void initializeMap() throws IOException,RemoteException {
        userCredentials = new HashMap<>();

        Path path = Paths.get(pathToCredentialsFile); // get file Path
        if (Files.notExists(path)) { // if file doesn't exist create it
            Files.createFile(path);
            return;
        }
        else if ( Files.size(Paths.get(pathToCredentialsFile)) == 0){
            return; // if file is created but doesn't have anything written to it do nothing
        }
        else {  // file exists and has data written to it
              try (FileReader reader = new FileReader(pathToCredentialsFile)) { // read from file
                  XStream stream = new XStream(new DomDriver());  // initialize Xstream
                  stream.alias("Users", Users.class);  // create alias for Users class
                  stream.alias("User", User.class); // create alias for User class
                  stream.addImplicitCollection(Users.class, "users");

                  Users data = (Users) stream.fromXML(reader);  // serialize XML to Users class

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
    public String encryptCardNumber(String username,String cardNumber) throws RemoteException {
        sb.setLength(0); // clear stringBuilder

        Privileges privilege = userCredentials.get(username).getPrivileges(); // get username privilege
        if( privilege.equals(Privileges.ENCRYPT) || privilege.equals(Privileges.BOTH)) // the user has functionality for encryption method
        {
            for (int i = 0; i < cardNumber.length(); i++) { // encrypting card number
                sb.append((int) (cardNumber.charAt(i) - '0' + offset) % 10);
            }
            return sb.toString();
        }
        else { // user has no rights for the encryption method
            return null;
        }
    }
    // decryption of card Number
    @Override
    public String decryptCardNumber(String username,String cardNumber) throws RemoteException {
        sb.setLength(0); // clear stringBuilder
        offset %= 10; // if key is more than 10 keep it in range [0-9]

        Privileges privilege = userCredentials.get(username).getPrivileges(); // get user privileges
        if (privilege.equals(Privileges.DECRYPT) || privilege.equals(Privileges.BOTH)) // user has privileges for decryption method
        {
            for (int i = 0; i < cardNumber.length(); i++) { // decrypting card number
                sb.append((int) (cardNumber.charAt(i) - '0' - offset) >= 0 ? (int) (cardNumber.charAt(i) - '0' - offset) : (int) (cardNumber.charAt(i) - '0' - offset) + 10);
            }
            return sb.toString();
        }
        else { // user has no rights for the decryption method
            return null;
        }
    }
    // validate if a client can continue to the main client functionality
    @Override
    public boolean validateUser(String username, String password) throws RemoteException {
       if( userCredentials.containsKey(username)){  // check if username is registered in Credentials
           if(userCredentials.get(username).getPassword().equals(password)) // check if username has specified password
                return true; // client is in database
           }
           return false; // client isn't registered in database
    }
}