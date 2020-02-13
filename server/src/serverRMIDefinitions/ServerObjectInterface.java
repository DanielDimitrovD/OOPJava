package serverRMIDefinitions;
import userPackage.Privileges;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerObjectInterface extends Remote {
    // encryption of card number
    String encryptCardNumber(String username,String cardNumber) throws RemoteException;
    // decryption of card number
    String decryptCardNumber(String username,String cardNumber) throws RemoteException;
    // validation of user
    boolean validateUser(String username,String password) throws RemoteException;
    // adding user to database
    void addUser(String username, String password, Privileges privileges) throws RemoteException;
}
