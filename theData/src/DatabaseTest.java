import static org.junit.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class DatabaseTest {
    private UserService userService;
    private DatabaseTest service;  // Replace with the actual class name containing sendPhotoMessage
    private final String sender = "user1";
    private final String receiver = "user2";
    private Message message;
    private Message message1;
    private Message message2;
    private Message message3;
    private User existingUser;
    private Photo photo;




    public void setUp() { //this is to set up
        userService = new UserService();
        userService.users = new ArrayList<>();
        service = Mockito.spy(new PhotoMessageService()); // for photo msg
        message1 = mock(Message.class);
        message2 = mock(Message.class);
        message3 = mock(Message.class);

        // Mock message details for sender and receiver
        when(message1.getSender()).thenReturn("sender");
        when(message1.getReceiver()).thenReturn("receiver");

        when(message2.getSender()).thenReturn("sender");
        when(message2.getReceiver()).thenReturn("receiver");

        when(message3.getSender()).thenReturn("otherSender");
        when(message3.getReceiver()).thenReturn("otherReceiver");

        // Adding messages to the MessageService's message list
        messageService.messages.add(message1);
        messageService.messages.add(message2);
        messageService.messages.add(message3);
        user = mock(User.class);
        when(user.getUsername()).thenReturn("testUser");
        userService.users = new ArrayList<>();
        userService.users.add(user);

        when(user.getPassword()).thenReturn("correctPassword");

        when(userService.getUser("testUser")).thenReturn(user);

        when(user1.getBio()).thenReturn("Bio for user1");
        when(user2.getBio()).thenReturn("Bio for user2");
        userService.DATABASE_FILE = DATABASE_FILE;


    }


    public void testCreateUserWithAvailableUsername() { //to see if a username can be created properly
        boolean result = userService.createUser("newUser", "password", "user@example.com", "defaultPFP.txt", "Bio");
        assertTrue(result, "Created user when username is available");
    }

    public void testCreateUserWithTakenUsername() { //see if username is taken works

        userService.createUser("existingUser", "password123", "existing@example.com", "Existing bio");
        boolean result = userService.createUser("existingUser", "anotherPassword", "another@example.com", "defaultPFP.txt", "Another bio");
        assertFalse(result, "User creation should fail when username is already taken");
    }


    public void testSendTextMessage_Success() { // see if able to send a msg

        when(yourClass.sendMessageInternal("Alice", "Bob", new Message("Alice", "Bob", "Hello"))).thenReturn(true);
        boolean result = classUnderTest.sendTextMessage("Alice", "Bob", "Hello");
        assertTrue(result);
    }


    public void testSendTextMessage_Failure() { //see if able to send a failed msg
        when(yourClass.sendMessageInternal("Alice", "Bob", new Message("Alice", "Bob", "Hello"))).thenReturn(false);
        boolean result = classUnderTest.sendTextMessage("Alice", "Bob", "Hello");
        assertFalse(result);
    }


    public void testSendTextMessage_NullSender() { //see if one with null sender
        assertFalse(classUnderTest.sendTextMessage(null, "Bob", "Hello"));
    }


    public void testSendTextMessage_NullReceiver() {
        assertFalse(classUnderTest.sendTextMessage("Alice", null, "Hello"));
    }


    public void testSendTextMessage_NullContent() {
        assertFalse(classUnderTest.sendTextMessage("Alice", "Bob", null));
    }
    //now for photo msgs to test them

    void testSendPhotoMessage_FileExists() {
        String photoPath = "path/to/existing/photo.jpg";
        File photoFile = mock(File.class);
        when(photoFile.exists()).thenReturn(true);

        doReturn(true).when(service).sendMessageInternal(eq(sender), eq(receiver), any(Message.class));

        boolean result = service.sendPhotoMessage(sender, receiver, photoPath);
        assertTrue(result, "Expected sendPhotoMessage to return true when photo file exists");

        verify(service).sendMessageInternal(eq(sender), eq(receiver), any(Message.class));
    }

    void testSendPhotoMessage_FileNotExists() {
        String photoPath = "path/to/nonexistent/photo.jpg";
        File photoFile = mock(File.class);
        when(photoFile.exists()).thenReturn(false);

        boolean result = service.sendPhotoMessage(sender, receiver, photoPath);
        assertFalse(result, "Expected sendPhotoMessage to return false when photo file does not exist");


        verify(service, never()).sendMessageInternal(anyString(), anyString(), any(Message.class));
    }

    //now for send msg internal 1) Success when both users exist and the receiver has not blocked the sender.
    //2) Failure when the sender or receiver does not exist.
    //3) Failure when the receiver has blocked the sender.

    public void testSendMessageInternal_Success() {
        // Receiver has not blocked the sender
        when(receiver.isBlocked(sender)).thenReturn(false);

        boolean result = messageService.sendMessageInternal("sender", "receiver", message);


        assertTrue(result, "Expected sendMessageInternal to return true for successful message send.");
        assertTrue(messageService.messages.contains(message), "Message should be added to messages list.");
    }
    public void testSendMessageInternal_Success() {

        when(receiver.isBlocked(sender)).thenReturn(false);

        boolean result = messageService.sendMessageInternal("sender", "receiver", message);

        assertTrue(result, "Expected sendMessageInternal to return true for successful message send.");
        assertTrue(messageService.messages.contains(message), "Message should be added to messages list.");
    }

    public void testSendMessageInternal_Failure_UserNotFound() {
        when(messageService.getUser("sender")).thenReturn(null);

        boolean result = messageService.sendMessageInternal("sender", "receiver", message);

        assertFalse(result, "Expected sendMessageInternal to return false when sender user is not found.");
    }


    public void testSendMessageInternal_Failure_ReceiverBlockedSender() {

        when(receiver.isBlocked(sender)).thenReturn(true);
        boolean result = messageService.sendMessageInternal("sender", "receiver", message);
        assertFalse(result, "Expected sendMessageInternal to return false when receiver has blocked sender.");
    }
    //get messgages test:

    public void testGetMessages_Success_MessagesExist() {
        ArrayList<Message> result = messageService.getMessages("sender", "receiver");


        assertEquals(2, result.size(), "Expected two messages between sender and receiver.");
        assertTrue(result.contains(message1), "Result should contain message1.");
        assertTrue(result.contains(message2), "Result should contain message2.");
    }


    public void testGetMessages_NoMessages() {
        ArrayList<Message> result = messageService.getMessages("unknownSender", "unknownReceiver");
        assertTrue(result.isEmpty(), "Expected an empty list when no messages match.");
    }


    public void testGetMessages_OtherMessagesExist() {
        ArrayList<Message> result = messageService.getMessages("sender", "receiver");
        assertFalse(result.contains(message3), "Result should not contain messages from other sender/receiver pairs.");
    }

    public void testDeleteUser_Success() {
        when(userService.getUser("testUser")).thenReturn(user);

        boolean result = userService.deleteUser("testUser");
        assertTrue(result, "Expected deleteUser to return true when user exists and is deleted.");
        assertFalse(userService.users.contains(user), "User should be removed from users list.");
    }


    public void testDeleteUser_Failure_UserNotFound() {

        when(userService.getUser("nonExistentUser")).thenReturn(null);

        boolean result = userService.deleteUser("nonExistentUser");

        assertFalse(result, "Expected deleteUser to return false when user does not exist.");
    }

    public void testAuthenticate_Success() {
        boolean result = userService.authenticate("testUser", "correctPassword");
        assertTrue(result, "Expected authenticate to return true for correct username and password.");
    }

    public void testAuthenticate_Failure_IncorrectPassword() {
        boolean result = userService.authenticate("testUser", "wrongPassword");
        assertFalse(result, "Expected authenticate to return false for incorrect password.");
    }

    public void testAuthenticate_Failure_UserNotFound() {
        when(userService.getUser("nonExistentUser")).thenReturn(null);

        boolean result = userService.authenticate("nonExistentUser", "anyPassword");

        assertFalse(result, "Expected authenticate to return false for non-existent user.");
    }

    public void testWriteUsersToDatabase() throws IOException {
        userService.writeUsersToDatabase();

        Path filePath = Path.of(DATABASE_FILE);
        assertTrue(Files.exists(filePath), "Database file should exist after writing users.");

        var lines = Files.readAllLines(filePath);

        assertEquals(2, lines.size(), "Expected 2 lines in the file for two users.");

        String expectedUser1Entry = "user1, password1, Bio for user1, user1Friends.txt, user1BlockedUsers.txt, user1Conversations.txt";
        assertEquals(expectedUser1Entry, lines.get(0), "First user entry does not match expected format.");

        String expectedUser2Entry = "user2, password2, Bio for user2, user2Friends.txt, user2BlockedUsers.txt, user2Conversations.txt";
        assertEquals(expectedUser2Entry, lines.get(1), "Second user entry does not match expected format.");
    }

    public void testWriteUsersToDatabase_FileIOException() {

        userService.DATABASE_FILE = "invalid_directory/tempDatabaseFile.txt";

        assertDoesNotThrow(() -> userService.writeUsersToDatabase(),
                "writeUsersToDatabase should handle IOException without throwing.");
    }













}





