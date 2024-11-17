import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Server class to handle all the logic based on the input
 * that is received from the client.
 * <p>
 * Purdue University -- CS18000 -- Fall 2024
 *
 * @author Elan Smyla, Aarav Mohanty, Hannah Cha, Kai Nietzche
 * @version November 17th, 2024
 */

public class Server implements Runnable { //extends thread
    public static final Object LOCK = new Object();
    private static Database database;


    public static ServerSocket serverSocket;

    public static void main(String[] args) {
        database = new Database();
        try {
            serverSocket = new ServerSocket(1234);
            System.out.println("Server is running on port " + 1234 + "...");

            while (true) {
                Thread t = new Thread(new Server());
                t.start();
            }
        } catch (OutOfMemoryError e) {
            ;
        } catch (IOException e) {
            ;
        }

    }

    public void run() {
        try {
            Socket socket = serverSocket.accept();
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
                            synchronized (LOCK) {
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
                            synchronized (LOCK) {
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
                                String choice;
                                try {
                                    choice = reader.readLine().trim();
                                } catch (NullPointerException e) {
                                    break;
                                }

                                switch (choice) {
                                    case "1":

                                        String friendName = reader.readLine().trim();
                                        if (friendName.isEmpty()) {
                                            break;
                                        }
                                        synchronized (LOCK) {

                                            User friend = database.getUser(friendName);
                                            if (friend != null && !user.getFriends().contains(friendName)) {
                                                writer.println("success");
                                                user.addFriend(friend);

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



                                        if (database.getUser(removeFriendName) != null) {
                                            synchronized (LOCK) {
                                                if(user.removeFriend(removeFriendName)) {

                                                    writer.println("success");
                                                }
                                                else {
                                                    writer.println("not able to remove friend");
                                                }
                                            }

                                        }


                                        break;

                                    case "3":
                                        String bLOCKUnbLOCKChoice = reader.readLine().trim();

                                        switch (bLOCKUnbLOCKChoice) {
                                            case "1":
                                                String bLOCKedUsername = reader.readLine().trim();
                                                synchronized (LOCK) {
                                                    User bLOCKedUser = database.getUser(bLOCKedUsername);

                                                    if (bLOCKedUser == null) {
                                                        writer.println("User not found.");
                                                        break;
                                                    }

                                                    if (user.bLOCKUser(bLOCKedUser)) {
                                                        writer.println("success");
                                                    } else {
                                                        writer.println("User could not be bLOCKed.");
                                                    }
                                                }

                                                break;

                                            case "2":
                                                String unbLOCKedUsername = reader.readLine().trim();
                                                synchronized (LOCK) {
                                                    User unbLOCKedUser = database.getUser(unbLOCKedUsername);

                                                    if (unbLOCKedUser == null) {
                                                        writer.println("User not found.");
                                                        break;
                                                    }

                                                    if (user.unbLOCKUser(unbLOCKedUser)) {
                                                        writer.println("success");
                                                        user.removeFriend(unbLOCKedUsername);
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
                                        synchronized (LOCK) {


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
                                                writer.println(
                                                    "Failed to send message. Ensure you are friends with "
                                                        + receiverUsername + " and " + receiverUsername
                                                        + " is your friend");
                                            }
                                        }
                                        break;

                                    case "5":

                                        String receiverUsername1 = reader.readLine().trim();
                                        synchronized (LOCK) {


                                            User receiver1 = database.getUser(receiverUsername1);

                                            if (receiver1 == null) {
                                                writer.println("User not found");
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

                                        User receiver2 = database.getUser(username2);
                                        if (receiver2 == null) {
                                            writer.println("User not found");
                                            break;
                                        }
                                        String content2 = reader.readLine().trim();
                                        synchronized (LOCK) {
                                            if (user.deleteMessage(receiver2, content2)) {
                                                writer.println("success");
                                            } else {
                                                writer.println("Failed to delete message.");
                                            }

                                        }

                                        break;

                                    case "7":
                                        String usernameToView = reader.readLine().trim();
                                        synchronized (LOCK) {
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
                                        synchronized (LOCK) {

                                            for (User user1 : database.getUsers()) {
                                                writer.println(user1.getUsername());
                                            }
                                            writer.println("END");

                                            String usernameToView1 = reader.readLine().trim();
                                            User profileUser3 = database.getUser(usernameToView1);

                                            if (profileUser3 != null && 
                                                database.getUsers().contains(profileUser3)) {
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
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

}
