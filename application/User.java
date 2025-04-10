package application;


/**
 
The User class represents a user entity in the system.
It contains the user's details such as userName, password, and role.*/
public class User {
    private String userName;
    private String password;
    private String name;
    private String email;
    private String roles; // For Storing multiple roles

    // Constructor for a user with multiple roles
    public User(String userName, String password, String name, String email, String roles) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    // Sets the role of the user.
    public void setRole(String roles) {
        this.roles = roles;
    }

    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRoles() {  return roles;}
}