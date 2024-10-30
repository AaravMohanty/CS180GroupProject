import java.util.ArrayList;

public class Database implements DataInterface {
    private ArrayList<User> users;  // List of all users
    private ArrayList<Message> messages;  // List of all messages

    public SocialNetwork() {
        users = new ArrayList<>();
        messages = new ArrayList<>();
    }

    // Create a new user and add to the list
    public boolean createUser(String username, String password, String email, String bio) {
        if (searchUser(username) == null) {  // Ensure username is unique
            users.add(new User(username, password, email, bio));
            return true;
        }
        return false;  // Username already exists
    }

    // Search for a user by username
    public User searchUser(String username) {
        for (User user : users) {
            if (user != null && user.username.equals(username)) {
                return user;
            }
        }
        return null;  // User not found
    }

    // Send a message between users
    public boolean sendMessage(String sender, String receiver, String content) {
        User senderUser = searchUser(sender);
        User receiverUser = searchUser(receiver);
        if (senderUser != null && receiverUser != null && !receiverUser.isBlocked(senderUser)) {
            messages.add(new Message(sender, receiver, content));
            return true;
        }
        return false;  // Either user not found or sender is blocked
    }

    // Get all messages between two users
    public ArrayList<Message> getMessages(String sender, String receiver) {
        ArrayList<Message> result = new ArrayList<>();
        for (Message message : messages) {
            if (message.getSender().equals(sender) && message.getReceiver().equals(receiver)) {
                result.add(message);
            }
        }
        return result;
    }
}

