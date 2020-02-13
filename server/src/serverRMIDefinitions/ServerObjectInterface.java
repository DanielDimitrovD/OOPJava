package serverRMIDefinitions;
import userPackage.Privileges;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerObjectInterface extends Remote {
    String encryptCardNumber(String username,String cardNumber) throws RemoteException;
    String decryptCardNumber(String username,String cardNumber) throws RemoteException;
    boolean validateUser(String username,String password) throws RemoteException;
    void addUser(String username, String password, Privileges privileges) throws RemoteException;
}
