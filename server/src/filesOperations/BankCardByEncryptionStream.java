package filesOperations;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Formatter;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

// class used to read and write from/to file with input sorted by Encrypted Bank Cards
public class BankCardByEncryptionStream {
    private File  f; // path to file
    private Scanner in; // used for input
    private Formatter out; // used for output

    // "sortedByEncryption.txt" is used for I/O for these functions

    public BankCardByEncryptionStream( ) {
        StringBuilder sb = new StringBuilder(); // initialize stringBuilder
        f = new File("sortedByEncryption.txt"); // get file path
        if (!f.exists()) { // check if file exists
            try {
            f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } // end default constructor

    // function to write records to file
    public void writeToFile(Set<Map.Entry<String,String>> data){
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        f = new File("sortedByEncryption.txt");
        // try to open file
        try{
            out = new Formatter(f);
        } catch (Exception e) { // error with file
            e.printStackTrace();
        }
        // collect all items from collection and append them to sb
        for ( Map.Entry<String,String > i : data){  // apend data to sb
            sb.append(String.format("%s %s%n",i.getKey(),i.getValue()));
        }
        // write to file using formatter
        try{
            out.format("%s%n",sb); // write sb data to file
        } catch (Exception e) {
            e.printStackTrace();
        }
        // close resource
        if( out != null)
            out.close();
    }

    // read from file and extract data for String format
    public String readFromFile(){
        StringBuilder sb = new StringBuilder();
        sb.setLength(0); // clear sb
        f = new File("sortedByEncryption.txt");
        // try to open file
        try{
            in = new Scanner(f); // get input stream
            while( in.hasNext()){ // iterate through file data
                sb.append(String.format("%-20s %-20s%n","Encryption: "+ in.next(),"Bank card number: "+ in.next())); // append data to sb
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally { // close resources
            if(in != null)
                in.close();
        }
        return sb.toString(); // return string representation of data
    }
}