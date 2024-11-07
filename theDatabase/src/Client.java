import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args)  throws UnknownHostException, IOException, ClassNotFoundException{
        String hostname;
        int port = 0;
        Scanner scan = new Scanner(System.in);

        try {
           System.out.println("Please enter a hostname!");
           hostname = scan.nextLine();
            if (hostname == null || hostname.isEmpty()) {
                throw new IllegalArgumentException("Hostname can't be empty.Connection failed.");
            }
            if (!hostname.equals("localhost")) {
                throw new IllegalArgumentException("Hostname isn't valid.Connection failed.");
            }
            String portString = scan.nextLine();

            try {
                port = Integer.parseInt(portString);
                if (port < 0 || port > 65535) {
                    throw new IllegalArgumentException("Invalid port number.Connection failed.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid port number format.Connection failed.");
            }

            Socket socket = new Socket(hostname, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            // now you established a connection, time for actual prompts
            while (true) {
                // Display main menu options
                System.out.println("Main Menu:");
                System.out.println("1. Create a New User");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice (1, 2, or 3): ");


                // Get user input for main menu choice
                String choice = scan.nextLine().trim();
                if(choice == null) {
                break;
                }
                out.println(choice);

    switch (choice) {
        case "1":
            while(true) {
                // Option to create a new user account
                System.out.println("Welcome to ProjectMedia! Please create a new account.");

                System.out.print("Enter username: ");
                String username = scan.nextLine().trim();
                out.println(username);

                System.out.print("Enter password: ");
                String password = scan.nextLine().trim();
                out.println(password);

                System.out.print("Enter bio: ");
                String bio = scan.nextLine().trim();
                out.println(bio);

                System.out.print("Enter profile picture filename: ");
                String pfp = scan.nextLine().trim();
                out.println(pfp); // sends it back to the server


                // Edge case: Check for empty fields
                if (username.isEmpty() || password.isEmpty() || bio.isEmpty() || pfp.isEmpty()) {
                    System.out.println("All fields are required. Please try again.");
                    break;
                }
                String successOrFailure = in.readLine();
                while (true) {
                    if (successOrFailure.equals("success")) { //from server side
                        System.out.println("Account created successfully!\n");
                    } else {
                        System.out.println("Username already taken.\nPlease re-run the program to try again.");
                        break; //user has to rerun program manually
                    }
                }
            }
                System.out.println("thanks for using our client!");
                socket.close(); // this will end case 1


                case "2":
                    // Option to log in to an existing account
                    //User loggedInUser = runProject.login();
                    System.out.println("Log In to ProjectMedia");
                    System.out.print("Enter username: ");
                    String userInputUsername = scan.nextLine().trim();
                    out.println(userInputUsername);

                    String line = in.readLine();
                    boolean soFar = true;
                    String[] user = new String[2];
                 while(line.equals("namedoesntExist")) { // reprompt until name does exist and u can create that user array
                     if (!line.equals("namedoesntExist")) { //on the server side
                         String fetchUsername = line;

                         user[0] = line; // user[2] = 1) username, 2) password
                         System.out.println("Username exists");
                         // return user; // Return the logged-in user // need to access the database and return user
                     } else {
                         System.out.println("Invalid username.Try again.");
                         soFar = false;
                         //  return null; // Return null if login fails
                     }
                 }

                    System.out.print("Enter password: ");
                    String userInputPassword = scan.nextLine().trim();
                    out.println(userInputPassword);
                    String nextLine = in.readLine();
while(nextLine.equals("passworddoesntExist")) {
    if (!nextLine.equals("passworddoesntExist")) {
        String fetchPass = nextLine;
        user[1] = nextLine;
        System.out.println("Password is valid");
    } else {
        System.out.println("Invalid username.");
        soFar = false;

    }
}
                    if(soFar){ // should eventually reach cause reprompts user if invalid
                        System.out.println("Logged in! Welcome back" + user[0]);
                    }


                    // If login is successful, show user-specific menu
                    if (soFar) {
                        // Loop for the user menu, available after logging in

                        // Display user-specific menu options
                        System.out.println("User Menu:");
                        System.out.println("1. Add Friend");
                        System.out.println("2. Remove Friend");
                        System.out.println("3. Block/Unblock User");
                        System.out.println("4. Send Message");
                        System.out.println("5. Send Photo");
                        System.out.println("6. Delete Message");
                        System.out.println("7. View Profile");
                        System.out.println("8. Search Users");
                        System.out.println("9. Logout");
                        System.out.print("Enter your choice (1-9): ");

                        // Get user input for user menu choice
                        String userChoice = scan.nextLine().trim();
                        out.println(userChoice);

                        switch (userChoice) {
                            case "1":
                                // Add a friend
                                //runProject.addFriend();
                                if(user == null){
                                    System.out.println("Please log in first.");
                                    return; //? is this going to go back to log in menu?
                                }

                                    System.out.print("Enter the username of the friend you want to add: ");
                                    String friendName = scan.nextLine().trim();

                                    do  {
                                        System.out.println("Username cannot be empty.");
                                    } while(friendName.isEmpty());
                                    out.println(friendName);

                               /*server side User friend = database.getUser(friendName);
                                if (friend != null) {
                                    user.addFriend(friend);
                                    System.out.println(friendName + " has been added to your friends.");
                                } else {
                                    System.out.println("User not found.");
                                }
                                */
                                String result = in.readLine();
                                if(!result.equals("friendDoesn'tExist")){
                                    String friend = in.readLine();
                                    System.out.println(friend + "has been added to your friends!");
                                } else{
                                    System.out.println("User not found");
                                }
                                break; // ends it there
                            case "2":
                                if (user == null) {
                                    System.out.println("Please log in first.");
                                    return;
                                }

                                System.out.print("Enter the username of the friend you want to remove: ");
                                String removeFriendName = scan.nextLine().trim();
                                do  {
                                    System.out.println("Username cannot be empty.");
                                } while(removeFriendName.isEmpty());
                                out.println(removeFriendName);

                                String readingLine = in.readLine();
                                if(!readingLine.equals("friendDoesn'tExist")){
                                    String friend = in.readLine();
                                    System.out.println(friend + "has been added to your friends!");
                                } else{
                                    System.out.println("User not found");
                                }
                                break;
                                // Remove a friend
                              //  runProject.removeFriend();

                            case "3":
                                if (user == null) {
                                    System.out.println("Please log in first.");
                                    return;
                                }

                                System.out.print("Enter the username of the user to block: ");
                                String blockedUsername = scan.nextLine().trim();
                                do  {
                                    System.out.println("Username cannot be empty.");
                                } while(blockedUsername.isEmpty());
                                out.println(blockedUsername);
                                out.println(blockedUsername); // send to server to block
                                String input = in.readLine(); //now u want to get the user blocked
                                //User blockedUser = scan.getUser(blockedUsername);

                               // if (blockedUser == null) {
                                if(!input.equals("blockedDoesn'tExist")){
                                    String blocked = in.readLine(); //should be the blockedusers name
                                    System.out.println(blocked + "has been added to your friends!");
                                } else{
                                    System.out.println("User not found");
                                }


                            System.out.print("Enter the username of the user to unblock: ");
                            String unblockedUsername = scan.nextLine().trim();
                            do  {
                                System.out.println("Username cannot be empty.");
                            } while(unblockedUsername.isEmpty());
                                out.println(unblockedUsername);
                                out.println(blockedUsername);
                                String inputTwo = in.readLine();

                                if(!inputTwo.equals("blockedDoesn'tExist")){
                                    String blocked = in.readLine(); //should be the blockedusers name
                                    System.out.println(blocked + "has been added to your friends!");
                                } else{
                                    System.out.println("User not found");
                                }
                                break;
                                // Block or unblock a user
                               // runProject.blockUser();

                            case "4":
                                if (user == null) {
                                    System.out.println("Please log in first.");
                                    return;
                                }

                                System.out.print("Enter the username of the receiver: ");
                                String receiverUsername = scan.nextLine().trim();
                                do{
                                    System.out.println("Username cannot be empty.");
                                }while((receiverUsername.isEmpty()));
                              //  User receiver = database.getUser(receiverUsername);
                                out.println(receiverUsername);

                                String newResult = in.readLine();
                                if (newResult.equals("receiverDoesn'tExist")) {
                                    System.out.println("User not found.");
                                    return;
                                }

                                System.out.print("Enter your message: ");
                                String content = scan.nextLine().trim();
                                if (content.isEmpty()) {
                                    System.out.println("Message cannot be empty.");
                                    return;
                                }
                                String sent = in.readLine();

                                if (sent.equals("Sent!")) {
                                    System.out.println("Message sent!");
                                } else {
                                    System.out.println("Failed to send message. Ensure you are friends with "
                                            + receiverUsername + " and " + receiverUsername
                                            + " is your friend");
                                }
                                // Send a message to another user
                               // runProject.sendMessage();
                                break;
                            case "5":
                                // Send a photo to another user
                                // runProject.sendPhoto();
                                break;
                            case "6":
                                if (user == null) {
                                    System.out.println("Please log in first.");
                                    return;
                                }
                                System.out.print("Enter the username of the user: ");
                                String newUsername = scan.nextLine().trim();
                               // User receiver = database.getUser(username);

                                do{
                                    System.out.println("Username cannot be empty.");
                                }while((newUsername.isEmpty())); //reprompts user

                                System.out.print("Enter your message: ");
                                String newContent = scan.nextLine().trim();
                                if (newContent.isEmpty()) {
                                    System.out.println("Message cannot be empty.");
                                    return;
                                }
                                String deleted = in.readLine();

                                if (deleted.equals("deleted!")) {
                                    System.out.println("Message deleted!");
                                } else {
                                    System.out.println("Failed to delete message.");
                                }
                                // Delete a message with another user
                               // runProject.deleteMessage();
                                break;
                            case "7":
                                // View a specific user's profile
                                runProject.viewUserProfile();
                                break;
                            case "8":
                                // Search for users and view profiles
                                runProject.search();
                                break;
                            case "9":
                                // Logout option to exit the user menu
                                System.out.println("Logging out...");
                                loggedInUser = null; // Clear logged-in user
                                break;
                            default:
                                // Handle invalid user menu choices
                                System.out.println("Invalid choice. Please try again.");
                        }

                        // Exit the user menu loop if logged out
                        if (loggedInUser == null) {
                            break; // Return to main menu
                        }
                    }

            break;

        case "3":
            // Exit the program
            System.out.println("Have a nice day!");
            scan.close(); // Close the scanner before exiting
            socket.close();
            return;

        default:
            // Handle invalid main menu choices
            System.out.println("Invalid choice. Please enter 1, 2, or 3.");
    }

            }System.out.println( "Thank you, bye!");
            socket.close();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }


    }


}
