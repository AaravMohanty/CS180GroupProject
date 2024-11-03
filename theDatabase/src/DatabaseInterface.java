/**
 //This interface defines a contract for managing user data in a system.
 //It declares methods for user creation, deletion, retrieval, authentication, 
 //and management of additional user properties such as bio, friends, and blocked users.
 */
public interface DatabaseInterface {

     //Creates a new user with the specified username, password, and bio.
    boolean createUser(String username, String password, String bio, String pfp);

    //Retrieves the user object associated with the specified username.
    User getUser(String username);

    //gives true/false whether or not sent
    boolean sendTextMessage(String sender, String receiver, String content);

    //gives true/false whether or not sent pic
    boolean sendPhotoMessage(String sender, String receiver, String photoPath);

    //meets criteria for sending a msg
    boolean sendMessageInternal(String sender, String receiver, Message message);

    //Authenticates a user based on the provided username and password.
    boolean authenticate(String username, String password);
}
