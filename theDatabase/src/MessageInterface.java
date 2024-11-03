import java.io.File;

public interface MessageInterface {

    // Gets the username of the message receiver
    String getReceiver();

    // Gets the text content of the message, or null if it is a photo message
    String getContent();

    // Gets the photo file associated with the message, or null if it is a text message
    File getPhoto();

    // Checks if the message is a photo message (returns true if the message contains a photo, false otherwise)
    boolean isPhotoMessage();

    // Writes the message to the specified conversation file
    void writeMessageToFile(String conversationFileName);
}
