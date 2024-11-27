import java.io.*;
import java.net.*;

public class BankClient1 {
    public static void main(String[] args) throws IOException {
        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            clientSocket = new Socket("localhost", 4545);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception e) {
            System.err.println("Connection error: " + e.getMessage());
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer, fromUser;

        System.out.println("BankClient1 connected. Enter commands (e.g., Add_money A 100):");

        while ((fromUser = stdIn.readLine()) != null) {
            out.println(fromUser);
            fromServer = in.readLine();
            System.out.println("Server response: " + fromServer);
        }

        out.close();
        in.close();
        stdIn.close();
        clientSocket.close();
    }
}