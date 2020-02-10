package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerObjectInterfaceImplementation extends UnicastRemoteObject implements ServerObjectInterface  {

    private StringBuilder sb; // used to build result of encrypted card number
    private int offset; // define offset of algorithm
    public ServerObjectInterfaceImplementation() throws RemoteException {
        sb = new StringBuilder();
        offset = 5;
    }
    // verify card Number with regex
    private static boolean verifyCardNumber( String cardNumber) throws RemoteException{
        return cardNumber.matches("^[3-6]\\d{15}$");
    }
    // verify card Number with luhn algorithm
    private static boolean verifyLuhnAlgorithm( String cardNumber) throws RemoteException {
        int sum = 0;
        char[] temp = cardNumber.toCharArray();
        int[] numbers = new int[temp.length];
        // convert temp chars to integers in numbers array
        for( int i=0; i< temp.length; i++){
            numbers[i] = (int)(temp[i] - '0');
        }
        // first half of luhn algorithm
        for( int i = 0; i < numbers.length; i+=2){
            if( numbers[i]*2 < 10){
                sum += numbers[i]*2;
            }
            else{
                sum += (numbers[i]/10) + (numbers[i]%10);
            }
        }
        // second half of luhn algorithm
        for( int i=1; i< numbers.length; i+=2){
            sum += numbers[i];
        }
        // check if sum of calculations mod 10 gives 0
        return  sum%10 == 0 ? true : false;
    }

    @Override
    public String encryptCardNumber(String cardNumber) throws RemoteException {
       if(cardNumber == null)
           return "No text entered";
       else if(verifyCardNumber(cardNumber) == false | verifyLuhnAlgorithm(cardNumber) == false)
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