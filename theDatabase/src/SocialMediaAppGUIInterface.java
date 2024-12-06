import java.awt.*;
import java.util.*;
import java.io.IOException;
import java.util.List;

public interface SocialMediaAppGUIInterface {

    void connectToServer(String host, int port) throws IOException;

    String login(String username, String password) throws IOException;

    String createAccount(String username, String password, String bio) throws IOException;

    void logout() throws IOException;

    List<String> refreshFriendsList() throws IOException;

    List<String> refreshBlockedList() throws IOException;

    String sendMessage(String friendUsername, String message) throws IOException;

    String deleteMessage(String friendUsername, String message) throws IOException;

    List<String> loadConversation(String friendUsername) throws IOException;

    List<String> searchUsers(String query) throws IOException;

    String blockUser(String username) throws IOException;

    String unblockUser(String username) throws IOException;
}
