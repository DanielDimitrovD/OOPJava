package userPackage;

import javafx.beans.property.SimpleStringProperty;

public class UserCardNumber {
    private final SimpleStringProperty username;
    private final SimpleStringProperty cardNumber;
    private final SimpleStringProperty encryptionNumber;

    public UserCardNumber(String username, String cardNumber, String encryptionNumber) {
        this.username = new SimpleStringProperty(username);
        this.cardNumber = new SimpleStringProperty(cardNumber);
        this.encryptionNumber = new SimpleStringProperty(encryptionNumber);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getCardNumber() {
        return cardNumber.get();
    }

    public SimpleStringProperty cardNumberProperty() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber.set(cardNumber);
    }

    public String getEncryptionNumber() {
        return encryptionNumber.get();
    }

    public SimpleStringProperty encryptionNumberProperty() {
        return encryptionNumber;
    }

    public void setEncryptionNumber(String encryptionNumber) {
        this.encryptionNumber.set(encryptionNumber);
    }
}
