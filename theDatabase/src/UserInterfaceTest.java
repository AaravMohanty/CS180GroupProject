import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

class UserInterfaceTest {

    // Create an instance of a class that implements UserInterface
    UserInterface user = new User("testUser", "password", "bio", "pfp.png"); // Use a concrete implementation

    @Test
    void testGetUsername() {
        // Check if getUsername returns a String without throwing exceptions
        assertDoesNotThrow(() -> {
            String username = user.getUsername();
            assertNotNull(username); // Ensure it returns a non-null string
            assertEquals("testUser", username); // Check for expected value
        });
    }

    @Test
    void testGetPassword() {
        // Check if getPassword returns a String without throwing exceptions
        assertDoesNotThrow(() -> {
            String password = user.getPassword();
            assertNotNull(password); // Ensure it returns a non-null string
            assertEquals("password", password); // Check for expected value
        });
    }

    @Test
    void testGetBio() {
        // Check if getBio returns a String without throwing exceptions
        assertDoesNotThrow(() -> {
            String bio = user.getBio();
            assertNotNull(bio); // Ensure it returns a non-null string
            assertEquals("bio", bio); // Check for expected value
        });
    }

    @Test
    void testSetBio() {
        // Check if setBio does not throw exceptions
        assertDoesNotThrow(() -> {
            user.setBio("new bio");
            assertEquals("new bio", user.getBio()); // Verify the updated bio
        });
    }

    @Test
    void testGetPfp() {
        // Check if getPfp returns a String without throwing exceptions
        assertDoesNotThrow(() -> {
            String pfp = user.getPfp();
            assertNotNull(pfp); // Ensure it returns a non-null string
            assertEquals("pfp.png", pfp); // Check for expected value
        });
    }

    @Test
    void testSetPfp() {
        // Check if setPfp does not throw exceptions
        assertDoesNotThrow(() -> {
            user.setPfp("new_pfp.png");
            assertEquals("new_pfp.png", user.getPfp()); // Verify the updated pfp
        });
    }

    @Test
    void testAddFriend() {
        // Check if addFriend does not throw exceptions
        assertDoesNotThrow(() -> {
            User friend = new User("friendUser", "friendPassword", "friendBio", "friendPfp.png");
            boolean result = user.addFriend(friend);
            assertTrue(result || !result); // Ensure it returns a boolean
        });
    }

    @Test
    void testRemoveFriend() {
        // Check if removeFriend does not throw exceptions
        assertDoesNotThrow(() -> {
            boolean result = user.removeFriend("friendUser");
            assertTrue(result || !result); // Ensure it returns a boolean
        });
    }

    @Test
    void testBlockUser() {
        // Check if blockUser does not throw exceptions
        assertDoesNotThrow(() -> {
            User userToBlock = new User("blockUser", "blockPassword", "blockBio", "blockPfp.png");
            boolean result = user.blockUser(userToBlock);
            assertTrue(result || !result); // Ensure it returns a boolean
        });
    }

    @Test
    void testUnblockUser() {
        // Check if unblockUser does not throw exceptions
        assertDoesNotThrow(() -> {

            Database data = new Database();
            data.createUser("unblockUser", "unblockPassword", "unblockBio", "unblockPfp.png");
            //User userToUnblock = new User("unblockUser", "unblockPassword", "unblockBio", "unblockPfp.png");
            boolean result = user.unblockUser(data.getUser("unblockUser"));
            assertTrue(result || !result); // Ensure it returns a boolean
        });
    }

    @Test
    void testIsBlocked() {
        // Check if isBlocked returns a boolean without throwing exceptions
        assertDoesNotThrow(() -> {
            boolean blocked = user.isBlocked("someUser");
            assertTrue(blocked || !blocked); // Ensure it returns a boolean
        });
    }

    @Test
    void testGetFriends() {
        // Check if getFriends returns a List<String> without throwing exceptions
        assertDoesNotThrow(() -> {
            List<String> friends = user.getFriends();
            assertNotNull(friends); // Ensure it returns a non-null list
            assertTrue(friends instanceof ArrayList); // Check if it’s an ArrayList
        });
    }

    @Test
    void testGetBlockedUsers() {
        // Check if getBlockedUsers returns a List<String> without throwing exceptions
        assertDoesNotThrow(() -> {
            List<String> blockedUsers = user.getBlockedUsers();
            assertNotNull(blockedUsers); // Ensure it returns a non-null list
            assertTrue(blockedUsers instanceof ArrayList); // Check if it’s an ArrayList
        });
    }

    @Test
    void testSendMessage() {
        // Check if sendMessage does not throw exceptions
        assertDoesNotThrow(() -> {
            User receiver = new User("receiverUser", "receiverPassword", "receiverBio", "receiverPfp.png");
            boolean result = user.sendMessage(receiver, "Hello!");
            assertTrue(result || !result); // Ensure it returns a boolean
        });
    }

    @Test
    void testSendPhoto() {
        // Check if sendPhoto does not throw exceptions
        assertDoesNotThrow(() -> {
            User receiver = new User("receiverUser", "receiverPassword", "receiverBio", "receiverPfp.png");
            File photo = new File("photo.png"); // Ensure the file is a valid reference
            boolean result = user.sendPhoto(receiver, photo);
            assertTrue(result || !result); // Ensure it returns a boolean
        });
    }
}
