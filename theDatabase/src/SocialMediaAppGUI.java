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

    private static JPanel addRefreshablePanel(JPanel contentPanel, Runnable refreshAction) {
        JPanel panel = new JPanel(new BorderLayout());
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            SwingUtilities.invokeLater(refreshAction);
        });

        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.NORTH);
        return panel;
    }

    private static void refreshFriendsList(DefaultListModel<String> friendsModel) {
        new Thread(() -> {
            try {
                out.println("get_friends"); // Request the friends list from the server
                String friend;
                SwingUtilities.invokeLater(friendsModel::clear); // Clear the current list

                // Add each friend to the model
                while (!(friend = in.readLine()).equals("END")) {
                    String finalFriend = friend;
                    SwingUtilities.invokeLater(() -> friendsModel.addElement(finalFriend));
                }
            } catch (IOException ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error fetching friends list."));
            }
        }).start();
    }


    private static void refreshBlockedList(DefaultListModel<String> blockedModel) {
        new Thread(() -> {
            try {
                out.println("get_blocked_users");
                String blockedUser;
                blockedModel.clear();
                while (!(blockedUser = in.readLine()).equals("END")) {
                    blockedModel.addElement(blockedUser);
                }
            } catch (IOException ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error fetching blocked users list."));
            }
        }).start();
    }

    // Method to load messages for the selected conversation
    private static void loadSelectedConversation(JList<String> conversationsList, JTextArea conversationArea) {
        String selectedConversation = conversationsList.getSelectedValue();
        if (selectedConversation == null) return;

        try {
            out.println("load_conversation");
            out.println(selectedConversation); // Send the selected conversation to the server

            String message;
            conversationArea.setText(""); // Clear the message area before loading
            while (!(message = in.readLine()).equals("END")) {
                // Skip adding "success" or other server response keywords to the message area
                if (!message.equals("success") && !message.equals("failure")) {
                    conversationArea.append(message + "\n");
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Failed to load messages.");
        }
    }


    private static void refreshConversationsList(JSplitPane splitPane) {
        // Locate the conversations list inside the left panel of the split pane
        JPanel leftPanel = (JPanel) splitPane.getLeftComponent();
        JScrollPane scrollPane = (JScrollPane) leftPanel.getComponent(1);
        JList<String> conversationsList = (JList<String>) scrollPane.getViewport().getView();

        DefaultListModel<String> conversationsModel = (DefaultListModel<String>) conversationsList.getModel();

        // Refresh the list dynamically
        new Thread(() -> {
            try {
                out.println("get_users");
                String conversation;
                SwingUtilities.invokeLater(conversationsModel::clear);
                while (!(conversation = in.readLine()).equals("END")) {
                    String finalConversation = conversation;
                    SwingUtilities.invokeLater(() -> conversationsModel.addElement(finalConversation));
                }
            } catch (IOException ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error fetching conversations list."));
            }
        }).start();
    }


    private static void clearSearchResults(DefaultListModel<String> searchResultsModel) {
        searchResultsModel.clear();
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

    // Updated createMainMenuGUI method
    private static void createMainMenuGUI() {
        JFrame frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Friends List Tab with Refresh Button
        DefaultListModel<String> friendsModel = new DefaultListModel<>();
        JPanel friendsPanel = createFriendsPanel(friendsModel);
        Runnable refreshFriends = () -> refreshFriendsList(friendsModel);
        tabbedPane.addTab("Friends List", addRefreshablePanel(friendsPanel, refreshFriends));

        // Blocked List Tab with Refresh Button
        JPanel blockedPanel = createBlockedListPanel(friendsModel);
        Runnable refreshBlocked = () -> refreshBlockedList((DefaultListModel<String>) ((JList) blockedPanel.getComponent(0)).getModel());
        tabbedPane.addTab("Blocked List", addRefreshablePanel(blockedPanel, refreshBlocked));

        // Conversations Tab with Refresh Button
        // Conversations Tab with Refresh Button
        JSplitPane conversationsSplitPane = (JSplitPane) createConversationsPanel().getComponent(0);
        Runnable refreshConversations = () -> refreshConversationsList(conversationsSplitPane);
        tabbedPane.addTab("Conversations", addRefreshablePanel(createConversationsPanel(), refreshConversations));


        // Search Users Tab with Refresh Button
        JPanel searchPanel = createSearchUsersPanel();
        Runnable refreshSearch = () -> clearSearchResults((DefaultListModel<String>) ((JList) searchPanel.getComponent(1)).getModel());
        tabbedPane.addTab("Search Users", addRefreshablePanel(searchPanel, refreshSearch));

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

        // Model to hold conversation names
        DefaultListModel<String> conversationsModel = new DefaultListModel<>();
        JList<String> conversationsList = new JList<>(conversationsModel);
        JTextArea conversationArea = new JTextArea();
        conversationArea.setEditable(false); // Read-only conversation area
        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        JButton refreshButton = new JButton("Refresh"); // Manual Refresh Button
        JButton deleteButton = new JButton("Delete");

        // Timer to refresh every X milliseconds (e.g., 5000 = 5 seconds)
        int refreshInterval = 500; // Change this value for different intervals
        Timer autoRefreshTimer = new Timer(refreshInterval, e -> loadSelectedConversation(conversationsList, conversationArea));

        // Left Panel - Conversations List
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Conversations"), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(conversationsList), BorderLayout.CENTER);

        // Right Panel - Message Area
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Messages"), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(conversationArea), BorderLayout.CENTER);
        rightPanel.add(refreshButton, BorderLayout.NORTH); // Add manual refresh button at the top

        // Bottom Panel - Message Input
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);
        messagePanel.add(deleteButton, BorderLayout.WEST);
        rightPanel.add(messagePanel, BorderLayout.SOUTH);


        // Split the left and right panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(200); // Set the initial size of the split
        panel.add(splitPane, BorderLayout.CENTER);

        // Load conversations into the list dynamically
        new Thread(() -> {
            try {
                out.println("get_users"); // Command to fetch all users (conversations)
                String conversation;
                while (!(conversation = in.readLine()).equals("END")) {
                    final String conversationCopy = conversation;
                    SwingUtilities.invokeLater(() -> conversationsModel.addElement(conversationCopy));
                }
            } catch (IOException ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Failed to load conversations."));
            }
        }).start();

        // Load messages when a conversation is selected
        conversationsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedConversation(conversationsList, conversationArea);
            }
        });

        // Refresh button reloads the currently selected conversation
        refreshButton.addActionListener(e -> loadSelectedConversation(conversationsList, conversationArea));

        // Automatically refresh messages every X seconds
        autoRefreshTimer.start();

        // Send message functionality
        sendButton.addActionListener(e -> {
            String message = messageField.getText().trim();
            String selectedConversation = conversationsList.getSelectedValue();

            if (message.isEmpty() || selectedConversation == null) {
                JOptionPane.showMessageDialog(panel, "Please select a conversation and enter a message.");
                return;
            }

            try {
                out.println("send_message");
                out.println(selectedConversation);
                out.println(message);
                messageField.setText(""); // Clear the input field

                // Reload the messages in the conversation area without adding a success message
                loadSelectedConversation(conversationsList, conversationArea);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Failed to send message.");
            }
        });


        deleteButton.addActionListener(e -> {
            String message = messageField.getText().trim();
            String selectedConversation = conversationsList.getSelectedValue();

            if (message.isEmpty() || selectedConversation == null) {
                JOptionPane.showMessageDialog(panel, "Please select a conversation and enter a message to delete.");
                return;
            }

            try {
                out.println("delete_message");
                out.println(selectedConversation);
                out.println(message);

                String response = in.readLine();
                if ("success".equals(response)) {
                    // Reload the messages in the conversation area without adding a success message
                    loadSelectedConversation(conversationsList, conversationArea);
                } else {
                    JOptionPane.showMessageDialog(panel, "Failed to delete the message. It may not exist.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error while deleting the message.");
            }
        });

        return panel;
    }


    private static JPanel createFriendsPanel(DefaultListModel<String> friendsModel) {
        JPanel panel = new JPanel(new BorderLayout());
        JList<String> friendsList = new JList<>(friendsModel);
        JTextField friendTextField = new JTextField();
        JButton addFriendButton = new JButton("Add Friend");
        JButton removeFriendButton = new JButton("Remove Friend");

        // Set up auto-refresh
        int refreshInterval = 10000; // Refresh every 10 seconds
        Timer autoRefreshTimer = new Timer(refreshInterval, e -> refreshFriendsList(friendsModel));
        autoRefreshTimer.start();

        // Populate friends list initially
        refreshFriendsList(friendsModel);

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