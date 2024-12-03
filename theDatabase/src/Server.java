import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server implements Runnable {
    public static final Object LOCK = new Object();
    private static Database database;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        database = new Database(); // Initialize the database
        try {
            serverSocket = new ServerSocket(1234); // Start the server
            System.out.println("Server is running on port 1234...");

            while (true) {
                Thread t = new Thread(new Server());
                t.start();
            }
        } catch (IOException | OutOfMemoryError e) {
            ;
        }
    }

    public void run() {
        try (Socket socket = serverSocket.accept();
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("New connection from " + socket.getRemoteSocketAddress());
            User currentUser = null;

            while (true) {
                String command = reader.readLine();
                if (command == null) break; // Terminate if no command is received
                switch (command) {
                    case "1": // Create Account
                        synchronized (LOCK) {
                            String username = reader.readLine();
                            String password = reader.readLine();
                            String bio = reader.readLine();
                            String pfp = reader.readLine();

                            if (database.createUser(username, password, bio, pfp)) {
                                writer.println("success");
                            } else {
                                writer.println("username_taken");
                            }
                        }
                        break;

                    case "2": // Login
                        synchronized (LOCK) {
                            String username = reader.readLine().trim();
                            String password = reader.readLine().trim();

                            if (database.authenticate(username, password)) {
                                currentUser = database.getUser(username);
                                writer.println("success");
                            } else {
                                writer.println("Invalid credentials.");
                            }
                        }
                        break;

                    case "logout": // Logout
                        synchronized (LOCK) {
                            currentUser = null; // Clear user session
                            writer.println("logout_success");
                        }
                        break;

                    case "get_users": // Fetch all users
                        synchronized (LOCK) {
                            for (User user : database.getUsers()) {
                                writer.println(user.getUsername()); // Send each username
                            }
                            writer.println("END"); // End of user list
                        }
                        break;

                    case "load_conversation": // Load messages for a conversation
                        String conversationName = reader.readLine();
                        if (currentUser != null) {
                            ArrayList<String> messages = currentUser.loadConversation(conversationName);
                            if (messages != null) {
                                for (String message : messages) {
                                    writer.println(message);
                                }
                            }
                        }
                        writer.println("END");
                        break;

                    case "search_user": // Search for a user
                        synchronized (LOCK) {
                            String query = reader.readLine().trim().toLowerCase();
                            for (User user : database.getUsers()) {
                                if (user.getUsername().toLowerCase().contains(query)) {
                                    writer.println(user.getUsername());
                                }
                            }
                            writer.println("END");
                        }
                        break;


                    case "get_user_details": // Fetch user details
                        synchronized (LOCK) {
                            String requestedUser = reader.readLine();
                            User user = database.getUser(requestedUser);
                            if (user != null) {
                                writer.println(user.getUsername());
                                writer.println(user.getBio());
                                writer.println(user.getPfp());
                            } else {
                                writer.println("User not found");
                            }
                        }
                        break;

                    case "save_profile_picture":
                        synchronized (LOCK) {
                            String username = reader.readLine();
                            String photoPath = reader.readLine();
                            boolean success = database.saveProfilePicture(username, photoPath);
                            writer.println(success ? "success" : "failure");
                        }
                        break;


                    case "send_message": // Send a message
                        String targetUser = reader.readLine();
                        String messageContent = reader.readLine();
                        if (currentUser != null && !messageContent.isEmpty()) {
                            User receiver = database.getUser(targetUser);
                            if (receiver != null) {
                                boolean success = currentUser.sendMessage(receiver, messageContent);
                                writer.println(success ? "success" : "failure");
                            } else {
                                writer.println("user_not_found");
                            }
                        }
                        break;

                    default:
                        writer.println("Invalid command.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
