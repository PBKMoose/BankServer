import java.net.*;
import java.io.*;

public class BankServerThread extends Thread {
    private Socket clientSocket = null;
    private BankSharedState sharedState;

    public BankServerThread(Socket socket, BankSharedState sharedState) {
        this.clientSocket = socket;
        this.sharedState = sharedState;
    }

    public void run() {
        try {
            System.out.println("Thread for client started.");
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine, outputLine;
            while ((inputLine = in.readLine()) != null) {
                try {
                    sharedState.acquireLock();
                    outputLine = sharedState.processRequest(inputLine);
                    out.println(outputLine);
                    sharedState.releaseLock();
                } catch (InterruptedException e) {
                    System.err.println("Thread interrupted: " + e.getMessage());
                }
            }

            out.close();
            in.close();
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}