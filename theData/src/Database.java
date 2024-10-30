import java.io.File; // Import File class for handling file paths
import java.util.ArrayList; // Import ArrayList for storing users and messages

// The Database class manages users and messages in a simple in-memory storage system.
public class Database {
    private ArrayList<User> users; // List to store User objects
    private ArrayList<Message> messages; // List to store Message objects

    // Constructor initializes the users and messages lists.
    public Database() {
        users = new ArrayList<>(); // Initialize the users list
        messages = new ArrayList<>(); // Initialize the messages list
    }

    // Creates a new user if the username is not already taken
    public boolean createUser(String username, String password, String email, String bio) {
        if (searchUser(username) == null) { // Check if the username is available
            users.add(new User(username, password, email, bio)); // Add new user
            return true; // Indicate success
        }
        return false; // Indicate failure (username already taken)
    }

    // Searches for a user by username
    public User searchUser(String username) {
        for (User user : users) { // Iterate through the user list
            if (user != null && user.getUsername().equals(username)) { // Check if username matches
                return user; // Return the found user
            }
        }
        return null; // Return null if no user is found
    }

    // Sends a text message from sender to receiver
    public boolean sendTextMessage(String sender, String receiver, String content) {
        return sendMessageInternal(sender, receiver, new Message(sender, receiver, content)); // Delegate to internal method
    }

    // Sends a photo message from sender to receiver
    public boolean sendPhotoMessage(String sender, String receiver, String photoPath) {
        File photo = new File(photoPath); // Create a File object for the photo
        if (!photo.exists()) { // Check if the photo file exists
            System.out.println("Photo file not found: " + photoPath); // Log error
            return false; // Indicate failure
        }
        return sendMessageInternal(sender, receiver, new Message(sender, receiver, photo)); // Delegate to internal method
    }

    // Internal method to handle message sending logic
    private boolean sendMessageInternal(String sender, String receiver, Message message) {
        User senderUser = searchUser(sender); // Find sender
        User receiverUser = searchUser(receiver); // Find receiver

        // Check if both users exist and if the receiver has not blocked the sender
        if (senderUser != null && receiverUser != null && !receiverUser.isBlocked(senderUser)) {
            messages.add(message); // Add the message to the list
            return true; // Indicate success
        }
        return false; // Indicate failure (either user not found or blocked)
    }

    // Retrieves messages exchanged between a specific sender and receiver
    public ArrayList<Message> getMessages(String sender, String receiver) {
        ArrayList<Message> result = new ArrayList<>(); // List to hold result messages
        for (Message message : messages) { // Iterate through messages
            // Check if the message matches the sender and receiver
            if (message.getSender().equals(sender) && message.getReceiver().equals(receiver)) {
                result.add(message); // Add to result list
            }
        }
        return result; // Return the list of messages
    }

    // Deletes a user by username
    public boolean deleteUser(String username) {
        User user = searchUser(username); // Find the user
        if (user != null) { // Check if user exists
            users.remove(user); // Remove the user from the list
            return true; // Indicate success
        }
        return false; // Indicate failure (user not found)
    }

    // Authenticates a user by checking username and password
    public boolean authenticate(String username, String password) {
        User user = searchUser(username); // Find the user
        return user != null && user.getPassword().equals(password); // Check if password matches
    }
}
