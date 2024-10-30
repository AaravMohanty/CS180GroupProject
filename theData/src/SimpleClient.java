import java.io.*;
import java.net.*;
import java.util.Scanner;

// SimpleClient class establishes a connection to a server and allows the user to send and receive messages.
public class SimpleClient {
    public static void main(String[] args) {
        // Use try-with-resources to ensure that resources are automatically closed.
        try (Scanner scan = new Scanner(System.in); // Scanner for user input
             Socket socket = new Socket("localhost", 8888); // Connect to the server on localhost at port 8888
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Input stream to read messages from the server
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) { // Output stream for sending messages to the server, with auto-flush enabled

            String response; // Variable to hold user input

            // Loop to continuously send messages to the server until "exit" is entered
            while (true) {
                // Prompt the user for input
                System.out.println("What do you want to send to the server?");
                response = scan.nextLine(); // Read user input

                // Send the message to the server
                writer.println(response);
                System.out.printf("Sent to server:\n%s\n", response); // Display the sent message

                // Read the server's response
                String s1 = reader.readLine();
                System.out.printf("Received from server:\n%s\n", s1); // Display the received message

                // Check if the user or server wants to exit the loop
                if (response.equalsIgnoreCase("exit") || s1.equalsIgnoreCase("exit")) {
                    System.out.println("Closing connection..."); // Notify user of closing connection
                    break; // Exit the loop
                }
            }

            // Resources (Socket, BufferedReader, PrintWriter) will be closed automatically by try-with-resources

        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace for any I/O exceptions that occur
        }
    }
}
