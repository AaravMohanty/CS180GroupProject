import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.IOException;
import java.util.List;

public interface SocialMediaAppGUIInterface {

    /**
     * Refreshes the friends list in the UI by fetching updated data from the server.
     */
    void refreshFriendsList();

    /**
     * Refreshes the blocked users list in the UI by fetching updated data from the server.
     */
    void refreshBlockedList();

    /**
     * Loads the conversation with a selected user and displays it in the UI.
     * @param selectedFriend The username of the selected friend.
     */
    void loadSelectedConversation(String selectedFriend);

    /**
     * Sends a message to the selected friend.
     * @param friendUsername The username of the friend to whom the message is sent.
     * @param message The message content to be sent.
     */
    void sendMessage(String friendUsername, String message);

    /**
     * Deletes a specific message from the selected conversation.
     * @param friendUsername The username of the friend whose message is to be deleted.
     * @param message The message content to delete.
     */
    void deleteMessage(String friendUsername, String message);

    /**
     * Creates and displays the login GUI for the application.
     */
    void createLoginGUI();

    /**
     * Creates and displays the account creation GUI for the application.
     */
    void createAccountGUI();

    /**
     * Creates and displays the main menu GUI for the application.
     */
    void createMainMenuGUI();

    /**
     * Creates and returns a JPanel for managing friends.
     * @return A JPanel containing the friends management UI.
     */
    JPanel createFriendsPanel();

    /**
     * Creates and returns a JPanel for managing blocked users.
     * @return A JPanel containing the blocked users management UI.
     */
    JPanel createBlockedListPanel();

    /**
     * Creates and returns a JPanel for conversations.
     * @return A JPanel containing the conversations UI.
     */
    JPanel createConversationsPanel();

    /**
     * Creates and returns a JPanel for searching users.
     * @return A JPanel containing the user search UI.
     */
    JPanel createSearchUsersPanel();
}
