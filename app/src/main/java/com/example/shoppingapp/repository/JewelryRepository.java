package com.example.shoppingapp.repository;
import android.content.Context;
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
import java.util.concurrent.atomic.AtomicInteger;

//  Class for interacting with Firebase Realtime Database related to jewelry items.
public class JewelryRepository {
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    public JewelryRepository() {
    }

    /**
     * Fetches jewelry details by the jewelry item's ID from the Firebase Realtime Database.
     * @param jewelryId  The ID of the jewelry item to fetch.
     * @return A Task that will resolve with the fetched JewelryItem.
     */
    public Task<JewelryItem> fetchJewelryDetailsByID(long jewelryId) {
        TaskCompletionSource<JewelryItem> taskSource = new TaskCompletionSource<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference categoryRef = database.getReference("jewelry");

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) { // Iterate over categories
                    for (DataSnapshot itemSnapshot : categorySnapshot.getChildren()) { // Iterate over items
                        Long id = itemSnapshot.child("id").getValue(Long.class);
                        if (id != null && id == jewelryId) {
                            String name = itemSnapshot.child("name").getValue(String.class);
                            Double price = itemSnapshot.child("price").getValue(Double.class);
                            String imageResName = itemSnapshot.child("imageResName").getValue(String.class);
                            Integer quantityInStock = itemSnapshot.child("quantityInStock").getValue(Integer.class);

                            if (name != null && price != null && imageResName != null && quantityInStock != null) {
                                JewelryItem jewelryItem = new JewelryItem(
                                        id,
                                        name,
                                        categorySnapshot.getKey(), // categoryName
                                        price,
                                        quantityInStock,
                                        imageResName
                                );

                                // Set the result for the task
                                taskSource.setResult(jewelryItem);
                                return;
                            }
                        }
                    }
                }
                // If not found, set null as the result
                taskSource.setResult(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Set the exception for the task
                taskSource.setException(new Exception(databaseError.getMessage()));
            }
        });

        return taskSource.getTask();
    }

    /**
     * Updates the quantities of jewelry items in stock based on the user's cart.
     * @param username  The username of the user.
     * @param context   The context of the current activity to show Toasts.
     * @return A task indicating the completion of the operation.
     */
    public Task<Void> updateQuantitiesInStock(String username, Context context) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        AtomicInteger pendingTasks = new AtomicInteger(0); // Track pending tasks
        boolean[] itemFound = {true}; // Flag to track if all items were found

        DatabaseReference jewelryRef = database.getReference("jewelry");
        DatabaseReference cartRef = database.getReference("userCart").child(username);

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot cartSnapshot) {
                if (cartSnapshot.exists()) {
                    // Iterate over all items in the user's cart
                    for (DataSnapshot cartItemSnapshot : cartSnapshot.getChildren()) {
                        Long itemId = cartItemSnapshot.child("id").getValue(Long.class);
                        Integer cartQuantity = cartItemSnapshot.child("quantity").getValue(Integer.class);

                        if (itemId != null && cartQuantity != null) {
                            pendingTasks.incrementAndGet(); // Increment for each cart item

                            // Iterate over all categories in the 'jewelry' table
                            jewelryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot jewelrySnapshot) {
                                    if (jewelrySnapshot.exists()) {
                                        boolean itemFoundInCategory = false;

                                        for (DataSnapshot categorySnapshot : jewelrySnapshot.getChildren()) {
                                            DataSnapshot itemSnapshot = categorySnapshot.child("entry" + itemId);
                                            if (itemSnapshot.exists()) {
                                                JewelryItem jewelryItem = itemSnapshot.getValue(JewelryItem.class);
                                                if (jewelryItem != null && jewelryItem.getQuantityInStock() >= cartQuantity) {
                                                    // Update stock quantity
                                                    int newQuantity = jewelryItem.getQuantityInStock() - cartQuantity;
                                                    itemSnapshot.getRef().child("quantityInStock").setValue(newQuantity);
                                                    itemFoundInCategory = true;
                                                    break;
                                                }
                                                if (jewelryItem != null && jewelryItem.getQuantityInStock() < cartQuantity) {
                                                    Toast.makeText(context,
                                                            "There are only " + jewelryItem.getQuantityInStock() +
                                                                    " available items in stock for product " + jewelryItem.getName(),
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }

                                        if (!itemFoundInCategory) {
                                            itemFound[0] = false; // Set flag to false if item is not found
                                        }
                                    }

                                    // Decrement pending tasks and complete if all tasks are finished
                                    if (pendingTasks.decrementAndGet() == 0) {
                                        if (itemFound[0]) {
                                            taskCompletionSource.setResult(null); // All tasks completed successfully
                                        } else {
                                            taskCompletionSource.setException(new Exception("One or more items not found in any category."));
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    taskCompletionSource.setException(error.toException());
                                }
                            });
                        }
                    }
                } else {
                    // If no cart items are found, complete the task successfully
                    taskCompletionSource.setResult(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskCompletionSource.setException(error.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    /**
     * The function adds items into dataSet variable (items from given category)
     * as a result of clicking on logo or menu
     *
     * @param categoryName  The name of the category to retrieve items from.
     * @return dataSet       A Task that representing the asynchronous operation to retrieve the dataset.
     */
    public Task<DataSnapshot> getRowsByCategoryName(String categoryName) {
        DatabaseReference jewelryRef;

        if (!categoryName.equals("all")) {
            // If category is not "all", get the items from the specific category
            jewelryRef = database.getReference("jewelry").child(categoryName);
        } else {
            // If category is "all", get all items from all categories
            jewelryRef = database.getReference("jewelry");
        }

        return jewelryRef.get(); // Return the Task object directly
    }

}
