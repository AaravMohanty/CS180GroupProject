/**
 //This interface defines a contract for managing user data in a system.
 //It declares methods for user creation, deletion, retrieval, authentication, 
 //and management of additional user properties such as bio, friends, and blocked users.
 */
public interface DataInterface {

     //Creates a new user with the specified username, password, email, and bio.
    User createUser(String username, String password, String email, String bio);

    //Deletes the user associated with the specified username.
    boolean deleteUser(String username);

    //Retrieves the user object associated with the specified username.
    User getUser(String username);


    //Authenticates a user based on the provided username and password.
    boolean authenticate(String username, String password);

    //Updates the bio of the specified user.
    boolean updateBio(String username, String newBio);


    //Adds a user to the friend list of the specified user.
    boolean addFriend(String username, String friendUsername);

    //Removes a user from the friend list of the specified user.
    boolean removeFriend(String username, String friendUsername);


    //Blocks a user from interacting with the specified user.
    boolean blockUser(String username, String blockedUsername);

    //Unblocks a previously blocked user.
    boolean unblockUser(String username, String blockedUsername);

    
     // Checks if a specific user is blocked by another user.
    boolean isBlocked(String username, String blockedUsername);
}
