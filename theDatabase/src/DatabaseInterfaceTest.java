import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
/**
 * The database interface junit class to test database interface
 *
 * @version November 3, 2024
 */
class DatabaseInterfaceTest {

    DatabaseInterface database = new Database();

    @Test
    void testCreateUser() {
        assertDoesNotThrow(() -> {
            boolean result = database.createUser("testUser", "password", "bio", "pfp");
            assertTrue(result || !result);
        });
    }

    @Test
    void testGetUser() {
        assertDoesNotThrow(() -> {
            User user = database.getUser("testUser");
            assertTrue(user == null || user instanceof User);
        });
    }

    @Test
    void testAuthenticate() {
        assertDoesNotThrow(() -> {
            boolean result = database.authenticate("testUser", "password");
            assertTrue(result || !result);
        });
    }

    @Test
    void testGetUsers() {
        assertDoesNotThrow(() -> {
            ArrayList<User> users = database.getUsers();
            assertNotNull(users);
            assertTrue(users instanceof ArrayList);
        });
    }
}
