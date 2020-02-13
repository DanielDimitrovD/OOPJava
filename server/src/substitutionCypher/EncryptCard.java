package substitutionCypher;

public class EncryptCard {
    private int offset; // offset of substitution Cypher
    private final StringBuilder sb; // string builder

    public EncryptCard(int offset) {
        this.offset = offset;
        sb = new StringBuilder();
    }

    public void setOffset(int offset) {
        this.offset = offset % 16;
    }

    // encryption method
    public final String encrypt(String cardNumber) {
        sb.setLength(0); // clear sb
        for (int i = 0; i < cardNumber.length(); i++) { // encrypting card number
            sb.append((int) (cardNumber.charAt(i) - '0' + offset) % 10);
        }
        return sb.toString();
    }

    // decryption method
    public final String decrypt(String cardNumber) {
        sb.setLength(0); // clear sb
        for (int i = 0; i < cardNumber.length(); i++) { // decrypting card number
            sb.append((int) (cardNumber.charAt(i) - '0' - offset%10) >= 0 ? (int) (cardNumber.charAt(i) - '0' - offset%10) : (int) (cardNumber.charAt(i) - '0' - offset%10) + 10);
        }
        return sb.toString();
    }
}
