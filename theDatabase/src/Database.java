import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList; // Import ArrayList for storing users and messages

/**
 * The Database class establishes access to all data.
 * <p>
 * Purdue University -- CS18000 -- Fall 2024
 *
 * @author Elan Smyla, Aarav Mohanty, Hannah Cha, Kai Nietzche
 * @version December 8th, 2024
 */

public class Database implements DatabaseInterface {
    public static ArrayList<User> users;
    public static final String DATABASE_FILE = "database.txt";
    Object o = new Object();

    public Database() {
        users = new ArrayList<>();
        synchronized (o) {
            File databaseFile = new File(DATABASE_FILE);

            try {
                databaseFile.createNewFile();
                try (BufferedReader reader =
                             new BufferedReader(new FileReader(DATABASE_FILE))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] data = line.split(",");

                        if (data.length >= 4) {
                            String username = data[0];
                            String password = data[1];
                            String bio = data[2];
                            String pfp = data[3];

                            if (getUser(username) == null) {
                                users.add(new User(username, password, bio));
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean createUser(String username,
                              String password, String bio) {
        if (getUser(username) == null) {
            User newUser = new User(username, password, bio);
            users.add(newUser);

            return true;
        }
        return false;
    }

    public User getUser(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }
        for (User user : users) {
            if (user != null && user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean authenticate(String username, String password) {
        if (username == null || password == null
                || username.isEmpty() || password.isEmpty()) {
            return false;
        }

        User user = getUser(username);
        return user != null && user.getPassword().equals(password);
    }

    public boolean saveProfilePicture(String username, String photoPath) {
        if (username == null || photoPath == null
                || !new File(photoPath).exists()) {
            return false;
        }

        String profilePictureFileName = username + "_profile.jpg";

        try {
            BufferedImage image = ImageIO.read(new File(photoPath));

            File profilePictureFile = new File(profilePictureFileName);
            ImageIO.write(image, "jpg", profilePictureFile);

            synchronized (o) {
                for (User user : users) {
                    if (user.getUsername().equals(username)) {
                        saveDatabase();
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving profile picture: " + e.getMessage());
            return false;
        }

        return false;
    }

    private void saveDatabase() {
        synchronized (o) {
            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(DATABASE_FILE, false))) {
                for (User user : users) {
                    String userEntry = String.format("%s,%s,%s",
                            user.getUsername(), user.getPassword(), user.getBio());
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