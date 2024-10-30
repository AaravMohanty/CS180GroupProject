import java.util.ArrayList; // Import ArrayList for storing friends and blocked users
import java.util.List; // Import List interface for returning lists

// The User class represents a user in the system, with properties for user details and relationships.
public class User {
    private String username; // The user's unique username
    private String password; // The user's password
    private String email; // The user's email address
    private String bio; // A short biography or description of the user
    private ArrayList<User> friends; // List of the user's friends
    private ArrayList<User> blockedUsers; // List of users that this user has blocked

    // Constructor initializes user properties and creates empty lists for friends and blocked users
    public User(String username, String password, String email, String bio) {
        this.username = username; // Set the username
        this.password = password; // Set the password
        this.email = email; // Set the email
        this.bio = bio; // Set the bio
        this.friends = new ArrayList<>(); // Initialize the friends list
        this.blockedUsers = new ArrayList<>(); // Initialize the blocked users list
    }

    // Getter for the username
    public String getUsername() {
        return username;
    }

    // Getter for the password
    public String getPassword() {
        return password;
    }

    // Getter for the email
    public String getEmail() {
        return email;
    }

    // Getter for the bio
    public String getBio() {
        return bio;
    }

    // Setter for the bio
    public void setBio(String bio) {
        this.bio = bio; // Update the bio
    }

    // Adds a friend to the user's friends list
    public boolean addFriend(User friend) {
        if (!friends.contains(friend)) { // Check if the friend is not already in the list
            friends.add(friend); // Add the friend
            return true; // Indicate success
        }
        return false; // Indicate failure (friend already exists)
    }

    // Removes a friend from the user's friends list
    public boolean removeFriend(User friend) {
        return friends.remove(friend); // Remove the friend and return the result
    }

    // Blocks a user, preventing them from interacting with this user
    public boolean blockUser(User user) {
        if (!blockedUsers.contains(user)) { // Check if the user is not already blocked
            blockedUsers.add(user); // Add the user to the blocked list
            return true; // Indicate success
        }
        return false; // Indicate failure (user already blocked)
    }

    // Unblocks a user, allowing them to interact with this user again
    public boolean unblockUser(User user) {
        return blockedUsers.remove(user); // Remove the user from the blocked list and return the result
    }

    // Checks if a user is blocked by this user
    public boolean isBlocked(User user) {
        return blockedUsers.contains(user); // Return true if the user is in the blocked list
    }

    // Returns a list of the user's friends
    public List<User> getFriends() {
        return new ArrayList<>(friends); // Return a copy of the friends list
    }

    // Returns a list of users that this user has blocked
    public List<User> getBlockedUsers() {
        return new ArrayList<>(blockedUsers); // Return a copy of the blocked users list
    }
}
