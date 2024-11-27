import java.net.*;
import java.io.*;

public class BankServer {
    public static void main(String[] args) throws IOException {
        ServerSocket bankServerSocket = null;
        int bankServerPort = 4545;
        boolean listening = true;

        // Initialize shared state with initial account balances
        BankSharedState sharedState = new BankSharedState();

        try {
            bankServerSocket = new ServerSocket(bankServerPort);
        } catch (IOException e) {
            System.err.println("Could not start BankServer on port: " + bankServerPort);
            System.exit(-1);
        }
        System.out.println("BankServer started on port: " + bankServerPort);

        while (listening) {
            new BankServerThread(bankServerSocket.accept(), sharedState).start();
            System.out.println("New BankServer thread started.");
        }
        bankServerSocket.close();
    }
}