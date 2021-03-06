package com.heavenlyhell.instamemo.models;

/**
 * Created by sande on 3/6/2018.
 */

public class User {
    private String username;
    private String user_id;
    private String email;
    private String phone_number;

    public User(String username, String user_id, String email, String phone_number) {
        this.username = username;
        this.user_id = user_id;
        this.email = email;
        this.phone_number = phone_number;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                ", phone_number='" + phone_number + '\'' +
                '}';
    }
}
