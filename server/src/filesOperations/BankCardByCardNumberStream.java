package filesOperations;

// class for I/O operations for bank card numbers and their encryption sorted by card numbers

import java.io.File;
import java.io.IOException;
import java.util.*;

// file used for I/O operations for this class is "sortedByCardNumber.txt"

public class BankCardByCardNumberStream {
    private File f; // path to file
    private Scanner in ; // used for input operations
    private Formatter out; // used to write to files

    public BankCardByCardNumberStream()
    {
        f = new File("sortedByCardNumber.txt"); // get path to file
        if(!f.exists()) { // check if file exists
            try{
            f.createNewFile(); // create file
        } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // function to write to file in sorted format by card number
    public void writeToFile(TreeSet<String> bankCards, Set<Map.Entry<String,String>> data){

        f = new File("sortedByCardNumber.txt");

        // try to open file
        try{
            out = new Formatter(f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        sb.setLength(0);

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
            out.format("%s%n",sb);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if( out != null) // close resources
            out.close();
    }

    // read data from file and return it's String representation
    public String readFromFile(){

        f = new File("sortedByCardNumber.txt");
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);

        // try to open file
        try{
            in = new Scanner(f);
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

    public String readFromFileWithoutFormat(){
        f = new File("sortedByCardNumber.txt");
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);

        // try to open file
        try{
            in = new Scanner(f);
            // iterate with while construction
            while( in.hasNext()){ // append formatted data to sb
                sb.append(String.format("%s %s%n",in.next(),in.next()));
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
