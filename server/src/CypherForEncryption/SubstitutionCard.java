package CypherForEncryption;

// class used for encryption of bank number
public class SubstitutionCard {
    private int offset; // offset of substitution Cypher
    private final StringBuilder sb; // string builder

    public SubstitutionCard(int offset) {
        this.offset = offset;
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
        offset++;
        return sb.toString();
    }
}
