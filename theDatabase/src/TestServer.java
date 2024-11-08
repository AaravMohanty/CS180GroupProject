import java.net.*;
import java.io.*;
public class TestServer {
    private static Database database;
    public static void main(String[] args){
        database = new Database();

        try{
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server is running on port " + 1234 + "...");
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("New connection from " + socket.getRemoteSocketAddress());
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                String line = reader.readLine();
                switch (line) {
                    case "1":
                        String username = reader.readLine();
                        String password = reader.readLine();
                        String bio = reader.readLine();
                        String pfp = reader.readLine();
                      //  if (username.isEmpty() || password.isEmpty() || bio.isEmpty() || pfp.isEmpty()) {
                       //     writer.println("All fields are required. Please try again.");
                        //    break;
                      //  }
                        if (database.createUser(username, password, bio, pfp)) {
                            writer.println("Account created successfully!\n");
                        } else {
                            writer.println("Username already taken.\nPlease re-run the program to try again.");
                        }

                        break;


                    case "2":
                        String username1 = reader.readLine().trim();
                        //String pass = reader.readLine().trim();
                        User user = database.getUser(username1);
                        do{
                            writer.println("Invalid username");
                        }while(user == null || !user.getUsername().equals(username1));
                        writer.println("success");
                        //writer.println("Login successful! Welcome back, " + username1 + "!");
                        String pass = reader.readLine().trim();
                       // User user = database.getUser(pass);
                        do{
                            writer.println("Invalid password");
                        }while(pass == null || !user.getPassword().equals(pass));
                        writer.println("success2");
                        writer.println("Login successful! Welcome back, " + username1 + "!");


                       /* if (user != null && user.getPassword().equals(pass)) {
                            writer.println("success");
                            writer.println("Login successful! Welcome back, " + username1 + "!");
                        } else {
                            writer.println("Invalid username or password.");
                        } //reprompt?
                        */

                        if(user != null){
                            while(true){
                                String choice = reader.readLine().trim();


                                switch (choice) {
                                    case "1":
                                        // Add a friend

                                       // System.out.print("Enter the username of the friend you want to add: ");
                                        String friendName = reader.readLine().trim();
                                      //  if (friendName.isEmpty()) {
                                       //     writer.println("Username cannot be empty.");
                                       //     break;
                                      //  }

                                        User friend = database.getUser(friendName);
                                        if (friend != null) {
                                            user.addFriend(friend);
                                            writer.println("success");
                                        } else {
                                            System.out.println("User not found.");
                                        }
                                        break;
                                    case "2":


                                        String removeFriendName = reader.readLine().trim();
                                    //    if (removeFriendName.isEmpty()) {
                                         //   writer.println("Username cannot be empty.");
                                      //      break;
                                      //  }

                                        if (database.getUser(removeFriendName) != null) {
                                            user.removeFriend(removeFriendName);
                                            writer.println("success");
                                        } else {
                                            System.out.println("User not found.");
                                        }
                                        break;
                                    case "3":
                                        // Block or unblock a user


                                        String blockedUsername = reader.readLine().trim();
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

                                        System.out.print("Enter the username of the user to unblock: ");
                                        String unblockedUsername = reader.readLine().trim();
                                        User unblockedUser = database.getUser(unblockedUsername);

                                        if (unblockedUser == null) {
                                            writer.println("User not found.");
                                            break;
                                        }

                                        if (user.unblockUser(unblockedUser)) {
                                            writer.println("success");
                                        } else {
                                            writer.println("failure");
                                        }
                                        break;
                                    case "4":


                                        System.out.print("Enter the username of the receiver: ");
                                        String receiverUsername = reader.readLine().trim();
                                        User receiver = database.getUser(receiverUsername);

                                        if (receiver == null) {
                                            writer.println("User not found.");
                                            break; //og if reciever != null? if decide to keep, need to add wjile to client with each method
                                        }


                                        String content = reader.readLine().trim();
                                        if (content.isEmpty()) {
                                            writer.println("Message cannot be empty.");
                                            break;
                                        }

                                        if (user.sendMessage(receiver, content)) {
                                            writer.println("success!");
                                        } else {
                                            writer.println("Failed to send message. Ensure you are friends with "
                                                    + receiverUsername + " and " + receiverUsername
                                                    + " is your friend"); //dont need to add, client is the one printing
                                        }
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
                                    //    if (content1.isEmpty()) {
                                          //  writer.println("Message cannot be empty.");
                                           // break;
                                       // }

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
                                          //  writer.println("Message cannot be empty.");
                                          //  break;
                                      //  }

                                        if (user.deleteMessage(receiver2, content2)) {
                                            writer.println("success");
                                        } else {
                                            writer.println("Failed to delete message.");
                                        }
                                    case "7":
                                        // View a specific user's profile


                                      //  writer.println("Available users:");
                                        for (User user1 : database.getUsers()) {
                                            writer.println("- " + user1.getUsername());
                                        }

                                        System.out.print("Enter the username of the profile to view: ");
                                        String usernameToView = reader.readLine().trim();
                                        User profileUser = database.getUser(usernameToView);

                                        if (profileUser != null) {
                                            if (profileUser != null && database.getUsers().contains(profileUser)) {
                                                writer.println(profileUser.displayUser());
                                            } else {
                                                writer.println("User not found.");
                                            } // View the selected profile
                                        } else {
                                            writer.println("User not found.");
                                        }
                                        break;
                                    case "8":
                                        // Search for users and view profiles
                                        if (user == null) {
                                            System.out.println("Please log in first.");
                                            return;
                                        }

                                        System.out.println("Available users:");
                                        for (User user1 : database.getUsers()) {
                                            System.out.println("- " + user1.getUsername());
                                        }

                                        System.out.print("Enter the username of the profile to view: ");
                                        String usernameToView1 = reader.readLine().trim();
                                        User profileUser3 = database.getUser(usernameToView1);

                                        if (profileUser3 != null) {
                                            if (profileUser3 != null && database.getUsers().contains(profileUser3)) {
                                                writer.println(profileUser3.displayUser());
                                            } else {
                                                writer.println("User not found.");
                                            } // View the selected profile
                                        } else {
                                            writer.println("User not found.");
                                        }
                                        break;
                                    case "9":
                                        // Logout option to exit the user menu
                                        System.out.println("Logging out...");
                                        user = null; // Clear logged-in user
                                        break;
                                    default:
                                        // Handle invalid user menu choices
                                        System.out.println("Invalid choice. Please try again.");
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
                }

            }
        }
        catch (IOException e){
            //put something here
        }
    }



}

