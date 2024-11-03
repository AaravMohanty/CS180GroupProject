import java.io.File;
import java.util.ArrayList; // Change List to ArrayList
import java.util.List;

public interface UserInterface {
    // Gets the username of the user
    String getUsername();

    // Gets the password of the user
    String getPassword();

    // Gets the bio of the user
    String getBio();

    // Sets the bio of the user
    void setBio(String bio);

    // Gets the profile picture filename
    String getPfp();

    // Sets the profile picture filename
    void setPfp(String pfp);

    // Adds a friend to the user's friend list
    boolean addFriend(User friend);

    // Removes a friend from the user's friend list
    boolean removeFriend(String friend);

    // Blocks a user
    boolean blockUser(User user);

    // Unblocks a user
    boolean unblockUser(User user);

    // Checks if a user is blocked
    boolean isBlocked(String user);

    // Gets the list of friends
    List<String> getFriends(); // Changed from List<String> to ArrayList<String>

    // Gets the list of blocked users
    List<String> getBlockedUsers(); // Changed from List<String> to ArrayList<String>

    // Sends a message to another user
    boolean sendMessage(User receiver, String message);

    // Sends a photo to another user
    boolean sendPhoto(User receiver, File photo);

    // Rewrites the contents of a file based on the provided list
    void rewriteToFile(String filename, ArrayList<String> list); // Changed List<String> to ArrayList<String>
}
