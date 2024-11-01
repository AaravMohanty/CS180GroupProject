import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList; // Import ArrayList for storing friends and blocked users
import java.util.List; // Import List interface for returning lists

// The User class represents a user in the system, with properties for user details and relationships.
public class User implements UserInterface{
    private String username; // The user's unique username
    private String password; // The user's password
    private String bio; // A short biography or description of the user
    private ArrayList<String> friends; // List of the user's friends
    private ArrayList<String> blockedUsers; // List of users that this user has blocked
    private ArrayList<String> conversations; // All conversations with other
    private String friendsFileName; // File with all friends
    private String blockedUsersFileName; //File with all blocked users
    private String conversationsFileName; //File with all conversations

    // Constructor initializes user properties and creates empty lists for friends and blocked users
    public User(String username, String password, String bio, String databaseFile) {
        this.username = username; // Set the username
        this.password = password; // Set the password
        this.bio = bio; // Set the bio
        this.friends = new ArrayList<>(); // Initialize the friends list
        this.blockedUsers = new ArrayList<>(); // Initialize the blocked users list
        this.conversations = new ArrayList<>(); // Initialize the conversations list
        friendsFileName = username + "friends.txt"; // Create File name for friends
        File friendsFile = new File(friendsFileName); // Create File friends
        blockedUsersFileName = username + "blockedUsers.txt"; // Create File name for blocked users
        File blockedUsersFile = new File(blockedUsersFileName); // Create file blocked users
        conversationsFileName = username + "conversations.txt"; // Create File name for convos
        File conversationsFile = new File(conversationsFileName); // Create File convos
        // Writes the user info onto a line of file named database.txt
        // output of database.txt is as follows:
        // username, password, bio, usernameFriends.txt, usernameBlocked.txt, usernameConvos.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(databaseFile, true))) {
            String userEntry = String.format("%s, %s, %s, %s, %s, %s",
                    username, password, bio, friendsFileName, blockedUsersFileName, conversationsFileName);
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

    // Adds a friend to the user's friends list
    public boolean addFriend(String friend) {
        if (!friends.contains(friend)) { // Check if the friend is not already in the list
            friends.add(friend); // Add the friend
            // write the friend to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(friendsFileName, true))){
                writer.write(friend);
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
    public boolean blockUser(String user) {
        if (!blockedUsers.contains(user)) { // Check if the user is not already blocked
            blockedUsers.add(user); // Add the user to the blocked list
            removeFriend(user); //removing user from friends list
            // write the blocked friend to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(friendsFileName, true))){
                writer.write(user);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true; // Indicate success
        }
        return false; // Indicate failure (user already blocked)
    }

    // Unblocks a user, allowing them to interact with this user again
    public boolean unblockUser(String user) {
        boolean unblocked = blockedUsers.remove(user); // boolean to store whether friend was unblocked or not
        if (unblocked) {
            rewriteToFile(blockedUsersFileName, blockedUsers); // if friend was removed then rewrite the blocked friends file
        }
        return unblocked; // Remove the user from the blocked list and return the result
    }

    // Checks if a user is blocked by this user
    public boolean isBlocked(String user) {
        return blockedUsers.contains(user); // Return true if the user is in the blocked list
    }

    // Returns a list of the user's friends
    public List<String> getFriends() {
        return new ArrayList<>(friends); // Return a copy of the friends list
    }

    // Returns a list of users that this user has blocked
    public List<String> getBlockedUsers() {
        return new ArrayList<>(blockedUsers); // Return a copy of the blocked users list
    }

    // method to send messages and write that to the convo file
    public void sendMessage(User receiver, String message) {
        //TODO: Create a method that writes to the conversations file
        // and creates files between each user sender and receiver.
        // After that you can use the writeMessageToFile method to
        // write the message/photo the appropriate file
        Message messages = new Message(receiver.getUsername(), message); //create new message
        messages.writeMessageToFile(); //TODO: Change file name to appropriate file name
    }

    // method to send photos and write to the convo file
    public void sendPhoto(User receiver, File photo) {
        Message messages = new Message(receiver.getUsername(), photo); //create new photo message
        messages.writeMessageToFile(); //TODO: Change file name to appropriate file name
    }

    // Method to add any info to a specific file.
    // Used to write contents of the arraylists into the specific files
    public void rewriteToFile(String filename, ArrayList<String> list) {
        // made append false so that it updates whenever someone removes someone from their list as well
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))){
            for (String user : list) {
                writer.write(user);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
