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
     private static boolean loggedin;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in); // Initialize the scanner
        loggedin = false;

        try {
            Socket socket = new Socket("localhost", 1234);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                System.out.println("Main Menu:");
                System.out.println("1. Create a New User");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice (1, 2, or 3): ");

                String choice = scan.nextLine().trim();
                out.println(choice); //matches
                if (choice == null) {
                    break;
                }
                //System.out.println(choice);

                switch (choice) {
                    case "1":
                        if(createNewAcc(scan, in, out) == 1){
                            break;
                        }

                    case "2":
                        //loggedin = false;
                        while (!loggedin) {
                           logIn(scan, in, out);
                        } //gui -> make back button so can cancel & break

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
                                    if(!addFriend(scan, in, out)) {
                                        break;
                                    };
                                    break;

                                case "2":
                                    if(!removeFriend(scan, in, out)) {
                                        break;
                                    };
                                    break;

                                case "3":
                                    System.out.println("Enter 1 to block a user or 2 to unblock a user:");
                                    String userChoice2 = scan.nextLine().trim();
                                    out.println(userChoice2);

                                    switch (userChoice2) {
                                        case "1":
                                            if(!blockUser(scan, in, out)) {
                                                break;
                                            };
                                            break;

                                        case "2":
                                            if(!unBlockUser(scan, in, out)) {
                                                break;
                                            };
                                            break;

                                        default:
                                            System.out.println("Invalid choice. Please enter 1 or 2.");
                                            break;
                                    }
                                    break;


                                case "4":
                                    if(!sendMessage(scan, in, out)) {
                                        break;
                                    }
                                    break;

                                case "5":
                                    if(!sendPhotoMsg(scan, in, out)) {
                                        break;
                                    }
                                    break;

                                case "6":
                                    if(!deleteMsg(scan, in, out)) {
                                        break;
                                    }
                                    break;


                                case "7":
                                    if(!viewProfile(scan, in, out)) {
                                        break;
                                    }
                                    break;


                                case "8":
                                    if(!searchUsers(scan, in, out)) {
                                        break;
                                    }
                                    break;


                                case "9":
                                    System.out.println("Logging out...");
                                    loggedin = false;
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


    public static int createNewAcc(Scanner scan, BufferedReader in, PrintWriter out) {
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
            System.out.println("All fields are required. Please try again.");
            return 1; // failed return to beginning to reprompy
        }
        try {
            String successOrFailure = in.readLine();
            System.out.println(successOrFailure);
            while (true) {
                if (successOrFailure.equals("success")) {
                    System.out.println("Account created successfully!\n");
                    return 1;  //or just return
                } else {
                    System.out.println("Username already taken.\nPlease re-run the program to try again.");
                    return 1;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
            return 1;
        }


    public static void logIn(Scanner scan, BufferedReader in, PrintWriter out){
        System.out.println("Log In to ProjectMedia");
        System.out.print("Enter username: ");
        String userInputUsername = scan.nextLine().trim();
        out.println(userInputUsername);

        System.out.print("Enter password: ");
        String userInputPassword = scan.nextLine().trim();
        out.println(userInputPassword);

        try{
            String newLine = in.readLine();
            if (newLine.equals("success")) {
               // String result = in.readLine();
                //System.out.println(result);
                loggedin = true;
            } else {
                System.out.println("Invalid username or password. Try again");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean addFriend(Scanner scan, BufferedReader in, PrintWriter out){
        System.out.print("Enter the username of the friend you want to add: ");
        try {
            String friendName = scan.nextLine().trim();

            if (friendName.isEmpty()) {
                System.out.println("Username cannot be empty.");
                return false;
            }
            out.println(friendName);

           String testLine = in.readLine();
            if(testLine.equals("user is null")){
                System.out.println("User cant be null.");
                return false;
            }




            String result = in.readLine();
            //System.out.print(result);
            if (result.equals("success")) {
                System.out.println(friendName + " has been added to your friends!");
            }
            else if (result.equals("already friends")){
                System.out.println(friendName + " is already friends!");
            }
            else if(result.equals("unblock first")){
                System.out.println("Be sure to unblock the user first. They are currently blocked.");
            }
            else {
                System.out.println("User not found");
            }
        } catch (Exception e) {
            e.printStackTrace(); //when official gui make sure "ur server crashed"
        }
        return false;
    }
    public static boolean removeFriend(Scanner scan, BufferedReader in, PrintWriter out) {
        System.out.print("Enter the username of the friend you want to remove: ");
        String removeFriendName = scan.nextLine().trim();

        if (removeFriendName.isEmpty()) {
            //System.out.println("Username cannot be empty.");
            return false;
        }
        out.println(removeFriendName);

        try {
            String readingLine = in.readLine();
            //System.out.print(readingLine);
            if (readingLine.equals("success")) {
                System.out.println(removeFriendName + " has been removed to your friends!");
            }
            else if(readingLine.equals("not already friends")){
                System.out.println("User was not already your friend");
            }else if(readingLine.equals("user is null")){
                System.out.println(" User does not exist");
            }
            else {
                System.out.println("User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean blockUser(Scanner scan, BufferedReader in, PrintWriter out){
        System.out.print("Enter the username of the user to block: ");
        String blockedUsername = scan.nextLine().trim();

        while (blockedUsername.isEmpty()) {
           // System.out.println("Username cannot be empty.");
          //  System.out.print("Enter the username of the user to block: ");
          //  blockedUsername = scan.nextLine().trim();
            return false;
        }

        out.println(blockedUsername);
        try {
            String blockResponse = in.readLine();

            if (blockResponse.equals("success")) {
                System.out.println(blockedUsername + " has been blocked.");
            } else if(blockResponse.equals("already blocked")){
                System.out.println("User was already blocked");
            }

            else {
                System.out.println(blockResponse);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static boolean unBlockUser(Scanner scan, BufferedReader in, PrintWriter out){
        System.out.print("Enter the username of the user to unblock: ");
        String unblockedUsername = scan.nextLine().trim();

        while (unblockedUsername.isEmpty()) {
            System.out.println("Username cannot be empty.");
           // System.out.print("Enter the username of the user to unblock: ");
           // unblockedUsername = scan.nextLine().trim();
            return false;
        }

        out.println(unblockedUsername);
        try {
            String unblockResponse = in.readLine();

            if (unblockResponse.equals("success")) {
                System.out.println(unblockedUsername + " has been unblocked.");
            }
            else if(unblockResponse.equals("already unblocked")){
                System.out.println("user is not blocked");
            }else {
                System.out.println("User not found or could not be unblocked.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean sendMessage(Scanner scan, BufferedReader in, PrintWriter out){
        System.out.print("Enter the username of the receiver: ");
        String receiverUsername = scan.nextLine().trim();
        out.println(receiverUsername);
        if (receiverUsername.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return false;
        }

        try {
           String resu = in.readLine();
           if (resu.equals("User not found.")) {
                System.out.println("User not found.");
                return false;
            } //potential hazard


            System.out.print("Enter your message: ");
            String content = scan.nextLine().trim();
            out.println(content);
            if (content.isEmpty()) {
                System.out.println("Message cannot be empty.");
                return false;
            }


            String sent = in.readLine();
            if (sent.equals("success")) {
                System.out.println("Message sent!");
            } else {
                System.out.println("Failed to send message. Ensure you are friends with " + receiverUsername + " and " + receiverUsername + " is your friend");
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean sendPhotoMsg(Scanner scan, BufferedReader in, PrintWriter out){
        System.out.print("Enter the username of the receiver: ");
        String receiveUsername = scan.nextLine().trim();
        out.println(receiveUsername);
        if (receiveUsername.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return false;
        }
        try{
        String newResult = in.readLine();
        if (newResult.equals("User not found.")) {
            System.out.println("User not found.");
            return false;
        }

        System.out.print("Enter your photo's filepath: ");
        String contentThree = scan.nextLine().trim();

        out.println(contentThree);
        if (contentThree.isEmpty()) {
            System.out.println("Message cannot be empty.");
            return false;
        }
        //try {
            String sentPhoto = in.readLine();
            if (sentPhoto.equals("success")) {
                System.out.println("Message sent!");
            } else {
                System.out.println("Failed to send Photo.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteMsg(Scanner scan, BufferedReader in, PrintWriter out){
        System.out.print("Enter the username of the user: ");
        String newUsername = scan.nextLine().trim();
        out.println(newUsername);
        if (newUsername.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return false;
        }
        try {

            String responsez = in.readLine();
            if (responsez.equals("empty")) {
                System.out.println("User cannot be null");
            }
        }catch(IOException e){
            e.printStackTrace();
        }


        System.out.print("Enter your message: ");
        String newContent = scan.nextLine().trim();
        out.println(newContent);
        if (newContent.isEmpty()) {
            System.out.println("Message cannot be empty.");
            return false;
        }
        try {
            String deleted = in.readLine();
            if (deleted.equals("success")) {
                System.out.println("Message deleted!");
            } else {
                System.out.println("Failed to delete message.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean viewProfile(Scanner scan, BufferedReader in, PrintWriter out){
        System.out.print("Enter the username of the profile you want to view: ");
        String usernameToView = scan.nextLine().trim();
        out.println(usernameToView);
        try {
            String isProfileUserValid = in.readLine();
            if (isProfileUserValid.equals("success")) {
                String next;
                while (!(next = in.readLine()).equals("END")) {
                    System.out.println(next);
                }
            } else {
                System.out.println("User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean searchUsers(Scanner scan, BufferedReader in, PrintWriter out){
        try{
        String allClear = in.readLine();
        System.out.println(allClear);
        if(allClear.equals("Please log in first.")){
            return false;
        }
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("Available users:");
        ArrayList<String> usernamesArray = new ArrayList<>();
        String userNames;
        try {
            userNames = in.readLine();
            while (!(userNames).equals("END")) {
                usernamesArray.add(userNames);
                System.out.println("- " + userNames);
                userNames = in.readLine();
            }
            System.out.print("Enter the username of the profile to view: ");
            String usernameViewing = scan.nextLine().trim();
            out.println(usernameViewing);
            if (usernameViewing.isEmpty()) {
                System.out.println("Username cannot be empty.");
                return false;
            }
            else {
                /*String next;
                while (!(next = in.readLine()).equals("END")) {
                    System.out.println(next);
                }
                return false;
                */
                String next = in.readLine();

                if(next.equals("end")){
                    String nextLine = in.readLine();
                    System.out.println(nextLine);
                     nextLine = in.readLine();
                    System.out.println(nextLine);
                     nextLine = in.readLine();
                    System.out.println(nextLine);
                }
                else {

                    System.out.println("No user found");
                }
                 return false;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
