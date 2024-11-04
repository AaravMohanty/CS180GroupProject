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
        Database database = new Database();
        RunProject runProject = new RunProject(database);

        // Example usage of the program
        runProject.createAccount(); // Create a new account
        User loggedInUser = runProject.login(); // Log in the user

        if (loggedInUser != null) {
            runProject.addFriend(); // Add a friend
            runProject.sendMessage(); // Send a message
            runProject.blockUser(); // Block a user
            runProject.search(); // Search and view user profiles
        }
    }
}
