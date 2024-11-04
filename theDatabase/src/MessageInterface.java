import java.io.*;

/**
 * The message interface to store message information
 *
 * Purdue University -- CS18000 -- Fall 2024 
 *
 * @author Elan Smyla, 11
 * @version November 3rd, 2024
 */

public interface MessageInterface {

    String getReceiver();

    String getContent();

    String getPhoto();

    boolean isPhotoMessage();

    void writeMessageToFile(String conversationFileName);
}
