import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MessageTest {

    @Test
    public void testTextMessageConstructor() {
        Message message = new Message("Alice", "Bob", "Hello, Bob!");
        Assert.assertEquals("Ensure that the sender is set correctly", "Alice", message.getSender());
        Assert.assertEquals("Ensure that the receiver is set correctly", "Bob", message.getReceiver());
        Assert.assertEquals("Ensure that the content is set correctly", "Hello, Bob!", message.getContent());
        Assert.assertNull("Ensure that the photo is null for text messages", message.getPhoto());
    }

    @Test
    public void testPhotoMessageConstructor() {
        File photoFile = new File("path/to/photo.jpg");
        Message message = new Message("Alice", "Bob", photoFile);
        Assert.assertEquals("Ensure that the sender is set correctly", "Alice", message.getSender());
        Assert.assertEquals("Ensure that the receiver is set correctly", "Bob", message.getReceiver());
        Assert.assertNull("Ensure that the content is null for photo messages", message.getContent());
        Assert.assertEquals("Ensure that the photo is set correctly", photoFile, message.getPhoto());
    }

    @Test
    public void testIsPhotoMessage() {
        Message textMessage = new Message("Alice", "Bob", "Hello, Bob!");
        Message photoMessage = new Message("Alice", "Bob", new File("path/to/photo.jpg"));

        Assert.assertFalse("Ensure that isPhotoMessage returns false for text messages", textMessage.isPhotoMessage());
        Assert.assertTrue("Ensure that isPhotoMessage returns true for photo messages", photoMessage.isPhotoMessage());
    }

    @Test
    public void testWriteMessageToFile() {
        String conversationFileName = "conversation.txt";
        Message textMessage = new Message("Alice", "Bob", "Hello, Bob!");

        // Write the message to a file
        textMessage.writeMessageToFile(conversationFileName);

        // Verify the content of the file
        try (BufferedReader reader = new BufferedReader(new FileReader(conversationFileName))) {
            String line;
            StringBuilder fileContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            String expectedContent = "Sender: Alice\nReceiver: Bob\nText Message: Hello, Bob!\n";
            Assert.assertEquals("Ensure that the message is written to the file correctly", expectedContent.trim(), fileContent.toString().trim());
        } catch (IOException e) {
            Assert.fail("An IO exception was encountered while reading conversation.txt");
        }
    }

    @Test
    public void testWritePhotoMessageToFile() {
        String conversationFileName = "conversation.txt";
        File photoFile = new File("path/to/photo.jpg");
        Message photoMessage = new Message("Alice", "Bob", photoFile);

        // Write the message to a file
        photoMessage.writeMessageToFile(conversationFileName);

        // Verify the content of the file
        try (BufferedReader reader = new BufferedReader(new FileReader(conversationFileName))) {
            String line;
            StringBuilder fileContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            String expectedContent = "Sender: Alice\nReceiver: Bob\nPhoto Message: " + photoFile.getAbsolutePath() + "\n";
            Assert.assertEquals("Ensure that the photo message is written to the file correctly", expectedContent.trim(), fileContent.toString().trim());
        } catch (IOException e) {
            Assert.fail("An IO exception was encountered while reading conversation.txt");
        }
    }
}

