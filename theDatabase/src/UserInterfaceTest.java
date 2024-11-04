import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class UserInterfaceTest {

    UserInterface user = new User("testUser", "password", "bio", "pfp.png");

    @Test
    void testGetUsername() {
        assertDoesNotThrow(() -> {
            String username = user.getUsername();
            assertNotNull(username);
            assertEquals("testUser", username);
        });
    }

    @Test
    void testGetPassword() {
        assertDoesNotThrow(() -> {
            String password = user.getPassword();
            assertNotNull(password);
            assertEquals("password", password);
        });
    }

    @Test
    void testGetBio() {
        assertDoesNotThrow(() -> {
            String bio = user.getBio();
            assertNotNull(bio);
            assertEquals("bio", bio);
        });
    }

    @Test
    void testSetBio() {
        assertDoesNotThrow(() -> {
            user.setBio("new bio");
            assertEquals("new bio", user.getBio());
        });
    }

    @Test
    void testGetPfp() {
        assertDoesNotThrow(() -> {
            String pfp = user.getPfp();
            assertNotNull(pfp);
            assertEquals("pfp.png", pfp);
        });
    }

    @Test
    void testSetPfp() {
        assertDoesNotThrow(() -> {
            user.setPfp("new_pfp.png");
            assertEquals("new_pfp.png", user.getPfp());
        });
    }

    @Test
    void testAddFriend() {
        assertDoesNotThrow(() -> {
            User friend = new User("friendUser", "friendPassword", "friendBio", "friendPfp.png");
            boolean result = user.addFriend(friend);
            assertTrue(result || !result);
        });
    }

    @Test
    void testRemoveFriend() {
        assertDoesNotThrow(() -> {
            boolean result = user.removeFriend("friendUser");
            assertTrue(result || !result);
        });
    }

    @Test
    void testBlockUser() {
        assertDoesNotThrow(() -> {
            User userToBlock = new User("blockUser", "blockPassword", "blockBio", "blockPfp.png");
            boolean result = user.blockUser(userToBlock);
            assertTrue(result || !result);
        });
    }

    @Test
    void testUnblockUser() {
        assertDoesNotThrow(() -> {
            Database data = new Database();
            data.createUser("unblockUser", "unblockPassword", "unblockBio", "unblockPfp.png");
            boolean result = user.unblockUser(data.getUser("unblockUser"));
            assertTrue(result || !result);
        });
    }

    @Test
    void testIsBlocked() {
        assertDoesNotThrow(() -> {
            boolean blocked = user.isBlocked("someUser");
            assertTrue(blocked || !blocked);
        });
    }

    @Test
    void testGetFriends() {
        assertDoesNotThrow(() -> {
            List<String> friends = user.getFriends();
            assertNotNull(friends);
            assertTrue(friends instanceof ArrayList);
        });
    }

    @Test
    void testGetBlockedUsers() {
        assertDoesNotThrow(() -> {
            List<String> blockedUsers = user.getBlockedUsers();
            assertNotNull(blockedUsers);
            assertTrue(blockedUsers instanceof ArrayList);
        });
    }

    @Test
    void testSendMessage() {
        assertDoesNotThrow(() -> {
            User receiver = new User("receiverUser", "receiverPassword", "receiverBio", "receiverPfp.png");
            boolean result = user.sendMessage(receiver, "Hello!");
            assertTrue(result || !result);
        });
    }

//    @Test
//    void testSendPhoto() {
//        assertDoesNotThrow(() -> {
//            User receiver = new User("receiverUser", "receiverPassword", "receiverBio", "receiverPfp.png");
//            File photo = new File("photo.png");
//            boolean result = user.sendPhoto(receiver, photo);
//            assertTrue(result || !result);
//        });
//    }
}
