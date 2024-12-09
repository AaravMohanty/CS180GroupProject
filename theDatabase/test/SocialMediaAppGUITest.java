import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The tests for the GUI/Client.
 * <p>
 * Purdue University -- CS18000 -- Fall 2024
 *
 * @author Elan Smyla, Aarav Mohanty, Hannah Cha, Kai Nietzche
 * @version December 8th, 2024
 */

class SocialMediaAppGUITest {

    private Socket mockSocket;
    private PrintWriter out;
    private BufferedReader in;

    @BeforeEach
    void setUp() throws IOException {
        PipedInputStream pipedInput = new PipedInputStream();
        PipedOutputStream pipedOutput = new PipedOutputStream(pipedInput);

        in = new BufferedReader(new InputStreamReader(pipedInput));
        out = new PrintWriter(new OutputStreamWriter(pipedOutput), true);

        mockSocket = new Socket() {
            @Override
            public InputStream getInputStream() {
                return pipedInput;
            }

            @Override
            public OutputStream getOutputStream() {
                return pipedOutput;
            }
        };

        SocialMediaAppGUI.setSocket(mockSocket);
        SocialMediaAppGUI.setInputReader(in);
        SocialMediaAppGUI.setOutputWriter(out);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (mockSocket != null) {
            mockSocket.close();
        }
    }

    @Test
    void testRefreshFriendsListWithReflection() throws Exception {
        DefaultListModel<String> friendsModel = new DefaultListModel<>();
        JList<String> friendsList = new JList<>(friendsModel);

        Thread serverSimulation = new Thread(() -> {
            try {
                out.println("friend1");
                out.flush();
                out.println("friend2");
                out.flush();
                out.println("END");
                out.flush();
            } catch (Exception e) {
                System.err.println("Error writing to mock output: " + e.getMessage());
            }
        });
        serverSimulation.start();

        java.lang.reflect.Method method = SocialMediaAppGUI.class.getDeclaredMethod(
                "refreshFriendsList", DefaultListModel.class, JList.class);
        method.setAccessible(true);

        serverSimulation.join();

        method.invoke(null, friendsModel, friendsList);

        for (int i = 0; i < friendsModel.size(); i++) {
            System.out.println("Friend: " + friendsModel.get(i));
        }
    }

    @Test
    void testInputReader() throws Exception {
        Thread serverSimulation = new Thread(() -> {
            try {
                out.println("friend1");
                out.println("friend2");
                out.println("END");
                out.flush();
            } catch (Exception ignored) {
            }
        });
        serverSimulation.start();

        String friend1 = in.readLine();
        String friend2 = in.readLine();
        String end = in.readLine();

        serverSimulation.join();

        assertEquals("friend1", friend1);
        assertEquals("friend2", friend2);
        assertEquals("END", end);
    }

}
