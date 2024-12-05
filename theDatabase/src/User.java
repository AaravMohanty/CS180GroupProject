import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList; // Import ArrayList for storing friends and blocked users
import java.util.Base64;
import java.util.List; // Import List interface for returning lists

/**
 * The class to create a user
 * <p>
 * Purdue University -- CS18000 -- Fall 2024
 *
 * @author Elan Smyla, Aarav Mohanty
 * @version November 3rd, 2024
 */

// The User class represents a user in the system, with properties for user details and relationships.
public class User implements UserInterface {
    private String username; // The user's unique username
    private String password; // The user's password
    private String bio; // A short biography or description of the user
    private String pfp; //A string that stores the filename for the profile picture
    private ArrayList<String> friends; // List of the user's friends
    private ArrayList<String> blockedUsers; // List of users that this user has blocked
    private ArrayList<String> conversations; // All conversations with other
    private ArrayList<ArrayList<String>> messages;
    String friendsFileName; // File with all friends
    private String blockedUsersFileName; //File with all blocked users
    private String conversationsFileName; //File with all conversations
    public Object o = new Object();

    // Constructor initializes user properties and creates empty lists for friends and blocked users
    public User(String username, String password, String bio, String pfp) {
        this.username = username; // Set the username
        this.password = password; // Set the password
        this.bio = bio; // Set the bio
        this.pfp = pfp;
        this.friends = new ArrayList<>(); // Initialize the friends list
        this.blockedUsers = new ArrayList<>(); // Initialize the blocked users list
        this.conversations = new ArrayList<>(); // Initialize the conversations list

        // Edge case handling for if everything is null or empty
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be null or empty.");
        }

        try {
            friendsFileName = username + "friends.txt"; // Create file name for friends
            File friendsFile = new File(friendsFileName); // Create file friends
            friendsFile.createNewFile();

            blockedUsersFileName = username + "blockedUsers.txt"; // Create file name for blocked users
            File blockedUsersFile = new File(blockedUsersFileName); // Create file blocked users
            blockedUsersFile.createNewFile();
            conversationsFileName = username + "conversations.txt"; // Create file name for convos
            File conversationsFile = new File(conversationsFileName); // Create file convos
            conversationsFile.createNewFile();
        } catch (IOException e) {
            // Print out to console that there is an error with files
            System.err.println("Error initializing files: " + e.getMessage());
        }
        synchronized (o) {
            // Check if user already exists in database.txt
            boolean userExists = false;
            try (BufferedReader reader = new BufferedReader(new FileReader(Database.DATABASE_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split(",");
                    if (details[0].equals(username)) {
                        userExists = true; // User already exists, so don't write again
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error checking user existence: " + e.getMessage());
            }

            // Writes the user info onto a line of file named database.txt
            // Output of database.txt is as follows:
            // username, password, bio, usernameFriends.txt, usernameBlocked.txt, usernameConvos.txt
            // Only write to the database file if the user does not already exist
            if (!userExists) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(Database.DATABASE_FILE, true))) {
                    String userEntry = String.format("%s,%s,%s,%s,%s,%s,%s",
                            username, password, bio, pfp, friendsFileName, blockedUsersFileName, conversationsFileName);
                    writer.write(userEntry);
                    writer.newLine();
                    writer.flush();
                } catch (IOException e) {
                    System.err.println("Error saving user to database: " + e.getMessage());
                }
            }

            // Populate the friends list from the friends file
            try (BufferedReader reader = new BufferedReader(new FileReader(friendsFileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    friends.add(line);
                }
            } catch (IOException e) {
                System.err.println("Error reading friends file: " + e.getMessage());
            }

            // Populate the blockedUsers list from the blocked users file
            try (BufferedReader reader = new BufferedReader(new FileReader(blockedUsersFileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    blockedUsers.add(line);
                }
            } catch (IOException e) {
                System.err.println("Error reading blocked users file: " + e.getMessage());
            }

            // Populate the conversations list from the conversations file
            try (BufferedReader reader = new BufferedReader(new FileReader(conversationsFileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    conversations.add(line); // Populate conversations
                }
            } catch (IOException e) {
                System.err.println("Error reading conversations file: " + e.getMessage());
            }

            // Initialize the messages array only if there are conversations
            if (!conversations.isEmpty()) {
                messages = new ArrayList<>(); // Initialize messages

                // Populate the messages array with conversations
                for (int index = 0; index < conversations.size(); index++) {
                    String conversationFileName = conversations.get(index); // Get the filename of the conversation
                    messages.add(index, new ArrayList<>()); // Initialize the ArrayList for each conversation

                    try (BufferedReader reader1 = new BufferedReader(new FileReader(conversationFileName))) {
                        String line1;
                        while ((line1 = reader1.readLine()) != null) {
                            messages.get(index).add(line1); // Now this won't throw an ArrayIndexOutOfBoundsException
                        }
                    } catch (IOException e) {
                        System.err.println("Error reading conversation file: " + e.getMessage());
                    }
                }
            } else {
                messages = new ArrayList<>(); // Initialize as an empty arraylist if no conversations exist
            }
        }
    }

    public List<String> getConversations() {
        return new ArrayList<>(conversations);
    }

    // Getter for the username
    public String getUsername() {
        return username;
    }

    // Getter for the password
    public String getPassword() {
        return password;
    }

    // Getter for the bio
    public String getBio() {
        return bio;
    }

    // Setter for the bio
    public void setBio(String bio) {
        this.bio = bio; // Update the bio
    }

    public String getPfp() {
        return pfp;
    }

    public void setPfp(String pfp) {
        this.pfp = pfp;
    }

    // Adds a friend to the user's friends list
    public boolean addFriend(User friend) {
        if (friend == null || friend == this || friends.contains(friend.getUsername()) || blockedUsers.contains(friend.getUsername())) {
            return false; // Invalid conditions for adding a friend
        }

        friends.add(friend.getUsername()); // Add the friend to the current user's list
        // Write the friend to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(friendsFileName, true))) {
            writer.write(friend.getUsername());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error adding friend: " + e.getMessage());
            return false;
        }

        return true;
    }

    // Removes a friend from the user's friends list
    public boolean removeFriend(String friendName) {
        if (friendName == null || friendName.isEmpty() || !friends.contains(friendName)) {
            return false; // Invalid conditions for removing a friend
        }

        boolean removed = friends.remove(friendName); // Remove the friend from the list
        if (removed) {
            rewriteToFile(friendsFileName, friends); // Update the friends file
        }
        return removed;
    }


    // Blocks a user, preventing them from interacting with this user
    public boolean blockUser(User user) {
        if (user == null || user == this || blockedUsers.contains(user.getUsername())) {
            return false; // return false if user is null, the same user, or already blocked
        }
        if (Database.users.contains(user)) { // Check if the user is not already blocked
            blockedUsers.add(user.getUsername()); // Add the user to the blocked list
            removeFriend(user.getUsername()); //removing user from friends list
            user.removeFriend(this.username);
            // write the blocked friend to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(blockedUsersFileName, true))) {
                writer.write(user.getUsername());
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                System.err.println("Error blocking user: " + e.getMessage());
                return false;
            }
            return true; // return true
        }
        return false; // return false (user already blocked)
    }

