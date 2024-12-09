
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The tests for user.
 * <p>
 * Purdue University -- CS18000 -- Fall 2024
 *
 * @author Elan Smyla, Aarav Mohanty, Hannah Cha, Kai Nietzche
 * @version December 8th, 2024
 */

public class UserTest {


    private User user1;
    private User user2;
    private User user3;


    @BeforeEach
    public void setUp() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1");
        db.createUser("user2", "password2", "Bio of user2");
        db.createUser("user3", "password3", "Bio of user3");

        user1 = db.getUser("user1");
        user2 = db.getUser("user2");
        user3 = db.getUser("user3");
    }


    @Test
    public void testAddFriend() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1");
        db.createUser("user2", "password2", "Bio of user2");
        db.createUser("user3", "password3", "Bio of user3");

        Assertions.assertTrue(db.getUser("user1").addFriend(db.getUser("user2")),
                "User2 should be added as a friend");
        Assertions.assertFalse(db.getUser("user1").addFriend(db.getUser("user2")),
                "User2 should not be added again");
        Assertions.assertFalse(db.getUser("user1").addFriend(null),
                "Adding null should return false");
        Assertions.assertFalse(db.getUser("user1").addFriend(db.getUser("user1")),
                "User1 cannot add themselves as a friend");

    }


    @Test
    public void testRemoveFriend() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1");
        db.createUser("user2", "password2", "Bio of user2");

        db.getUser("user1").addFriend(user2);
        Assertions.assertTrue(db.getUser("user1").removeFriend(db.getUser("user2").getUsername()),
                "User2 should be removed as a friend");
        Assertions.assertFalse(db.getUser("user1").removeFriend(db.getUser("user2").getUsername()),
                "User2 should already be removed");
        Assertions.assertFalse(db.getUser("user1").removeFriend("nonExistentUser"),
                "Removing a non-existent friend should return false");
    }

    @Test
    public void testBlockUser() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1");
        db.createUser("user2", "password2", "Bio of user2");

        Assertions.assertFalse(db.getUser("user1").blockUser(db.getUser("user2")),
                "User2 should be blocked");
        Assertions.assertFalse(db.getUser("user1").blockUser(db.getUser("user2")),
                "User2 should not be blocked again");
        Assertions.assertFalse(db.getUser("user1").blockUser(null),
                "Blocking null should return false");
        Assertions.assertFalse(db.getUser("user1").blockUser(db.getUser("user1")),
                "User1 cannot block themselves");
    }


    @Test
    public void testUnblockUser() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1");
        db.createUser("user2", "password2", "Bio of user2");
        db.createUser("user3", "password3", "Bio of user3");

        db.getUser("user1").blockUser(db.getUser("user2"));
        Assertions.assertTrue(db.getUser("user1").unblockUser(db.getUser("user2")),
                "User2 should be unblocked");
        Assertions.assertFalse(db.getUser("user1").unblockUser(db.getUser("user2")),
                "User2 should already be unblocked");
        Assertions.assertFalse(db.getUser("user1").unblockUser(db.getUser("user3")),
                "User3 is not blocked, should return false");
    }


    @Test
    public void testSendMessage() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1");
        db.createUser("user2", "password2", "Bio of user2");
        db.createUser("user3", "password3", "Bio of user3");

        db.getUser("user1").addFriend(db.getUser("user2"));
        db.getUser("user2").addFriend(db.getUser("user1"));
        Assertions.assertTrue(db.getUser("user1").sendMessage(db.getUser("user2"), "Hello!"),
                "Message should be sent successfully");
        Assertions.assertFalse(db.getUser("user1").sendMessage(null, "Hello!"),
                "Sending message to null should return false");
        Assertions.assertFalse(db.getUser("user1").sendMessage(db.getUser("user2"), ""),
                "Sending an empty message should return false");
        Assertions.assertTrue(db.getUser("user1").blockUser(db.getUser("user2")),
                "User2 should be blocked");
        Assertions.assertFalse(db.getUser("user1").sendMessage(db.getUser("user2"), "Should not be sent"),
                "Message should not be sent to a blocked user");
    }


    @Test
    public void testIsBlocked() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1");
        db.createUser("user2", "password2", "Bio of user2");
        db.createUser("user3", "password3", "Bio of user3");

        db.getUser("user1").blockUser(db.getUser("user2"));
        Assertions.assertTrue(db.getUser("user1").isBlocked(db.getUser("user2").getUsername()),
                "User2 should be blocked by user1");
        Assertions.assertFalse(db.getUser("user1").isBlocked(db.getUser("user3").getUsername()),
                "User3 is not blocked by user1");
    }

    @Test
    public void testGetFriends() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1");
        db.createUser("user2", "password2", "Bio of user2");
        db.createUser("user3", "password3", "Bio of user3");

        db.getUser("user1").addFriend(db.getUser("user2"));
        Assertions.assertEquals(1, db.getUser("user1").getFriends().size(),
                "User1 should have one friend");
        Assertions.assertTrue(db.getUser("user1").getFriends().contains(db.getUser("user2").getUsername()),
                "User1's friends should include user2");
    }

    @Test
    public void testGetBlockedUsers() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1");
        db.createUser("user2", "password2", "Bio of user2");
        db.createUser("user3", "password3", "Bio of user3");

        db.getUser("user1").blockUser(db.getUser("user2"));
        Assertions.assertEquals(1, db.getUser("user1").getBlockedUsers().size(),
                "User1 should have one blocked user");
        Assertions.assertFalse(db.getUser("user1").getBlockedUsers().contains(
                        db.getUser("user1").getUsername()),
                "User1's blocked users should include user2");
    }

}
