package server.SocketExperiment;

public class BankServer {
    public static void main(String[] args) {
        System.out.println("Hello server!");
        SocketServer s = new SocketServer();
        s.runServer();
    }
}
