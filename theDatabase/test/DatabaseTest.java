import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

/**
 * A program that tests the database.
 * <p>
 * Purdue University -- CS18000 -- Fall 2024
 *
 * @author Elan Smyla, Aarav Mohanty
 * @version November 3rd, 2024
 */

class DatabaseTest {


    private Database database;


    // Setup method that runs before each test
    @BeforeEach
    void setUp() {
        database = new Database(); // Create a new Database instance for each test
    }


    // Cleanup method that runs after each test
    @AfterEach
    void tearDown() {
        // Clear the users list after each test to prevent state carryover
        database.getUsers().clear();
        // Delete the database file if it exists
        File file = new File(Database.DATABASE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }


    @Test
    void testCreateUser() {
        // Should succeed
        Assertions.assertTrue(database.createUser("testUser", "password123", "This is a bio."));
        //        Assertions.assertTrue(database.createUser("testUser", "password123", "This is a bio.", "pfp.png"));
        // Should fail (duplicate)
        Assertions.assertFalse(database.createUser("testUser", "password456", "Another bio."));
        //        Assertions.assertFalse(database.createUser("testUser", "password456", "Another bio.", "pfp2.png"));
    }


    @Test
    void testGetUser() {
        database.createUser("testUser", "password123", "This is a bio.");
        //database.createUser("testUser", "password123", "This is a bio.", "pfp.png");
        User user = database.getUser("testUser");
        assertNotNull(user); // User should be found
        Assertions.assertEquals("testUser", user.getUsername()); // Check username
        assertNull(database.getUser("nonexistentUser")); // Should return null for nonexistent user
    }


    @Test
    void testAuthenticate() {
        boolean userCreated = database.createUser("testUser", "password123", "This is a bio.");
        //boolean userCreated = database.createUser("testUser", "password123", "This is a bio.", "pfp.png");
        assertTrue(userCreated, "User creation failed; test cannot proceed.");

        Assertions.assertTrue(database.authenticate("testUser", "password123")); // Should succeed
        Assertions.assertFalse(database.authenticate("testUser", "wrongPassword")); // Should fail (wrong password)
        Assertions.assertFalse(database.authenticate("nonexistentUser", "password123")); // Should fail (nonexistent user)
        Assertions.assertFalse(database.authenticate("", "password123")); // Should fail (invalid username)
        Assertions.assertFalse(database.authenticate("testUser", "")); // Should fail (invalid password)
    }
}
