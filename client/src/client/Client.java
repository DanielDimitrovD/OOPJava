package client;

import server.ServerObjectInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    private static Scanner scanner;
    public static void main(String[] args) {
        try {
            scanner = new Scanner(System.in);
            Registry registry  = LocateRegistry.getRegistry(12345);
            ServerObjectInterface server = (ServerObjectInterface)registry.lookup("ServerObjectInterfaceImplementation");

            System.out.println("Enter code to decrypt");
            String input = scanner.next();
            String result = server.decryptCardNumber(input);

            System.out.printf("The card number %s has decryption number %s%n",input,result);

    } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

    }
}
