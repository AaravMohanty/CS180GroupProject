import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SimpleClient {
    public static void main(String[] args) {
        try (Scanner scan = new Scanner(System.in);
             Socket socket = new Socket("localhost", 8888); // Connect to the server
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Input stream
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) { // Output stream with auto-flush

            String response;
            while (true) {
                System.out.println("What do you want to send to the server?");
                response = scan.nextLine();

                writer.println(response); // Send the message
                System.out.printf("Sent to server:\n%s\n", response);

                String s1 = reader.readLine(); // Read the server's response
                System.out.printf("Received from server:\n%s\n", s1);

                // Check if the user or server wants to exit the loop
                if (response.equalsIgnoreCase("exit") || s1.equalsIgnoreCase("exit")) {
                    System.out.println("Closing connection...");
                    break; // Exit the loop
                }
            }

            // Socket and other resources will be closed automatically by try-with-resources

        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }
}
