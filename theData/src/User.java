import java.util.ArrayList; // Import ArrayList for managing friends and blocked users
import java.util.List;      // Import List interface for return types

// The User class implements UserInterface and represents a user in the social network.
public class User implements UserInterface {
    private String username;              // The user's username
    private String password;              // The user's password
    private String email;                 // The user's email address
    private String bio;                   // The user's biography
    private ArrayList<User> friends;      // List of the user's friends
    private ArrayList<User> blockedUsers; // List of users blocked by this user

    // Constructor to initialize a new User object with provided details
    public User(String username, String password, String email, String bio) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.bio = bio;
        this.friends = new ArrayList<>();       // Initialize friends list
        this.blockedUsers = new ArrayList<>();  // Initialize blocked users list
    }

    // Returns the username of the user
    public String getUsername() {
        return username;
    }

    // Returns the password of the user
    public String getPassword() {
        return password;
    }

    // Returns the email address of the user
    public String getEmail() {
        return email;
    }

    // Returns the biography of the user
    public String getBio() {
        return bio;
    }

    // Sets a new biography for the user
    public void setBio(String bio) {
        this.bio = bio; // Update the user's biography
    }

    // Adds a friend to the user's friend list
    public boolean addFriend(User friend) {
        // Check if the user is not already a friend
        if (!friends.contains(friend)) {
            friends.add(friend);  // Add the friend to the list
            return true;          // Friend added successfully
        }
        return false;  // Already a friend, addition failed
    }

    // Removes a friend from the user's friend list
    public boolean removeFriend(User friend) {
        return friends.remove(friend);  // Remove the friend if present
    }

    // Blocks a user, preventing them from interacting with this user
    public boolean blockUser(User user) {
        // Check if the user is not already blocked
        if (!blockedUsers.contains(user)) {
            blockedUsers.add(user);  // Add the user to the blocked list
            return true;             // User blocked successfully
        }
        return false;  // Already blocked, action failed
    }

    // Unblocks a previously blocked user
    public boolean unblockUser(User user) {
        return blockedUsers.remove(user);  // Remove the user from the blocked list if present
    }

    // Checks if a specific user is blocked by this user
    public boolean isBlocked(User user) {
        return blockedUsers.contains(user);  // Return true if blocked, false otherwise
    }

    // Returns a list of the user's friends
    public List<User> getFriends() {
        return new ArrayList<>(friends); // Return a copy of the friends list
    }

    // Returns a list of users blocked by this user
    public List<User> getBlockedUsers() {
        return new ArrayList<>(blockedUsers); // Return a copy of the blocked users list
    }
}
