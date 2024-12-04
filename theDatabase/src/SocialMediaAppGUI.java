import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class SocialMediaAppGUI {
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static String currentUser;

    public static void main(String[] args) {
        try {
            socket = new Socket("localhost", 1234);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            SwingUtilities.invokeLater(SocialMediaAppGUI::createLoginGUI);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cannot connect to the server.");
        }
    }

    private static void createLoginGUI() {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
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
            String username = userText.getText().trim();
            String password = new String(passwordText.getPassword()).trim();
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                return;
            }
            out.println("2");
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

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void createAccountGUI() {
        JFrame frame = new JFrame("Create Account");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
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
            String username = userText.getText().trim();
            String password = new String(passwordText.getPassword()).trim();
            String bio = bioText.getText().trim();
            String pfp = pfpText.getText().trim();

            if (username.isEmpty() || password.isEmpty() || bio.isEmpty() || pfp.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                return;
            }

            out.println("1");
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

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void createMainMenuGUI() {
        JFrame frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Shared DefaultListModel for Friends List
        DefaultListModel<String> friendsModel = new DefaultListModel<>();

        // Friends List Tab
        JPanel friendsPanel = createFriendsPanel(friendsModel);
        tabbedPane.addTab("Friends List", friendsPanel);

        // Blocked List Tab
        JPanel blockedPanel = createBlockedListPanel(friendsModel);
        tabbedPane.addTab("Blocked List", blockedPanel);

        // Conversations Tab
        JPanel conversationsPanel = createConversationsPanel();
        tabbedPane.addTab("Conversations", conversationsPanel);

        // Search Users Tab
        JPanel searchPanel = createSearchUsersPanel();
        tabbedPane.addTab("Search Users", searchPanel);

        // Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            out.println("logout");
            try {
                String response = in.readLine();
                if ("logout_success".equals(response)) {
                    frame.dispose();
                    createLoginGUI();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(logoutButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }



    private static JPanel createConversationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<String> conversationsModel = new DefaultListModel<>();
        JList<String> conversationsList = new JList<>(conversationsModel);
        JTextArea conversationArea = new JTextArea();
        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Send");

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

        panel.add(new JScrollPane(conversationsList), BorderLayout.WEST);
        panel.add(new JScrollPane(conversationArea), BorderLayout.CENTER);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);
        panel.add(messagePanel, BorderLayout.SOUTH);

        return panel;
    }

    private static JPanel createFriendsPanel(DefaultListModel<String> friendsModel) {
        JPanel panel = new JPanel(new BorderLayout());
        JList<String> friendsList = new JList<>(friendsModel);
        JTextField friendTextField = new JTextField();
        JButton addFriendButton = new JButton("Add Friend");
        JButton removeFriendButton = new JButton("Remove Friend");

        // Populate friends list when the panel is opened
        new Thread(() -> {
            try {
                out.println("get_friends");
                String friend;
                friendsModel.clear(); // Clear existing entries before populating
                while (!(friend = in.readLine()).equals("END")) {
                    friendsModel.addElement(friend);
                }
            } catch (IOException ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error fetching friends list."));
                ex.printStackTrace();
            }
        }).start();

        addFriendButton.addActionListener(e -> {
            String friendName = friendTextField.getText().trim();
            if (!friendName.isEmpty()) {
                new Thread(() -> {
                    try {
                        out.println("add_friend");
                        out.println(friendName);

                        String response = in.readLine();
                        SwingUtilities.invokeLater(() -> {
                            if ("success".equals(response)) {
                                friendsModel.addElement(friendName);
                                JOptionPane.showMessageDialog(null, "Friend added successfully.");
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to add friend. User may not exist, is blocked, or is already a friend.");
                            }
                        });
                    } catch (IOException ex) {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error communicating with the server."));
                        ex.printStackTrace();
                    }
                }).start();
            } else {
                JOptionPane.showMessageDialog(null, "Friend's username cannot be empty.");
            }
        });

        removeFriendButton.addActionListener(e -> {
            String selectedFriend = friendsList.getSelectedValue();
            if (selectedFriend != null) {
                new Thread(() -> {
                    try {
                        out.println("remove_friend");
                        out.println(selectedFriend);

                        String response = in.readLine();
                        SwingUtilities.invokeLater(() -> {
                            if ("success".equals(response)) {
                                friendsModel.removeElement(selectedFriend);
                                JOptionPane.showMessageDialog(null, "Friend removed successfully.");
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to remove friend.");
                            }
                        });
                    } catch (IOException ex) {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error communicating with the server."));
                        ex.printStackTrace();
                    }
                }).start();
            } else {
                JOptionPane.showMessageDialog(null, "Please select a friend to remove.");
            }
        });

        JPanel controlPanel = new JPanel(new GridLayout(1, 2));
        controlPanel.add(friendTextField);
        controlPanel.add(addFriendButton);

        panel.add(new JScrollPane(friendsList), BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(removeFriendButton, BorderLayout.SOUTH);

        return panel;
    }

    private static JPanel createBlockedListPanel(DefaultListModel<String> friendsModel) {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<String> blockedModel = new DefaultListModel<>();
        JList<String> blockedList = new JList<>(blockedModel);
        JTextField blockTextField = new JTextField();
        JButton blockUserButton = new JButton("Block User");
        JButton unblockUserButton = new JButton("Unblock User");

        // Populate blocked list when the panel is opened
        new Thread(() -> {
            try {
                out.println("get_blocked_users");
                String blockedUser;
                blockedModel.clear(); // Clear existing entries before populating
                while (!(blockedUser = in.readLine()).equals("END")) {
                    blockedModel.addElement(blockedUser);
                }
            } catch (IOException ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error fetching blocked list."));
                ex.printStackTrace();
            }
        }).start();

        blockUserButton.addActionListener(e -> {
            String usernameToBlock = blockTextField.getText().trim();
            if (!usernameToBlock.isEmpty()) {
                new Thread(() -> {
                    try {
                        out.println("block_user");
                        out.println(usernameToBlock);

                        String response = in.readLine();
                        SwingUtilities.invokeLater(() -> {
                            if ("success".equals(response)) {
                                blockedModel.addElement(usernameToBlock); // Add to blocked list

                                // Remove the user from the friends list if they exist
                                if (friendsModel.contains(usernameToBlock)) {
                                    friendsModel.removeElement(usernameToBlock);
                                }
                                JOptionPane.showMessageDialog(null, "User blocked successfully.");
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to block user. They may not exist or are already blocked.");
                            }
                        });
                    } catch (IOException ex) {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error communicating with the server."));
                        ex.printStackTrace();
                    }
                }).start();
            } else {
                JOptionPane.showMessageDialog(null, "Username cannot be empty.");
            }
        });

        unblockUserButton.addActionListener(e -> {
            String selectedBlockedUser = blockedList.getSelectedValue();
            if (selectedBlockedUser != null) {
                new Thread(() -> {
                    try {
                        out.println("unblock_user");
                        out.println(selectedBlockedUser);

                        String response = in.readLine();
                        SwingUtilities.invokeLater(() -> {
                            if ("success".equals(response)) {
                                blockedModel.removeElement(selectedBlockedUser);
                                JOptionPane.showMessageDialog(null, "User unblocked successfully.");
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to unblock user.");
                            }
                        });
                    } catch (IOException ex) {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error communicating with the server."));
                        ex.printStackTrace();
                    }
                }).start();
            } else {
                JOptionPane.showMessageDialog(null, "Please select a user to unblock.");
            }
        });

        JPanel controlPanel = new JPanel(new GridLayout(1, 2));
        controlPanel.add(blockTextField);
        controlPanel.add(blockUserButton);

        panel.add(new JScrollPane(blockedList), BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(unblockUserButton, BorderLayout.SOUTH);

        return panel;
    }


    private static JPanel createSearchUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<String> searchResultsModel = new DefaultListModel<>();
        JList<String> searchResults = new JList<>(searchResultsModel);
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            searchResultsModel.clear();
            if (!query.isEmpty()) {
                out.println("search_user");
                out.println(query);
                try {
                    String result;
                    while (!(result = in.readLine()).equals("END")) {
                        searchResultsModel.addElement(result);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to fetch search results.");
                }
            }
        });

        searchResults.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedUser = searchResults.getSelectedValue();
                    if (selectedUser != null) {
                        // Fetch user details from the server
                        new Thread(() -> {
                            try {
                                out.println("get_user_details");
                                out.println(selectedUser);

                                String username = in.readLine();
                                String bio = in.readLine();
                                String pfp = in.readLine();

                                SwingUtilities.invokeLater(() -> {
                                    // Display user details
                                    JFrame detailsFrame = new JFrame("User Details");
                                    detailsFrame.setSize(400, 300);
                                    detailsFrame.setLayout(new GridLayout(4, 1));

                                    JLabel usernameLabel = new JLabel("Username: " + username);
                                    JLabel bioLabel = new JLabel("Bio: " + bio);
                                    JLabel pfpLabel = new JLabel("Profile Picture: " + pfp);

                                    JButton closeButton = new JButton("Close");
                                    closeButton.addActionListener(closeEvent -> detailsFrame.dispose());

                                    detailsFrame.add(usernameLabel);
                                    detailsFrame.add(bioLabel);
                                    detailsFrame.add(pfpLabel);
                                    detailsFrame.add(closeButton);

                                    detailsFrame.setVisible(true);
                                });
                            } catch (IOException ex) {
                                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Failed to fetch user details."));
                            }
                        }).start();
                    }
                }
            }
        });

        panel.add(searchField, BorderLayout.NORTH);
        panel.add(new JScrollPane(searchResults), BorderLayout.CENTER);
        panel.add(searchButton, BorderLayout.SOUTH);

        return panel;
    }
}
