/**
 //This interface defines a contract for managing user data in a system.
 //It declares methods for user creation, deletion, retrieval, authentication,
 //and management of additional user properties such as bio, friends, and blocked users.
 */
public interface DatabaseInterface {

    // Creates a new user with the specified username, password, bio, and profile picture.
    boolean createUser(String username, String password, String bio, String pfp);

    // Retrieves the user object associated with the specified username.
    User getUser(String username);

    // Sends a text message from sender to receiver and returns true if successful.
    boolean sendTextMessage(String sender, String receiver, String content);

    // Sends a photo message from sender to receiver using the specified photo path and returns true if successful.
    boolean sendPhotoMessage(String sender, String receiver, String photoPath);

    // Authenticates a user based on the provided username and password.
    boolean authenticate(String username, String password);
}

