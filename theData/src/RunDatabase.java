import java.net.*; // Import networking classes
import java.io.*;  // Import I/O classes
import java.util.ArrayList;


// The RunDatabase class implements the runDataInterface
// to manage user and message storage while handling client connections.
public class RunDatabase implements runDataInterface {
    boolean running = true;

    // Starts the server to listen for incoming client connections
    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket( 8888)) { // Create a server socket on port 8888
            System.out.println("Server started..."); // Notify that the server is running

            // Continuously accept client connections
            while (running) {
                Socket client = serverSocket.accept(); // Wait for a client to connect
                System.out.println("Client connected: " + client.getInetAddress()); // Log client address
                handleClient(client); // Handle the connected client
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print any exceptions that occur
        }
    }

    // Handles communication with a connected client
    private void handleClient(Socket client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream())); // Input stream to read messages from the client
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) { // Output stream to send messages to the client

            String message; // Variable to store incoming messages

            // Continuously read messages from the client
            while ((message = in.readLine()) != null) {
                if (message.equals("exit")) {
                    running = false;
                }
                System.out.println("Received: " + message); // Log the received message
                out.println("Echo: " + message);  // Send an echo response back to the client
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print any exceptions that occur
        }
    }

    // Saves a user to the database (implementation placeholder)
    public void saveUser(User user) {
        // Logic to save the user to a database would go here
    }

    // Loads a user from the database by username (implementation placeholder)
    public User loadUser(String username) {
        // Logic to load the user from a database would go here
        return null; // Placeholder return value
    }

    // Saves a message to the database (implementation placeholder)
    public void saveMessage(Message message) {
        // Logic to save the message to a database would go here
    }

    // Loads all messages associated with a specific username (implementation placeholder)
    public ArrayList<Message> loadMessages(String username) {
        // Logic to load messages from a database would go here
        return new ArrayList<>(); // Placeholder return value
    }

    public static void main(String[] args) {
        RunDatabase database = new RunDatabase(); // Create an instance of RunDatabase
        database.startServer(); // Start the server

    }

}
