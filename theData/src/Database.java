import java.util.ArrayList;

// The Database class implements the DataInterface for managing users and messages.
public class Database implements DataInterface {

    private ArrayList<User> users;       // List to store all users
    private ArrayList<Message> messages;  // List to store all messages

    // Constructor to initialize the lists for users and messages
    public Database() {
        users = new ArrayList<>();  // Initialize the user list
        messages = new ArrayList<>(); // Initialize the message list
    }

    // Creates a new user and adds them to the user list
    public boolean createUser(String username, String password, String email, String bio) {
        // Check if the username is already taken
        if (searchUser(username) == null) {
            // Add a new User object to the list
            users.add(new User(username, password, email, bio));
            return true;  // User created successfully
        }
        return false;  // User creation failed due to existing username
    }

    // Searches for a user by their username
    public User searchUser(String username) {
        // Loop through users to find the one with the matching username
        for (User user : users) {
            if (user != null && user.getUsername().equals(username)) {
                return user;  // Return the found user
            }
        }
        return null;  // No user found
    }

    // Sends a message from one user to another
    public boolean sendMessage(String sender, String receiver, String content) {
        User senderUser = searchUser(sender);     // Find the sender
        User receiverUser = searchUser(receiver); // Find the receiver

        // Check if both users exist and if the sender is not blocked
        if (senderUser != null && receiverUser != null && !receiverUser.isBlocked(senderUser)) {
            // Create and add the message to the list
            messages.add(new Message(sender, receiver, content));
            return true;  // Message sent successfully
        }
        return false;  // Message sending failed
    }

    // Retrieves all messages exchanged between two users
    public ArrayList<Message> getMessages(String sender, String receiver) {
        ArrayList<Message> result = new ArrayList<>(); // List to hold messages between users

        // Loop through messages to find those sent between the specified users
        for (Message message : messages) {
            if (message.getSender().equals(sender) && message.getReceiver().equals(receiver)) {
                result.add(message);  // Add the message to the results
            }
        }
        return result;  // Return the list of messages
    }

    public User createUser(String username, String password, String email) {
        // Logic to create user would go here
        return null;
    }

    public boolean deleteUser(String username) {
        // Logic to delete user would go here
        return false;
    }

    public User getUser(String username) {
        // Logic to return the user with name username would go here
        return null; //placeholder bceause red underline is annoying
    }

    public boolean authenticate(String username, String password) {
        // Logic to authenticate the client would go here
        return false;
    }
}