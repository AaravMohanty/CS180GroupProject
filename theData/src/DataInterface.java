/**
 //This interface defines a contract for managing user data in a system.
 //It declares methods for user creation, deletion, retrieval, authentication, 
 //and management of additional user properties such as bio, friends, and blocked users.
 */
public interface DataInterface {

     //Creates a new user with the specified username, password, and bio.
    boolean createUser(String username, String password, String bio);

    //Retrieves the user object associated with the specified username.
    User getUser(String username);

    //Authenticates a user based on the provided username and password.
    boolean authenticate(String username, String password);
}
