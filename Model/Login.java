package Model;

public class Login {
    private static int userId;
    private static String username;

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        Login.userId = userId;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Login.username = username;
    }
}
