import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class RunDataBaseTest {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(RunDataBaseTest.class);
        if (result.wasSuccessful()) {
            System.out.println("Excellent - Test ran successfully");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    public static class TestCase {

        @Test
        public void testRunDatabaseClassDeclaration() {
            Class<?> clazz = RunDatabase.class;

            int modifiers = clazz.getModifiers();
            Assert.assertTrue("Ensure that RunDatabase is public!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that RunDatabase is NOT abstract!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that RunDatabase extends Object!", Object.class, clazz.getSuperclass());
            Assert.assertEquals("Ensure that RunDatabase implements no interfaces!", 0, clazz.getInterfaces().length);
        }

        @Test
        public void testStartServer() throws IOException {
            RunDatabase runDatabase = new RunDatabase();
            Thread serverThread = new Thread(() -> runDatabase.startServer());
            serverThread.start();

            // Simulate a client connection
            try (Socket clientSocket = new Socket("localhost", 8888);
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                out.println("Hello Server");
                String response = in.readLine();
                Assert.assertEquals("Echo: Hello Server", response);
            }

            runDatabase.running = false; // Stop the server after the test
            try {
                serverThread.join(1000); // Wait for server thread to finish
            } catch (InterruptedException e) {
                Assert.fail("Server thread was interrupted.");
            }
        }

        @Test
        public void testSaveUser() {
            RunDatabase runDatabase = new RunDatabase();
            User testUser = new User("testUser"); // Assuming a User class exists
            runDatabase.saveUser(testUser);
            // You could add assertions here to verify the user is saved correctly
        }

        @Test
        public void testLoadUser() {
            RunDatabase runDatabase = new RunDatabase();
            User testUser = new User("testUser"); // Assuming a User class exists
            runDatabase.saveUser(testUser);
            User loadedUser = runDatabase.loadUser("testUser");
            Assert.assertNotNull("Ensure the user is loaded", loadedUser);
            Assert.assertEquals("Ensure the loaded user matches the saved user", testUser.getUsername(), loadedUser.getUsername());
        }

        @Test
        public void testSaveMessage() {
            RunDatabase runDatabase = new RunDatabase();
            Message testMessage = new Message("testUser", "Hello World"); // Assuming a Message class exists
            runDatabase.saveMessage(testMessage);
            // Add assertions to verify the message is saved correctly
        }

        @Test
        public void testLoadMessages() {
            RunDatabase runDatabase = new RunDatabase();
            Message testMessage = new Message("testUser", "Hello World"); // Assuming a Message class exists
            runDatabase.saveMessage(testMessage);
            ArrayList<Message> messages = runDatabase.loadMessages("testUser");
            Assert.assertFalse("Ensure messages are loaded", messages.isEmpty());
            Assert.assertEquals("Ensure the loaded message matches", testMessage.getContent(), messages.get(0).getContent());
        }

        // Additional tests can be added here for more functionality as needed
    }
}

