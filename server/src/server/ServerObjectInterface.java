package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerObjectInterface extends Remote {
    String encryptCardNumber(String cardNumber) throws RemoteException;
    String decryptCardNumber(String cardNumber) throws RemoteException;
    boolean validateUser(String username,String password) throws RemoteException;
}
