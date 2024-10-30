import java.util.List;

// The UserInterface defines the methods for managing user data and interactions.
public interface UserInterface {

    // Returns the username of the user
    String getUsername();

    // Returns the password of the user
    String getPassword();

    // Returns the email address of the user
    String getEmail();

    // Returns the biography of the user
    String getBio();

    // Sets a new biography for the user
    void setBio(String bio);

    // Adds a friend to the user's friend list
    boolean addFriend(User friend);

    // Removes a friend from the user's friend list
    boolean removeFriend(User friend);

    // Blocks a user from interacting with this user
    boolean blockUser(User user);

    // Unblocks a previously blocked user
    boolean unblockUser(User user);

    // Checks if a specific user is blocked by this user
    boolean isBlocked(User user);

    // Returns a list of the user's friends
    List<User> getFriends();

    // Returns a list of users blocked by this user
    List<User> getBlockedUsers();
}
