package com.example.shoppingapp.model;

/* Represents a registered user in the shopping app.
   Each user has a username and a phone number, which are stored in the database. */

public class User {
    private String username;
    private String phone;

    /**
     * Constructs a new User instance with the specified username and phone number.
     * @param username  the username of the user.
     * @param phone     the phone number of the user.
     */
    public User(String username, String phone) {
        this.username = username;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
