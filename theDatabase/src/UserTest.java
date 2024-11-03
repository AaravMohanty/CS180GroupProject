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
//            Assert.assertEquals("Ensure that User implements UserInterface!", 1, superinterfaces.length);
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
//            User user1 = new User("user1", "password1", "Bio 1", "pfp1.png", DATABASE_FILE);
//            User friendUser = new User("friendUser", "password2", "Bio 2", "pfp2.png", DATABASE_FILE);
//            Assert.assertTrue(user1.addFriend(friendUser));
//            List<String> friends = user1.getFriends();
//            Assert.assertEquals(1, friends.size());
//            Assert.assertTrue(friends.contains("friendUser"));
//        }
//
//        @Test
//        public void testRemoveFriend() {
//            User user1 = new User("user1", "password1", "Bio 1", "pfp1.png", DATABASE_FILE);
//            User friendUser = new User("friendUser", "password2", "Bio 2", "pfp2.png", DATABASE_FILE);
//            user1.addFriend(friendUser);
//
//            Assert.assertTrue(user1.removeFriend("friendUser"));
//            Assert.assertFalse(user1.getFriends().contains(friendUser));
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
