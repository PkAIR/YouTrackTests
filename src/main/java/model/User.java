package model;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String passwordConfirmation;
    private String newPassword;
    private String newPasswordConfirmation;
    private String fullName;
    private String email;
    private String jabber;
    private ArrayList<UserGroup> groups;

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJabber() {
        return jabber;
    }

    public void setJabber(String jabber) {
        this.jabber = jabber;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }

    public void setNewPasswordConfirmation(String newPasswordConfirmation) {
        this.newPasswordConfirmation = newPasswordConfirmation;
    }

    public ArrayList<UserGroup> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<UserGroup> groups) {
        this.groups = groups;
    }

    public User(String userName,
                String password,
                String fullName,
                String email,
                String jabber,
                ArrayList<UserGroup> groups) {
        this.username = userName;
        this.password = password;
        this.passwordConfirmation = password;
        this.fullName = fullName;
        this.email = email;
        this.jabber = jabber;
        this.groups = groups;
    }
}
