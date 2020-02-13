package server.SocketExperiment;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    private int portNumber = 44444;
    ServerSocket serverSocket = null;
    ExecutorService service;

    public void runServer(){
        try{
            serverSocket = new ServerSocket(portNumber);// create server Socket
            service = Executors.newCachedThreadPool();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        while(true){
            try{
                Socket clientSocket = serverSocket.accept();
                ClientRunnable runnable = new ClientRunnable(clientSocket);
                service.execute(runnable);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }
}
