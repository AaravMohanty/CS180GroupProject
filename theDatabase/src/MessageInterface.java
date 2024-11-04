import java.io.*;

/**
 * The message interface for the database class
 *
 * @version November 3, 2024
 */

public interface MessageInterface {

    String getReceiver();

    String getContent();

    String getPhoto();

    boolean isPhotoMessage();

    void writeMessageToFile(String conversationFileName);
}
