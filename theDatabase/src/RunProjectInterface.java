/**
 * The runProject interface for the runProject class
 *
 * @version November 3, 2024
 */

public interface RunProjectInterface {
    void createAccount();      // Method for creating a new user account

    User login();              // Method for logging in an existing user

    void addFriend();          // Method to add a friend

    void removeFriend();       // Method to remove a friend

    void blockUser();          // Method to block or unblock a user

    void sendMessage();        // Method to send a message to another user

    void viewUserProfile();    // Method to view a user's profile by username

    void search();             // Method to search for users and view profiles

    void viewUserProfile(User user); // Method to view a specific user's profile
}
