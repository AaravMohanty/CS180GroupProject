import java.io.*;
import java.net.*;

public class SimpleClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8888); // Connect to the server on localhost at port 8888
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // Output stream to send messages
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) { // Input stream to read responses

            // Send a test message to the server
            out.println("Hello, Server!");

            // Read and print the server's response
            String response = in.readLine();
            System.out.println("Server response: " + response);
        } catch (IOException e) {
            e.printStackTrace(); // Print any exceptions that occur
        }
    }
}
