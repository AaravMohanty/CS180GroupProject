import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
    private Database database;

    @BeforeEach
    void setUp() {
        database = new Database();
    }

    @AfterEach
    void tearDown() {
        File databaseFile = new File(Database.DATABASE_FILE);
        if (databaseFile.exists()) {
            databaseFile.delete(); // Clean up database file
        }
    }

    @Test
    void testCreateUser() {
        boolean created = database.createUser("newUser", "password", "This is a bio");
        assertTrue(created);

        User newUser = database.getUser("newUser");
        assertNotNull(newUser);
        assertEquals("newUser", newUser.getUsername());
        assertEquals("password", newUser.getPassword());
        assertEquals("This is a bio", newUser.getBio());
    }

    @Test
    void testAuthenticateUser() {
        database.createUser("testUser", "testPass", "Test Bio");

        boolean authenticated = database.authenticate("testUser", "testPass");
        assertTrue(authenticated);

        boolean failedAuth = database.authenticate("testUser", "wrongPass");
        assertFalse(failedAuth);
    }

    @Test
    void testAddFriend() {
        database.createUser("user1", "pass1", "bio1");
        database.createUser("user2", "pass2", "bio2");

        User user1 = database.getUser("user1");
        User user2 = database.getUser("user2");

        boolean success = user1.addFriend(user2);
        assertTrue(success);
        assertTrue(user1.getFriends().contains("user2"));
    }
}
