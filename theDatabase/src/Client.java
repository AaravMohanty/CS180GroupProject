import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String hostname;
        int port = 0;
        Scanner scan = new Scanner(System.in);
        //create a new thread, then during main then new thread, terinate and close thread, dif variable counts amt of thread -> static variable, success add one to thread ct-> sees how many users online

        try {
           System.out.println("Please enter a hostname!");
           hostname = scan.nextLine();
            if (hostname == null || hostname.isEmpty()) {
                throw new IllegalArgumentException("Hostname can't be empty.Connection failed.");
            }
            if (!hostname.equals("localhost")) {
                throw new IllegalArgumentException("Hostname isn't valid.Connection failed.");
            }
            System.out.println("Please enter a port!");
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
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

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
                out.println(choice); // good
                if (choice == null) {
                    break;
                }
                System.out.println(choice);
                //out.println(choice);
                switch (choice) {
        case "1":
            while(true) {
                // Option to create a new user account

                System.out.println("Welcome to ProjectMedia! Please create a new account.");

                System.out.print("Enter username: ");
                String username = scan.nextLine().trim();
                //System.out.println(username);
                out.println(username); //good

                System.out.print("Enter password: ");
                String password = scan.nextLine().trim();
                //System.out.println(password);

                out.println(password); //good

                System.out.print("Enter bio: ");
                String bio = scan.nextLine().trim();
                //System.out.println(bio);

                out.println(bio); //good

                System.out.print("Enter profile picture filename: ");
                String pfp = scan.nextLine().trim();
                //System.out.println(pfp);
                out.println(pfp); // sends it back to the server //good


                // Edge case: Check for empty fields
                if (username.isEmpty() || password.isEmpty() || bio.isEmpty() || pfp.isEmpty()) {

                    System.out.println("All fields are required. Please try again.");
                    break;
                }
                String successOrFailure = in.readLine();
                System.out.println(successOrFailure);
                while (true) {
                    if (successOrFailure.equals("success")) { //from server side
                        System.out.println("Account created successfully!\n");
                        break;
                    } else {
                        System.out.println("Username already taken.\nPlease re-run the program to try again.");
                        break; //user has to rerun program manually
                    }
                }
                break;
            }
                System.out.println("thanks for using our client!");
            //socket.close(); // this will end case 1]
            break;


                case "2":
                    // Option to log in to an existing account
                    //User loggedInUser = runProject.login();
                    boolean loggedin = false;
                    while(!loggedin) {
                        System.out.println("Log In to ProjectMedia");
                        System.out.print("Enter username: ");
                        String userInputUsername = scan.nextLine().trim();
                        out.println(userInputUsername);

                        //String line = in.readLine();
                        //  while(!line.equals("success")) {
                        //if (line.equals("success")) {
                        System.out.println("Enter password: ");
                        String userInputPassword = scan.nextLine().trim();
                        out.println(userInputPassword);
                        String newLine = in.readLine();
                        //while (!newLine.equals("success")) {
                            if (newLine.equals("success")) {
                                String result = in.readLine();
                                System.out.println(result); // welcome back user
                                loggedin = true;
                            }
                            else {

                                //}
                                System.out.println("Invalid username. Try again");
                            }
                        //  }
                    }

                    boolean soFar = true;
                    /*
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
                 }*/



/*while(nextLine.equals("passworddoesntExist")) {
    if (!nextLine.equals("passworddoesntExist")) {
        String fetchPass = nextLine;
        user[1] = nextLine;
        System.out.println("Password is valid");
    } else {
        System.out.println("Invalid username.");
        soFar = false;

    }
}*/
                   // if(soFar){ // should eventually reach cause reprompts user if invalid
                      //  System.out.println("Logged in! Welcome back" + user[0]);
                   // }


                    // If login is successful, show user-specific menu
                    while(soFar) {
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
                                //  if(user == null){
                                //   System.out.println("Please log in first.");
                                //  return; //? is this going to go back to log in menu?
                                // }

                                System.out.print("Enter the username of the friend you want to add: ");
                                String friendName = scan.nextLine().trim();

                                if(friendName.isEmpty()) {
                                    System.out.println("Username cannot be empty.");
                                    break;
                                }
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
                                if (result.equals("success")) {
                                    //String friend = in.readLine();
                                    System.out.println(friendName + " has been added to your friends!");
                                } else {
                                    System.out.println("User not found");
                                }
                                break; // ends it there
                            case "2":
                              /*  if (user == null) {
                                    System.out.println("Please log in first.");
                                    return;
                                }
                                */

                                System.out.print("Enter the username of the friend you want to remove: ");
                                String removeFriendName = scan.nextLine().trim();

                                if(removeFriendName.isEmpty()){
                                    System.out.println("Username cannot be empty.");
                                break;
                                }
                                out.println(removeFriendName);

                                String readingLine = in.readLine();
                                if (readingLine.equals("success")) {
                                    System.out.println(removeFriendName + "has been removed to your friends!");
                                } else {
                                    System.out.println("User not found");//doesnt make sense to reprompt until exists
                                }


                                break;
                            // Remove a friend
                            //  runProject.removeFriend();

                            case "3":
                                System.out.println("Enter 1 to block a user or 2 to unblock a user:");
                                String userChoice2 = scan.nextLine().trim();
                                out.println(userChoice2);

                                switch (userChoice2) {
                                    case "1":
                                        System.out.print("Enter the username of the user to block: ");
                                        String blockedUsername = scan.nextLine().trim();


                                        while (blockedUsername.isEmpty()) {
                                            System.out.println("Username cannot be empty.");
                                            System.out.print("Enter the username of the user to block: ");
                                            blockedUsername = scan.nextLine().trim();
                                        }

                                        out.println(blockedUsername);
                                        String blockResponse = in.readLine();


                                        if (blockResponse.equals("success")) {
                                            System.out.println(blockedUsername + " has been blocked.");
                                        } else {
                                            System.out.println("User not found or could not be blocked.");
                                        }
                                        break;

                                    case "2":
                                        System.out.print("Enter the username of the user to unblock: ");
                                        String unblockedUsername = scan.nextLine().trim();


                                        while (unblockedUsername.isEmpty()) {
                                            System.out.println("Username cannot be empty.");
                                            System.out.print("Enter the username of the user to unblock: ");
                                            unblockedUsername = scan.nextLine().trim();
                                        }

                                        out.println(unblockedUsername);
                                        String unblockResponse = in.readLine();


                                        if (unblockResponse.equals("success")) {
                                            System.out.println(unblockedUsername + " has been unblocked.");
                                        } else {
                                            System.out.println("User not found or could not be unblocked.");
                                        }
                                        break;

                                    default:
                                        System.out.println("Invalid choice. Please enter 1 or 2.");
                                        break;
                                }
                                break;

                            // Block or unblock a user
                            // runProject.blockUser();

                            case "4":


                                System.out.print("Enter the username of the receiver: ");
                                String receiverUsername = scan.nextLine().trim();
                                if (receiverUsername.isEmpty()) {
                                    System.out.println("Username cannot be empty.");
                                    break;
                                }
                                //  User receiver = database.getUser(receiverUsername);
                                out.println(receiverUsername);

                                String newResult = in.readLine();
                                if (newResult.equals("User not found.")) {
                                    System.out.println("User not found.");
                                    break;
                                }

                                System.out.print("Enter your message: ");
                                String content = scan.nextLine().trim();
                                if (content.isEmpty()) {
                                    System.out.println("Message cannot be empty.");
                                    break;
                                }
                                String sent = in.readLine();

                                if (sent.equals("success")) {
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


                                System.out.print("Enter the username of the receiver: ");
                                String receiveUsername = scan.nextLine().trim();
                                //User receiver = database.getUser(receiverUsername);
                                if (receiveUsername.isEmpty()) {
                                    System.out.println("Username cannot be empty.");
                                    break;
                                }

                                System.out.print("Enter your photo's filepath: ");
                                String contentThree = scan.nextLine().trim();
                                if (contentThree.isEmpty()) {
                                    System.out.println("Message cannot be empty.");
                                    return;
                                }

                                String sentPhoto = in.readLine();
                                if (sentPhoto.equals("Message sent!")) {
                                    System.out.println("Message sent!");
                                } else {
                                    System.out.println("Failed to send Photo.");
                                }
                                // Send a photo to another user
                                // runProject.sendPhoto();
                                break;
                            case "6":

                                System.out.print("Enter the username of the user: ");
                                String newUsername = scan.nextLine().trim();
                                // User receiver = database.getUser(username);
                                if (newUsername.isEmpty()) {
                                    System.out.println("Username cannot be empty.");
                                    break;
                                } //reprompts user

                                System.out.print("Enter your message: ");
                                String newContent = scan.nextLine().trim();
                                if (newContent.isEmpty()) {
                                    System.out.println("Message cannot be empty.");
                                    return;
                                }
                                String deleted = in.readLine();

                                if (deleted.equals("success")) {
                                    System.out.println("Message deleted!");
                                } else {
                                    System.out.println("Failed to delete message.");
                                }
                                // Delete a message with another user
                                // runProject.deleteMessage();
                                break;
                            case "7":
                                //create users
                                //  while(){




                                System.out.print("Enter the username of the profile you want to view: ");
                                String usernameToView = scan.nextLine().trim();
                                out.println(usernameToView);
                               // User profileUser = database.getUser(usernameToView);
                                String isProfileUserValid =  in.readLine();
                                System.out.println(isProfileUserValid);

                             /*   if (isProfileUserValid.equals("valid")) {
                                    String displayUser = in.readLine(); //if valid carry out the following function
                                   // System.out.println(profileUser.displayUser());
                                    System.out.println(displayUser);
                                } else {
                                    System.out.println("User not found.");
                                }

                              */
                                // View a specific user's profile
                                //runProject.viewUserProfile();
                                break;
                            case "8":

                                System.out.println("Available users:");
                                ArrayList<String> usernamesArray = new ArrayList<>();


                             //   while(!hasMoreUsers.equals("more")) {
                                    String userNames = in.readLine();
                                    while(userNames != null) {
                                        usernamesArray.add(userNames);
                                        System.out.println("- " + userNames);
                                        //String userNamez = in.readLine(); //keep getting the userna,es
                                    }
                               // }

                                System.out.print("Enter the username of the profile to view: ");
                                String usernameViewing = scan.nextLine().trim();
                                out.println(usernameViewing);
                                if(usernamesArray.isEmpty()) {
                                    System.out.println("Username cannot be empty.");
                                }
                                if(!usernamesArray.contains(usernameViewing)){
                                    System.out.println("User does not exist.");
                                }

                               // User profileUser = database.getUser(usernameToView);

                              //  if (profileUser != null) {
                                   // viewUserProfile(profileUser); // View the selected profile
                               // } else {
                                 //   System.out.println("User not found.");
                            //    }

                                    String wantedUserProfile = in.readLine();
                                    //viewUserProfile(profileUser) & return to client
                                    System.out.println(wantedUserProfile);


                                // Search for users and view profiles
                               // runProject.search();
                                break;
                            case "9":
                                // Logout option to exit the user menu
                                System.out.println("Logging out...");
                                String log = in.readLine();
                                if (log.equals("LoggedOut")) {
                                    break; // Return to main menu
                                }
                               // loggedInUser = null; // Clear logged-in user
                                break;
                            default: //would this run again? -> test
                                // Handle invalid user menu choices
                                System.out.println("Invalid choice. Please try again.");
                        }

                        // Exit the user menu loop if logged out
                        //after case 9

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
