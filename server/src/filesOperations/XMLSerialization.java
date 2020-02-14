package filesOperations;



import userPackage.Users;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;

import java.io.*;
 // class used to serialize data in XML format
public class XMLSerialization {

     // file path
     private File destination;

     public XMLSerialization(String pathToCredentialsFile){
          setDestination("data.xml");
     }

     // set file path
     public void setDestination(String path) {
          this.destination = new File(path);
     }

     // write records to XML file
     public void writeXML(Users users) throws FileNotFoundException {
          XMLEncoder encoder = null;
          try {
               encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(destination)));
               encoder.writeObject(users); // write Users class object
          } catch (FileNotFoundException e) {
               System.out.println("Error finding file destination.");
          } finally {
               if( encoder !=null)
                    encoder.close();
          }
     }
     // read XML from file
     public Users readXML() throws FileNotFoundException {
          XMLDecoder decoder = null;
          Users users = new Users();
          try{
               if(destination.length() != 0) { // file not empty{
                    decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(destination)));
                    users = (Users) decoder.readObject(); // get Users class with cast
               }
          } catch (FileNotFoundException e) {
               e.printStackTrace();
               System.out.println("Error finding file destination.");
          } finally {
               if(decoder != null)
                    decoder.close();
          }
          return users;
     }
}
