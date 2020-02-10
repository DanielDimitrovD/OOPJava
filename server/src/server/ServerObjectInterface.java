package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerObjectInterface extends Remote {
    String encryptCardNumber(String cardNumber) throws RemoteException;
}
