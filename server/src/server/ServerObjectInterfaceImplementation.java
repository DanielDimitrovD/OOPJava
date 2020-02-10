package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.regex.Matcher;

public class ServerObjectInterfaceImplementation extends UnicastRemoteObject implements ServerObjectInterface  {

    private StringBuilder sb; // used to build result of encrypted card number
    private int offset; // define offset of algorithm
    public ServerObjectInterfaceImplementation() throws RemoteException {
        sb = new StringBuilder();
        offset = 5;
    }

    private static boolean verifyCardNumber( String cardNumber) throws RemoteException{
        return cardNumber.matches("^\\d+$");
    }

    @Override
    public String encryptCardNumber(String cardNumber) throws RemoteException {
       if(cardNumber == null)
           return "No text entered";
       else if(verifyCardNumber(cardNumber) == false)
           return "Not a valid card number!";
       else {
            char[] temp = cardNumber.toCharArray();
            int[] convert = new int[temp.length];
            // convert char array to int array for processing
            for( int i =0; i< temp.length; i++){
                int currentNumber = (int)(temp[i] - '0');
                convert[i] = currentNumber;
            }
            // cypher card number
           for( int i=0; i< convert.length; i++){
               convert[i] = (convert[i] + offset) % 10; // using % to stay in range 0-9
           }
           for( int i=0 ; i< temp.length; i++) {
               temp[i] = (char)( convert[i]+'0');
           }
           for( int i=0; i<temp.length;i++){
               sb.append(temp[i]);
           }
           return sb.toString();
        }
    }
}
