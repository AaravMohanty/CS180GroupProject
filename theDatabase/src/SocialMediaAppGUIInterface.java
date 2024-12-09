import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.IOException;
import java.util.List;

/**
 * The client interface.
 * <p>
 * Purdue University -- CS18000 -- Fall 2024
 *
 * @author Elan Smyla, Aarav Mohanty, Hannah Cha, Kai Nietzche
 * @version December 8th, 2024
 */

public interface SocialMediaAppGUIInterface {
    void refreshFriendsList();

    void refreshBlockedList();

    void loadSelectedConversation(String selectedFriend);

    void sendMessage(String friendUsername, String message);

    void deleteMessage(String friendUsername, String message);

    void createLoginGUI();

    void createAccountGUI();

    void createMainMenuGUI();

    JPanel createFriendsPanel();

    JPanel createBlockedListPanel();

    JPanel createConversationsPanel();

    JPanel createSearchUsersPanel();
}
