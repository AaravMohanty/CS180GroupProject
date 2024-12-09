import java.util.ArrayList;

/**
 * The interface for the database class.
 * <p>
 * Purdue University -- CS18000 -- Fall 2024
 *
 * @author Elan Smyla, Aarav Mohanty, Hannah Cha, Kai Nietzche
 * @version December 8th, 2024
 */

public interface DatabaseInterface {
    boolean createUser(String username, String password, String bio);

    User getUser(String username);

    boolean authenticate(String username, String password);

    ArrayList<User> getUsers();
}
