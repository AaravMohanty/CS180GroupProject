public interface UserData {
    String getUsername();
    String getPassword();
    String getEmail();
    String getBio();
    void setBio(String bio);
    boolean isBlocked(User otherUser);
}
