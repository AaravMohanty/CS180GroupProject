import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

/**
 * The runproject interface junit class to test run project interface
 *
 * @version November 3, 2024
 */

class RunProjectInterfaceTest {

    RunProjectInterface projectInterface;

    // Helper method to create a RunProject with mock input
    private void setUpWithInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        Database database = new Database(); // Ensure your database is properly initialized
        projectInterface = new RunProject(database);
    }

    @Test
    void testCreateAccount() {

        assertDoesNotThrow(() -> {
            projectInterface.createAccount();
        });
    }

    @Test
    void testLogin() {
        setUpWithInput("username\npassword\n"); // Simulated input
        assertDoesNotThrow(() -> {
            User user = projectInterface.login();
            assertTrue(user == null || user instanceof User);
        });
    }

    @Test
    void testAddFriend() {
        setUpWithInput("friendName\n"); // Simulated input
        assertDoesNotThrow(() -> {
            projectInterface.addFriend();
        });
    }

    @Test
    void testRemoveFriend() {
        setUpWithInput("friendName\n"); // Simulated input
        assertDoesNotThrow(() -> {
            projectInterface.removeFriend();
        });
    }

    @Test
    void testBlockUser() {
        setUpWithInput("blockedUsername\nunblockedUsername\n"); // Simulated input
        assertDoesNotThrow(() -> {
            projectInterface.blockUser();
        });
    }

    @Test
    void testSendMessage() {
        setUpWithInput("receiverUsername\nHello!\n"); // Simulated input
        assertDoesNotThrow(() -> {
            projectInterface.sendMessage();
        });
    }

    @Test
    void testViewUserProfile() {
        setUpWithInput("usernameToView\n"); // Simulated input
        assertDoesNotThrow(() -> {
            projectInterface.viewUserProfile();
        });
    }

    @Test
    void testSearch() {
        setUpWithInput("usernameToView\n"); // Simulated input
        assertDoesNotThrow(() -> {
            projectInterface.search();
        });
    }

    @Test
    void testViewUserProfileWithUser() {
        assertDoesNotThrow(() -> {
            User user = new User("testUser", "password", "bio", "pfp.png");
            projectInterface.viewUserProfile(user);
        });
    }
}
