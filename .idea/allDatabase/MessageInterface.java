// The MessageInterface defines methods for accessing message details.
public interface MessageInterface {

    // Returns the username of the message sender
    String getSender();

    // Returns the username of the message receiver
    String getReceiver();

    // Returns the content of the message
    String getContent();

    // Returns the timestamp of when the message was sent
    double getTimestamp();
}
