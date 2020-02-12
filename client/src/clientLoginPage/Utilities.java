package clientLoginPage;

public class Utilities {

    // verify username format
    static boolean verifyUsername(String username) {  return  username.matches("^\\a+\\d+$");}
    // verify card number format
    static boolean verifyCardNumber(String cardNumber) {   // verify encryption input
        return cardNumber.matches("^[3-6]\\d{15}$");
    }
    // verify card number format
    static boolean verifyDecryptNumber( String cardNumber) { // verify decryption input
        return cardNumber.matches("^\\d{16}$");
    }
    // remove whitespaces from card number
    static String  removeWhiteSpaces( String cardNumber) {  // removes whitespaces from card number
        return  cardNumber.replaceAll("\\s+","");
    }
    // verify card number validity by Luhn Algorithm
    static boolean verifyLuhn(String cardNumber) {

        int sum = 0;  // accumulator
        int length = cardNumber.length(); // length of cardNumber
        boolean isSecondNumber = false; // boolean to represent every second number
        for( int i = length -1; i>=0; i--){

            int digit = cardNumber.charAt(i) - '0';  // converting char to int

            if( isSecondNumber == true)
                digit *= 2;   // multiply every second digit

            sum +=  digit%10 + digit/10;  // handle numbers with two digits
            isSecondNumber = !isSecondNumber;  //  change boolean on every iteration
        }
        return (sum % 10 ==0); // check if the sum fulfills the Luhn condition
    }
}