    // Unblocks a user, allowing them to interact with this user again
    public boolean unblockUser(User user) {
        if (user != null) {
            // boolean to store whether friend was unblocked or not
            boolean unblocked = blockedUsers.remove(user.getUsername());
            if (unblocked && Database.users.contains(user)) {
                // if friend removed rewrite the blocked friends file
                rewriteToFile(blockedUsersFileName, blockedUsers);
            }
            return unblocked; // Remove the user from the blocked list and return the result
        }
        return false;
    }

    // Checks if a user is blocked by this user
    public boolean isBlocked(String user) {
        return blockedUsers.contains(user); // Return true if the user is in the blocked list
    }

    // Returns a list of the user's friends
    public List<String> getFriends() {
        return friends; // Return a copy of the friends list
    }

    // Returns a list of users that this user has blocked
    public List<String> getBlockedUsers() {
        return blockedUsers; // Return a copy of the blocked users list
    }

    // method to send messages and write that to the convo file
    public synchronized boolean sendMessage(User receiver, String message) {
        if (receiver == null || receiver == this || message == null || message.trim().isEmpty() ||
                receiver.isBlocked(username) || isBlocked(receiver.getUsername())) {
            return false; // Invalid operation
        }

        String conversationFile;
        if (username.compareTo(receiver.getUsername()) < 0) {
            conversationFile = username + "_" + receiver.getUsername() + "_Messages.txt";
        } else {
            conversationFile = receiver.getUsername() + "_" + username + "_Messages.txt";
        }

        int index = conversations.indexOf(conversationFile);
        if (index == -1) {
            conversations.add(conversationFile);
            messages.add(new ArrayList<>());
            index = conversations.size() - 1;
        }

        // Reload messages from file to ensure synchronization
        reloadMessagesFromFile(conversationFile, index);

        ArrayList<String> conversationMessages = messages.get(index);
        String nMessage = username + ": " + message;
        conversationMessages.add(nMessage);

        rewriteToFile(conversationFile, conversationMessages);

        // Reload messages from the file to keep all clients synchronized
        reloadMessagesFromFile(conversationFile, index);

        return true;
    }


