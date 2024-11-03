import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList; // Import ArrayList for storing friends and blocked users
import java.util.List; // Import List interface for returning lists

// The User class represents a user in the system, with properties for user details and relationships.
public class User {
    private String username; // The user's unique username
    private String password; // The user's password
    private String bio; // A short biography or description of the user
    private String pfp; //A string that stores the filename for the profile picture
    private ArrayList<String> friends; // List of the user's friends
    private ArrayList<String> blockedUsers; // List of users that this user has blocked
    private ArrayList<String> conversations; // All conversations with other
    private String friendsFileName; // File with all friends
    private String blockedUsersFileName; //File with all blocked users
    private String conversationsFileName; //File with all conversations

    // Constructor initializes user properties and creates empty lists for friends and blocked users
    public User(String username, String password, String bio, String pfp) {
        this.username = username; // Set the username
        this.password = password; // Set the password
        this.bio = bio; // Set the bio
        this.pfp = pfp;
        this.friends = new ArrayList<>(); // Initialize the friends list
        this.blockedUsers = new ArrayList<>(); // Initialize the blocked users list
        this.conversations = new ArrayList<>(); // Initialize the conversations list

        try {
            friendsFileName = username + "friends.txt"; // Create File name for friends
            File friendsFile = new File(friendsFileName); // Create File friends
            friendsFile.createNewFile();
            blockedUsersFileName = username + "blockedUsers.txt"; // Create File name for blocked users
            File blockedUsersFile = new File(blockedUsersFileName); // Create file blocked users
            blockedUsersFile.createNewFile();
            conversationsFileName = username + "conversations.txt"; // Create File name for convos
            File conversationsFile = new File(conversationsFileName); // Create File convos
            conversationsFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Writes the user info onto a line of file named database.txt
        // output of database.txt is as follows:
        // username, password, bio, usernameFriends.txt, usernameBlocked.txt, usernameConvos.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Database.DATABASE_FILE, true))) {
            String userEntry = String.format("%s,%s,%s,%s,%s,%s,%s",
                    username, password, bio, pfp, friendsFileName, blockedUsersFileName, conversationsFileName);
            writer.write(userEntry);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // populating the arraylists with what is already stored in the file for data persistence
        try (BufferedReader reader = new BufferedReader(new FileReader(friendsFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                friends.add((line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(blockedUsersFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                blockedUsers.add((line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(conversationsFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                conversations.add((line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (!friends.contains(friend.getUsername()) && Database.users.contains(friend)) { // Check if the friend is not already in the list
            friends.add(friend.getUsername()); // Add the friend
            // write the friend to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(friendsFileName, true))) {
                writer.write(friend.getUsername());
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true; // Indicate success
        }
        return false; // Indicate failure (friend already exists)
    }

    // Removes a friend from the user's friends list
    public boolean removeFriend(String friend) {
        boolean removed = friends.remove(friend); // boolean to store whether friend was removed or not
        if (removed) {
            rewriteToFile(friendsFileName, friends); // if it was removed then rewrite the friends file
        }
        return removed; // Remove the friend and return the result
    }

    // Blocks a user, preventing them from interacting with this user
    public boolean blockUser(User user) {
        if (user != null) {
            if (!blockedUsers.contains(user.getUsername()) && Database.users.contains(user)) { // Check if the user is not already blocked
                blockedUsers.add(user.getUsername()); // Add the user to the blocked list
                removeFriend(user.getUsername()); //removing user from friends list
                user.removeFriend(this.username);
                // write the blocked friend to the file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(blockedUsersFileName, true))) {
                    writer.write(user.getUsername());
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true; // Indicate success
            }
        }
        return false; // Indicate failure (user already blocked)
    }

    // Unblocks a user, allowing them to interact with this user again
    public boolean unblockUser(User user) {
        if (user != null) {
            boolean unblocked = blockedUsers.remove(user.getUsername()); // boolean to store whether friend was unblocked or not
            if (unblocked && Database.users.contains(user)) {
                rewriteToFile(blockedUsersFileName, blockedUsers); // if friend was removed then rewrite the blocked friends file
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
    public boolean sendMessage(User receiver, String message) {
        // Check if both users exist and if the receiver has not blocked the sender
        if (receiver == null || receiver.isBlocked(username) || isBlocked(receiver.getUsername())) {
            return false; // Return failure if receiver is blocked or null
        }

        // Define a consistent file name based on lexicographical order to avoid duplicate files
        String conversationFile;
        if (username.compareTo(receiver.getUsername()) < 0) {
            conversationFile = username + "_" + receiver.getUsername() + "_Messages.txt";
        } else {
            conversationFile = receiver.getUsername() + "_" + username + "_Messages.txt";
        }

        // Check if this conversation file is already recorded in both users' conversation lists
        if (!conversations.contains(conversationFile)) {
            conversations.add(conversationFile);
            rewriteToFile(conversationsFileName, conversations); // Update the sender's conversation file
        }
        if (!receiver.conversations.contains(conversationFile)) {
            receiver.conversations.add(conversationFile);
            receiver.rewriteToFile(receiver.conversationsFileName, receiver.conversations); // Update the receiver's conversation file
        }

        // Write the message to the conversation file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(conversationFile, true))) {
            writer.write(username + ": " + message); // Prefix the message with sender's username
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Return failure if writing message fails
        }

        return true; // Indicate success
    }

//    public boolean sendPhotoMessage(User receiver, String photoPath) {
//        File photo = new File(photoPath); // Create a File object for the photo
//        if (!photo.exists()) { // Check if the photo file exists
//            System.out.println("Photo file not found: " + photoPath); // Log error
//            return false; // Indicate failure
//        }
//        return sendMessage(receiver, photoPath); // Delegate to internal method
//    }


    // Method to send photos and write the actual photo content to the conversation file
    public boolean sendPhoto(User receiver, File photo) {
        if (receiver == null || receiver.isBlocked(username) || isBlocked(receiver.getUsername())) {
            return false; // Return failure if the receiver is blocked or null
        }

        // Define a consistent file name based on lexicographical order to avoid duplicate files
        String conversationFile;
        if (username.compareTo(receiver.getUsername()) < 0) {
            conversationFile = username + "_" + receiver.getUsername() + "_Messages.txt";
        } else {
            conversationFile = receiver.getUsername() + "_" + username + "_Messages.txt";
        }

        // Check if this conversation file is already recorded in both users' conversation lists
        if (!conversations.contains(conversationFile)) {
            conversations.add(conversationFile);
            rewriteToFile(conversationsFileName, conversations); // Update the sender's conversation file
        }
        if (!receiver.conversations.contains(conversationFile)) {
            receiver.conversations.add(conversationFile);
            receiver.rewriteToFile(receiver.conversationsFileName, receiver.conversations); // Update the receiver's conversation file
        }

        // Record the photo path in the conversation file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(conversationFile, true))) {
            writer.write(username + " sent a photo: " + photo.getPath());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write photo message: " + e.getMessage());
            return false;
        }

        // Update the receiver’s conversation file if necessary
        if (!receiver.conversations.contains(username)) {
            receiver.conversations.add(username);
            receiver.rewriteToFile(receiver.conversationsFileName, receiver.conversations);
        }

        return true; // Indicate success
    }


    // Method to add any info to a specific file.
    // Used to write contents of the arraylists into the specific files
    public void rewriteToFile(String filename, ArrayList<String> list) {
        // made append false so that it updates whenever someone removes someone from their list as well
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            for (String user : list) {
                writer.write(user);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String displayUser() {
        return String.format("username: \"%s\"\nbio: \"%s\"\nprofile picture: \"%s\"",
                username, bio, pfp);
    }
}
