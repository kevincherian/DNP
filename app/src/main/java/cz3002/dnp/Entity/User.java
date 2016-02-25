package cz3002.dnp.Entity;

/**
 * Created by hizac on 24/2/2016.
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String fullname;
    private String email;
    private boolean type; // 0 for patient, 1 for doctor

    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getFullname() {
        return fullname;
    }
    public String getEmail() {
        return email;
    }
    public boolean isDoctor() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public void setType(boolean type) {
        this.type = type;
    }
}
