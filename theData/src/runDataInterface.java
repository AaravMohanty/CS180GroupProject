import java.util.ArrayList;

// The runDatabase interface defines methods for saving and loading user and message data.
public interface runDataInterface {

    // Saves a user to the database
    void saveUser(User user);

    // Loads a user from the database by username
    User loadUser(String username);

    // Saves a message to the database
    void saveMessage(Message message);

    // Loads all messages associated with a specific username
    ArrayList<Message> loadMessages(String username);
}
