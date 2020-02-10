package server;

import java.rmi.RemoteException;

public class Utilities {
    static boolean verifyCardNumber(String cardNumber) {
        return cardNumber.matches("^[3-6]\\d{15}$");
    }

    static boolean verifyLuhnAlgorithm(String cardNumber) {
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
}