    // method to delete messages and write that to the convo file
    public synchronized boolean deleteMessage(User receiver, String message) {
        String nMessage = username + ": " + message; // Message format including the current user's username

        if (receiver == null || receiver == this || message == null || message.trim().isEmpty() ||
                receiver.isBlocked(username) || isBlocked(receiver.getUsername())) {
            return false; // Invalid operation
        }

        String conversationFile;
        if (username.compareTo(receiver.getUsername()) < 0) {
            conversationFile = username + "_" + receiver.getUsername() + "_Messages.txt";
        } else {
            conversationFile = receiver.getUsername() + "_" + username + "_Messages.txt";
        }

        File file = new File(conversationFile);
        if (!file.exists()) {
            return false; // Conversation file does not exist
        }

        int index = conversations.indexOf(conversationFile);
        if (index == -1) {
            return false; // Conversation not found in memory
        }

        // Reload messages from file to ensure synchronization
        reloadMessagesFromFile(conversationFile, index);

        ArrayList<String> conversationMessages = messages.get(index);
        boolean messageRemoved = false;

        for (int i = 0; i < conversationMessages.size(); i++) {
            String currentMessage = conversationMessages.get(i);
            if (currentMessage.equals(nMessage)) { // Delete only the current user's message
                conversationMessages.remove(i);
                messageRemoved = true;
                break;
            }
        }

        if (messageRemoved) {
            // Rewrite updated messages to the file
            rewriteToFile(conversationFile, conversationMessages);
        }

        // Reload messages from the file to keep all clients synchronized
        reloadMessagesFromFile(conversationFile, index);

        return messageRemoved;
    }

    private synchronized void reloadMessagesFromFile(String conversationFile, int index) {
        ArrayList<String> updatedMessages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(conversationFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                updatedMessages.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reloading messages from file: " + e.getMessage());
        }
        messages.set(index, updatedMessages); // Update the in-memory messages
    }

    public synchronized boolean sendPhoto(User receiver, String photoPath) {
        // Check if the receiver is valid, the photo path exists, and the users are not blocked
        if (receiver == null || receiver == this || photoPath == null ||
                !new File(photoPath).exists() || !this.friends.contains(receiver.getUsername()) ||
                receiver.isBlocked(username) || isBlocked(receiver.getUsername())) {
            return false; // Return failure if receiver, photo path is invalid, or blocked
        }

        // Define a consistent file name based on lexicographical order to avoid duplicate files
        String conversationFile;
        if (username.compareTo(receiver.getUsername()) < 0) {
            conversationFile = username + "_" + receiver.getUsername() + "_Messages.txt";
        } else {
            conversationFile = receiver.getUsername() + "_" + username + "_Messages.txt";
        }

        // Ensure the conversation file exists
        File convFile = new File(conversationFile);
        if (!convFile.exists()) {
            try {
                convFile.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating conversation file: " + e.getMessage());
                return false;
            }
        }

        int photoCount = 0;

        // Count existing photo entries in the conversation file to ensure a unique photo name
        try (BufferedReader reader = new BufferedReader(new FileReader(conversationFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("_photo")) {
                    photoCount++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading conversation file for photo count: " + e.getMessage());
            return false;
        }

        // Generate a unique file name for the photo
        String photoFileName = String.format("%s_%s_photo_%d.jpg", username, receiver.getUsername(), photoCount);

        // Read and save the image file
        try {
            BufferedImage image = ImageIO.read(new File(photoPath)); // Load the original image
            ImageIO.write(image, "jpg", new File(photoFileName)); // Save it directly as a JPG file

            // Log the saved photo file in the conversation file
            try (BufferedWriter convWriter = new BufferedWriter(new FileWriter(convFile, true))) {
                convWriter.write(username + ": " + photoFileName); // Record the filename in the conversation
                convWriter.newLine();
                convWriter.flush();
            }
        } catch (IOException e) {
            System.err.println("Failed to save or write photo: " + e.getMessage());
            return false;
        }

        return true; // Indicate success
    }


    // Method to add any info to a specific file.
    // Used to write contents of the arraylists into the specific files
    public synchronized void rewriteToFile(String filename, ArrayList<String> list) {
        // made append false so that it updates whenever someone removes someone from their list as well
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            for (String user : list) {
                writer.write(user);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String displayUser() {
        return String.format("username: \"%s\"\nbio: \"%s\"\nprofile picture: \"%s\"",
                username, bio, pfp);
    }


    // Method to load messages for a given conversation
    public synchronized ArrayList<String> loadConversation(String selectedUser) {
        // Generate conversation file name in lexicographical order
        String conversationFile = (username.compareTo(selectedUser) < 0)
                ? username + "_" + selectedUser + "_Messages.txt"
                : selectedUser + "_" + username + "_Messages.txt";

        ArrayList<String> messagesForConversation = new ArrayList<>();
        File convFile = new File(conversationFile);

        if (!convFile.exists()) {
            return null; // Return null if the conversation file doesn't exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(convFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                messagesForConversation.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error loading conversation: " + e.getMessage());
        }

        return messagesForConversation;
    }


}