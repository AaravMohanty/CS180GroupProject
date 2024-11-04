import java.util.*;

public interface RunProjectInterface {
    // Method to create a new user account
    void createAccount();

    // Method for user login, returns a User if successful
    User login();

    // Method to add a friend
    void addFriend();

    // Method to remove a friend
    void removeFriend();

    // Method to block or unblock a user
    void blockUser();

    // Method to send a message
    void sendMessage();

    // Method to view a user profile
    void viewUserProfile();

    // Method to display available users and view a selected profile
    void search();

    // Method to view a specific user's profile
    void viewUserProfile(User user);
}
