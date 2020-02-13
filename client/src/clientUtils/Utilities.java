package clientUtils;

public class Utilities {

    // verify username format
    public static boolean verifyUsername(String username) {  return  username.matches("^\\a+\\d+$");}
    // verify card number format
    public static boolean verifyCardNumber( String cardNumber) { return cardNumber.matches("^\\d{16}$");}

    public static boolean verifyDecryptNumber( String cardNumber) { // verify decryption input
        return cardNumber.matches("^\\d+$");
    }
    // remove whitespaces from card number
    public static String  removeWhiteSpaces( String cardNumber) {  // removes whitespaces from card number
        return  cardNumber.replaceAll("\\s+","");
    }
}