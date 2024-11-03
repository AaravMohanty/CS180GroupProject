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
        if (user.blockUser(database.getUser(blockedUsername))) { // Assuming blockUser method exists in User class
            System.out.println(blockedUsername + " has been blocked.");
        } else {
            System.out.println("User not found.");
        }

        System.out.print("Enter the username of the user to unblock: ");
        String unblockedUsername = scanner.nextLine();
        if (user.unblockUser(database.getUser(unblockedUsername))) {
            System.out.println(blockedUsername + " has been unblocked.");
        } else {
            System.out.println("User not found.");
        }
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
    public void viewUserProfile() {
        System.out.print("Enter the username to view: ");
        String usernameToView = scanner.nextLine();
        User profileUser = database.getUser(usernameToView);
        if (profileUser != null && database.users.contains(database.getUser(usernameToView))) {
            System.out.println(profileUser.displayUser());
        } else {
            System.out.println("User not found.");
        }
    }

    //view specific user profile
    public void viewUserProfile(User user) {
        if (user != null && database.users.contains(user)) {
            System.out.println(user.displayUser());
        } else {
            System.out.println("User not found.");
        }
    }


    public static void main(String[] args) {
        Database database = new Database(); // Create a new Database instance
        RunProject RunProject = new RunProject(database); // Create RunProject with the database

        // Example usage
        System.out.println("hi");
        RunProject.createAccount(); // Create a new account
        User loggedInUser = RunProject.login(); // Log in the user

        if (loggedInUser != null) {
            RunProject.addFriend();
            RunProject.sendMessage(); // Send a message
            RunProject.blockUser();
            RunProject.viewUserProfile();
            //task 1:
//            RunProject.removeFriend(); make this method here and test removing a friend
            //task 2:
//            RunProject.search(); displays all the users so you can
//            look through them and when one is selected, call viewUserProfile();
//            At this point if you feel we have the necessary methods and features for the
//            project you can push and let Hannah and Kai make the interfaces and test classes
            //task 3:
//            Make all applicable methods handle edge cases and all errors.
//            You can test this by running the code above after creating the other features
//            specified in task 2 here and just testing a bunch of different inputs. no need
//            for test cases and classes for this. (this way we know the code works before
//            they possibly screw up the test cases
            //task 4:
//            Make all methods and classes thread safe where they need to be
//            (idk exactly where you need to do this but we are accessing a
//            few static variable a lot and in different classes. Also for
//            writing and reading the messages between users etc... just do it
//            everywhere you think its necessary)
        }
    }

}
