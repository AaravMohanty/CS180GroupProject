import java.util.ArrayList;

/**
 * A program that creates the database interface.
 * <p>
 * Purdue University -- CS18000 -- Fall 2024
 *
 * @author Elan Smyla, Aarav Mohanty
 * @version November 3rd, 2024
 */

public interface DatabaseInterface {
    // Creates a new user if the username is not already taken
    boolean createUser(String username, String password, String bio, String pfp);

    // Searches for a user by username
    User getUser(String username);

    // Authenticates a user by checking username and password
    boolean authenticate(String username, String password);

    // Returns the list of users
    ArrayList<User> getUsers();


}
