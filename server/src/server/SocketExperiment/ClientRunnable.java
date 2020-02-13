package server.SocketExperiment;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import serverDefinitions.User;
import serverDefinitions.Users;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ClientRunnable implements Runnable {

    private final Socket clientSocket;
 //   private HashMap<String, User> data;
    private String pathToUserConfigFile;

    private ObjectOutputStream out;
    private ObjectInputStream in;


    public ClientRunnable(Socket socket) throws IOException {
        clientSocket = socket;  // set socket
        pathToUserConfigFile = "D:\\encryptionProject\\server\\src\\serverData\\data.xml"; // set configuration file path
   //     data = new HashMap<>();

  /*      try(FileReader reader = new FileReader(pathToUserConfigFile)){
            XStream xStream = new XStream(new DomDriver()); // initialize xStream
            xStream.alias("Users", Users.class); // create tag for Users class
            xStream.alias("User",User.class); // create tag for User class
            xStream.addImplicitCollection(Users.class,"users"); // create Users root tagx

            Users temp = (Users)xStream.fromXML(reader); // convert from XML to Users class

            for( User i : temp.getUsers()){ // fill data
                data.put(i.getUsername(),new User(i.getUsername(),i.getPassword(),i.getPrivileges()));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    @Override
    public void run() {
        try {
          //BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())) ;
          //PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);

            System.out.println("User connected successfully.");

            out = new ObjectOutputStream(clientSocket.getOutputStream()); // initialize output stream
            out.flush();

            in = new ObjectInputStream(clientSocket.getInputStream()); // initialize input stream

            String read = (String)in.readObject();
            System.out.println("Client with ip:"+clientSocket.getInetAddress()+" said:" + read);
            out.writeObject("Asta la vista");
         /*   LoginUser userInfo = (LoginUser)in.readObject();
            if( data.containsKey(userInfo.getUsername())){
                String password = data.get(userInfo.getUsername()).getPassword();
                if(password.equals(userInfo.getPassword()))
                    out.writeObject("CONFIRMED");
            }
            else{
                out.writeObject("UNKNOWN");
            }

          */

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
