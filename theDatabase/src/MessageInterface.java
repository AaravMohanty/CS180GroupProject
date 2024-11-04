import java.io.*;

public interface MessageInterface {

    String getReceiver();

    String getContent();

    String getPhoto();

    boolean isPhotoMessage();

    void writeMessageToFile(String conversationFileName);
}
