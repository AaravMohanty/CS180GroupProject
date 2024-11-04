import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

/**
 * The message test to store message information
 *
 * Purdue University -- CS18000 -- Fall 2024 
 *
 * @author Elan Smyla, 11
 * @version November 3rd, 2024
 */

class MessageInterfaceTest {

    // Create an instance of a class that implements MessageInterface
    MessageInterface message = new Message("testUser", "Hello!"); // Use a concrete implementation

    @Test
    void testGetReceiver() {
        // Check if getReceiver returns a String without throwing exceptions
        assertDoesNotThrow(() -> {
            String receiver = message.getReceiver();
            assertNotNull(receiver); // Ensure it returns a non-null string
            assertEquals("testUser", receiver); // Check for expected value
        });
    }

    @Test
    void testGetContent() {
        // Check if getContent returns a String or null without throwing exceptions
        assertDoesNotThrow(() -> {
            String content = message.getContent();
            assertNotNull(content); // Ensure it returns a non-null string
            assertEquals("Hello!", content); // Check for expected value
        });
    }

    @Test
    void testGetPhoto() {
        // Check if getPhoto returns a File or null without throwing exceptions
        assertDoesNotThrow(() -> {
            String photo = message.getPhoto();
            assertNull(photo); // Expecting null for a text message
        });
    }

    @Test
    void testIsPhotoMessage() {
        // Check if isPhotoMessage returns a boolean without throwing exceptions
        assertDoesNotThrow(() -> {
            boolean isPhoto = message.isPhotoMessage();
            assertFalse(isPhoto); // Expecting false for a text message
        });
    }

    @Test
    void testWriteMessageToFile() {
        // Check if writeMessageToFile does not throw exceptions
        assertDoesNotThrow(() -> {
            message.writeMessageToFile("test_conversation.txt");
            // Here you might want to check if the file was actually created, but that's outside of the method's scope.
        });
    }
}
