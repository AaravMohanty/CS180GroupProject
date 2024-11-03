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

    // Adds a friend to the user's friend list
    boolean addFriend(String friend);

    // Removes a friend from the user's friend list
    boolean removeFriend(String friend);

    // Blocks a user
    boolean blockUser(String user);

    // Unblocks a user
    boolean unblockUser(String user);

    // Checks if a user is blocked
    boolean isBlocked(String user);

    // Gets the list of friends
    List<String> getFriends();

    // Gets the list of blocked users
    List<String> getBlockedUsers();
}
