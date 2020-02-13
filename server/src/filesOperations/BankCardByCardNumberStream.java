package filesOperations;

// class for I/O operations for bank card numbers and their encryption sorted by card numbers

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

// file used for I/O operations for this class is "sortedByCardNumber.txt"

public class BankCardByCardNumberStream {
    private Path filePath; // path to file
    private Scanner in ; // used for input operations
    private Formatter out; // used to write to files
    private StringBuilder sb; // used for string operations

    public BankCardByCardNumberStream()
    {
        sb = new StringBuilder(); // initialize sb
        filePath = Paths.get("sortedByCardNumber.txt"); // get path to file
        if(!Files.exists(filePath)) { // check if file exists
            try{
            Files.createFile(filePath); // create file
        } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // function to write to file in sorted format by card number
    public void writeToFile(TreeSet<String> bankCards, Set<Map.Entry<String,String>> data){
        sb.setLength(0); // clear sb
        // try to open file
        try{
            out = new Formatter((OutputStream) filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // iterate through bankCards and their encryption data
        for( String bCard:bankCards){
            for(Map.Entry<String,String> card:data ){
                if(bCard.equals(card.getValue())){
                    sb.append(String.format("%s \t%s%n", bCard, card.getKey()));
                }
            }
        }
        // try to write to file
        try{
            out.format("%s%n",sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if( out != null) // close resources
            out.close();
    }

    // read data from file and return it's String representation
    public String readFromFile(){
        sb.setLength(0); // clear sb
        // try to open file
        try{
            in = new Scanner(filePath);
            // iterate with while construction
            while( in.hasNext()){ // append formatted data to sb
                sb.append(String.format("%-20s %-20s%n","Bank card number: "+ in.next(),"Encryption: "+ in.next()));
            } // end while
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // close resources
            if( in != null)
                in.close();
        }
        // return sb representation in String format
        return sb.toString();
    }
}
