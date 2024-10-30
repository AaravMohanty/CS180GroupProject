// This interface defines a contract for managing user data in a system.
// It declares methods for user creation, deletion, retrieval, and authentication.
public interface DataInterface {


    // Creates a new user with the specified username, password, and email.
    User createUser(String username, String password, String email);


    // Deletes the user associated with the specified username.
    boolean deleteUser(String username);


    // Retrieves the user object associated with the specified username.
    User getUser(String username);


    //Authenticates a user based on the provided username and password.
    boolean authenticate(String username, String password);
}
