import java.util.List;

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
    //boolean sendPhoto(User receiver, File photo);
    String displayUser();
}
