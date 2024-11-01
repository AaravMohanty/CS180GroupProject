import java.io.File;

public interface MessageInterface {
        // Gets the receiver of the message
    String getReceiver();

    // Gets the content of the message
    String getContent();

    // Gets the photo associated with the message
    File getPhoto();

    // Checks if the message is a photo message
    boolean isPhotoMessage();
}
