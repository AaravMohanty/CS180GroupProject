import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class SocialMediaAppGUI {
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static String currentUser;
    private static volatile boolean loggedIn = true; // Controls thread execution
    private static Timer autoRefreshTimer;
    // Timer for auto-refreshing the message area
    private static Timer messageRefreshTimer;
    private static JList<String> friendsList;

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

    private static void refreshFriendsList(DefaultListModel<String> friendsModel, JList<String> friendsList) {
        new Thread(() -> {
            try {
                if (!loggedIn || out == null) return; // Stop if logged out or connection is closed
                out.println("get_friends"); // Request the friends list from the server
                String friend;

                // Store the currently selected friend
                String selectedFriend = SocialMediaAppGUI.friendsList.getSelectedValue();

                SwingUtilities.invokeLater(friendsModel::clear); // Clear the current list

                while (loggedIn && (friend = in.readLine()) != null && !friend.equals("END")) {
                    String finalFriend = friend; // Final variable for lambda
                    SwingUtilities.invokeLater(() -> friendsModel.addElement(finalFriend));
                }

                // Restore the previous selection
                SwingUtilities.invokeLater(() -> SocialMediaAppGUI.friendsList.setSelectedValue(selectedFriend, true));
            } catch (IOException ex) {
                if (loggedIn) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error fetching friends list."));
                }
            } catch (NullPointerException npe) {
                System.err.println("Thread terminated: Connection is closed.");
            }
        }).start();
    }

    private static void refreshBlockedList(DefaultListModel<String> blockedModel) {
        new Thread(() -> {
            try {
                if (!loggedIn || out == null) return; // Stop if logged out or connection is closed
                out.println("get_blocked_users");
                String blockedUser;
                SwingUtilities.invokeLater(blockedModel::clear);

                while (loggedIn && (blockedUser = in.readLine()) != null && !blockedUser.equals("END")) {
                    String finalBlockedUser = blockedUser; // Declare a final variable for the lambda
                    SwingUtilities.invokeLater(() -> blockedModel.addElement(finalBlockedUser));
                }
            } catch (IOException ex) {
                if (loggedIn) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error fetching blocked users list."));
                }
            } catch (NullPointerException npe) {
                System.err.println("Thread terminated: Connection is closed.");
            }
        }).start();
    }

    // Method to load messages for the selected conversation
    private static void loadSelectedConversation(JList<String> conversationsList, JTextArea conversationArea) {
        String selectedFriend = conversationsList.getSelectedValue();
        if (selectedFriend == null) return;

        // Synchronize access to prevent race conditions
        synchronized (conversationArea) {
            try {
                out.println("load_conversation");
                out.println(selectedFriend); // Send the friend's name to the server

                String message;
                StringBuilder messageBuffer = new StringBuilder();
                while (!(message = in.readLine()).equals("END")) {
                    // Append message to buffer
                    messageBuffer.append(message).append("\n");
                }

                // Update UI in the event-dispatching thread
                SwingUtilities.invokeLater(() -> {
                    conversationArea.setText(messageBuffer.toString());
                });
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to load messages.");
            }
        }
    }

    private static void refreshConversationsList(DefaultListModel<String> conversationsModel, JList<String> conversationsList) {
        new Thread(() -> {
            try {
                if (!loggedIn || out == null) return; // Stop if logged out or connection is closed
                out.println("get_friends"); // Request the user's friends list from the server
                String friend;

                // Store the currently selected friend
                String selectedFriend = conversationsList.getSelectedValue();

                SwingUtilities.invokeLater(conversationsModel::clear); // Clear the current list

                while (loggedIn && (friend = in.readLine()) != null && !friend.equals("END")) {
                    String finalFriend = friend; // Final variable for lambda
                    SwingUtilities.invokeLater(() -> conversationsModel.addElement(finalFriend));
                }

                // Restore the previous selection
                SwingUtilities.invokeLater(() -> conversationsList.setSelectedValue(selectedFriend, true));
            } catch (IOException ex) {
                if (loggedIn) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error fetching friends list."));
                }
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
            try {
                if (socket == null || socket.isClosed()) {
                    socket = new Socket("localhost", 1234);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream(), true);
                }

                String username = userText.getText().trim();
                String password = new String(passwordText.getPassword()).trim();
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                    return;
                }

                out.println("2");
                out.println(username);
                out.println(password);
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
                JOptionPane.showMessageDialog(frame, "Error connecting to the server. Please try again.");
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
        loggedIn = true;
        JFrame frame = new JFrame(currentUser);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Friends List Tab
        DefaultListModel<String> friendsModel = new DefaultListModel<>();
        JPanel friendsPanel = createFriendsPanel(friendsModel);
        tabbedPane.addTab("Friends List", friendsPanel);

        // Blocked List Tab
        DefaultListModel<String> blockedModel = new DefaultListModel<>();
        JPanel blockedPanel = createBlockedListPanel(friendsModel);
        tabbedPane.addTab("Blocked List", blockedPanel);

        // Conversations Tab
        tabbedPane.addTab("Conversations", createConversationsPanel());

        // Search Users Tab
        DefaultListModel<String> searchResultsModel = new DefaultListModel<>();
        tabbedPane.addTab("Search Users", createSearchUsersPanel());

        // Add logout functionality
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            try {
                if (out != null) {
                    out.println("logout");
                }
                if (in != null && "logout_success".equals(in.readLine())) {
                    loggedIn = false; // Stop all background threads
                    currentUser = null;

                    if (frame != null) {
                        frame.dispose();
                    }

                    if (socket != null) {
                        socket.close();
                    }
                    socket = null;
                    in = null;
                    out = null;

                    createLoginGUI();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error during logout. Please try again.");
            }
        });

        // Refresh the friends list
        refreshFriendsList(friendsModel, friendsList);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(logoutButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private static JPanel createConversationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Model to hold friend's names
        DefaultListModel<String> conversationsModel = new DefaultListModel<>();
        JList<String> conversationsList = new JList<>(conversationsModel);
        JTextArea conversationArea = new JTextArea();
        conversationArea.setEditable(false); // Read-only conversation area
        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");

        // Timer for auto-refreshing messages
        Timer messageRefreshTimer = new Timer(1000, e -> {
            String selectedFriend = conversationsList.getSelectedValue();
            if (loggedIn && selectedFriend != null) {
                loadSelectedConversation(conversationsList, conversationArea);
            }
        });

        // Start the timer initially
        messageRefreshTimer.start();

        // Left Panel - Friends List
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Friends"), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(conversationsList), BorderLayout.CENTER);
        leftPanel.add(refreshButton, BorderLayout.SOUTH); // Add refresh button to left panel

        // Right Panel - Message Area
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Messages"), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(conversationArea), BorderLayout.CENTER);

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

        // Refresh Button Logic
        refreshButton.addActionListener(e -> refreshConversationsList(conversationsModel, conversationsList));

        // Load friends dynamically on initialization
        refreshConversationsList(conversationsModel, conversationsList);

        // Update messages when a friend is selected
        conversationsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                messageRefreshTimer.stop(); // Stop timer while updating
                loadSelectedConversation(conversationsList, conversationArea);
                messageRefreshTimer.start(); // Restart timer for the new selection
            }
        });

        // Send message functionality
        sendButton.addActionListener(e -> {
            String message = messageField.getText().trim();
            String selectedFriend = conversationsList.getSelectedValue();

            if (message.isEmpty() || selectedFriend == null) {
                JOptionPane.showMessageDialog(panel, "Please select a friend and enter a message.");
                return;
            }

            try {
                out.println("send_message");
                out.println(selectedFriend);
                out.println(message);

                String response = in.readLine(); // Read server response
                if ("not_friends".equals(response)) {
                    JOptionPane.showMessageDialog(panel, "You and the selected user must be friends to send messages.");
                } else if ("failure".equals(response)) {
                    JOptionPane.showMessageDialog(panel, "Failed to send the message. Please check restrictions.");
                } else {
                    messageField.setText(""); // Clear the input field
                    loadSelectedConversation(conversationsList, conversationArea);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Failed to send message.");
            }
        });

        deleteButton.addActionListener(e -> {
            String message = messageField.getText().trim();
            String selectedFriend = conversationsList.getSelectedValue();

            if (message.isEmpty() || selectedFriend == null) {
                JOptionPane.showMessageDialog(panel, "Please select a friend and enter a message to delete.");
                return;
            }

            try {
                out.println("delete_message");
                out.println(selectedFriend);
                out.println(message);

                String response = in.readLine(); // Read server response
                if ("not_friends".equals(response)) {
                    JOptionPane.showMessageDialog(panel, "You and the selected user must be friends to delete messages.");
                } else if ("failure".equals(response)) {
                    JOptionPane.showMessageDialog(panel, "Failed to delete the message. It may not exist.");
                } else {
                    loadSelectedConversation(conversationsList, conversationArea);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error while deleting the message.");
            }
        });

        return panel;
    }

    private static JPanel createFriendsPanel(DefaultListModel<String> friendsModel) {
        JPanel panel = new JPanel(new BorderLayout());
        friendsList = new JList<>(friendsModel); // Initialize the class-wide friendsList variable
        JTextField friendTextField = new JTextField();
        JButton addFriendButton = new JButton("Add Friend");
        JButton removeFriendButton = new JButton("Remove Friend");

        autoRefreshTimer = new Timer(1000, e -> {
            if (loggedIn) {
                refreshFriendsList(friendsModel, friendsList); // No need to pass friendsList here
            }
        });
        autoRefreshTimer.start();

        // Populate friends list initially
        refreshFriendsList(friendsModel, friendsList);

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
        JButton refreshButton = new JButton("Refresh");

        // Method to load all users
        Runnable loadAllUsers = () -> {
            new Thread(() -> {
                try {
                    out.println("get_users"); // Command to fetch all users
                    String user;
                    SwingUtilities.invokeLater(searchResultsModel::clear); // Clear the current list
                    while (!(user = in.readLine()).equals("END")) {
                        String finalUser = user;
                        SwingUtilities.invokeLater(() -> searchResultsModel.addElement(finalUser));
                    }
                } catch (IOException ex) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Failed to fetch users."));
                }
            }).start();
        };

        // Load all users when the panel is initialized
        loadAllUsers.run();

        // Search functionality
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            searchResultsModel.clear(); // Clear current search results
            if (!query.isEmpty()) {
                new Thread(() -> {
                    out.println("search_user");
                    out.println(query);
                    try {
                        String result;
                        while (!(result = in.readLine()).equals("END")) {
                            String finalResult = result;
                            SwingUtilities.invokeLater(() -> searchResultsModel.addElement(finalResult));
                        }
                    } catch (IOException ex) {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Failed to fetch search results."));
                    }
                }).start();
            } else {
                loadAllUsers.run(); // Reload all users if the search field is empty
            }
        });

        // Refresh button resets the tab to its original state
        refreshButton.addActionListener(e -> {
            searchField.setText(""); // Clear the search field
            loadAllUsers.run(); // Reload all users
        });

        // Handle selection of a user in the search results
        searchResults.addListSelectionListener(e -> {
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
        });

        // Create a control panel for the search field and buttons
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(searchField, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(searchButton);
        buttonPanel.add(refreshButton);
        controlPanel.add(buttonPanel, BorderLayout.EAST);

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(searchResults), BorderLayout.CENTER);

        return panel;
    }

}