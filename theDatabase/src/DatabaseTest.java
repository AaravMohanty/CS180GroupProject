import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

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
        assertTrue(database.createUser("testUser", "password123", "This is a bio.", "pfp.png"));
        assertFalse(database.createUser("testUser", "password456", "Another bio.", "pfp2.png"));
    }

    @Test
    void testGetUser() {
        database.createUser("testUser", "password123", "This is a bio.", "pfp.png");
        User user = database.getUser("testUser");
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
        assertNull(database.getUser("nonexistentUser"));
    }

    @Test
    void testAuthenticate() {
        database.createUser("testUser", "password123", "This is a bio.", "pfp.png");
        assertTrue(database.authenticate("testUser", "password123"));
        assertFalse(database.authenticate("testUser", "wrongPassword"));
        assertFalse(database.authenticate("nonexistentUser", "password123"));
        assertFalse(database.authenticate("", "password123"));
        assertFalse(database.authenticate("testUser", ""));
    }
}
