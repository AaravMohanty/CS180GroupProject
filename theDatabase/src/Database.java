import java.io.*;
import java.util.ArrayList; // Import ArrayList for storing users and messages

/**
 * A program that creates the database.
 * <p>
 * Purdue University -- CS18000 -- Fall 2024
 *
 * @author Elan Smyla, Aarav Mohanty
 * @version November 3rd, 2024
 */

// The Database class manages users and messages in a simple in-memory storage system.
public class Database implements DatabaseInterface {
    public static ArrayList<User> users; // List to store User objects
    private ArrayList<Message> messages; // List to store Message objects
    public static final String DATABASE_FILE = "database.txt"; // Initializing the database file
    Object o = new Object();

    // Constructor initializes the users and messages lists.
    public Database() {
        users = new ArrayList<>(); // Initialize the users list
        messages = new ArrayList<>(); // Initialize the messages list
        synchronized (o) {
            File databaseFile = new File(DATABASE_FILE); // create database file

            try {
                databaseFile.createNewFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_FILE))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] data = line.split(",");

                        // Ensure the data array contains at least four elements before accessing them
                        if (data.length >= 4) {
                            String username = data[0];
                            String password = data[1];
                            String bio = data[2];
                            String pfp = data[3];

                            // Avoid adding duplicate users
                            if (getUser(username) == null) {
                                users.add(new User(username, password, bio, pfp));
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Creates a new user if the username is not already taken
    public boolean createUser(String username, String password, String bio, String pfp) {
        if (getUser(username) == null) { // Check if the username is available
            // Create the user and add it to the in-memory list
            User newUser = new User(username, password, bio, pfp);
            users.add(newUser);

            // Initialize conversation files between the new user and all other users
            String newUsername = newUser.getUsername();

            // Create conversations with all existing users
            for (User existingUser : users) {
                if (!existingUser.getUsername().equals(newUsername)) {
                    String conversationFile;
                    if (newUsername.compareTo(existingUser.getUsername()) < 0) {
                        conversationFile = newUsername + "_" + existingUser.getUsername() + "_Messages.txt";
                    } else {
                        conversationFile = existingUser.getUsername() + "_" + newUsername + "_Messages.txt";
                    }

                    // Create the conversation file
                    synchronized (o) {
                        try {
                            // Create the conversation file
                            File convFile = new File(conversationFile);
                            convFile.createNewFile();

                            // Append the conversation filename to the user's conversation file
                            String fileName = existingUser.getUsername() + "conversations.txt";
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
                                writer.write(conversationFile + "\n");
                            }

                            // Also append to the new user's conversation file
                            fileName = newUser.getUsername() + "conversations.txt";
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
                                writer.write(conversationFile + "\n");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            return true; // Indicate success
        }
        return false; // Indicate failure (username already taken)
    }


    // Searches for a user by username
    public User getUser(String username) {
        // Edge case: username is null or empty
        if (username == null || username.isEmpty()) {
            return null; // Return null if username is invalid
        }
        for (User user : users) { // Iterate through the user list
            if (user != null && user.getUsername().equals(username)) { // Check if username matches
                return user; // Return the found user
            }
        }
        return null; // Return null if no user is found
    }

    // Authenticates a user by checking username and password
    public boolean authenticate(String username, String password) {
        // Edge cases:
        // 1. Username or password is null or empty
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return false; // Indicate failure due to invalid credentials
        }

        User user = getUser(username); // Find the user
        return user != null && user.getPassword().equals(password); // Check if password matches
    }

    public ArrayList<User> getUsers() {
        return users;
    }


}
