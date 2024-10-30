import java.net.*;
import java.io.*;

public class runDatabase implements runDataInterface{
    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            System.out.println("Server started...");
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client connected: " + client.getInetAddress());
                handleClient(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                out.println("Echo: " + message);  // Simple echo for now
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
