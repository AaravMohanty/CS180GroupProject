import java.io.*;

// The Message class represents a message sent between users, which can be either text or a photo.
public class Message implements MessageInterface{
    private String receiver; // The username of the message receiver
    private String content; // The text content of the message
    private String photoPath; // Optional photo for photo messages
    private boolean photo;

    // Constructor for creating a text message
    public Message(String receiver, String content) {
        // Edge case: Check if receiver or content is null or empty
        if (receiver == null || receiver.isEmpty() || content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Receiver and content must not be null or empty for text messages.");
        }
        this.receiver = receiver; // Set the receiver's username
        this.content = content; // Set the text content
        this.photoPath = null; // No photo for text messages
    }

    // Constructor for creating a photo message
    public Message(String receiver, boolean photo, String photoPath) {
        // Edge case: Check if receiver is null or empty, or if photo is null
        if (receiver == null || receiver.isEmpty() || photoPath == null) {
            throw new IllegalArgumentException("Receiver must not be null or empty, and photo must not be null for photo messages.");
        }
        this.receiver = receiver; // Set the receiver's username
        this.photoPath = photoPath; // Set the photo file
        this.content = null; // No text content for photo messages
    }

    // Getter for the receiver's username
    public String getReceiver() {
        return receiver;
    }

    // Getter for the text content of the message
    public String getContent() {
        return content;
    }

    // Getter for the photo associated with the message
    public String getPhoto() {
        return photoPath;
    }

    // Checks if the message is a photo message
    public boolean isPhotoMessage() {
        return photoPath != null; // Returns true if the photo field is not null
    }

    // Method to write message content to the specified conversation file
    public synchronized void writeMessageToFile(String conversationFileName) {
        // Edge case: Ensure conversation file name is not null or empty
        if (conversationFileName == null || conversationFileName.isEmpty()) {
            throw new IllegalArgumentException("Conversation file name must not be null or empty.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(conversationFileName, true))) {
            writer.write("Receiver: " + receiver);
            writer.write(" - "); // Separator for readability

            if (isPhotoMessage()) {
                // Assuming 'photoPath' is a String that holds the path of the photo
                writer.write("Photo Message: " + photoPath); // Writing the photo path directly
            } else {
                writer.write("Text Message: " + content);
            }
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing message to file: " + e.getMessage());
        }
    }
}
