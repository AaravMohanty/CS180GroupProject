import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static String currentUser;

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
                    currentUser = username;
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
            String pfp = pfpText.getText(); // File path for profile picture

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
                    out.println("save_profile_picture");
                    out.println(username);
                    out.println(pfp);
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
        DefaultListModel<String> conversationsModel = new DefaultListModel<>();
        JList<String> conversationsList = new JList<>(conversationsModel);
        JTextArea conversationArea = new JTextArea();
        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        JButton deleteButton = new JButton("Delete");

        // Load conversations
        conversationsList.addListSelectionListener(e -> {
            String selectedConversation = conversationsList.getSelectedValue();
            if (selectedConversation == null) return;
            conversationArea.setText("");
            try {
                out.println("load_conversation");
                out.println(selectedConversation);
                String message;
                while (!(message = in.readLine()).equals("END")) {
                    conversationArea.append(message + "\n");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to load messages.");
            }
        });

        sendButton.addActionListener(e -> {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                out.println("send_message");
                out.println(conversationsList.getSelectedValue());
                out.println(message);
                messageField.setText("");
                conversationArea.append(currentUser + ": " + message + "\n");
            }
        });

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
        DefaultListModel<String> friendsModel = new DefaultListModel<>();
        JList<String> friendsList = new JList<>(friendsModel);

        JTextField friendTextField = new JTextField();
        JButton friendButton = new JButton("Add Friend");
        JButton blockButton = new JButton("Block User");

        friendButton.addActionListener(e -> {
            String friendName = friendTextField.getText().trim();
            if (!friendName.isEmpty()) {
                out.println("add_friend");
                out.println(friendName);
                try {
                    String response = in.readLine();
                    if ("success".equals(response)) {
                        friendsModel.addElement(friendName);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to add friend.");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        blockButton.addActionListener(e -> {
            String blockName = friendTextField.getText().trim();
            if (!blockName.isEmpty()) {
                out.println("block_user");
                out.println(blockName);
                try {
                    String response = in.readLine();
                    if ("success".equals(response)) {
                        friendsModel.removeElement(blockName);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to block user.");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JPanel friendsControlPanel = new JPanel(new GridLayout(1, 3));
        friendsControlPanel.add(friendTextField);
        friendsControlPanel.add(friendButton);
        friendsControlPanel.add(blockButton);

        friendsPanel.add(new JScrollPane(friendsList), BorderLayout.CENTER);
        friendsPanel.add(friendsControlPanel, BorderLayout.SOUTH);

        // Search Users Tab
        JPanel searchPanel = new JPanel(new BorderLayout());
        DefaultListModel<String> searchModel = new DefaultListModel<>();
        JList<String> searchResults = new JList<>(searchModel);
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                fetchSearchResults();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fetchSearchResults();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fetchSearchResults();
            }

            private void fetchSearchResults() {
                SwingUtilities.invokeLater(() -> {
                    String searchQuery = searchField.getText().trim();
                    searchModel.clear();

                    if (searchQuery.isEmpty()) {
                        out.println("get_users");
                    } else {
                        out.println("search_user");
                        out.println(searchQuery);
                    }

                    try {
                        String userDetails;
                        while (!(userDetails = in.readLine()).equals("END")) {
                            searchModel.addElement(userDetails);
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Failed to fetch user list.");
                    }
                });
            }
        });


        searchResults.addListSelectionListener(e -> {
            String selectedUser = searchResults.getSelectedValue();
            if (selectedUser != null) {
                out.println("get_user_details");
                out.println(selectedUser);

                try {
                    String username = in.readLine();
                    String bio = in.readLine();
                    String pfp = in.readLine();

                    JFrame detailsFrame = new JFrame("User Details");
                    detailsFrame.setSize(400, 300);
                    detailsFrame.setLayout(new GridLayout(4, 1));

                    JLabel usernameLabel = new JLabel("Username: " + username);
                    JLabel bioLabel = new JLabel("Bio: " + bio);
                    ImageIcon profileImage = new ImageIcon(pfp); // Load the image from the file path
                    JLabel pfpLabel = new JLabel("Profile Picture:");
                    JLabel imageLabel = new JLabel(profileImage); // Display the image
                    JButton backButton = new JButton("Back");

                    backButton.addActionListener(backEvent -> {
                        detailsFrame.dispose();
                    });

                    detailsFrame.add(usernameLabel);
                    detailsFrame.add(bioLabel);
                    detailsFrame.add(pfpLabel);
                    detailsFrame.add(imageLabel); // Add the image label to the frame
                    detailsFrame.add(backButton);


                    detailsFrame.setVisible(true);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to fetch user details.");
                }
            }
        });

        searchPanel.add(searchField, BorderLayout.NORTH);
        searchPanel.add(new JScrollPane(searchResults), BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.SOUTH);
        tabbedPane.addTab("Search Users", searchPanel);


        // Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            out.println("logout");
            try {
                String response = in.readLine();
                if ("logout_success".equals(response)) {
                    currentUser = null;
                    frame.dispose();
                    createLoginGUI();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        tabbedPane.addTab("Conversations", conversationsPanel);
        tabbedPane.addTab("Friends List", friendsPanel);
        tabbedPane.addTab("Search Users", searchPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(logoutButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
