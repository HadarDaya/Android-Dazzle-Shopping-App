package com.example.shoppingapp.repository;

import com.example.shoppingapp.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//  Class for interacting with Firebase Realtime Database related to users.
public class UserRepository {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    /**
     * Inserts a user's data (username and phone number) into the Firebase Realtime Database.
     * @param username      username The username of the user.
     * @param phone         The phone number of the user.
     */
    public void insertUsername(String username, String phone) {
        DatabaseReference myRef = database.getReference("users").child(username);

        // build an object of User
        User user = new User(username,phone);

        // Put it in the realtime database
        myRef.setValue(user);
    }
}
