import java.util.ArrayList;

public class User implements UserInterface {
    private String username;
    private String password;
    private String email;
    private String bio;
    private ArrayList<User> friends;
    private ArrayList<User> blockedUsers;

    public User(String username, String password, String email, String bio) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.bio = bio;
        this.friends = new ArrayList<>();
        this.blockedUsers = new ArrayList<>();
    }

    // Add a friend
    public boolean addFriend(User friend) {
        if (!friends.contains(friend)) {
            friends.add(friend);
            return true;
        }
        return false;  // Already a friend
    }

    // Remove a friend
    public boolean removeFriend(User friend) {
        return friends.remove(friend);  // Removes if present
    }

    // Block a user
    public boolean blockUser(User user) {
        if (!blockedUsers.contains(user)) {
            blockedUsers.add(user);
            return true;
        }
        return false;  // Already blocked
    }

    // Unblock a user
    public boolean unblockUser(User user) {
        return blockedUsers.remove(user);  // Removes if present
    }

    // Check if a user is blocked
    public boolean isBlocked(User user) {
        return blockedUsers.contains(user);
    }
}
