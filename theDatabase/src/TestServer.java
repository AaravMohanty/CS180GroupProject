import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestServer { //extends thread
    public static final Object lock = new Object();
    private static Database database;

    public static void main(String[] args) {
        database = new Database();

        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server is running on port " + 1234 + "...");
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    BufferedReader reader = null;
                    PrintWriter writer = null;
                    try {
                        System.out.println("New connection from " + socket.getRemoteSocketAddress());
                        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        writer = new PrintWriter(socket.getOutputStream(), true);

                        while (true) {
                            String line = reader.readLine();
                            System.out.println(line);
                            switch (line) {
                                case "1":
                                    synchronized (lock) {
                                        String username = reader.readLine();
                                        String password = reader.readLine();
                                        String bio = reader.readLine();
                                        String pfp = reader.readLine(); //one extra sent to server then off by 1

                                        if (database.createUser(username, password, bio, pfp)) {
                                            writer.println("success");
                                        } else {
                                            writer.println("random");
                                        }

                                        break;
                                    }

                                case "2":
                                    User user;
                                    synchronized (lock) {
                                        while (true) {

                                            String username1 = reader.readLine().trim();
                                            System.out.println("username " + username1);
                                            String pass = reader.readLine().trim();
                                            System.out.println("password '" + pass + "'");
                                            user = database.getUser(username1);
                                            System.out.println(user.displayUser());
                                            System.out.println("'" + user.getPassword() + "'");
                                            if (user != null) {
                                                System.out.println("user is not null");
                                            } else {
                                                System.out.println("user is null");
                                            }
                                            if (user != null && user.getPassword().trim().equals(pass)) {
                                                writer.println("success");
                                                writer.println("Login successful! Welcome back, " + username1 + "!");
                                                break;
                                            } else {
                                                writer.println("Invalid username or password.");
                                            }
                                        }
                                    }
                                    boolean sofar = true;
                                    while (sofar) {

                                            String choice = reader.readLine().trim();

                                            switch (choice) {
                                                case "1":

                                                    String friendName = reader.readLine().trim();
                                                    if (friendName.isEmpty()) {
                                                        break;
                                                    }
                                                    synchronized(lock) {
                                                        List<String> Friends1 = user.getFriends();
                                                        User friend = database.getUser(friendName);
                                                        if (friend != null && !Friends1.contains(friendName)) {
                                                            writer.println("success");
                                                            user.addFriend(friend);
                                                            Friends1.add(friendName);
                                                        } else {
                                                            writer.println("not able to add friend");
                                                        }
                                                    }
                                                    break;

                                                case "2":
                                                    if (user == null) {
                                                        break;
                                                    }
                                                    String removeFriendName = reader.readLine().trim();
                                                    List<String> Friends = null;
                                                    if (!removeFriendName.isEmpty()) {
                                                        Friends = user.getFriends();
                                                    }
                                                    synchronized(lock) {

                                                        if (database.getUser(removeFriendName) != null && Friends.contains(removeFriendName)) {
                                                            user.removeFriend(removeFriendName);
                                                            writer.println("success");
                                                        } else {
                                                            writer.println("not able to remove friend");
                                                        }
                                                    }
                                                    break;

                                                case "3":
                                                    String blockUnblockChoice = reader.readLine().trim();

                                                    switch (blockUnblockChoice) {
                                                        case "1":
                                                            String blockedUsername = reader.readLine().trim();
                                                            synchronized(lock){
                                                                User blockedUser = database.getUser(blockedUsername);

                                                                if (blockedUser == null) {
                                                                    writer.println("User not found.");
                                                                    break;
                                                                }

                                                                if (user.blockUser(blockedUser)) {
                                                                    writer.println("success");
                                                                } else {
                                                                    writer.println("User could not be blocked.");
                                                                }
                                                            }

                                                            break;

                                                        case "2":
                                                            String unblockedUsername = reader.readLine().trim();
                                                            synchronized(lock) {
                                                                User unblockedUser = database.getUser(unblockedUsername);

                                                                if (unblockedUser == null) {
                                                                    writer.println("User not found.");
                                                                    break;
                                                                }

                                                                if (user.unblockUser(unblockedUser)) {
                                                                    writer.println("success");
                                                                    user.removeFriend(unblockedUsername);
                                                                } else {
                                                                    writer.println("failure");
                                                                }
                                                            }
                                                            break;

                                                        default:
                                                            writer.println("Invalid choice.");
                                                            break;
                                                    }
                                                    break;

                                                case "4":
                                                    String receiverUsername = reader.readLine().trim();
                                                    if (receiverUsername.isEmpty()) {
                                                        break;
                                                    }
                                                    synchronized(lock) {


                                                        User receiver = database.getUser(receiverUsername);

                                                        if (receiver == null) {
                                                            writer.println("User not found.");
                                                            break;
                                                        }
                                                        writer.println("userfound");

                                                        String content = reader.readLine().trim();

                                                        if (user.sendMessage(receiver, content)) {
                                                            writer.println("success");
                                                        } else {
                                                            writer.println("Failed to send message. Ensure you are friends with "
                                                                    + receiverUsername + " and " + receiverUsername
                                                                    + " is your friend");
                                                        }
                                                    }
                                                    break;

                                                case "5":

                                                    String receiverUsername1 = reader.readLine().trim();
                                                    synchronized(lock) {


                                                        User receiver1 = database.getUser(receiverUsername1);

                                                        if (receiver1 == null) {
                                                            break;
                                                        }

                                                        String content1 = reader.readLine().trim();
                                                        if (content1.isEmpty()) {
                                                            break;
                                                        }

                                                        if (user.sendPhoto(receiver1, content1)) {
                                                            writer.println("Message sent!");
                                                        } else {
                                                            writer.println("Failed to send Photo.");
                                                        }
                                                    }
                                                    break;

                                                case "6":
                                                    String username2 = reader.readLine().trim();
                                                    synchronized(lock) {
                                                        User receiver2 = database.getUser(username2);
                                                        if (receiver2 == null) {
                                                            break;
                                                        }
                                                        String content2 = reader.readLine().trim();

                                                        if (user.deleteMessage(receiver2, content2)) {
                                                            writer.println("success");
                                                        } else {
                                                            writer.println("Failed to delete message.");
                                                        }
                                                    }
                                                    break;

                                                case "7":
                                                    String usernameToView = reader.readLine().trim();
                                                    synchronized(lock) {
                                                        User profileUser = database.getUser(usernameToView);

                                                        if (profileUser != null && database.getUsers().contains(profileUser)) {
                                                            writer.println("success");
                                                            writer.println(profileUser.displayUser());
                                                            writer.println("END");
                                                        } else {
                                                            writer.println("User not found.");
                                                        }
                                                    }
                                                    break;

                                                case "8":
                                                    if (user == null) {
                                                        writer.println("Please log in first.");
                                                        return;
                                                    }
                                                    synchronized(lock) {

                                                        for (User user1 : database.getUsers()) {
                                                            writer.println(user1.getUsername());
                                                        }
                                                        writer.println("END");

                                                        String usernameToView1 = reader.readLine().trim();
                                                        User profileUser3 = database.getUser(usernameToView1);

                                                        if (profileUser3 != null && database.getUsers().contains(profileUser3)) {
                                                            writer.println(profileUser3.displayUser());
                                                            writer.println("END");
                                                        } else {
                                                            writer.println("User not found.");
                                                        }
                                                    }
                                                    break;

                                                case "9":
                                                    user = null;

                                                    System.out.println("User after logout: " + user);
                                                    sofar = false;
                                                    break;

                                                default:
                                                    writer.println("Invalid choice. Please try again.");
                                                    break;
                                            }

                                    }
                                break;
                                case "terminate":
                                    writer.println("Have a nice day!");
                                    reader.close();
                                    writer.close();
                                    return;

                                default:
                                    //writer.println("Invalid choice. Please enter 1, 2, or 3.");
                                    break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                System.err.println("Error closing the reader: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        writer.close();
                    }
                }).start();
            }
        } catch (IOException e) {
            //put something here
        }
    }
}
