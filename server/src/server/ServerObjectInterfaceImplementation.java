package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerObjectInterfaceImplementation extends UnicastRemoteObject implements ServerObjectInterface  {

    private char[] domain;
    private StringBuilder sb; // used to build result of encrypted card number
    private int offset; // define offset of algorithm
    public ServerObjectInterfaceImplementation() throws RemoteException {
        domain = new char[10];
        for(int i=0; i<domain.length;i++){
            domain[i] = (char)( i + '0');
        }
        sb = new StringBuilder();
        offset = 5;
    }

    @Override
    public String encryptCardNumber(String cardNumber) throws RemoteException {
       if(cardNumber == null)
           return "No text entered";
       else if(!Utilities.verifyCardNumber(cardNumber) | !Utilities.verifyLuhnAlgorithm(cardNumber))
           return "Not a valid card number!";
       else {
            sb.setLength(0); // clear stringBuilder
           for( int i=0; i<cardNumber.length();i++){
               sb.append( (int)(cardNumber.charAt(i) -'0' + offset)%10);
           }
           return sb.toString();
        }
    }
    // decryption of card Number
    @Override
    public String decryptCardNumber(String cardNumber) throws RemoteException {
        sb.setLength(0); // clear stringBuilder
        for( int i=0; i<cardNumber.length(); i++){
            sb.append(  (int)(cardNumber.charAt(i) -'0' - offset) > 0 ?  (int)(cardNumber.charAt(i) -'0' - offset) :  (int)(cardNumber.charAt(i) -'0' - offset) +10);
        }
        return sb.toString();
    }
}