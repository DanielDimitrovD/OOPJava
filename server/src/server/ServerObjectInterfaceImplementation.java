package server;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import serverG.User;
import serverG.Users;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ServerObjectInterfaceImplementation extends UnicastRemoteObject implements ServerObjectInterface  {

    private Map<String,String> userCredetentials;
    private StringBuilder sb; // used to build result of encrypted card number
    private int offset; // define offset of algorithm
    public ServerObjectInterfaceImplementation() throws RemoteException {
        sb = new StringBuilder();
        offset = 5;
        initializeMap();
    }

    // initialize userCredentials
    private void initializeMap() throws RemoteException{
        userCredetentials = new HashMap<>();

        try( FileReader reader = new FileReader("data.xml")){ // read from file
            XStream stream = new XStream(new DomDriver());  // initialize Xstream
            stream.alias("Users",Users.class);  // create alias for Users class
            stream.alias("user",User.class); // create alias for User class
            stream.addImplicitCollection(Users.class,"users");

            Users data = (Users)stream.fromXML(reader);  // serialize XML to Users class

            userCredetentials = new HashMap<>();
            for( User i : data.getUsers()){    // fill HashMap with user values
                userCredetentials.put(i.getUsername(),i.getPassword());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String encryptCardNumber(String cardNumber) throws RemoteException {
        sb.setLength(0); // clear stringBuilder
        for( int i=0; i<cardNumber.length();i++){
            sb.append( (int)(cardNumber.charAt(i) -'0' + offset) % 10 );
        }
        return sb.toString();
    }
    // decryption of card Number
    @Override
    public String decryptCardNumber(String cardNumber) throws RemoteException {
        sb.setLength(0); // clear stringBuilder
        for( int i=0; i<cardNumber.length(); i++){
            sb.append((int)(cardNumber.charAt(i) -'0' - offset) >= 0 ?  (int)(cardNumber.charAt(i) -'0' - offset) : (int)(cardNumber.charAt(i) -'0' - offset) +10);
        }
        return sb.toString();
    }

    // validate if a client can continue to the main client page
    @Override
    public boolean validateUser(String username, String password) throws RemoteException {
       if( userCredetentials.containsKey(username)){
           if( userCredetentials.containsValue(password)){
               return  true;
           }
       }
           return false;
    }

}