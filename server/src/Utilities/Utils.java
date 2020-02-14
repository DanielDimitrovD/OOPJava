package Utilities;

import javafx.scene.control.Alert;

public class Utils {

    // check username and password in Server Adding Tab for pattern
    public static boolean checkUserAndPassword(String username) { return username.matches("^[a-zA-Z]+\\d*$"); }

    // verify card number format
    public  static boolean verifyCardNumber(String cardNumber) {   // verify encryption input
        return cardNumber.matches("^[3-6]\\d{15}$");
    }

    // verify card number validity by Luhn Algorithm
    public  static boolean verifyLuhn(String cardNumber) {

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

    // method to create an alert with type,title,headerText,contextText for reuse
    public static void showMessage(Alert.AlertType type, String title, String headerText, String contextText)  {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.showAndWait();
    }
}
