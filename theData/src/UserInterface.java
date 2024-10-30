import java.util.List;

public interface UserInterface {
    // Gets the username of the user
    String getUsername();

    // Gets the password of the user
    String getPassword();

    // Gets the email of the user
    String getEmail();

    // Gets the bio of the user
    String getBio();

    // Sets the bio of the user
    void setBio(String bio);

    // Adds a friend to the user's friend list
    boolean addFriend(User friend);

    // Removes a friend from the user's friend list
    boolean removeFriend(User friend);

    // Blocks a user
    boolean blockUser(User user);

    // Unblocks a user
    boolean unblockUser(User user);

    // Checks if a user is blocked
    boolean isBlocked(User user);

    // Gets the list of friends
    List<User> getFriends();

    // Gets the list of blocked users
    List<User> getBlockedUsers();
}
