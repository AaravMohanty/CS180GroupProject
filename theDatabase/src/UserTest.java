
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * The test to create a user
 * <p>
 * Purdue University -- CS18000 -- Fall 2024
 *
 * @author Elan Smyla, 11
 * @version November 3rd, 2024
 */

public class UserTest {


    private User user1;
    private User user2;
    private User user3;


    @BeforeEach
    public void setUp() {
        // Create test users
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");

        user1 = db.getUser("user1");
        user2 = db.getUser("user2");
        user3 = db.getUser("user3");
    }


    @Test
    public void testAddFriend() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");

        assertTrue(db.getUser("user1").addFriend(db.getUser("user2")), "User2 should be added as a friend");
        assertFalse(db.getUser("user1").addFriend(db.getUser("user2")), "User2 should not be added again");
        assertFalse(db.getUser("user1").addFriend(null), "Adding null should return false");
        assertFalse(db.getUser("user1").addFriend(db.getUser("user1")), "User1 cannot add themselves as a friend");

    }


    @Test
    public void testRemoveFriend() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.getUser("user1").addFriend(user2);
        assertTrue(db.getUser("user1").removeFriend(db.getUser("user2").getUsername()),
                   "User2 should be removed as a friend");
        assertFalse(db.getUser("user1").removeFriend(db.getUser("user2").getUsername()),
                    "User2 should already be removed");
        assertFalse(db.getUser("user1").removeFriend("nonExistentUser"),
                    "Removing a non-existent friend should return false");
    }

    @Test
    public void testBlockUser() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        assertFalse(db.getUser("user1").blockUser(db.getUser("user2")),
                    "User2 should be blocked");
        assertFalse(db.getUser("user1").blockUser(db.getUser("user2")),
                    "User2 should not be blocked again");
        assertFalse(db.getUser("user1").blockUser(null),
                    "Blocking null should return false");
        assertFalse(db.getUser("user1").blockUser(db.getUser("user1")),
                    "User1 cannot block themselves");
    }


    @Test
    public void testUnblockUser() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");


        db.getUser("user1").blockUser(db.getUser("user2"));
        assertTrue(db.getUser("user1").unblockUser(db.getUser("user2")), 
                   "User2 should be unblocked");
        assertFalse(db.getUser("user1").unblockUser(db.getUser("user2")), 
                    "User2 should already be unblocked");
        assertFalse(db.getUser("user1").unblockUser(db.getUser("user3")), 
                    "User3 is not blocked, should return false");
    }


    @Test
    public void testSendMessage() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");


        db.getUser("user1").addFriend(db.getUser("user2"));
        assertTrue(db.getUser("user1").sendMessage(db.getUser("user2"), "Hello!"), 
                   "Message should be sent successfully");
        assertFalse(db.getUser("user1").sendMessage(null, "Hello!"), 
                    "Sending message to null should return false");
        assertFalse(db.getUser("user1").sendMessage(db.getUser("user2"), ""), 
                    "Sending an empty message should return false");
        assertTrue(db.getUser("user1").blockUser(db.getUser("user2")), 
                   "User2 should be blocked");
        assertFalse(db.getUser("user1").sendMessage(db.getUser("user2"), "Should not be sent"), 
                    "Message should not be sent to a blocked user");
    }


    @Test
    public void testIsBlocked() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");


        db.getUser("user1").blockUser(db.getUser("user2"));
        assertTrue(db.getUser("user1").isBlocked(db.getUser("user2").getUsername()), 
                   "User2 should be blocked by user1");
        assertFalse(db.getUser("user1").isBlocked(db.getUser("user3").getUsername()), 
                    "User3 is not blocked by user1");
    }


    @Test
    public void testGetFriends() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");
        db.getUser("user1").addFriend(db.getUser("user2"));
        assertEquals(1, db.getUser("user1").getFriends().size(), 
                     "User1 should have one friend");
        assertTrue(db.getUser("user1").getFriends().contains(db.getUser("user2").getUsername()), 
                   "User1's friends should include user2");
    }


    @Test
    public void testGetBlockedUsers() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");
        db.getUser("user1").blockUser(db.getUser("user2"));
        assertEquals(1, db.getUser("user1").getBlockedUsers().size(), 
                     "User1 should have one blocked user");
        assertFalse(db.getUser("user1").getBlockedUsers().contains(db.getUser("user1").getUsername()), 
                    "User1's blocked users should include user2");
    }


}
