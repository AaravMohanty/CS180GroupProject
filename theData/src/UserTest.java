import org.junit.Assert;
import org.junit.Test;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * A framework to run public test cases for User class.
 *
 * Purdue University -- CS18000 -- Fall 2024
 *
 * @author Your Name
 * @version Nov 1, 2024
 */
public class UserTest {

    public static class TestCase {

        private static final String INFILE = "input.txt";

        @Test(timeout = 1000)
        public void UserClassDeclarationTest() {
            Class<?> clazz = User.class;

            int modifiers = clazz.getModifiers();
            Class<?> superclass = clazz.getSuperclass();
            Class<?>[] superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that User is public!",
                    Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that User is NOT abstract!",
                    Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that User extends Object!",
                    Object.class, superclass);
            Assert.assertEquals("Ensure that User implements UserInterface!",
                    1, superinterfaces.length);
            Assert.assertEquals("Ensure that User implements UserInterface!",
                    UserInterface.class, superinterfaces[0]);
        }

        @Test
        public void testUserConstructor() {
            User user = new User("testUser", "password123", "email@example.com", "This is a bio.");
            Assert.assertEquals("testUser", user.getUsername());
            Assert.assertEquals("password123", user.getPassword());
            Assert.assertEquals("This is a bio.", user.getBio());
        }

        @Test
        public void testAddFriend() {
            User user1 = new User("user1", "password1", "email1@example.com", "Bio 1");
            User user2 = new User("user2", "password2", "email2@example.com", "Bio 2");

            Assert.assertTrue(user1.addFriend(user2));
            Assert.assertEquals(1, user1.getFriends().size());
            Assert.assertTrue(user1.getFriends().contains(user2));
        }

        @Test
        public void testRemoveFriend() {
            User user1 = new User("user1", "password1", "email1@example.com", "Bio 1");
            User user2 = new User("user2", "password2", "email2@example.com", "Bio 2");
            user1.addFriend(user2);

            Assert.assertTrue(user1.removeFriend(user2));
            Assert.assertFalse(user1.getFriends().contains(user2));
        }

        @Test
        public void testBlockUser() {
            User user1 = new User("user1", "password1", "email1@example.com", "Bio 1");
            User user2 = new User("user2", "password2", "email2@example.com", "Bio 2");

            Assert.assertTrue(user1.blockUser(user2));
            Assert.assertTrue(user1.isBlocked(user2));
        }

        @Test
        public void testUnblockUser() {
            User user1 = new User("user1", "password1", "email1@example.com", "Bio 1");
            User user2 = new User("user2", "password2", "email2@example.com", "Bio 2");
            user1.blockUser(user2);

            Assert.assertTrue(user1.unblockUser(user2));
            Assert.assertFalse(user1.isBlocked(user2));
        }

        @Test
        public void testGetFriends() {
            User user1 = new User("user1", "password1", "email1@example.com", "Bio 1");
            User user2 = new User("user2", "password2", "email2@example.com", "Bio 2");
            user1.addFriend(user2);

            List<User> friends = user1.getFriends();
            Assert.assertEquals(1, friends.size());
            Assert.assertTrue(friends.contains(user2));
        }

        @Test
        public void testGetBlockedUsers() {
            User user1 = new User("user1", "password1", "email1@example.com", "Bio 1");
            User user2 = new User("user2", "password2", "email2@example.com", "Bio 2");
            user1.blockUser(user2);

            List<User> blockedUsers = user1.getBlockedUsers();
            Assert.assertEquals(1, blockedUsers.size());
            Assert.assertTrue(blockedUsers.contains(user2));
        }

        @Test
        public void testSendMessage() {
            User sender = new User("sender", "password", "email@example.com", "Sender Bio");
            User receiver = new User("receiver", "password", "receiver@example.com", "Receiver Bio");
            sender.sendMessage(receiver, "Hello!");

            // Check if the message was saved in the conversation file
            // This will depend on your Message class implementation
            // Add appropriate assertions here
        }

        @Test
        public void testSendPhoto() {
            User sender = new User("sender", "password", "email@example.com", "Sender Bio");
            User receiver = new User("receiver", "password", "receiver@example.com", "Receiver Bio");
            File photo = new File("path/to/photo.jpg");
            sender.sendPhoto(receiver, photo);

            // Check if the photo message was saved in the conversation file
            // This will depend on your Message class implementation
            // Add appropriate assertions here
        }
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCase.class);
        if (result.wasSuccessful()) {
            System.out.println("Excellent - Test ran successfully");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }
}

