//import org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Test;
//import java.util.ArrayList;
//import java.util.*;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import java.io.*;
//
//
//public class DatabaseTest {
//
//    ArrayList<User> users = new ArrayList<User>();
//    private static final String tempFile = Database.DATABASE_FILE;
//    Database first = new Database();
//
//    public void setUp() throws IOException{ //this is to set up
//        Database first = new Database();
//        ArrayList<User> users = first.getUsers();
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
//            writer.write("user1,password1,bio1,pfp1,dFile1\n");
//        }
//    }
//    void testDatabaseConstructor() {
//        assertNotNull(users, "Users list has users and is not null");
//        User user1 = users.get(0); // first user in the users arraylist
//        assertEquals("user1", user1.getUsername()); // assertequals is just a way to check if first and second "" are eq
//        assertEquals("password1", user1.getPassword());
//        assertEquals("bio1", user1.getBio());
//        assertEquals("pfp1", user1.getPfp());
//        assertEquals("dFile1", user1.getDFile());
//        ArrayList<Message> messages = first.getMessages();  // Assuming a getter for messages
//        assertNotNull(messages, "Messages list should be initialized");
//        assertTrue(messages.isEmpty(), "Messages list should be empty initially");
//
//    }
//
//    void testGetDatabaseFile() {
//        String result = Database.getDatabaseFile();
//        assertEquals("database.db", result, "The getDatabaseFile method should return the correct file.");
//    }
//
//    boolean createUser(String username, String password, String bio, String pfp) {
//        boolean result = first.createUser("testUser", "password123", "This is a bio.", "pfp.png");
//
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//}