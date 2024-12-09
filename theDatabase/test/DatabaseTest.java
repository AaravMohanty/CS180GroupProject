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

    @BeforeEach
    void setUp() {
        database = new Database();
    }

    @AfterEach
    void tearDown() {
        database.getUsers().clear();
        File file = new File(Database.DATABASE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testCreateUser() {
        Assertions.assertTrue(database.createUser("testUser", "password123", "This is a bio."));
        Assertions.assertFalse(database.createUser("testUser", "password456", "Another bio."));
    }

    @Test
    void testGetUser() {
        database.createUser("testUser", "password123", "This is a bio.");
        User user = database.getUser("testUser");
        assertNotNull(user);
        Assertions.assertEquals("testUser", user.getUsername());
        assertNull(database.getUser("nonexistentUser"));
    }

    @Test
    void testAuthenticate() {
        boolean userCreated = database.createUser("testUser", "password123", "This is a bio.");
        assertTrue(userCreated, "User creation failed; test cannot proceed.");

        Assertions.assertTrue(database.authenticate("testUser", "password123"));
        Assertions.assertFalse(database.authenticate("testUser", "wrongPassword"));
        Assertions.assertFalse(database.authenticate("nonexistentUser", "password123"));
        Assertions.assertFalse(database.authenticate("", "password123"));
        Assertions.assertFalse(database.authenticate("testUser", ""));
    }
}
