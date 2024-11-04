import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;

/**
 * The message test to store message information
 *
 * Purdue University -- CS18000 -- Fall 2024 
 *
 * @author Elan Smyla, 11
 * @version November 3rd, 2024
 */

class MessageTest {

    @Test
    void testCreateTextMessage() {
        Message message = new Message("receiverUser", "Hello, World!");
        assertEquals("receiverUser", message.getReceiver());
        assertEquals("Hello, World!", message.getContent());
        assertNull(message.getPhoto());
        assertFalse(message.isPhotoMessage());
    }

    @Test
    void testCreatePhotoMessage() {
        Message message = new Message("receiverUser", true, "photo.png");
        assertEquals("receiverUser", message.getReceiver());
        assertNull(message.getContent());
        assertEquals("photo.png", message.getPhoto());
        assertTrue(message.isPhotoMessage());
    }

    @Test
    void testInvalidTextMessageCreation() {
        assertThrows(IllegalArgumentException.class, () -> new Message(null, "Hello!"));
        assertThrows(IllegalArgumentException.class, () -> new Message("receiverUser", ""));
    }

    @Test
    void testInvalidPhotoMessageCreation() {
        assertThrows(IllegalArgumentException.class, () -> new Message(null, true, "photo.png"));
        assertThrows(IllegalArgumentException.class, () -> new Message("receiverUser", true, null));
    }

    @Test
    void testWriteMessageToFile() throws IOException {
        String filename = "testConversation.txt";
        File file = new File(filename);
        if (file.exists()) {
            file.delete(); // Ensure the file is clean before testing
        }

        Message message = new Message("receiverUser", "Hello, World!");
        message.writeMessageToFile(filename);

        assertTrue(file.exists());

        // Check the contents of the file
        String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
        assertTrue(content.contains("Receiver: receiverUser"));
        assertTrue(content.contains("Text Message: Hello, World!"));

        file.delete(); // Clean up after the test
    }

    @Test
    void testWritePhotoMessageToFile() throws IOException {
        String filename = "testPhotoConversation.txt";
        File file = new File(filename);
        if (file.exists()) {
            file.delete(); // Ensure the file is clean before testing
        }

        Message message = new Message("receiverUser", true, "photo.png");
        message.writeMessageToFile(filename);

        assertTrue(file.exists());

        // Check the contents of the file
        String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
        assertTrue(content.contains("Receiver: receiverUser"));
        assertTrue(content.contains("Photo Message: photo.png"));

        file.delete(); // Clean up after the test
    }

    @Test
    void testWriteMessageToFileWithInvalidFileName() {
        Message message = new Message("receiverUser", "Hello, World!");
        assertThrows(IllegalArgumentException.class, () -> message.writeMessageToFile(null));
        assertThrows(IllegalArgumentException.class, () -> message.writeMessageToFile(""));
    }
}
