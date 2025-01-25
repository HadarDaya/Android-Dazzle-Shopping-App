package com.example.shoppingapp.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.shoppingapp.model.JewelryItem;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

//  Class for interacting with Firebase Realtime Database related to user cart.
public class UserCartRepository {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    public UserCartRepository() {
    }

    /**
     * Inserts a jewelry item into the user cart in the Firebase Realtime Database.
     * If the item already exists in the user's cart, it will show a Toast message.
     * @param item      The JewelryItem to add to the cart.
     * @param username  The username of the user.
     * @param context   The context to show Toast messages.
     */
    public void insertIntoUserCart(JewelryItem item, String username, Context context) {
        DatabaseReference tableUserCart = database.getReference("userCart");

        // Check if the item already exists in the user's cart
        tableUserCart.child(username).child(String.valueOf(item.getId()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Item already exists in the cart
                            String message = "Item " + item.getName() + " already exists in your cart.";
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } else {
                            // Insert a new entry with id and quantity
                            Map<String, Object> itemData = new HashMap<>();
                            itemData.put("id", item.getId());
                            itemData.put("quantity", 1);

                            tableUserCart.child(username).child(String.valueOf(item.getId()))
                                    .setValue(itemData)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            String message = "Item " + item.getName() + " was successfully added to your cart.";
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        } else {
                                            String message = "Failed to add item " + item.getName() + " to your cart.";
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors
                        String message = "Error: " + databaseError.getMessage();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Updates the quantity of a specific jewelry item in the user's cart.
     * @param username      The username of the user.
     * @param jewelryID     The ID of the jewelry item to update.
     * @param newQuantity   The new quantity to set.
     */
    public void updateUserCartQuantity(String username, long jewelryID, int newQuantity) {
        DatabaseReference userCartRef = database.getReference("userCart");

        /* Access the specific user by 'username' and then the jewelry by 'jewelryID'
            Updates the quantity of the specific jewelry item for the user in Firebase */
        userCartRef.child(username).child(String.valueOf(jewelryID)).child("quantity")
                .setValue(newQuantity)
                .addOnSuccessListener(aVoid -> {
                    // Success: The quantity was updated
                    Log.d("updateUserCartQuantity" ,"Quantity updated successfully for jewelryID: " + jewelryID + " and username: " + username);
                })
                .addOnFailureListener(e -> {
                    // Failure: Handle error
                    Log.d("updateUserCartQuantity" ,"Error updating quantity: " + e.getMessage());
                });
    }

    /**
     * Removes a jewelry item from the user's cart.
     * @param username              The username of the user.
     * @param jewelryIdToRemove     The ID of the jewelry item to remove.
     */
    public Task<Void> removeRowFromUserCart(String username, long jewelryIdToRemove) {
        // Create a TaskCompletionSource to manage the task
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        DatabaseReference userCartRef = database.getReference("userCart");

    /* Access the specific user by 'username' and then the jewelry item by 'jewelryID'
       Removes the specific jewelry item from the user's cart in Firebase */
        userCartRef.child(username).child(String.valueOf(jewelryIdToRemove))
                .removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Notify the task of successful completion
                    Log.d("removeJewelryFromCart", "Jewelry item removed successfully for jewelryID: " + jewelryIdToRemove + " and username: " + username);
                    taskCompletionSource.setResult(null);
                })
                .addOnFailureListener(e -> {
                    // Notify the task of failure
                    Log.d("removeJewelryFromCart", "Error removing jewelry item: " + e.getMessage());
                    taskCompletionSource.setException(e);
                });

        // Return the Task to the caller
        return taskCompletionSource.getTask();
    }

    /**
     * Removes all jewelry items from the user's cart.
     * @param username The username of the user.
     * @return  A task indicating the completion of the operation.
     */
    public Task<Void> removeAllFromUserCart(String username) {
        // TaskCompletionSource to manage the task
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        DatabaseReference userCartRef = database.getReference("userCart").child(username);

        // Removes all items in the user's cart from Firebase
        userCartRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Success: Signal task completion
                    Log.d("removeAllUserCart", "Jewelries in cart were removed successfully for username: " + username);
                    taskCompletionSource.setResult(null); // Success with no result
                })
                .addOnFailureListener(e -> {
                    // Failure: Signal task failure
                    Log.d("removeAllUserCart", "Error removing from user's cart: " + e.getMessage());
                    taskCompletionSource.setException(e); // Pass the exception to the task
                });

        // Return the task
        return taskCompletionSource.getTask();
    }

    /**
     * The function adds items into dataSet variable (items to show in cart for given user)
     * as a result of clicking on shopping cart icon
     *
     * @param username      The username of the user whose cart items should be retrieved.
     * @return dataSet      A Task that representing the asynchronous operation to retrieve the dataset after addition.
     */
    public Task<DataSnapshot> getCartByUsername(String username) {
        DatabaseReference jewelryForUserRef;

        jewelryForUserRef = database.getReference("userCart").child(username);

        return jewelryForUserRef.get(); // Return the Task object directly
    }
}
