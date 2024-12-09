import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * The user class.
 * <p>
 * Purdue University -- CS18000 -- Fall 2024
 *
 * @author Elan Smyla, Aarav Mohanty, Hannah Cha, Kai Nietzche
 * @version December 8th, 2024
 */

public class User implements UserInterface {
    public Object o = new Object();
    String friendsFileName;
    private String username;
    private String password;
    private String bio;
    private ArrayList<String> friends;
    private ArrayList<String> blockedUsers;
    private ArrayList<String> conversations;
    private ArrayList<ArrayList<String>> messages;
    private String blockedUsersFileName;
    private String conversationsFileName;

    public User(String username, String password, String bio) {
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.friends = new ArrayList<>();
        this.blockedUsers = new ArrayList<>();
        this.conversations = new ArrayList<>();

        if (username == null || username.isEmpty()
                || password == null || password.isEmpty()) {
            throw new IllegalArgumentException(
                    "Username and password cannot be null or empty.");
        }

        try {
            friendsFileName = username + "friends.txt";
            File friendsFile = new File(friendsFileName);
            friendsFile.createNewFile();

            blockedUsersFileName = username + "blockedUsers.txt";
            File blockedUsersFile = new File(blockedUsersFileName);
            blockedUsersFile.createNewFile();
            conversationsFileName = username + "conversations.txt";
            File conversationsFile = new File(conversationsFileName);
            conversationsFile.createNewFile();
        } catch (IOException e) {
            System.err.println("Error initializing files: " + e.getMessage());
        }
        synchronized (o) {

            boolean userExists = false;
            try (BufferedReader reader = new BufferedReader(
                    new FileReader(Database.DATABASE_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split(",");
                    if (details[0].equals(username)) {
                        userExists = true;
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error checking user existence: "
                        + e.getMessage());
            }

            if (!userExists) {
                try (BufferedWriter writer = new BufferedWriter(
                        new FileWriter(Database.DATABASE_FILE, true))) {
                    String userEntry = String.format("%s,%s,%s,%s,%s,%s",
                            username, password, bio, friendsFileName,
                            blockedUsersFileName, conversationsFileName);
                    writer.write(userEntry);
                    writer.newLine();
                    writer.flush();
                } catch (IOException e) {
                    System.err.println("Error saving user to database: " + e.getMessage());
                }
            }

            try (BufferedReader reader = new BufferedReader(
                    new FileReader(friendsFileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    friends.add(line);
                }
            } catch (IOException e) {
                System.err.println("Error reading friends file: " + e.getMessage());
            }

            try (BufferedReader reader = new BufferedReader(
                    new FileReader(blockedUsersFileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    blockedUsers.add(line);
                }
            } catch (IOException e) {
                System.err.println("Error reading blocked users file: " + e.getMessage());
            }

            try (BufferedReader reader = new BufferedReader(
                    new FileReader(conversationsFileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    conversations.add(line);
                }
            } catch (IOException e) {
                System.err.println("Error reading conversations file: "
                        + e.getMessage());
            }

            if (!conversations.isEmpty()) {
                messages = new ArrayList<>();
                for (int index = 0; index < conversations.size(); index++) {
                    String conversationFileName = conversations.get(index);
                    messages.add(index, new ArrayList<>());
                    try (BufferedReader reader1 = new BufferedReader(
                            new FileReader(conversationFileName))) {
                        String line1;
                        while ((line1 = reader1.readLine()) != null) {
                            messages.get(index).add(line1);
                        }
                    } catch (IOException e) {
                        System.err.println("Error reading conversation file: "
                                + e.getMessage());
                    }
                }
            } else {
                messages = new ArrayList<>();
            }
        }
    }

    public List<String> getConversations() {
        return new ArrayList<>(conversations);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean addFriend(User friend) {
        if (friend == null || friend == this
                || friends.contains(friend.getUsername())
                || blockedUsers.contains(friend.getUsername())
                || friend.blockedUsers.contains(username)) {
            return false;
        }

        friends.add(friend.getUsername());
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(friendsFileName, true))) {
            writer.write(friend.getUsername());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error adding friend: "
                    + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean removeFriend(String friendName) {
        if (friendName == null || friendName.isEmpty() || !friends.contains(friendName)) {
            return false;
        }

        boolean removed = friends.remove(friendName);
        if (removed) {
            rewriteToFile(friendsFileName, friends);
        }
        return removed;
    }


    public boolean blockUser(User user) {
        if (user == null || user == this || blockedUsers.contains(user.getUsername())) {
            return false;
        }
        if (Database.users.contains(user)) {
            blockedUsers.add(user.getUsername());
            removeFriend(user.getUsername());
            user.removeFriend(this.username);
            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(blockedUsersFileName, true))) {
                writer.write(user.getUsername());
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                System.err.println("Error blocking user: " + e.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean unblockUser(User user) {
        if (user != null) {
            boolean unblocked = blockedUsers.remove(user.getUsername());
            if (unblocked && Database.users.contains(user)) {
                rewriteToFile(blockedUsersFileName, blockedUsers);
            }
            return unblocked;
        }
        return false;
    }

    public boolean isBlocked(String user) {
        return blockedUsers.contains(user);
    }

    public List<String> getFriends() {
        return friends;
    }

    public List<String> getBlockedUsers() {
        return blockedUsers;
    }

    public synchronized boolean sendMessage(User receiver, String message) {
        if (receiver == null || receiver == this || message == null
                || message.trim().isEmpty() ||
                receiver.isBlocked(username) || isBlocked(receiver.getUsername())
                || !friends.contains(receiver.getUsername())
                || !receiver.getFriends().contains(username)) {
            return false;
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

        reloadMessagesFromFile(conversationFile, index);

        ArrayList<String> conversationMessages = messages.get(index);
        String nMessage = username + ": " + message;
        conversationMessages.add(nMessage);

        rewriteToFile(conversationFile, conversationMessages);

        reloadMessagesFromFile(conversationFile, index);

        return true;
    }

    public synchronized boolean deleteMessage(User receiver, String message) {
        String nMessage = username + ": " + message;

        if (receiver == null || receiver == this || message == null
                || message.trim().isEmpty()
                || receiver.isBlocked(username)
                || isBlocked(receiver.getUsername())
                || !friends.contains(receiver.getUsername())
                || !receiver.getFriends().contains(username)) {
            return false;
        }

        String conversationFile;
        if (username.compareTo(receiver.getUsername()) < 0) {
            conversationFile = username + "_" + receiver.getUsername() + "_Messages.txt";
        } else {
            conversationFile = receiver.getUsername() + "_" + username + "_Messages.txt";
        }

        File file = new File(conversationFile);
        if (!file.exists()) {
            return false;
        }

        int index = conversations.indexOf(conversationFile);
        if (index == -1) {
            return false;
        }

        reloadMessagesFromFile(conversationFile, index);

        ArrayList<String> conversationMessages = messages.get(index);
        boolean messageRemoved = false;

        for (int i = 0; i < conversationMessages.size(); i++) {
            String currentMessage = conversationMessages.get(i);
            if (currentMessage.equals(nMessage)) {
                conversationMessages.remove(i);
                messageRemoved = true;
                break;
            }
        }

        if (messageRemoved) {
            rewriteToFile(conversationFile, conversationMessages);
        }

        reloadMessagesFromFile(conversationFile, index);
        return messageRemoved;
    }

    private synchronized void reloadMessagesFromFile(String conversationFile, int index) {
        ArrayList<String> updatedMessages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(conversationFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                updatedMessages.add(line);
            }
        } catch (IOException e) {
            //System.err.println("Error reloading messages from file: "
            //+ e.getMessage());
        }
        messages.set(index, updatedMessages);
    }

    public synchronized boolean sendPhoto(User receiver, String photoPath) {
        if (receiver == null || receiver == this || photoPath == null
                || !new File(photoPath).exists()
                || !this.friends.contains(receiver.getUsername())
                || receiver.isBlocked(username)
                || isBlocked(receiver.getUsername())) {
            return false;
        }

        String conversationFile;
        if (username.compareTo(receiver.getUsername()) < 0) {
            conversationFile = username + "_" + receiver.getUsername() + "_Messages.txt";
        } else {
            conversationFile = receiver.getUsername() + "_" + username + "_Messages.txt";
        }

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

        String photoFileName = String.format("%s_%s_photo_%d.jpg",
                username, receiver.getUsername(), photoCount);

        try {
            BufferedImage image = ImageIO.read(new File(photoPath));
            ImageIO.write(image, "jpg", new File(photoFileName));

            try (BufferedWriter convWriter = new BufferedWriter(
                    new FileWriter(convFile, true))) {
                convWriter.write(username + ": " + photoFileName);
                convWriter.newLine();
                convWriter.flush();
            }
        } catch (IOException e) {
            System.err.println("Failed to save or write photo: "
                    + e.getMessage());
            return false;
        }
        return true;
    }

    public synchronized void rewriteToFile(String filename, ArrayList<String> list) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filename, false))) {
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
        return String.format("username: \"%s\"\nbio: \"%s\"\n\"",
                username, bio);
    }

    public synchronized ArrayList<String> loadConversation(String selectedUser) {
        String conversationFile = (username.compareTo(selectedUser) < 0)
                ? username + "_" + selectedUser + "_Messages.txt"
                : selectedUser + "_" + username + "_Messages.txt";

        ArrayList<String> messagesForConversation = new ArrayList<>();
        File convFile = new File(conversationFile);

        if (!convFile.exists()) {
            return null;
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