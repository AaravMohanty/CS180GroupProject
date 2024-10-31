import java.io.BufferedWriter;
import java.io.File; // Import File class for handling file paths
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList; // Import ArrayList for storing users and messages

// The Database class manages users and messages in a simple in-memory storage system.
public class Database implements DataInterface{
    private ArrayList<User> users; // List to store User objects
    private ArrayList<Message> messages; // List to store Message objects
    private static final String DATABASE_FILE = "database.txt"; // Initializing the database file

    // Constructor initializes the users and messages lists.
    public Database() {
        users = new ArrayList<>(); // Initialize the users list
        messages = new ArrayList<>(); // Initialize the messages list
    }

    // Creates a new user if the username is not already taken
    public boolean createUser(String username, String password, String email, String bio) {
        if (getUser(username) == null) { // Check if the username is available
            users.add(new User(username, password, email, bio)); // Add new user
            return true; // Indicate success
        }
        return false; // Indicate failure (username already taken)
    }

    // Searches for a user by username
    public User getUser(String username) {
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
        User senderUser = getUser(sender); // Find sender
        User receiverUser = getUser(receiver); // Find receiver

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
        User user = getUser(username); // Find the user
        if (user != null) { // Check if user exists
            users.remove(user); // Remove the user from the list
            return true; // Indicate success
        }
        return false; // Indicate failure (user not found)
    }

    // Authenticates a user by checking username and password
    public boolean authenticate(String username, String password) {
        User user = getUser(username); // Find the user
        return user != null && user.getPassword().equals(password); // Check if password matches
    }

    // Method to add all info into database.txt
    public void writeToDatabase() throws IOException {
        writeUsersToDatabase();
    }

    // Method to add all user info into database.txt
    public void writeUsersToDatabase() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATABASE_FILE))) {
            for (User user : users) {
                String userEntry = String.format("%s, %s, %s, %s, %s, %s",
                        user.getUsername(),
                        user.getPassword(),
                        user.getBio(),
                        user.getUsername() + "Friends.txt",
                        user.getUsername() + "BlockedUsers.txt",
                        user.getUsername() + "Conversations.txt");
                writer.write(userEntry);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to write message content to the specified conversation file
    // Write all messages to the messages file
    //TODO
    public void writeMessagesToDatabase(String conversation) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter())) {
            for (Message message : messages) {
                String messageEntry;
                if (message.isPhotoMessage()) {
                    messageEntry = String.format("Sender: %s, Receiver: %s, Photo: %s",
                            message.getSender(), message.getReceiver(), message.getPhoto().getPath());
                } else {
                    messageEntry = String.format("Sender: %s, Receiver: %s, Content: %s",
                            message.getSender(), message.getReceiver(), message.getContent());
                }
                writer.write(messageEntry);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
