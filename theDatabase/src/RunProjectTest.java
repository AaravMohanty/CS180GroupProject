import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;

/**
 * The test for the class to run the project
 * <p>
 * Purdue University -- CS18000 -- Fall 2024
 *
 * @author Elan Smyla, Aarav Mohanty
 * @version November 3rd, 2024
 */

public class RunProjectTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(outputStream));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testMainMenuExit() {
        // Simulate user input: Choose option "3" to exit the program immediately
        String simulatedInput = "3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Call main method
        RunProject.main(new String[]{});

        // Capture output and verify that exit message is displayed
        String output = outputStream.toString();
        assertTrue("Output should contain exit message.", output.contains("Have a nice day!"));
    }

    @Test
    public void testCreateAccountAndExit() {
        // Simulate user input: Choose "1" to create a new user, then "3" to exit
        String simulatedInput = "1\na\na\na\na\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Call main method
        RunProject.main(new String[]{});

        // Capture output and verify expected steps are reached and that it reaches the end
        String output = outputStream.toString();
        assertTrue("Output should contain main menu.", output.contains("Main Menu:"));
        assertTrue("Output should contain account creation prompt.", output.contains("Create a New User"));
        assertTrue("Output should contain exit message.", output.contains("Have a nice day!"));
    }

    @Test
    public void testInvalidChoice() {
        // Simulate invalid choice "4", then "3" to exit
        String simulatedInput = "4\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Call main method
        RunProject.main(new String[]{});

        // Capture output and check for invalid choice message
        String output = outputStream.toString();
        assertTrue("Output should indicate invalid choice.",
                   output.contains("Invalid choice. Please enter 1, 2, or 3."));
        assertTrue("Output should contain exit message.", output.contains("Have a nice day!"));
    }

    @Test
    public void testLogIn() {
        // Simulate user input: Choose "1" to create a new user, then "3" to exit
        String simulatedInput = "1\na\na\na\na\n2\na\na\n9\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Call main method
        RunProject.main(new String[]{});

        // Capture output and verify expected steps are reached and that it reaches the end
        String output = outputStream.toString();
        assertTrue("Output should contain main menu.", output.contains("Main Menu:"));
        assertTrue("Output should contain account creation prompt.", output.contains("Create a New User"));
        assertTrue("Output should contain exit message.", output.contains("Have a nice day!"));
    }

    @Test
    public void testAddFriend() {
        // Simulate user input: Choose "1" to create a new user, then "3" to exit
        String simulatedInput = "1\na\na\na\na\n" + "1\nb\nb\nb\nb\n" +
                "2\na\na\n1\nb\n9\n3";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Call main method
        RunProject.main(new String[]{});

        // Capture output and verify expected steps are reached and that it reaches the end
        String output = outputStream.toString();
        assertTrue("Output should contain main menu.", output.contains("Main Menu:"));
        assertTrue("Output should contain account creation prompt.", output.contains("Create a New User"));
        assertTrue("Output should contain add user prompt.", output.contains("b has been added to your friends.\n"));
        assertTrue("Output should contain exit message.", output.contains("Have a nice day!"));
    }

    @Test
    public void testRemoveFriend() {
        // Simulate user input: Choose "1" to create a new user, then "3" to exit
        String simulatedInput = "1\na\na\na\na\n" + "1\nb\nb\nb\nb\n" +
                "2\na\na\n" + "1\nb\n" + "2\nb\n9\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Call main method
        RunProject.main(new String[]{});

        // Capture output and verify expected steps are reached and that it reaches the end
        String output = outputStream.toString();
        assertTrue("Output should contain main menu.", output.contains("Main Menu:"));
        assertTrue("Output should contain account creation prompt.",
                   output.contains("Create a New User"));
        assertTrue("Output should contain remove user prompt.", 
                   output.contains("b has been removed from your friends.\n"));
        assertTrue("Output should contain exit message.", output.contains("Have a nice day!"));
    }

    @Test
    public void testBlockUnblockUser() {
        // Simulate user input: Choose "1" to create a new user, then "3" to exit
        String simulatedInput = "1\na\na\na\na\n" + "1\nb\nb\nb\nb\n" +
                "2\na\na\n" + "1\nb\n" + "3\nb\nb\n9\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Call main method
        RunProject.main(new String[]{});

        // Capture output and verify expected steps are reached and that it reaches the end
        String output = outputStream.toString();
        assertTrue("Output should contain main menu.", output.contains("Main Menu:"));
        assertTrue("Output should contain account creation prompt.", output.contains("Create a New User"));
        assertTrue("Output should contain block user success.", output.contains("b has been blocked.\n"));
        assertTrue("Output should contain unblock user success.", output.contains("b has been unblocked.\n"));
        assertTrue("Output should contain exit message.", output.contains("Have a nice day!"));
    }

    @Test
    public void testSendMessage() {
        // Simulate user input: Choose "1" to create a new user, then "3" to exit
        String simulatedInput = "1\na\na\na\na\n" + "1\nb\nb\nb\nb\n" +
                "2\na\na\n" + "1\nb\n" + "4\nb\nhi\n9\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Call main method
        RunProject.main(new String[]{});

        // Capture output and verify expected steps are reached and that it reaches the end
        String output = outputStream.toString();
        assertTrue("Output should contain main menu.", output.contains("Main Menu:"));
        assertTrue("Output should contain account creation prompt.", output.contains("Create a New User"));
        assertTrue("Output should contain message success.", output.contains("Message sent!\n"));
        assertTrue("Output should contain exit message.", output.contains("Have a nice day!"));
    }

    @Test
    public void testSendPhoto() {
        // Simulate user input: Choose "1" to create a new user, then "3" to exit
        String simulatedInput = "1\na\na\na\na\n" + "1\nb\nb\nb\nb\n" +
                "2\na\na\n" + "1\nb\n" + "5\nb\nphoto1.png\n9\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Call main method
        RunProject.main(new String[]{});

        // Capture output and verify expected steps are reached and that it reaches the end
        String output = outputStream.toString();
        assertTrue("Output should contain main menu.", output.contains("Main Menu:"));
        assertTrue("Output should contain account creation prompt.",
                   output.contains("Create a New User"));
        assertTrue("Output should contain photo sending failure b/c no file found.",
                   output.contains("Failed to send Photo.\n"));
        assertTrue("Output should contain exit message.", output.contains("Have a nice day!"));
    }

    @Test
    public void testDeleteMessage() {
        // Simulate user input: Choose "1" to create a new user, then "3" to exit
        String simulatedInput = "1\na\na\na\na\n" + "1\nb\nb\nb\nb\n" +
                "2\na\na\n" + "1\nb\n" + "4\nb\nhi\n" + "6\nb\na: hi\n9\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Call main method
        RunProject.main(new String[]{});

        // Capture output and verify expected steps are reached and that it reaches the end
        String output = outputStream.toString();
        assertTrue("Output should contain main menu.", output.contains("Main Menu:"));
        assertTrue("Output should contain account creation prompt.", output.contains("Create a New User"));
        assertTrue("Output should contain deletion success.", output.contains("Message deleted!"));
        assertTrue("Output should contain exit message.", output.contains("Have a nice day!"));
    }

    @Test
    public void testViewUserProfile() {
        // Simulate user input: Choose "1" to create a new user, then "3" to exit
        String simulatedInput = "1\na\na\na\na\n" + "1\nb\nb\nb\nb\n" +
                "2\na\na\n" + "1\nb\n" + "7\nb\n9\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Call main method
        RunProject.main(new String[]{});

        // Capture output and verify expected steps are reached and that it reaches the end
        String output = outputStream.toString();
        assertTrue("Output should contain main menu.", output.contains("Main Menu:"));
        assertTrue("Output should contain account creation prompt.", output.contains("Create a New User"));
        assertTrue("Output should contain Logging out....", output.contains("username: \"b\"\n" +
                "bio: \"b\"\n" +
                "profile picture: \"b\""));
        assertTrue("Output should contain exit message.", output.contains("Have a nice day!"));
    }

    @Test
    public void testSearchUsers() {
        // Simulate user input: Choose "1" to create a new user, then "3" to exit
        String simulatedInput = "1\na\na\na\na\n" + "1\nb\nb\nb\nb\n" +
                "2\na\na\n" + "1\nb\n" + "8\nb\n9\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Call main method
        RunProject.main(new String[]{});

        // Capture output and verify expected steps are reached and that it reaches the end
        String output = outputStream.toString();
        assertTrue("Output should contain main menu.", output.contains("Main Menu:"));
        assertTrue("Output should contain account creation prompt.", output.contains("Create a New User"));
        assertTrue("Output should contain - b message.", output.contains("- b"));
        assertTrue("Output should contain exit message.", output.contains("Have a nice day!"));
    }

    @Test
    public void testLogout() {
        // Simulate user input: Choose "1" to create a new user, then "3" to exit
        String simulatedInput = "1\na\na\na\na\n" + "1\nb\nb\nb\nb\n" +
                "2\na\na\n9\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Call main method
        RunProject.main(new String[]{});

        // Capture output and verify expected steps are reached and that it reaches the end
        String output = outputStream.toString();
        assertTrue("Output should contain main menu.", output.contains("Main Menu:"));
        assertTrue("Output should contain account creation prompt.", output.contains("Create a New User"));
        assertTrue("Output should contain Logging out....", output.contains("Logging out..."));
        assertTrue("Output should contain exit message.", output.contains("Have a nice day!"));
    }


}
