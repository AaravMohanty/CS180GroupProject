
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTest {
    private UserService userService;

    public void setUp() { //this is to set up
        userService = new UserService();
        userService.users = new ArrayList<>();
    }


    public void testCreateUserWithAvailableUsername() { //to see if a username can be created properly
        boolean result = userService.createUser("newUser", "password", "user@example.com", "Bio");
        assertTrue(result, "Created user when username is available");
    }

    public void testCreateUserWithTakenUsername() { //see if username is taken works

        userService.createUser("existingUser", "password123", "existing@example.com", "Existing bio");
        boolean result = userService.createUser("existingUser", "anotherPassword", "another@example.com", "Another bio");
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



}




