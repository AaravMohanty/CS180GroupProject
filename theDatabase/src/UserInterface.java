import java.util.ArrayList;
import java.util.List;

/**
 * The interface to create a user
 * <p>
 * Purdue University -- CS18000 -- Fall 2024
 *
 * @author Elan Smyla, Aarav Mohanty
 * @version November 3rd, 2024
 */

public interface UserInterface {
    String getUsername();

    String getPassword();

    String getBio();

    void setBio(String bio);

    String getPfp();

    void setPfp(String pfp);

    boolean addFriend(User friend);

    boolean removeFriend(String friend);

    boolean blockUser(User user);

    boolean unblockUser(User user);

    boolean isBlocked(String user);

    List<String> getFriends();

    List<String> getBlockedUsers();

    boolean sendMessage(User receiver, String message);

    boolean deleteMessage(User receiver, String message);

    boolean sendPhoto(User receiver, String photoPath);

    void rewriteToFile(String filePath, ArrayList<String> list);

    String displayUser();
}
