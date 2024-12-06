
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server implements Runnable, ServerInterface {
    public static final Object LOCK = new Object();
    private static Database database;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        database = new Database(); // Initialize the database
        try {
            serverSocket = new ServerSocket(1234); // Start the server
            System.out.println("Server is running on port 1234...");

            try {
                while (true) {
                    Thread t = new Thread(new Server());
                    t.start();
                }
            } catch (OutOfMemoryError e) {
                System.out.println("Server is running on port 1234...");
            }
        } catch (IOException | OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try (Socket socket = serverSocket.accept();
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("New connection from " + socket.getRemoteSocketAddress());
            User currentUser = null;

            while (true) {
                String command = reader.    readLine();
                if (command == null) break; // Terminate if no command is received
                switch (command) {
                    case "1": // Create Account
                        synchronized (LOCK) {
                            String username = reader.readLine();
                            String password = reader.readLine();
                            String bio = reader.readLine();
                            //String pfp = reader.readLine();

//                            if (database.createUser(username, password, bio, pfp)) {
//                                writer.println("success");
//                            }
                            if (database.createUser(username, password, bio)) {
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

                    case "get_conversations": // Fetch all conversations for the current user
                        synchronized (LOCK) {
                            if (currentUser == null) {
                                writer.println("not_logged_in");
                                break;
                            }
                            try {
                                for (String conversation : currentUser.getConversations()) {
                                    writer.println(conversation); // Send each conversation name
                                }
                                writer.println("END"); // End of conversations list
                            } catch (Exception e) {
                                writer.println("error");
                                e.printStackTrace();
                            }
                        }
                        break;

                    case "load_conversation": // Load messages for a specific conversation
                        synchronized (LOCK) {
                            if (currentUser == null) {
                                writer.println("not_logged_in");
                                break;
                            }
                            String conversationName = reader.readLine();
                            if (conversationName == null || conversationName.isEmpty()) {
                                writer.println("error");
                                break;
                            }
                            try {
                                ArrayList<String> messages = currentUser.loadConversation(conversationName);
                                if (messages != null) {
                                    for (String message : messages) {
                                        writer.println(message); // Send each message
                                    }
                                } else {
                                    writer.println("Send a message to start the conversation :)");
                                }
                                writer.println("END"); // End of messages
                            } catch (Exception e) {
                                writer.println("error");
                                e.printStackTrace();
                            }
                        }
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
                                //writer.println(user.getPfp());
                            } else {
                                writer.println("User not found");
                            }
                        }
                        break;

                    case "save_profile_picture": // Save profile picture
                        synchronized (LOCK) {
                            String username = reader.readLine();
                            String photoPath = reader.readLine();
                            boolean success = database.saveProfilePicture(username, photoPath);
                            writer.println(success ? "success" : "failure");
                        }
                        break;

                    case "send_message":
                        synchronized (LOCK) {
                            if (currentUser == null) {
                                writer.println("not_logged_in");
                                break;
                            }
                            String conversationName = reader.readLine();
                            String messageContent = reader.readLine();
                            if (conversationName == null || messageContent == null || conversationName.isEmpty() || messageContent.isEmpty()) {
                                writer.println("error");
                                break;
                            }
                            try {
                                String[] users = conversationName.split("_Messages.txt")[0].split("_");
                                String otherUser = users[0].equals(currentUser.getUsername()) ? users[1] : users[0];

                                User receiver = database.getUser(otherUser);
                                if (receiver != null && currentUser.getFriends().contains(otherUser) && receiver.getFriends().contains(currentUser.getUsername())) {
                                    boolean success = currentUser.sendMessage(receiver, messageContent);
                                    writer.println(success ? "success" : "failure");
                                } else {
                                    writer.println("not_friends");
                                }
                            } catch (Exception e) {
                                writer.println("error");
                                e.printStackTrace();
                            }
                        }
                        break;

                    case "delete_message":
                        synchronized (LOCK) {
                            if (currentUser == null) {
                                writer.println("not_logged_in");
                                break;
                            }
                            String conversationName = reader.readLine();
                            String messageContent = reader.readLine();
                            if (conversationName == null || messageContent == null || conversationName.isEmpty() || messageContent.isEmpty()) {
                                writer.println("error");
                                break;
                            }
                            try {
                                String[] users = conversationName.split("_Messages.txt")[0].split("_");
                                String otherUser = users[0].equals(currentUser.getUsername()) ? users[1] : users[0];

                                User receiver = database.getUser(otherUser);
                                if (receiver != null && currentUser.getFriends().contains(otherUser) && receiver.getFriends().contains(currentUser.getUsername())) {
                                    boolean success = currentUser.deleteMessage(receiver, messageContent);
                                    writer.println(success ? "success" : "failure");
                                } else {
                                    writer.println("not_friends");
                                }
                            } catch (Exception e) {
                                writer.println("error");
                                e.printStackTrace();
                            }
                        }
                        break;

                    case "add_friend":
                        synchronized (LOCK) {
                            if (currentUser == null) {
                                writer.println("not_logged_in"); // User not logged in
                                break;
                            }

                            String friendName = reader.readLine().trim();
                            if (friendName.isEmpty()) {
                                writer.println("failure"); // Empty friend name provided
                                break;
                            }

                            User friend = database.getUser(friendName); // Fetch the User object
                            if (friend == null) {
                                writer.println("failure"); // Friend doesn't exist
                                break;
                            }

                            // Only add the friend to the current user's list
                            boolean success = currentUser.addFriend(friend);
                            writer.println(success ? "success" : "failure");
                        }
                        break;


                    case "get_friends":
                        synchronized (LOCK) {
                            if (currentUser == null) {
                                writer.println("not_logged_in");
                                break;
                            }
                            for (String friend : currentUser.getFriends()) {
                                writer.println(friend);
                            }
                            writer.println("END");
                        }
                        break;


                    case "remove_friend":
                        synchronized (LOCK) {
                            if (currentUser == null) {
                                writer.println("not_logged_in"); // User not logged in
                                break;
                            }

                            String friendName = reader.readLine().trim();
                            if (friendName.isEmpty()) {
                                writer.println("failure"); // Empty friend name provided
                                break;
                            }

                            // Only remove the friend from the current user's list
                            boolean success = currentUser.removeFriend(friendName);
                            writer.println(success ? "success" : "failure");
                        }
                        break;

                    case "get_blocked_users":
                        synchronized (LOCK) {
                            if (currentUser == null) {
                                writer.println("not_logged_in");
                                break;
                            }
                            for (String blockedUser : currentUser.getBlockedUsers()) {
                                writer.println(blockedUser);
                            }
                            writer.println("END");
                        }
                        break;

                    case "block_user":
                        synchronized (LOCK) {
                            if (currentUser == null) {
                                writer.println("not_logged_in");
                                break;
                            }

                            String usernameToBlock = reader.readLine().trim();
                            User userToBlock = database.getUser(usernameToBlock);
                            if (userToBlock != null && currentUser.blockUser(userToBlock)) {
                                writer.println("success");
                            } else {
                                writer.println("failure");
                            }
                        }
                        break;

                    case "unblock_user":
                        synchronized (LOCK) {
                            if (currentUser == null) {
                                writer.println("not_logged_in");
                                break;
                            }

                            String usernameToUnblock = reader.readLine().trim();
                            User userToUnblock = database.getUser(usernameToUnblock);
                            if (userToUnblock != null && currentUser.unblockUser(userToUnblock)) {
                                writer.println("success");
                            } else {
                                writer.println("failure");
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