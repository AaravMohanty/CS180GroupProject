README

How to compile and run the program:
Go to your project directory which contains all of this project’s files on terminal and run the command: javac *.java to compile all of the files
After compiling, you can run the main method class called “RunProject.java” using the following command on terminal: java runProject
When you run that, the program will give you a menu of options and you can follow the instructions accordingly to create an account, log into an account, add friends, remove friends, message users, block users, unblock users, and view users.

Submission:
Elan Smyla - Submitted Vocareum workspace

Class Description:
Database Class: 
Functionality:
Manages storage and retrieval of user data. The class reads from and writes to a database file (database.txt), storing user information such as username, password, bio, profile picture filename, friends list file, blocked users file, and conversations file.
Testing:
 In order to test the Database class, there is a JUnitTest for each individual method/constructor. Utilizing AssertEquals, we were able to check each method was able to produce the correct output, and we were able to account for instances where there were null or empty inputs. 
Relationship to other classes:
Used by RunProject for user management (creation, retrieval, and authentication). It is also accessed by the User class for profile-related functionalities such as adding/removing, blocking/unblocking, sending messages, etc.

User Class:
Functionality:
Manages all user information such as username, password, bio, profile picture, along with methods for managing relationships with other users and friends on the social media platform.
Testing:
To test the User Class, we were able to create a JUnitTest for each individual method/constructor. Utilizing AssertEquals, AssertFalse, AssertNull, we were able to check if the expected values output, and check to make sure each condition holds true in the test. 
Relationship to other classes:
The User class is accessed and modified by the RunProject class for user-related actions and utilizes the Message class for the methods that require sending messages. The Database class manages instances of User.

Message Class:
Functionality:
Creates a message, which can either be text or a photo. Includes methods to retrieve message content and to write message details to a specified conversation file.
Testing:
In order to test the message class, there is a JUnitTest for each individual method/constructor. For both constructors in the message class, we utilized the AssertEquals method inorder to ensure each component equaled the expected value. 
Relationship to other classes:
Used by User for sending and receiving messages and is called by RunProject to manage message operations between users.

RunProject Class:
Functionality:
It is the main method for the program. Manages user interactions, such as creating accounts, logging in, viewing profiles, adding friends, blocking users, and sending messages.
Testing:
Inorder to check the RunProject class, there is a JUnitTest for each method. In order to verify we utilize the AssertTrue method. 
Relationship to other classes:
This class essentially runs the whole program by utilizing the functions in all the other three classes to run the social media app.

DatabaseInterface Interface:
Functionality:
Defines the core methods that a database class should implement, such as getting texts, getting photos, and getting receivers. This interface ensures methods like createUser, getUser, and authenticate are implemented.
Testing:
In order to check the DatabaseInterface test, we’re able to utilize the assertNull, assertEquals, and assertTrue to ensure each portion of the interface is accurate. 
Relationship to other classes:
This is the interface for the database class.

MessageInterface Interface:
Functionality:
Defines the core methods that a message class should implement, such as creating users, retrieving users, and authenticating users. This interface ensures methods like getReceiver, getContent, and getPhoto are implemented.
Testing:
In order to check the MessageInterface test, we’re able to utilize the assertNull, assertEquals, and assertTrue to ensure each portion of the interface is accurate. 
Relationship to other classes:
This is the interface for the message class.

UserInterface Interface:
Functionality:
Defines the core methods that a user class should implement, such as adding friends, removing friends, sending messages, etc.. This interface ensures all appropriate methods are implemented.
Testing:
In order to check the UserInterface test, we’re able to utilize the assertNull, assertEquals, and assertTrue to ensure each portion of the interface is accurate. 
Relationship to other classes:
This is the interface for the user class.

RunProject Interface:
Functionality:
Defines the core methods that a RunProject class should implement, such as creating accounts, logging in, and viewing profiles. 
Testing:
In order to check the RunProject test, we’re able to utilize the assertNull, assertEquals, and assertTrue to ensure each portion of the interface is accurate. By including these methods we are able to call the original methods and check if the outputs are up to standard. 
Relationship to other classes:
This is the interface for the RunProject class.

DatabaseTest Class:
Functionality: 
This is to test the Database class. 
Testing: 
There was no testing for this test class. 
Relationship to other classes:
This class is in correlation to the Database class, and works to test it and works smoothly. 

MessageTest Class:
Functionality: 
This is to test the Message class. 
Testing: 
There was no testing for this test class. 
Relationship to other classes:
This class is in correlation to the Message class, and works to test it and works smoothly. 

UserTest Class:
Functionality: 
Utilizes JUnitTest cases in order to ensure the functionality of the user class.  
Testing:
There was no testing for this test class. 
Relationship to other classes:
This class is in correlation to the User class, and works to test it and works smoothly. 






