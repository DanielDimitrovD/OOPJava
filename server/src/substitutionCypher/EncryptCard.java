package substitutionCypher;

public class EncryptCard {
    private int offset; // offset of substitution Cypher
    private final StringBuilder sb; // string builder

    public EncryptCard(int offset) {
        this.offset = offset;
        offset %= 16;
        sb = new StringBuilder();
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    // encryption method
    public final String encrypt(String cardNumber) {
        sb.setLength(0); // clear sb
        for (int i = 0; i < cardNumber.length(); i++) { // encrypting card number
            sb.append((int) (cardNumber.charAt(i) - '0' + offset) % 10);
        }
        return sb.toString();
    }
}
