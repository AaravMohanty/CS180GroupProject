public interface DataInterface {
    User createUser(String username, String password, String email);
    boolean deleteUser(String username);
    User getUser(String username);
    boolean authenticate(String username, String password);
}