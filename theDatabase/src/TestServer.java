import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestServer {
    private static Database database;
    public static void main(String[] args){
        database = new Database();

        try{
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server is running on port " + 1234 + "...");
            while(true){
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    BufferedReader reader = null;
                    PrintWriter writer = null;
                    try {
                        System.out.println("New connection from " + socket.getRemoteSocketAddress());
                        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        writer = new PrintWriter(socket.getOutputStream(), true);
                        String line = reader.readLine();
                        switch (line) {
                            case "1":
                                String username = reader.readLine();
                                String password = reader.readLine();
                                String bio = reader.readLine();
                                String pfp = reader.readLine();
                               // if (username.isEmpty() || password.isEmpty() || bio.isEmpty() || pfp.isEmpty()) {
                                   // writer.println("All fields are required. Please try again.");
                                   // break;
                              //  }
                                if (database.createUser(username, password, bio, pfp)) {
                                    writer.println("success");
                                } else {
                                    writer.println("random");
                                }

                                break;


                            case "2":
                                User user;
                                while(true) {
                                    String username1 = reader.readLine().trim();
                                    String pass = reader.readLine().trim();
                                    user = database.getUser(username1);
                                    if (user != null && user.getPassword().equals(pass)) {
                                        // writer.println("Login successful! Welcome back, " + username1 + "!");
                                        writer.println("success");
                                        writer.println("Login successful! Welcome back, " + username1 + "!");
                                        break;
                                    } else {
                                        writer.println("Invalid username or password.");
                                    }

                                }
                                if (user != null) {
                                    while (true) {
                                        String choice = reader.readLine().trim();


                                        switch (choice) {
                                            case "1":
                                                // Add a friend

                                               // System.out.print("Enter the username of the friend you want to add: ");
                                                String friendName = reader.readLine().trim();
                                                 if (friendName.isEmpty()) {
                                                // writer.println("Username cannot be empty.");
                                                break;
                                                }

                                                List<String> Friends1 = user.getFriends();
                                                User friend = database.getUser(friendName);
                                                if ( friend != null) { //wanna make sure not already  friends
                                                    writer.println("success");
                                                    user.addFriend(friend);
                                                    Friends1.add(friendName);
                                                   //System.out.println(friendName + "has been added as your friend");
                                                } else {
                                                    writer.println("not able to add friend");
                                                }
                                                break;

                                            case "2":

                                                String removeFriendName = reader.readLine().trim();
                                                // if (removeFriendName.isEmpty()) {
                                                //   writer.println("Username cannot be empty.");
                                                //   break;
                                                // }
                                                List<String> Friends = user.getFriends();


                                                if (database.getUser(removeFriendName) != null && Friends.contains(removeFriendName)) {//user is friends
                                                    user.removeFriend(removeFriendName);
                                                    writer.println("success");
                                                } else {
                                                    writer.println("not able to remove friend");
                                                }
                                                break;

                                            case "3":

                                                // Receive the block or unblock choice from the client
                                                String blockUnblockChoice = reader.readLine().trim();

                                                switch (blockUnblockChoice) {
                                                    case "1":  // Block user
                                                        String blockedUsername = reader.readLine().trim();
                                                        User blockedUser = database.getUser(blockedUsername);

                                                        if (blockedUser == null) {
                                                            writer.println("User not found.");
                                                            break;
                                                        }

                                                        // Attempt to block the user
                                                        if (user.blockUser(blockedUser)) {
                                                            writer.println("success");
                                                        } else {
                                                            writer.println("User could not be blocked.");
                                                        }
                                                        break;

                                                    case "2":  // Unblock user
                                                        String unblockedUsername = reader.readLine().trim();
                                                        User unblockedUser = database.getUser(unblockedUsername);

                                                        if (unblockedUser == null) {
                                                            writer.println("User not found.");  // Send response to client
                                                            break;
                                                        }


                                                        if (user.unblockUser(unblockedUser)) {
                                                            writer.println("success");
                                                        } else {
                                                            writer.println("failure");
                                                        }
                                                        break;

                                                    default:
                                                        writer.println("Invalid choice.");
                                                        break;
                                                }
                                                break;

                                            case "4":


                                                System.out.print("Enter the username of the receiver: ");
                                                String receiverUsername = reader.readLine().trim();
                                                User receiver = database.getUser(receiverUsername);

                                                if (receiver == null) {
                                                    writer.println("User not found.");
                                                    break;
                                                }

//this might not match up > need to test content here too?
                                                String content = reader.readLine().trim();
                                                // if (content.isEmpty()) {
                                                //     writer.println("Message cannot be empty.");
                                                //   break;
                                                //   }

                                                if (user.sendMessage(receiver, content)) {
                                                    writer.println("success!");
                                                } else {
                                                    writer.println("Failed to send message. Ensure you are friends with "
                                                            + receiverUsername + " and " + receiverUsername
                                                            + " is your friend");
                                                }
                                                break;
                                            case "5":
                                                // Send a photo to another user


                                                //System.out.print("Enter the username of the receiver: ");
                                                String receiverUsername1 = reader.readLine().trim();
                                                User receiver1 = database.getUser(receiverUsername1);

                                                if (receiver1 == null) {
                                                    writer.println("User not found.");
                                                    break;
                                                }

                                                //System.out.print("Enter your photo's filepath: ");
                                                String content1 = reader.readLine().trim();
                                                //  if (content1.isEmpty()) {
                                                //    writer.println("Message cannot be empty.");
                                                //   break;
                                                //    }

                                                if (user.sendPhoto(receiver1, content1)) {
                                                    System.out.println("Message sent!");
                                                } else {
                                                    System.out.println("Failed to send Photo.");
                                                }
                                                break;
                                            case "6":
                                                // Delete a message with another user

                                                //System.out.print("Enter the username of the user: ");
                                                String username2 = reader.readLine().trim();
                                                User receiver2 = database.getUser(username2);

                                                if (receiver2 == null) {
                                                    writer.println("User not found.");
                                                    break;
                                                }

                                                //System.out.print("Enter your message: ");
                                                String content2 = reader.readLine().trim();
                                                // if (content2.isEmpty()) {
                                                // writer.println("Message cannot be empty.");
                                                //break;
                                                //  }

                                                if (user.deleteMessage(receiver2, content2)) {
                                                    writer.println("success");
                                                } else {
                                                    writer.println("Failed to delete message.");
                                                }
                                                break;
                                            case "7":
                                                // View a specific user's profile


                                                //  writer.println("Available users:");

                                                //  for (User user1 : database.getUsers()) {
                                                //  writer.println("- " + user1.getUsername());
                                                //  }
                                                // writer.println("done");

                                                //  System.out.print("Enter the username of the profile to view: ");
                                                String usernameToView = reader.readLine().trim();
                                                User profileUser = database.getUser(usernameToView);


                                                if (profileUser != null && database.getUsers().contains(profileUser)) {
                                                    writer.println(profileUser.displayUser());
                                                } else {
                                                    writer.println("User not found.");
                                                } // View the selected profile


                                                break;
                                            case "8":
                                                // Search for users and view profiles
                                                if (user == null) {
                                                    writer.println("Please log in first.");
                                                    return;
                                                }

                                                //   writer.println("Available users:");
                                                // boolean keepGoing = true;
                                                //while(keepGoing) {
                                                for (User user1 : database.getUsers()) {
                                                    writer.println("- " + user1.getUsername());
                                                }
                                                //  keepGoing = false;
                                                // writer.println("more");
                                                //}

                                                //   writer.print("Enter the username of the profile to view: ");
                                                String usernameToView1 = reader.readLine().trim();
                                                User profileUser3 = database.getUser(usernameToView1);


                                                if (profileUser3 != null && database.getUsers().contains(profileUser3)) {
                                                    writer.println(profileUser3.displayUser());
                                                } else {
                                                    writer.println("User not found.");
                                                } // View the selected profile
                                                ;

                                                break;
                                            case "9":
                                                // Logout option to exit the user menu
                                                // writer.println("Logging out...");
                                                user = null; // Clear logged-in user
                                                break;
                                            default:
                                                // Handle invalid user menu choices
                                                writer.println("Invalid choice. Please try again.");
                                                break;
                                        }


                                    }
                                }


                            case "3":
                                // Exit the program
                                writer.println("Have a nice day!");
                                reader.close();
                                writer.close(); // Close the scanner before exiting
                                return;

                            default:
                                // Handle invalid main menu choices
                                writer.println("Invalid choice. Please enter 1, 2, or 3.");
                                break;

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
        }
        catch (IOException e){
            //put something here
        }
    }



}

