import java.util.Scanner; // Import Scanner for user input

/**
 * The  class to run the project
 *
 * Purdue University -- CS18000 -- Fall 2024 
 *
 * @author Elan Smyla, 11
 * @version November 3rd, 2024
 */

// The RunProject class handles user interactions and manages user profiles and messaging.
public class RunProject implements RunProjectInterface {
    private Database database; // Reference to the Database
    private static Scanner scanner; // Scanner for user input
    private User user;

    // Constructor initializes the RunProject with a Database instance.
    public RunProject(Database database) {
        this.database = database; // Set the database reference
        this.scanner = new Scanner(System.in); // Initialize the scanner
    }

    // Method to create a new user account
    public void createAccount() {
        System.out.println("Welcome to ProjectMedia! Please create a new account.");
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        System.out.print("Enter bio: ");
        String bio = scanner.nextLine().trim();

        System.out.print("Enter profile picture filename: ");
        String pfp = scanner.nextLine().trim();

        // Edge case: Check for empty fields
        if (username.isEmpty() || password.isEmpty() || bio.isEmpty() || pfp.isEmpty()) {
            System.out.println("All fields are required. Please try again.");
            return;
        }

        // Attempt to create user account
        if (database.createUser(username, password, bio, pfp)) {
            System.out.println("Account created successfully!");
        } else {
            System.out.println("Username already taken. Please try again.");
        }
    }

