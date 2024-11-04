import java.util.*;

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
