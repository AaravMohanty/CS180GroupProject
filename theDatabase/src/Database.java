import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
    public static final String DATABASE_FILE = "database.txt"; // Initializing the database file
    Object o = new Object();

    // Constructor initializes the users and messages lists.
    public Database() {
        users = new ArrayList<>(); // Initialize the users list
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
                                users.add(new User(username, password, bio));
                            }
//                            if (getUser(username) == null) {
//                                users.add(new User(username, password, bio, pfp));
//                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Creates a new user if the username is not already taken
    public boolean createUser(String username, String password, String bio) {
        if (getUser(username) == null) { // Check if the username is available
            // Create the user and add it to the in-memory list
            User newUser = new User(username, password, bio);
            users.add(newUser);

            return true; // Indicate success
        }
        return false; // Indicate failure (username already taken)
    }

//    public boolean createUser(String username, String password, String bio, String pfp) {
//        if (getUser(username) == null) { // Check if the username is available
//            // Create the user and add it to the in-memory list
//            User newUser = new User(username, password, bio, pfp);
//            users.add(newUser);
//
//            return true; // Indicate success
//        }
//        return false; // Indicate failure (username already taken)
//    }


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

    public boolean saveProfilePicture(String username, String photoPath) {
        if (username == null || photoPath == null || !new File(photoPath).exists()) {
            return false; // Invalid input or file does not exist
        }

        String profilePictureFileName = username + "_profile.jpg"; // Define unique file name for profile picture

        try {
            // Load the original image
            BufferedImage image = ImageIO.read(new File(photoPath));

            // Save the profile picture to the disk
            File profilePictureFile = new File(profilePictureFileName);
            ImageIO.write(image, "jpg", profilePictureFile);

            // Update the user's record in the database
            synchronized (o) {
                for (User user : users) {
                    if (user.getUsername().equals(username)) {
                        //user.setPfp(profilePictureFileName); // Update user's profile picture field
                        saveDatabase(); // Persist the changes
                        return true; // Indicate success
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving profile picture: " + e.getMessage());
            return false;
        }

        return false; // User not found or operation failed
    }

    // Helper method to save the entire database back to the file
    private void saveDatabase() {
        synchronized (o) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATABASE_FILE, false))) {
                for (User user : users) {
                    String userEntry = String.format("%s,%s,%s",
                            user.getUsername(), user.getPassword(), user.getBio());
//                    String userEntry = String.format("%s,%s,%s,%s",
//                            user.getUsername(), user.getPassword(), user.getBio(), user.getPfp());
                    writer.write(userEntry);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Error saving database: " + e.getMessage());
            }
        }
    }


    public synchronized ArrayList<User> getUsers() {
        return new ArrayList<>(users);
    }

}