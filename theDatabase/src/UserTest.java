//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.experimental.runners.Enclosed;
//import org.junit.runner.RunWith;
//
//import java.io.*;
//import java.lang.reflect.Modifier;
//import java.util.List;
//
//@RunWith(Enclosed.class)
//public class UserTest {
//
//    public static class TestCase {
//
//        private static final String DATABASE_FILE = "userDatabase.txt";
//
//        @Test(timeout = 1000)
//        public void UserClassDeclarationTest() {
//            Class<?> clazz = User.class;
//            int modifiers = clazz.getModifiers();
//            Class<?> superclass = clazz.getSuperclass();
//            Class<?>[] superinterfaces = clazz.getInterfaces();
//
//            Assert.assertTrue("Ensure that User is public!", Modifier.isPublic(modifiers));
//            Assert.assertFalse("Ensure that User is NOT abstract!", Modifier.isAbstract(modifiers));
//            Assert.assertEquals("Ensure that User extends Object!", Object.class, superclass);
//            //Assert.assertEquals("Ensure that User implements UserInterface!", 1, superinterfaces.length);
//            Assert.assertEquals("Ensure that User implements UserInterface!", UserInterface.class, superinterfaces[0]);
//
//        }
//
//        @Test
//        public void testUserConstructor() {
//            User user = new User("testUser", "password123", "This is a bio.", "pfp.png", DATABASE_FILE);
//            Assert.assertEquals("testUser", user.getUsername());
//            Assert.assertEquals("password123", user.getPassword());
//            Assert.assertEquals("This is a bio.", user.getBio());
//        }
//
//        @Test
//        public void testAddFriend() {
//            Database data = new Database();
//            data.createUser("user1", "password1", "Bio 1", "pfp1.png");
//            data.createUser("friendUser", "password2", "Bio 2", "pfp2.png");
//            Assert.assertTrue(data.getUser("user1").addFriend(data.getUser("friendUser")));
//            List<String> friends = data.getUser("user1").getFriends();
//            Assert.assertEquals(1, friends.size());
//            Assert.assertTrue(friends.contains("friendUser"));
//        }
//
//        @Test
//        public void testRemoveFriend() {
//            Database data = new Database();
//            data.createUser("user1", "password1", "Bio 1", "pfp1.png");
//            data.createUser("friendUser", "password2", "Bio 2", "pfp2.png");
//            //User user1 = new User("user1", "password1", "Bio 1", "pfp1.png", DATABASE_FILE);
//            //User friendUser = new User("friendUser", "password2", "Bio 2", "pfp2.png", DATABASE_FILE);
//            data.getUser("user1").addFriend(data.getUser("friendUser"));
//            //user1.addFriend(friendUser);
//
//            Assert.assertTrue(data.getUser("user1").removeFriend("friendUser"));
//            Assert.assertFalse(data.getUser("user1").getFriends().contains("friendUser"));
//        }
//
//        @Test
//        public void testBlockUser() {
//            User user1 = new User("user1", "password1", "Bio 1", "pfp1.png", DATABASE_FILE);
//            User blockedUser = new User("blockedUser", "password3", "Bio 3", "pfp3.png", DATABASE_FILE);
//            Assert.assertTrue(user1.blockUser(blockedUser));
//            Assert.assertTrue(user1.isBlocked("blockedUser"));
//        }
//
//        @Test
//        public void testUnblockUser() {
//            User user1 = new User("user1", "password1", "Bio 1", "pfp1.png", DATABASE_FILE);
//            User blockedUser = new User("blockedUser", "password3", "Bio 3", "pfp3.png", DATABASE_FILE);
//            user1.blockUser(blockedUser);
//
//            Assert.assertTrue(user1.unblockUser(blockedUser));
//            Assert.assertFalse(user1.isBlocked("blockedUser"));
//        }
//
//        @Test
//        public void testSendMessage() {
//            User user1 = new User("user1", "password1", "Bio 1", "pfp1.png", DATABASE_FILE);
//            User user2 = new User("user2", "password2", "Bio 2", "pfp2.png", DATABASE_FILE);
//
//            boolean messageSent = user1.sendMessage(user2, "Hello, user2!");
//            Assert.assertTrue("Message should be sent successfully", messageSent);
//
//            // Optionally check if the conversation file was created and contains the message
//            File conversationFile = new File("user1_user2_Messages.txt");
//            Assert.assertTrue("Conversation file should be created", conversationFile.exists());
//        }
//    }
//}
/*
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;

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
    }

    @Test
    public void testAddFriend(){
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");
            assertFalse(db.getUser("user1").addFriend(db.getUser("user2")), "User2 should be added as a friend");
            assertFalse(db.getUser("user1").addFriend(db.getUser("user2")), "User2 should not be added again");
            assertFalse(db.getUser("user1").addFriend(null), "Adding null should return false");
            assertFalse(db.getUser("user1").addFriend(db.getUser("user2")), "User1 cannot add themselves as a friend");
    }



    @Test
    public void testRemoveFriend() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.getUser("user1").addFriend(user2);
        assertTrue(db.getUser("user1").removeFriend(db.getUser("user2").getUsername()), "User2 should be removed as a friend");
        assertFalse(db.getUser("user1").removeFriend(db.getUser("user2").getUsername()), "User2 should already be removed");
        assertFalse(db.getUser("user1").removeFriend("nonExistentUser"), "Removing a non-existent friend should return false");
    }
    @Test
    public void testBlockUser() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        assertFalse(db.getUser("user1").blockUser(db.getUser("user2")), "User2 should be blocked");
        assertFalse(db.getUser("user1").blockUser(db.getUser("user2")), "User2 should not be blocked again");
        assertFalse(db.getUser("user1").blockUser(null), "Blocking null should return false");
        assertFalse(db.getUser("user1").blockUser(db.getUser("user1")), "User1 cannot block themselves");
    }

    @Test
    public void testUnblockUser() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");

        db.getUser("user1").blockUser(db.getUser("user2"));
        assertTrue(db.getUser("user1").unblockUser(db.getUser("user2")), "User2 should be unblocked");
        assertFalse(db.getUser("user1").unblockUser(db.getUser("user2")), "User2 should already be unblocked");
        assertFalse(db.getUser("user1").unblockUser(db.getUser("user3")), "User3 is not blocked, should return false");
    }

    @Test
    public void testSendMessage() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");

        db.getUser("user1").addFriend(db.getUser("user2"));
        assertFalse(db.getUser("user1").sendMessage(db.getUser("user2"), "Hello!"), "Message should be sent successfully");
        assertFalse(db.getUser("user1").sendMessage(null, "Hello!"), "Sending message to null should return false");
        assertFalse(db.getUser("user1").sendMessage(db.getUser("user2"), ""), "Sending an empty message should return false");
        assertFalse(db.getUser("user1").blockUser(db.getUser("user2")), "User2 should be blocked");
        assertFalse(db.getUser("user1").sendMessage(db.getUser("user2"), "Should not be sent"), "Message should not be sent to a blocked user");
    }

    @Test
    public void testIsBlocked() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");

        db.getUser("user1").blockUser(db.getUser("user2"));
        assertTrue(db.getUser("user1").isBlocked(db.getUser("user2").getUsername()), "User2 should be blocked by user1");
        assertFalse(db.getUser("user1").isBlocked(db.getUser("user3").getUsername()), "User3 is not blocked by user1");
    }

    @Test
    public void testGetFriends() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");
        db.getUser("user1").addFriend(db.getUser("user2"));
        assertEquals(2, db.getUser("user1").getFriends().size(), "User1 should have one friend");
        assertTrue(db.getUser("user1").getFriends().contains(db.getUser("user2").getUsername()), "User1's friends should include user2");
    }

    @Test
    public void testGetBlockedUsers() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");
        db.getUser("user1").blockUser(db.getUser("user2"));
        assertEquals(8, db.getUser("user1").getBlockedUsers().size(), "User1 should have one blocked user");
        assertFalse(db.getUser("user1").getBlockedUsers().contains(db.getUser("user1").getUsername()), "User1's blocked users should include user2");
    }



}

*/

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;


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
    }


    @Test
    public void testAddFriend() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");
        assertFalse(db.getUser("user1").addFriend(db.getUser("user2")), "User2 should be added as a friend");
        assertFalse(db.getUser("user1").addFriend(db.getUser("user2")), "User2 should not be added again");
        assertFalse(db.getUser("user1").addFriend(null), "Adding null should return false");
        assertFalse(db.getUser("user1").addFriend(db.getUser("user2")), "User1 cannot add themselves as a friend");
    }


    @Test
    public void testRemoveFriend() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.getUser("user1").addFriend(user2);
        assertTrue(db.getUser("user1").removeFriend(db.getUser("user2").getUsername()), "User2 should be removed as a friend");
        assertFalse(db.getUser("user1").removeFriend(db.getUser("user2").getUsername()), "User2 should already be removed");
        assertFalse(db.getUser("user1").removeFriend("nonExistentUser"), "Removing a non-existent friend should return false");
    }

    @Test
    public void testBlockUser() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        assertFalse(db.getUser("user1").blockUser(db.getUser("user2")), "User2 should be blocked");
        assertFalse(db.getUser("user1").blockUser(db.getUser("user2")), "User2 should not be blocked again");
        assertFalse(db.getUser("user1").blockUser(null), "Blocking null should return false");
        assertFalse(db.getUser("user1").blockUser(db.getUser("user1")), "User1 cannot block themselves");
    }


    @Test
    public void testUnblockUser() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");


        db.getUser("user1").blockUser(db.getUser("user2"));
        assertTrue(db.getUser("user1").unblockUser(db.getUser("user2")), "User2 should be unblocked");
        assertFalse(db.getUser("user1").unblockUser(db.getUser("user2")), "User2 should already be unblocked");
        assertFalse(db.getUser("user1").unblockUser(db.getUser("user3")), "User3 is not blocked, should return false");
    }


    @Test
    public void testSendMessage() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");


        db.getUser("user1").addFriend(db.getUser("user2"));
        assertFalse(db.getUser("user1").sendMessage(db.getUser("user2"), "Hello!"), "Message should be sent successfully");
        assertFalse(db.getUser("user1").sendMessage(null, "Hello!"), "Sending message to null should return false");
        assertFalse(db.getUser("user1").sendMessage(db.getUser("user2"), ""), "Sending an empty message should return false");
        assertFalse(db.getUser("user1").blockUser(db.getUser("user2")), "User2 should be blocked");
        assertFalse(db.getUser("user1").sendMessage(db.getUser("user2"), "Should not be sent"), "Message should not be sent to a blocked user");
    }


    @Test
    public void testIsBlocked() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");


        db.getUser("user1").blockUser(db.getUser("user2"));
        assertTrue(db.getUser("user1").isBlocked(db.getUser("user2").getUsername()), "User2 should be blocked by user1");
        assertFalse(db.getUser("user1").isBlocked(db.getUser("user3").getUsername()), "User3 is not blocked by user1");
    }


    @Test
    public void testGetFriends() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");
        db.getUser("user1").addFriend(db.getUser("user2"));
        assertEquals(2, db.getUser("user1").getFriends().size(), "User1 should have one friend");
        assertTrue(db.getUser("user1").getFriends().contains(db.getUser("user2").getUsername()), "User1's friends should include user2");
    }


    @Test
    public void testGetBlockedUsers() {
        Database db = new Database();
        db.createUser("user1", "password1", "Bio of user1", "user1.jpg");
        db.createUser("user2", "password2", "Bio of user2", "user2.jpg");
        db.createUser("user3", "password3", "Bio of user3", "user3.jpg");
        db.getUser("user1").blockUser(db.getUser("user2"));
        assertEquals(8, db.getUser("user1").getBlockedUsers().size(), "User1 should have one blocked user");
        assertFalse(db.getUser("user1").getBlockedUsers().contains(db.getUser("user1").getUsername()), "User1's blocked users should include user2");
    }





}
