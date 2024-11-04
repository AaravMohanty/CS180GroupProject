/**
 * The  interface to run the project
 *
 * Purdue University -- CS18000 -- Fall 2024 
 *
 * @author Elan Smyla, Aarav Mohanty
 * @version November 3rd, 2024
 */

public interface RunProjectInterface {
    void createAccount();      // Method for creating a new user account

    User login();              // Method for logging in an existing user

    void addFriend();          // Method to add a friend

    void removeFriend();       // Method to remove a friend

    void blockUser();          // Method to block or unblock a user

    void sendMessage();        // Method to send a message to another user

    void deleteMessage();      // Method to delete a message to another user

    void sendPhoto();          // Method to send a photo to another user

    void viewUserProfile();    // Method to view a user's profile by username

    void search();             // Method to search for users and view profiles

    void viewUserProfile(User user); // Method to view a specific user's profile
}
