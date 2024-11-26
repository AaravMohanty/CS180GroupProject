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
            //hi
        } catch (IOException e) {
            //hi
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
                    if(line.equals(null)){
                        break;
                    }
                    switch (line) {
                        case "1":
                            synchronized (LOCK) {
                                String username = reader.readLine(); //match
                                String password = reader.readLine();
                                String bio = reader.readLine();
                                String pfp = reader.readLine(); //one extra sent to server then off by 1

                                if (username.isEmpty() || password.isEmpty() || bio.isEmpty() || pfp.isEmpty()) {
                                    break; // failed return to beginning to reprompy
                                }

                                if (database.createUser(username, password, bio, pfp)) {
                                    writer.println("success");
                                } else {
                                    writer.println("random");
                                    break;
                                }

                                break;
                            }

                        case "2":
                            User user;
                            synchronized (LOCK) {
                                while (true) {

                                    String username1 = reader.readLine().trim();
                                   // System.out.println("username " + username1);
                                    String pass = reader.readLine().trim();
                                   // System.out.println("password '" + pass + "'");
                                    user = database.getUser(username1);
                                   // System.out.println(user.displayUser());
                                   // System.out.println("'" + user.getPassword() + "'");
                                    if (user != null) {
                                        System.out.println("user is not null");
                                    } else {
                                        System.out.println("user is null");
                                    }
                                    if (user != null && user.getPassword().trim().equals(pass)) {
                                        writer.println("success");
                                       // writer.println("Login successful! Welcome back, " + username1 + "!");
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
                                            if (friend != null && !user.getFriends().contains(friendName) && !user.getBlockedUsers().contains(friendName)) {
                                                writer.println("success");
                                                user.addFriend(friend);


                                            } else if(user.getFriends().contains(friendName)){
                                                writer.println("already friends");
                                            }
                                            else if(user.getBlockedUsers().contains(friendName)){
                                                writer.println("unblock first");
                                            }

                                            else {
                                                writer.println("not able to add friend");
                                            }
                                        }
                                        break;

                                    case "2":
                                        if (user == null) {
                                            break;
                                        }
                                        String removeFriendName = reader.readLine().trim();

                                        if (removeFriendName.isEmpty()) {
                                            break;
                                        }
                                        User removeFriend = database.getUser(removeFriendName);


                                            synchronized (LOCK) {
                                                if( removeFriend != null && user.getFriends().contains(removeFriendName)) {
                                                    writer.println("success");
                                                    user.removeFriend(removeFriendName);
                                                }
                                                else if(!user.getFriends().contains(removeFriendName)){
                                                    writer.println("not already friends");
                                                }
                                                else {
                                                    writer.println("not able to remove friend");
                                                }
                                            }
                                            break;



                                    case "3":
                                        String blockUnblockChoice = reader.readLine().trim();

                                        switch (blockUnblockChoice) {
                                            case "1":
                                                String blockedUsername = reader.readLine().trim();
                                                synchronized (LOCK) {
                                                    User blockedUser = database.getUser(blockedUsername);

                                                    if (blockedUser == null) {
                                                        writer.println("User not found.");
                                                        break;
                                                    }

                                                    if (user.blockUser(blockedUser)) {
                                                        writer.println("success");
                                                    }  else if(user.getBlockedUsers().contains(blockedUsername)){
                                                        writer.println("already blocked");
                                                    }

                                                    else {
                                                        writer.println("User could not be blocked.");
                                                    }
                                                }

                                                break;

                                            case "2":
                                                String unblockedUsername = reader.readLine().trim();
                                                synchronized (LOCK) {
                                                    User unblockedUser = database.getUser(unblockedUsername);



                                                    if (unblockedUser == null) {
                                                        writer.println("User not found.");
                                                        break;
                                                    }

                                                    if (user.getBlockedUsers().contains(unblockedUsername)) {
                                                        if (user.unblockUser(unblockedUser)) {
                                                            writer.println("success");
                                                        } else {
                                                            writer.println("already unblocked");
                                                        }
                                                    }

                                                    else {
                                                        writer.println("User could not be unblocked.");
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
                                           else{
                                               writer.println("userfound");
                                            }

                                            String content = reader.readLine().trim();

                                            if (content.isEmpty()) {
                                                System.out.println("Message cannot be empty.");
                                                break;
                                            }

                                            if (user.getFriends().contains(receiverUsername) && user.sendMessage(receiver, content)) { //& you cant be friends if blocked so accounts for that
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
                                        if (receiverUsername1.isEmpty()) {
                                            System.out.println("Username cannot be empty.");
                                            break;
                                        }
                                        synchronized (LOCK) {


                                            User receiver1 = database.getUser(receiverUsername1);

                                            if (receiver1 == null) {
                                                writer.println("User not found");
                                                break;
                                            }
                                            else{
                                                writer.println("all good");
                                            }



                                            String content1 = reader.readLine().trim();
                                            if (content1.isEmpty()) {
                                                break;
                                            }

                                            if (user.getFriends().contains(receiver1) && user.sendPhoto(receiver1, content1)) {
                                                writer.println("success");
                                            } else {
                                                writer.println("Failed to send Photo.Make sure you're friends with " + receiver1 + " and they're friend's with you.");
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
                                            break;
                                        }
                                        writer.println("all good");
                                        synchronized (LOCK) {

                                            for (User user1 : database.getUsers()) {
                                                writer.println(user1.getUsername());
                                            }
                                            writer.println("END");

                                            String usernameToView1 = reader.readLine().trim();
                                            if (usernameToView1.isEmpty()) {
                                                System.out.println("Username cannot be empty.");
                                                break;
                                            }
                                            User profileUser3 = database.getUser(usernameToView1);

                                            if (profileUser3 != null &&
                                                    database.getUsers().contains(profileUser3)) {
                                                writer.println("end");
                                                writer.println(profileUser3.displayUser());
                                                //writer.println("END");
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
