package serverDefinitions;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerRMI {
    // main method
    public static void main(String[] args) {
        try {
        ServerObjectInterface serverObjectInterface = new ServerObjectInterfaceImplementation();
        Registry registry = LocateRegistry.createRegistry(12345);
        registry.bind("ServerObjectInterfaceImplementation",serverObjectInterface);
        System.out.println("Encryption object registed");
        System.out.println("Press Return to quit.");
        int key = System.in.read();
        System.exit(0);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
