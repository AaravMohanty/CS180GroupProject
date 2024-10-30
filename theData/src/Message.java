// The Message class represents a message exchanged between users in the social network.
public class Message {
    private String sender;    // The username of the message sender
    private String receiver;  // The username of the message receiver
    private String content;   // The content of the message

    // Constructor to initialize a new Message object with sender, receiver, and content
    public Message(String sender, String receiver, String content) {
        this.sender = sender;    // Set the sender's username
        this.receiver = receiver; // Set the receiver's username
        this.content = content;   // Set the message content
    }

    // Returns the username of the sender
    public String getSender() {
        return sender;
    }

    // Returns the username of the receiver
    public String getReceiver() {
        return receiver;
    }

    // Returns the content of the message
    public String getContent() {
        return content;
    }
}
