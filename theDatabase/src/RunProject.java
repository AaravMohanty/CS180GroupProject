import java.util.Scanner; // Import Scanner for user input

// The RunProject class handles user interactions and manages user profiles and messaging.
public class RunProject {
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
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter bio: ");
        String bio = scanner.nextLine();
        System.out.print("Enter profile picture filename: ");
        String pfp = scanner.nextLine();

        if (database.createUser(username, password, bio, pfp)) {
            System.out.println("Account created successfully!");
        } else {
            System.out.println("Username already taken. Please try again.");
        }
    }

    // Method for user login
    public User login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        user = database.getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login successful!");
            return user; // Return the logged-in user
        } else {
            System.out.println("Invalid username or password.");
            return null; // Return null if login fails
        }
    }

    // Method to add a friend
    public void addFriend() {
        System.out.println(user.getUsername() + ": Who do you want to add as a friend? ");
        String friendName = scanner.nextLine();
        if (database.getUser(friendName) != null) {
            user.addFriend(database.getUser(friendName));
            System.out.println(friendName + " has been added to your friends.");
        } else {
            System.out.println("User not found.");
        }
    }

    // Method to block a user
    public void blockUser() {
        System.out.print("Enter the username of the user to block: ");
        String blockedUsername = scanner.nextLine();
        user.blockUser(database.getUser(blockedUsername)); // Assuming blockUser method exists in User class
        System.out.println(blockedUsername + " has been blocked.");

        System.out.print("Enter the username of the user to unblock: ");
        String unblockedUsername = scanner.nextLine();
        user.unblockUser(database.getUser(unblockedUsername));
        System.out.println(blockedUsername + " has been unblocked.");
    }

    // Method to send a message
    public void sendMessage() {
        System.out.print("Enter the username of the receiver: ");
        String receiverUsername = scanner.nextLine();
        User receiver = database.getUser(receiverUsername);
        if (receiver != null) {
            System.out.print("Enter your message: ");
            String content = scanner.nextLine();
            if (user.sendMessage(receiver, content)) {
                System.out.println("Message sent!");
            } else {
                System.out.println("Failed to send message.");
            }
        } else {
            System.out.println("User not found.");
        }
    }

    // Method to view user profile
    public void viewUserProfile(User user) {
        System.out.print("Enter the username to view: ");
        String usernameToView = scanner.nextLine();
        User profileUser = database.getUser(usernameToView);
        if (profileUser != null) {
            System.out.println("Username: " + profileUser.getUsername());
            System.out.println("Bio: " + profileUser.getBio());
            // Add more user details as needed
        } else {
            System.out.println("User not found.");
        }
    }

    public static void main(String[] args) {
        Database database = new Database(); // Create a new Database instance
        RunProject RunProject = new RunProject(database); // Create RunProject with the database

        // Example usage
       // RunProject.createAccount(); // Create a new account
        User loggedInUser = RunProject.login(); // Log in the user

        if (loggedInUser != null) {
            //RunProject.addFriend();
            RunProject.sendMessage(); // Send a message
            RunProject.blockUser();
        }
    }

}
