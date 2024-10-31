import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList; // Import ArrayList for storing friends and blocked users
import java.util.List; // Import List interface for returning lists

// The User class represents a user in the system, with properties for user details and relationships.
public class User implements UserInterface{
    private String username; // The user's unique username
    private String password; // The user's password
    private String bio; // A short biography or description of the user
    private ArrayList<User> friends; // List of the user's friends
    private ArrayList<User> blockedUsers; // List of users that this user has blocked
    private ArrayList<User> conversations; // All conversations with other
    private String friendsFileName; // File with all friends
    private String blockedUsersFileName; //File with all blocked users
    private String conversationsFileName; //File with all conversations

    // Constructor initializes user properties and creates empty lists for friends and blocked users
    public User(String username, String password, String email, String bio) {
        this.username = username; // Set the username
        this.password = password; // Set the password
        this.bio = bio; // Set the bio
        this.friends = new ArrayList<>(); // Initialize the friends list
        this.blockedUsers = new ArrayList<>(); // Initialize the blocked users list
        this.conversations = new ArrayList<>(); // Initialize the conversations list
        friendsFileName = username + "friends.txt"; // Create File name for friends
        File friendsFile = new File(friendsFileName); // Create File friends
        blockedUsersFileName = username + "blockedUsers.txt"; // Create File name for blocked users
        File blockedUsersFile = new File(blockedUsersFileName); // Create file blocked users
        conversationsFileName = username + "conversations.txt"; // Create File name for convos
        File conversationsFile = new File(conversationsFileName); // Create File convos

    }

    // Getter for the username
    public String getUsername() {
        return username;
    }

    // Getter for the password
    public String getPassword() {
        return password;
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
            writeToFile(friendsFileName, friends); // write the friend's username to file
            return true; // Indicate success
        }
        return false; // Indicate failure (friend already exists)
    }

    // Removes a friend from the user's friends list
    public boolean removeFriend(User friend) {
        boolean removed  = friends.remove(friend); // boolean to store whether friend was removed or not
        if (removed) {
            writeToFile(friendsFileName, friends); // if it was removed then rewrite the friends file
        }
        return removed; // Remove the friend and return the result
    }

    // Blocks a user, preventing them from interacting with this user
    public boolean blockUser(User user) {
        if (!blockedUsers.contains(user)) { // Check if the user is not already blocked
            blockedUsers.add(user); // Add the user to the blocked list
            writeToFile(blockedUsersFileName, blockedUsers); // write the blocked user to blocked list file
            return true; // Indicate success
        }
        return false; // Indicate failure (user already blocked)
    }

    // Unblocks a user, allowing them to interact with this user again
    public boolean unblockUser(User user) {
        boolean unblocked = blockedUsers.remove(user); // boolean to store whether friend was unblocked or not
        if (unblocked) {
            writeToFile(blockedUsersFileName, blockedUsers); // if friend was removed then rewrite the friends file
        }
        return unblocked; // Remove the user from the blocked list and return the result
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

    // method to send messages and write that to the convo file
    public void sendMessage(User receiver, String message) {
        Message messages = new Message(this.username, receiver.getUsername(), message); //create new message
        messages.writeMessageToFile(conversationsFileName); //store message in convo file
    }

    // method to send photos and write to the convo file
    public void sendPhoto(User receiver, File photo) {
        Message messages = new Message(this.username, receiver.getUsername(), photo); //create new photo message
        messages.writeMessageToFile(conversationsFileName); //store new photo message in convo file
    }

    // Method to add any info to a specific file.
    // Used to write contents of the arraylists into the specific files
    public void writeToFile(String filename, ArrayList<User> list) {
        // made append false so that it updates whenever someone removes someone from their list as well
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))){
            for (User user : list) {
                writer.write(user.getUsername());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
