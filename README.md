# README -- Clarity Social Messaging 

**How to compile and run the program:**
1. Go to your project directory which contains all of this project’s files on terminal and run the command: javac *.java to compile all of the files
2. To run the project with multiple clients, run the "Server.java" class using the following command on terminal: java Server -- followed by as many clients as needed using the following command on terminal: java Client
3. When you run that, the program will give you a menu of options and you can follow the instructions accordingly to create an account, log into an account, or exit, followed by a series of options dependent on what you choose. 

**Submission:**
Elan Smyla - Submitted Vocareum workspace, presentation, and report

# Class Descriptions:

## Database Class: 

Functionality:
Manages storage and retrieval of user data. The class reads from and writes to a database file (database.txt), storing user information such as username, password, bio, friends list file, blocked users file, and conversations file.

Testing:
In order to test the Database class, there is a JUnitTest for each individual method/constructor. Utilizing AssertEquals, we were able to check each method was able to produce the correct output, and we were able to account for instances where there were null or empty inputs. 

Relationship to other classes:
Used by RunProject for user management (creation, retrieval, and authentication). It is also accessed by the User class for profile-related functionalities such as adding/removing, blocking/unblocking, sending messages, etc.

## User Class:

Functionality:
Manages all user information such as username, password, bio, along with methods for managing relationships with other users and friends on the social media platform.

Testing:
To test the User Class, we were able to create a JUnitTest for each individual method/constructor. Utilizing AssertEquals, AssertFalse, AssertNull, we were able to check if the expected values output, and check to make sure each condition holds true in the test. 

Relationship to other classes:
The User class is accessed and modified by the SocialMediaGUI class for user-related actions. The Database class manages instances of User.

## DatabaseInterface Interface:

Functionality:
Defines the core methods that a database class should implement, such as getting texts, and getting receivers. This interface ensures methods like createUser, getUser, and authenticate are implemented.

Testing:
In order to check the DatabaseInterface test, we’re able to utilize the assertNull, assertEquals, and assertTrue to ensure each portion of the interface is accurate. 

Relationship to other classes:
This is the interface for the database class.

## UserInterface Interface:

Functionality:
Defines the core methods that a user class should implement, such as adding friends, removing friends, sending messages, etc.. This interface ensures all appropriate methods are implemented.

Testing:
In order to check the UserInterface test, we’re able to utilize the assertNull, assertEquals, and assertTrue to ensure each portion of the interface is accurate. 

Relationship to other classes:
This is the interface for the user class.

## DatabaseTest Class:

Functionality: 
This is to test the Database class. 

Testing: 
There was no testing for this test class. 

Relationship to other classes:
This class is in correlation to the Database class, and works to test it and works smoothly. 

## UserTest Class:

Functionality: 
Utilizes JUnitTest cases in order to ensure the functionality of the user class.  

Testing:
There was no testing for this test class. 

Relationship to other classes:
This class is in correlation to the User class, and works to test it and works smoothly.

## Server Class

Functionality:
The server class that handles all the logic of our program. Communicates with SocialMediaGUI to determine the action that needs to be taken. Additionally, implements Runnable to create Threads and maintains Thread concurrency.

Testing:
Created a JUnit test to ensure all the methods returned the appropriate outputs based on the inputs that are entered.

Relationship to other classes:
Implements the runnable interface and communicates with the client class to receive and send information.

## Server Interface

Functionality: 
Defines the methods used in the Server class.

## ServerTest Class

Functionality: 
Utilizes JUnitTest cases in order to ensure the functionality of the server class.  

Testing:
There was no testing for this test class. 

Relationship to other classes:
This class is in correlation to the User class, and works to test it and works smoothly.

## SocialMediaAppGUI Interface

Functionality: 
Defines the methods used in the Server class.

## SocialMediaAppGUI Class

Functionality:
The client class handles all the prompting for our user as well as sends information to the server class so that the appropriate logic can be executed based on the user's input into the client.

Testing:
There was no JUnit test for this class as it all involved Network IO.

Relationship to other classes:
Sends and receives information from the server class.

## SocialMediaAppGUI Interface

Functionality: 
Defines the methods used in the SocialMediaAppGUI class.

## SocialMediaAppGUI Test Class

Functionality: 
Utilizes JUnitTest cases in order to ensure the functionality of the SocialMediaGUI class.  

Testing:
There was no testing for this test class. 

Relationship to other classes:
This class is in correlation to the User class, and works to test it and works smoothly.


# How to Run and Use the Application

## Prerequisites

- **Java Development Kit (JDK)**: Ensure you have JDK 11 or above installed.
- **IDE**: An IDE like IntelliJ IDEA or Eclipse for easier development and execution.

## Setup and Installation

1. Clone the repository
2. Open the project in your preferred Java IDE.
3. Ensure all dependencies are resolved (e.g., standard Java libraries).

### Server Setup
1. Run the Server.java file to start the backend server.
   - The server listens on port 1234 by default.
2. You should see a message like Server is running on port 1234....
### Client Setup
1. Run the SocialMediaAppGUI.java file to launch the client interface.
2. The application will connect to the server running on localhost:1234.


#### User Management
- **Sign Up**:
  - Click on "Create Account" in the login GUI.
  - Provide the following details:
    - **Username**: A unique identifier for your account.
    - **Password**: A secure password.
    - **Bio**: A short biography or description about yourself.
  - Click "Create" to register.
  - If the username is already taken, you'll be prompted to choose another username.
- **Log In**:
  - Enter your registered username and password in the login screen.
  - Click "Login" to access your account.
  - If the credentials are invalid, you will be notified to try again.

#### Friend Management
- **Add Friend**:
  - Navigate to the **Friends List** tab in the main menu.
  - Type the username of the user you want to add in the input field.
  - Click "Add Friend" to send a friend request.
  - If successful, the friend will be added to your list. If the user doesn't exist or is already your friend, you'll receive an error message.
- **Remove Friend**:
  - Select a friend from the displayed friend list.
  - Click "Remove Friend" to delete them from your list.

#### Conversations
- **View Conversations**:
  - Navigate to the **Conversations** tab in the main menu.
  - Select a friend from the list to load past messages.
  - You may have to click the refresh button if a conversation was recently added or deleted and when new users are made while the client is running.
- **Send Messages**:
  - Type your message in the text input box.
  - Click the "Send" button to deliver the message.
  - Messages are displayed with the sender's username for context.
- **Delete Messages**:
  - Select the message you want to delete and type it in the input box.
  - Click the "Delete" button to remove it.
  - Only messages sent by you can be deleted.

#### Blocking System
- **Block User**:
  - Go to the **Blocked List** tab.
  - Enter the username of the user you want to block.
  - Click "Block User" to prevent interactions with that user.
  - Blocking removes the user from your friends list automatically.
- **Unblock User**:
  - Select a blocked user from the displayed list.
  - Click "Unblock User" to allow them to interact with you again.

#### Search Users
- Navigate to the **Search Users** tab.
- Enter a username or partial query in the search box.
- Click "Search" to find matching users.
- Select a user from the results to view their profile details, including their username and bio.

#### Logout
- Click the **Logout** button in the main menu to log out of a user.
- You will be redirected to the login screen.
- All active threads for fetching data will be stopped to ensure proper disconnection.
- Exit all clients and terminate the server to stop the program.
- Disclaimer: exiting out of one client will not cause the others to stop or the server to stop.

---

Follow these instructions to make the most out of the application.

