public interface runDatabase {
    void saveUser(User user);
    User loadUser(String username);
    void saveMessage(Message message);
    List<Message> loadMessages(String username);
}
