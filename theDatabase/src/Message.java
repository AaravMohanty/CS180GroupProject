    import java.io.*;

    // The Message class represents a message sent between users, which can be either text or a photo.
    public class Message implements MessageInterface {
        private String receiver; // The username of the message receiver
        private String content; // The text content of the message
        private File photo; // Optional photo for photo messages

        // Constructor for creating a text
        public Message(String receiver, String content) {
            this.receiver = receiver; // Set the receiver's username
            this.content = content; // Set the text content
            this.photo = null; // No photo for text messages
        }

        // Constructor for creating a photo message
        public Message(String receiver, File photo) {
            this.receiver = receiver; // Set the receiver's username
            this.photo = photo; // Set the photo file
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
        public File getPhoto() {
            return photo;
        }

        // Checks if the message is a photo message
        public boolean isPhotoMessage() {
            return photo != null; // Returns true if the photo field is not null
        }

        // Method to write message content to the specified conversation file
        public void writeMessageToFile(String conversationFileName) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(conversationFileName, true))) {
                writer.write("Receiver: " + receiver);

                if (isPhotoMessage()) {
                    writer.write("Photo Message: " + photo);
                } else {
                    writer.write("Text Message: " + content);
                }
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
