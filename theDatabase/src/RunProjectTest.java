import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;


class RunProjectTest {
   private Database database;
   private RunProject runProject;
   private User testUser;


   @BeforeEach
   void setUp() {
       database = new Database();
       runProject = new RunProject(database);
       database.createUser("testUser", "password123", "Test Bio", "profile.jpg");
       testUser = database.getUser("testUser");
   }


   @Test
   void testLoginSuccess() {
       String simulatedInput = "testUser\npassword123\n";
       System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));


       User loggedInUser = runProject.login();


       assertNotNull(loggedInUser, "Login should be successful for valid credentials.");
       assertEquals("testUser", loggedInUser.getUsername());
   }


   @Test
   void testAddFriend() {
       // Log in the test user
       String simulatedInput = "testUser\npassword123\n";
       System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
       runProject.login();


       // Create a new friend user in the database
       database.createUser("newFriend", "friendPass", "Friend Bio", "friend.jpg");


       // Add the friend and check if they were added successfully
       assertDoesNotThrow(() -> runProject.addFriend());
       User friend = database.getUser("newFriend");
       assertNotNull(friend);
       assertTrue(testUser.getFriends().contains(friend), "Friend should be added.");
   }


   @Test
   void testRemoveFriend() {
       String simulatedInput = "testUser\npassword123\n";
       System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
       runProject.login();


       // Add a friend and then remove them
       User friend = new User("friendUser", "pass", "Bio", "pic.jpg");
       database.createUser("friendUser", "pass", "Bio", "pic.jpg");
       testUser.addFriend(friend);


       runProject.removeFriend();
       assertFalse(testUser.getFriends().contains(friend), "Friend should be removed.");
   }


   @Test
   void testBlockUser() {
       String simulatedInput = "testUser\npassword123\n";
       System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
       runProject.login();


       // Create a user to block and then block them
       database.createUser("blockUser", "pass", "Bio", "pic.jpg");


       assertDoesNotThrow(() -> runProject.blockUser());
       User blockUser = database.getUser("blockUser");
       assertTrue(testUser.getBlockedUsers().contains(blockUser), "User should be blocked.");
   }


  
   @Test
   void testSendMessage() {
       String simulatedInput = "testUser\npassword123\n";
       System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
       runProject.login();


       // Create a receiver and send a message
       database.createUser("receiver", "pass", "Bio", "pic.jpg");


       String message = "Hello!";
       assertDoesNotThrow(() -> runProject.sendMessage());
       User receiver = database.getUser("receiver");
       //assertTrue(receiver.getMessages().contains(message), "Message should be sent.");
   }


   @AfterEach
   void tearDown() {
       System.setIn(System.in); // Reset System.in after each test
   }
}

