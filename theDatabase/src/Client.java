
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client {
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args) {
        try {
            socket = new Socket("localhost", 1234);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            SwingUtilities.invokeLater(Client::createLoginGUI);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cannot connect to the server.");
        }
    }

    private static void createLoginGUI() {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userText = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordText = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Create Account");

        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(loginButton);
        panel.add(createAccountButton);

        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                return;
            }
            out.println("2"); // Login action
            out.println(username);
            out.println(password);
            try {
                String response = in.readLine();
                if ("success".equals(response)) {
                    frame.dispose();
                    createMainMenuGUI();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials. Try again.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        createAccountButton.addActionListener(e -> {
            frame.dispose();
            createAccountGUI();
        });

        frame.setVisible(true);
    }

    private static void createAccountGUI() {
        JFrame frame = new JFrame("Create Account");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(new GridLayout(5, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userText = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordText = new JPasswordField(20);
        JLabel bioLabel = new JLabel("Bio:");
        JTextField bioText = new JTextField(20);
        JLabel pfpLabel = new JLabel("Profile Picture:");
        JTextField pfpText = new JTextField(20);

        JButton createButton = new JButton("Create");
        JButton backButton = new JButton("Back");

        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(bioLabel);
        panel.add(bioText);
        panel.add(pfpLabel);
        panel.add(pfpText);
        panel.add(createButton);
        panel.add(backButton);

        createButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());
            String bio = bioText.getText();
            String pfp = pfpText.getText();

            if (username.isEmpty() || password.isEmpty() || bio.isEmpty() || pfp.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                return;
            }
            out.println("1"); // Create account action
            out.println(username);
            out.println(password);
            out.println(bio);
            out.println(pfp);
            try {
                String response = in.readLine();
                if ("success".equals(response)) {
                    JOptionPane.showMessageDialog(frame, "Account created successfully!");
                    frame.dispose();
                    createLoginGUI();
                } else if ("username_taken".equals(response)) {
                    JOptionPane.showMessageDialog(frame, "Username already taken. Try another.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Account creation failed. Please try again.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            createLoginGUI();
        });

        frame.setVisible(true);
    }

    private static void createMainMenuGUI() {
        JFrame frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Conversations Tab
        JPanel conversationsPanel = new JPanel(new BorderLayout());
        JList<String> conversationsList = new JList<>(new String[]{"Conversation 1", "Conversation 2"});
        JTextArea conversationArea = new JTextArea();
        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        JButton deleteButton = new JButton("Delete");

        conversationsPanel.add(new JScrollPane(conversationsList), BorderLayout.WEST);
        conversationsPanel.add(new JScrollPane(conversationArea), BorderLayout.CENTER);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(messageField, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(sendButton);
        buttonPanel.add(deleteButton);
        messagePanel.add(buttonPanel, BorderLayout.EAST);
        conversationsPanel.add(messagePanel, BorderLayout.SOUTH);

        // Friends Tab
        JPanel friendsPanel = new JPanel(new BorderLayout());
        JList<String> friendsList = new JList<>(new String[]{"Friend 1", "Friend 2"});
        JButton addFriendButton = new JButton("Add Friend");
        JButton removeFriendButton = new JButton("Remove Friend");
        JButton blockFriendButton = new JButton("Block/Unblock Friend");
        friendsPanel.add(new JScrollPane(friendsList), BorderLayout.CENTER);
        JPanel friendsButtonPanel = new JPanel(new GridLayout(1, 3));
        friendsButtonPanel.add(addFriendButton);
        friendsButtonPanel.add(removeFriendButton);
        friendsButtonPanel.add(blockFriendButton);
        friendsPanel.add(friendsButtonPanel, BorderLayout.SOUTH);

        // Search Users Tab
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        JList<String> searchResults = new JList<>(new String[]{"User 1", "User 2"});
        searchPanel.add(searchField, BorderLayout.NORTH);
        searchPanel.add(new JScrollPane(searchResults), BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.SOUTH);

        tabbedPane.addTab("Conversations", conversationsPanel);
        tabbedPane.addTab("Friends List", friendsPanel);
        tabbedPane.addTab("Search Users", searchPanel);

        frame.add(tabbedPane);
        frame.setVisible(true);
    }
}
