import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Client class to prompt the user and interact with the server.
 * <p>
 * Purdue University -- CS18000 -- Fall 2024
 *
 * @author Elan Smyla, Aarav Mohanty, Hannah Cha, Kai Nietzche
 * @version November 17th, 2024
 */

public class Client {
    public static void main(String[] args) {
        String hostname;
        int port = 0;
        Scanner scan = new Scanner(System.in);

        try {
            ////System.out.println("Please enter a hostname!");
            //hostname = scan.nextLine();
            hostname = "localhost";
            if (hostname == null || hostname.isEmpty()) {
                throw new IllegalArgumentException("Hostname can't be empty.Connection failed.");
            }
            if (!hostname.equals("localhost")) {
                throw new IllegalArgumentException("Hostname isn't valid.Connection failed.");
            }
            ////System.out.println("Please enter a port!");
            //String portString = scan.nextLine();
            port = 1234;

            try {
                //port = Integer.parseInt(portString);
                if (port < 0 || port > 65535) {
                    throw new IllegalArgumentException("Invalid port number.Connection failed.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid port number format.Connection failed.");
            }

            Socket socket = new Socket(hostname, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                System.out.println("Main Menu:");
                System.out.println("1. Create a New User");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice (1, 2, or 3): ");

                String choice = scan.nextLine().trim();
                out.println(choice);
                if (choice == null) {
                    break;
                }
                //System.out.println(choice);

                switch (choice) {
                    case "1":
                        while (true) {
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
                            out.println(pfp);

                            if (username.isEmpty() || password.isEmpty() || bio.isEmpty() || pfp.isEmpty()) {
                                //System.out.println("All fields are required. Please try again.");
                                break;
                            }
                            String successOrFailure = in.readLine();
                            //System.out.println(successOrFailure);
                            while (true) {
                                if (successOrFailure.equals("success")) {
                                    //System.out.println("Account created successfully!\n");
                                    break;
                                } else {
                                    //System.out.println("Username already taken.\nPlease re-run the program to try again.");
                                    break;
                                }
                            }
                            break;
                        }
                        break;

                    case "2":
                        boolean loggedin = false;
                        while (!loggedin) {
                            System.out.println("Log In to ProjectMedia");
                            System.out.print("Enter username: ");
                            String userInputUsername = scan.nextLine().trim();
                            out.println(userInputUsername);

                            System.out.print("Enter password: ");
                            String userInputPassword = scan.nextLine().trim();
                            out.println(userInputPassword);

                            String newLine = in.readLine();
                            System.out.println("the writer is printing" + newLine);

                            if (newLine.equals("success")) {
                                String result = in.readLine();
                                //System.out.println(result);
                                loggedin = true;
                            } else {
                                System.out.println("Invalid username or password. Try again");
                            }
                        }

                        boolean soFar = true;

                        while (soFar) {


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
                            String userChoice = scan.nextLine().trim();
                            out.println(userChoice);

                            switch (userChoice) {
                                case "1":
                                    System.out.print("Enter the username of the friend you want to add: ");
                                    String friendName = scan.nextLine().trim();

                                    if (friendName.isEmpty()) {
                                        //System.out.println("Username cannot be empty.");
                                        break;
                                    }
                                    out.println(friendName);

                                    String result = in.readLine();
                                    //System.out.print(result);
                                    if (result.equals("success")) {
                                        //System.out.println(friendName + " has been added to your friends!");
                                    } else {
                                        //System.out.println("User not found");
                                    }
                                    break;
                                case "2":
                                    System.out.print("Enter the username of the friend you want to remove: ");
                                    String removeFriendName = scan.nextLine().trim();

                                    if (removeFriendName.isEmpty()) {
                                        //System.out.println("Username cannot be empty.");
                                        break;
                                    }
                                    out.println(removeFriendName);

                                    String readingLine = in.readLine();
                                    //System.out.print(readingLine);
                                    if (readingLine.equals("success")) {
                                        //System.out.println(removeFriendName + "has been removed to your friends!");
                                    } else {
                                        //System.out.println("User not found");
                                    }
                                    break;

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
                                                //System.out.println(blockedUsername + " has been blocked.");
                                            } else {
                                                //System.out.println(blockResponse);
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
                                                //System.out.println(unblockedUsername + " has been unblocked.");
                                            } else {
                                                //System.out.println("User not found or could not be unblocked.");
                                            }
                                            break;

                                        default:
                                            System.out.println("Invalid choice. Please enter 1 or 2.");
                                            break;
                                    }
                                    break;


                                case "4":
                                    System.out.print("Enter the username of the receiver: ");
                                    String receiverUsername = scan.nextLine().trim();
                                    if (receiverUsername.isEmpty()) {
                                        System.out.println("Username cannot be empty.");
                                        break;
                                    }
                                    out.println(receiverUsername);
                                    String newResult = in.readLine();
                                    if (newResult.equals("User not found.")) {
                                        //System.out.println("User not found.");
                                        break;
                                    }
                                    System.out.print("Enter your message: ");
                                    String content = scan.nextLine().trim();
                                    if (content.isEmpty()) {
                                        System.out.println("Message cannot be empty.");
                                        break;
                                    }
                                    out.println(content);
                                    String sent = in.readLine();
                                    if (sent.equals("success")) {
                                        //System.out.println("Message sent!");
                                    } else {
                                        //System.out.println("Failed to send message. Ensure you are friends with " + receiverUsername + " and " + receiverUsername + " is your friend");
                                    }
                                    break;

                                case "5":
                                    System.out.print("Enter the username of the receiver: ");
                                    String receiveUsername = scan.nextLine().trim();
                                    out.println(receiveUsername);
                                    if (receiveUsername.isEmpty()) {
                                        System.out.println("Username cannot be empty.");
                                        break;
                                    }
                                    System.out.print("Enter your photo's filepath: ");
                                    String contentThree;
                                    try {
                                        contentThree = scan.nextLine().trim();
                                    } catch (NullPointerException e) {
                                        break;
                                    }
                                    out.println(contentThree);
                                    if (contentThree.isEmpty()) {
                                        System.out.println("Message cannot be empty.");
                                        break;
                                    }
                                    String sentPhoto = in.readLine();
                                    if (sentPhoto.equals("Message sent!")) {
                                        //System.out.println("Message sent!");
                                    } else {
                                        //System.out.println("Failed to send Photo.");
                                    }
                                    break;

                                case "6":
                                    System.out.print("Enter the username of the user: ");
                                    String newUsername = scan.nextLine().trim();
                                    out.println(newUsername);
                                    if (newUsername.isEmpty()) {
                                        System.out.println("Username cannot be empty.");
                                        break;
                                    }
                                    System.out.print("Enter your message: ");
                                    String newContent = scan.nextLine().trim();
                                    out.println(newContent);
                                    if (newContent.isEmpty()) {
                                        System.out.println("Message cannot be empty.");
                                        break;
                                    }
                                    String deleted = in.readLine();
                                    if (deleted.equals("success")) {
                                        //System.out.println("Message deleted!");
                                    } else {
                                        //System.out.println("Failed to delete message.");
                                    }
                                    break;

                                case "7":
                                    System.out.print("Enter the username of the profile you want to view: ");
                                    String usernameToView = scan.nextLine().trim();
                                    out.println(usernameToView);
                                    String isProfileUserValid = in.readLine();
                                    if (isProfileUserValid.equals("success")) {
                                        String next;
                                        while (!(next = in.readLine()).equals("END")) {
                                            System.out.println(next);
                                        }
                                    } else {
                                        //System.out.println("User not found");
                                    }
                                    break;

                                case "8":
                                    System.out.println("Available users:");
                                    ArrayList<String> usernamesArray = new ArrayList<>();
                                    String userNames;
                                    while (!(userNames = in.readLine()).equals("END")) {
                                        usernamesArray.add(userNames);
                                        System.out.println("- " + userNames);
                                    }
                                    System.out.print("Enter the username of the profile to view: ");
                                    String usernameViewing = scan.nextLine().trim();
                                    out.println(usernameViewing);
                                    if (usernameViewing.isEmpty()) {
                                        System.out.println("Username cannot be empty.");
                                    } else if (!usernamesArray.contains(usernameViewing)) {
                                        //System.out.println("User does not exist.");
                                    } else {
                                        String next;
                                        while (!(next = in.readLine()).equals("END")) {
                                            //System.out.println(next);
                                        }
                                    }
                                    break;

                                case "9":
                                    System.out.println("Logging out...");
                                    soFar = false;
                                    break;

                                default:
                                    System.out.println("Invalid choice. Please try again.");
                                    break;
                            }
                        }
                        break;
                    case "3":
                        System.out.println("Have a nice day!");
                        out.println("terminate");
                        scan.close();
                        socket.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
                ////System.out.println("Thank you, bye!");
                //socket.close();
            }


        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}


