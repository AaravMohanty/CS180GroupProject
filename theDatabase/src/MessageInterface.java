import java.io.*;

public interface MessageInterface {

    /**
     * Gets the username of the message receiver.
     * @return the receiver's username.
     */
    String getReceiver();

    /**
     * Gets the text content of the message.
     * @return the text content of the message, or null if it is a photo message.
     */
    String getContent();

    /**
     * Gets the photo file associated with the message.
     * @return the photo file associated with the message, or null if it is a text message.
     */
    String getPhoto();

    /**
     * Checks if the message is a photo message.
     * @return true if the message contains a photo, false otherwise.
     */
    boolean isPhotoMessage();

    /**
     * Writes the message to the specified conversation file.
     * @param conversationFileName the name of the file to write the message to.
     * @throws IllegalArgumentException if the conversation file name is null or empty.
     * @throws IOException if an I/O error occurs while writing to the file.
     */
    void writeMessageToFile(String conversationFileName);
}
