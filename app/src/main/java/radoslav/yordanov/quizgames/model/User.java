package radoslav.yordanov.quizgames.model;

/**
 * Created by radoslav on 4/28/16.
 */
public class User {
    private String status;
    private int id;
    private String username;
    private String password;
    private int user_roleId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUser_roleId() {
        return user_roleId;
    }

    public void setUser_roleId(int user_roleId) {
        this.user_roleId = user_roleId;
    }
}