    // Method for user login
    public User login() {
        System.out.println("Log In to ProjectMedia");
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        user = database.getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login successful! Welcome back, " + username + "!");
            return user; // Return the logged-in user
        } else {
            System.out.println("Invalid username or password.");
            return null; // Return null if login fails
        }
    }

    // Method to add a friend
    public void addFriend() {
        if (user == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.print("Enter the username of the friend you want to add: ");
        String friendName = scanner.nextLine().trim();
        if (friendName.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }

        User friend = database.getUser(friendName);
        if (friend != null) {
            user.addFriend(friend);
            System.out.println(friendName + " has been added to your friends.");
        } else {
            System.out.println("User not found.");
        }
    }

    // Method to remove a friend
    public void removeFriend() {
        if (user == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.print("Enter the username of the friend you want to remove: ");
        String removeFriendName = scanner.nextLine().trim();
        if (removeFriendName.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }

        if (database.getUser(removeFriendName) != null) {
            user.removeFriend(removeFriendName);
            System.out.println(removeFriendName + " has been removed from your friends.");
        } else {
            System.out.println("User not found.");
        }
    }

    // Method to block or unblock a user
    public void blockUser() {
        if (user == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.print("Enter the username of the user to block: ");
        String blockedUsername = scanner.nextLine().trim();
        User blockedUser = database.getUser(blockedUsername);

        if (blockedUser == null) {
            System.out.println("User not found.");
            return;
        }

        if (user.blockUser(blockedUser)) {
            System.out.println(blockedUsername + " has been blocked.");
        } else {
            System.out.println("User could not be blocked.");
        }

        System.out.print("Enter the username of the user to unblock: ");
        String unblockedUsername = scanner.nextLine().trim();
        User unblockedUser = database.getUser(unblockedUsername);

        if (unblockedUser == null) {
            System.out.println("User not found.");
            return;
        }

        if (user.unblockUser(unblockedUser)) {
            System.out.println(unblockedUsername + " has been unblocked.");
        } else {
            System.out.println("User could not be unblocked.");
        }
    }

    // Method to send a message
    public void sendMessage() {
        if (user == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.print("Enter the username of the receiver: ");
        String receiverUsername = scanner.nextLine().trim();
        User receiver = database.getUser(receiverUsername);

        if (receiver == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.print("Enter your message: ");
        String content = scanner.nextLine().trim();
        if (content.isEmpty()) {
            System.out.println("Message cannot be empty.");
            return;
        }

        if (user.sendMessage(receiver, content)) {
            System.out.println("Message sent!");
        } else {
            System.out.println("Failed to send message.");
        }
    }

    // Method to delete a message
    public void deleteMessage() {
        if (user == null) {
            System.out.println("Please log in first.");
            return;
        }
        System.out.print("Enter the username of the user: ");
        String username = scanner.nextLine().trim();
        User receiver = database.getUser(username);

        if (receiver == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.print("Enter your message: ");
        String content = scanner.nextLine().trim();
        if (content.isEmpty()) {
            System.out.println("Message cannot be empty.");
            return;
        }

        if (user.deleteMessage(receiver, content)) {
            System.out.println("Message deleted!");
        } else {
            System.out.println("Failed to delete message.");
        }

    }

    // Method to send a message
    public void sendPhoto() {
        if (user == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.print("Enter the username of the receiver: ");
        String receiverUsername = scanner.nextLine().trim();
        User receiver = database.getUser(receiverUsername);

        if (receiver == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.print("Enter your photo's filepath: ");
        String content = scanner.nextLine().trim();
        if (content.isEmpty()) {
            System.out.println("Message cannot be empty.");
            return;
        }

        if (user.sendPhoto(receiver, content)) {
            System.out.println("Message sent!");
        } else {
            System.out.println("Failed to send Photo.");
        }
    }

    // Method to view user profile
    public void viewUserProfile() {
        if (user == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.print("Enter the username of the profile you want to view: ");
        String usernameToView = scanner.nextLine().trim();
        User profileUser = database.getUser(usernameToView);

        if (profileUser != null) {
            System.out.println(profileUser.displayUser());
        } else {
            System.out.println("User not found.");
        }
    }

    // Method to display available users and view a selected profile
    public void search() {
        if (user == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.println("Available users:");
        for (User user : database.getUsers()) {
            System.out.println("- " + user.getUsername());
        }

        System.out.print("Enter the username of the profile to view: ");
        String usernameToView = scanner.nextLine().trim();
        User profileUser = database.getUser(usernameToView);

        if (profileUser != null) {
            viewUserProfile(profileUser); // View the selected profile
        } else {
            System.out.println("User not found.");
        }
    }

    // Method to view a specific user's profile
    public void viewUserProfile(User user) {
        if (user != null && database.getUsers().contains(user)) {
            System.out.println(user.displayUser());
        } else {
            System.out.println("User not found.");
        }
    }

    public static void main(String[] args) {
        // Initialize the database and RunProject instance
        Database database = new Database();
        RunProject runProject = new RunProject(database);

        // Main loop for the primary menu
        while (true) {
            // Display main menu options
            System.out.println("Main Menu:");
            System.out.println("1. Create a New User");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1, 2, or 3): ");

            // Get user input for main menu choice
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    // Option to create a new user account
                    runProject.createAccount();
                    break;

                case "2":
                    // Option to log in to an existing account
                    User loggedInUser = runProject.login();

                    // If login is successful, show user-specific menu
                    if (loggedInUser != null) {
                        // Loop for the user menu, available after logging in
                        while (true) {
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
                            String userChoice = scanner.nextLine().trim();

                            switch (userChoice) {
                                case "1":
                                    // Add a friend
                                    runProject.addFriend();
                                    break;
                                case "2":
                                    // Remove a friend
                                    runProject.removeFriend();
                                    break;
                                case "3":
                                    // Block or unblock a user
                                    runProject.blockUser();
                                    break;
                                case "4":
                                    // Send a message to another user
                                    runProject.sendMessage();
                                    break;
                                case "5":
                                    // Send a photo to another user
                                    runProject.sendPhoto();
                                    break;
                                case "6":
                                    // Delete a message with another user
                                    runProject.deleteMessage();
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
                    }
                    break;

                case "3":
                    // Exit the program
                    System.out.println("Have a nice day!");
                    scanner.close(); // Close the scanner before exiting
                    return;

                default:
                    // Handle invalid main menu choices
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

}
