
import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * The server class / backend for the project.
 * <p>
 * Purdue University -- CS18000 -- Fall 2024
 *
 * @author Elan Smyla, Aarav Mohanty, Hannah Cha, Kai Nietzche
 * @version December 8th, 2024
 */

public class Server implements Runnable, ServerInterface {
    public static final Object LOCK = new Object();
    private static Database database;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        database = new Database();
        try {
            serverSocket = new ServerSocket(1234);
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
                String command = reader.readLine();
                if (command == null) break;
                switch (command) {
                    case "1":
                        synchronized (LOCK) {
                            String username = reader.readLine();
                            String password = reader.readLine();
                            String bio = reader.readLine();

                            if (database.createUser(username, password, bio)) {
                                writer.println("success");
                            } else {
                                writer.println("username_taken");
                            }
                        }
                        break;

                    case "2":
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

                    case "logout":
                        synchronized (LOCK) {
                            currentUser = null;
                            writer.println("logout_success");
                        }
                        break;

                    case "get_users":
                        synchronized (LOCK) {
                            for (User user : database.getUsers()) {
                                writer.println(user.getUsername());
                            }
                            writer.println("END");
                        }
                        break;

                    case "get_conversations":
                        synchronized (LOCK) {
                            if (currentUser == null) {
                                writer.println("not_logged_in");
                                break;
                            }
                            try {
                                for (String conversation : currentUser.getConversations()) {
                                    writer.println(conversation);
                                }
                                writer.println("END");
                            } catch (Exception e) {
                                writer.println("error");
                                e.printStackTrace();
                            }
                        }
                        break;

                    case "load_conversation":
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
                                        writer.println(message);
                                    }
                                } else {
                                    writer.println("Send a message to start the conversation :)");
                                }
                                writer.println("END");
                            } catch (Exception e) {
                                writer.println("error");
                                e.printStackTrace();
                            }
                        }
                        break;

                    case "search_user":
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

                    case "get_user_details":
                        synchronized (LOCK) {
                            String requestedUser = reader.readLine();
                            User user = database.getUser(requestedUser);
                            if (user != null) {
                                writer.println(user.getUsername());
                                writer.println(user.getBio());
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

                    case "send_message":
                        synchronized (LOCK) {
                            if (currentUser == null) {
                                writer.println("not_logged_in");
                                break;
                            }
                            String conversationName = reader.readLine();
                            String messageContent = reader.readLine();
                            if (conversationName == null
                                    || messageContent == null
                                    || conversationName.isEmpty()
                                    || messageContent.isEmpty()) {
                                writer.println("error");
                                break;
                            }
                            try {
                                String[] users = conversationName.split("_Messages.txt")[0].split("_");
                                String otherUser = users[0].equals(currentUser.getUsername()) ? users[1] : users[0];

                                User receiver = database.getUser(otherUser);
                                if (receiver != null
                                        && currentUser.getFriends().contains(otherUser)
                                        && receiver.getFriends().contains(currentUser.getUsername())) {
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
                            if (conversationName == null
                                    || messageContent == null
                                    || conversationName.isEmpty()
                                    || messageContent.isEmpty()) {
                                writer.println("error");
                                break;
                            }
                            try {
                                String[] users = conversationName.split("_Messages.txt")[0].split("_");
                                String otherUser = users[0].equals(currentUser.getUsername()) ? users[1] : users[0];

                                User receiver = database.getUser(otherUser);
                                if (receiver != null
                                        && currentUser.getFriends().contains(otherUser)
                                        && receiver.getFriends().contains(currentUser.getUsername())) {
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
                                writer.println("not_logged_in");
                                break;
                            }

                            String friendName = reader.readLine().trim();
                            if (friendName.isEmpty()) {
                                writer.println("failure");
                                break;
                            }

                            User friend = database.getUser(friendName);
                            if (friend == null) {
                                writer.println("failure");
                                break;
                            }

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
                                writer.println("not_logged_in");
                                break;
                            }

                            String friendName = reader.readLine().trim();
                            if (friendName.isEmpty()) {
                                writer.println("failure");
                                break;
                            }

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