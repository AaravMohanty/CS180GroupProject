import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleClientTest {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(SimpleClientTest.class);
        if (result.wasSuccessful()) {
            System.out.println("All tests ran successfully.");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    @Test(timeout = 5000)
    public void testClientConnection() {
        Thread serverThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8888)) {
                // Accept a client connection
                Socket socket = serverSocket.accept();
                // Close the connection for this test
                socket.close();
            } catch (IOException e) {
                Assert.fail("Server failed to start.");
            }
        });
        serverThread.start();

        // Allow time for the server to start
        try {
            Thread.sleep(1000);
            new SimpleClient(); // Attempt to run the client, which will connect to the server
        } catch (InterruptedException e) {
            Assert.fail("Test interrupted.");
        }
    }

    @Test(timeout = 5000)
    public void testMessageSendingAndReceiving() {
        final String[] receivedMessage = {null}; // Array to capture the message received by the client

        Thread serverThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8888);
                 Socket socket = serverSocket.accept();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

                // Read the message from the client
                String message = reader.readLine();
                receivedMessage[0] = message; // Store the received message

                // Respond to the client
                writer.println("Echo: " + message); // Echo the message back
            } catch (IOException e) {
                Assert.fail("Server failed during message handling.");
            }
        });
        serverThread.start();

        // Allow time for the server to start
        try {
            Thread.sleep(1000);
            // Run the client in a separate thread
            new Thread(() -> {
                String[] args = {}; // Dummy args
                SimpleClient.main(args);
            }).start();

            // Give time for the client to send a message
            Thread.sleep(1000);

            // Simulate sending a message and receiving the response
            Assert.assertEquals("Test Message", receivedMessage[0]);
        } catch (InterruptedException e) {
            Assert.fail("Test interrupted.");
        }
    }

    @Test(timeout = 5000)
    public void testClientExit() {
        Thread serverThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8888);
                 Socket socket = serverSocket.accept();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

                // Read the message from the client
                String message = reader.readLine();
                if ("exit".equalsIgnoreCase(message)) {
                    writer.println("exit"); // Respond to indicate exit
                }
            } catch (IOException e) {
                Assert.fail("Server failed during exit handling.");
            }
        });
        serverThread.start();

        // Allow time for the server to start
        try {
            Thread.sleep(1000);
            // Run the client in a separate thread
            new Thread(() -> {
                String[] args = {}; // Dummy args
                SimpleClient.main(args);
            }).start();

            // Give time for the client to send a message
            Thread.sleep(1000);

            // Simulate sending the exit command
            // We can use the same method to check if the client handled the exit correctly
            Assert.assertEquals("exit", receivedMessage[0]); // Check if the exit command was sent
        } catch (InterruptedException e) {
            Assert.fail("Test interrupted.");
        }
    }
}
