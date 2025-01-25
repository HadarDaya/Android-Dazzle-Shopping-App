package com.example.shoppingapp.adapter;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.Utils;
import com.example.shoppingapp.repository.JewelryRepository;
import com.example.shoppingapp.repository.UserCartRepository;
import com.example.shoppingapp.model.JewelryItem;
import com.example.shoppingapp.model.UserCartItem;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

/* CharacterAdapter extends from RecyclerView.Adapter
    This class responsible for connecting the jewelry items in the user's cart to the view (RecyclerView). */
public class UserCartAdapter extends RecyclerView.Adapter<UserCartAdapter.MyViewHolder> {
    /* dataSet Holds the data we want to display in the RecyclerView.
       Defined as an ArrayList of UserCartItem objects, which are the items the user add and their information (including name, quantity , image, etc). */
    private ArrayList<UserCartItem> dataSet; // the array of data (retrieved from database)
    private final JewelryRepository jewelryRepository = new JewelryRepository(); // Helper for managing Firebase database operations related to jewelry items.
    private final UserCartRepository userCartHelper = new UserCartRepository(); // Helper  for managing user cart data in Firebase.
    private final String username; // Username of the currently logged-in user
    private final TextView textViewTotalCartPrice; // TextView to display the total price of items in the cart.

    /**
     * Constructor for UserCartAdapter
     * @param dataSet                   ArrayList of UserCartItem objects to display in the RecyclerView.
     * @param username                  The currently logged-in user's username.
     * @param textViewTotalCartPrice    extView for displaying the total price.
     */
    public UserCartAdapter(ArrayList<UserCartItem> dataSet, String username,
                           TextView textViewTotalCartPrice) {
        this.dataSet = dataSet;
        this.username = username;
        this.textViewTotalCartPrice = textViewTotalCartPrice;
    }

    /* The inner class MyViewHolder
        Role: Holds references to the display elements (TextView, ImageView) that appear in each row of the RecyclerView.
        When creating a new row (new ViewHolder), it looks for view elements by their id.*/
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName;
        TextView textViewItemTotalPrice;
        ImageView imageView;
        Button removeFromCartButton;
        TextInputEditText quantityTextInput;

        /**
         * Constructor for the ViewHolder- Pulls out all the organs that exist inside the card according to id
         * @param itemView The view for the item in the RecyclerView.
         */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.row_user_cart_name);
            textViewItemTotalPrice = itemView.findViewById(R.id.row_user_cart_total_price);
            imageView = itemView.findViewById(R.id.row_user_cart_image);
            removeFromCartButton = itemView.findViewById(R.id.row_item_remove_from_cart_btn);
            quantityTextInput = itemView.findViewById(R.id.row_user_cart_quantity);
        }
    }

    /* Lay out the map for us on top of our line.
       The function Creates a new view for each row in the RecyclerView.*/
    @NonNull
    public UserCartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_items_user_cart, parent, false);
        return new MyViewHolder(view);
    }

    /* Build one row inside our recyclerView
        The function connects data to a specific row in the RecyclerView.
        According to the position, extracts information from the list (dataSet) and updates the views in the ViewHolder.*/
    @Override
    public void onBindViewHolder(@NonNull UserCartAdapter.MyViewHolder holder, int position) {
        UserCartItem userCartItem = dataSet.get(position);

        jewelryRepository.fetchJewelryDetailsByID(userCartItem.getItemId())
                .addOnSuccessListener(jewelryItem -> {
                    if (jewelryItem != null) {
                        // Set the initial display of the jewelry item
                        initialDisplayJewelryItem(holder, jewelryItem, userCartItem);
                        setEvents(holder, jewelryItem, position);
                    } else {
                        System.err.println("Jewelry item not found");
                    }
                })
                .addOnFailureListener(e -> System.err.println("Error fetching data: " + e.getMessage()));
    }

    /**
     * The function sets the events (clicking, onTextChanged etc) for objects in view
     * @param holder        The ViewHolder for the item.
     * @param jewelryItem   The jewelry item associated with the cart item.
     * @param position      The position of the item in the dataset.
     */
    public void setEvents(UserCartAdapter.MyViewHolder holder, JewelryItem jewelryItem, int position)
    {
        // Event listener for quantity changes
        holder.quantityTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do not allow empty string- display "1" by default
                if (s.length() == 0) {
                    holder.quantityTextInput.setText(String.valueOf(1)); // Default to "1" if empty
                    holder.quantityTextInput.setSelection(1); // Move cursor to the end
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                String newText = s.toString();
                int currentPosition = holder.getAdapterPosition();

                // Validate position to prevent IndexOutOfBoundsException
                // (because we remove items from dataset)
                if (currentPosition != RecyclerView.NO_POSITION && currentPosition < dataSet.size()) {
                    try {
                        if (!newText.isEmpty()) {
                            int newQuantity = Integer.parseInt(newText);

                            // Update view of total price
                            double totalPrice = jewelryItem.getPrice() * newQuantity;
                            String totalPriceString = "$" + totalPrice;
                            holder.textViewItemTotalPrice.setText(totalPriceString);

                            // Prevent triggering the TextWatcher recursively by directly updating the dataset
                            dataSet.get(currentPosition).setQuantity(newQuantity);

                            // Update quantity in the Firebase database
                            userCartHelper.updateUserCartQuantity(username, jewelryItem.getId(), newQuantity);

                /* Update total price in the cart
                   Note: we don't need to notify the adapter that the item was changed,
                   because we already update as a part of this function */
                            updateTotalPrice();
                        }
                    } catch (NumberFormatException e) {
                        Log.e("UserCartAdapter", "Invalid number format in quantity input: " + e.getMessage());
                    }
                } else {
                    Log.e("UserCartAdapter", "Invalid position detected in afterTextChanged: " + currentPosition);
                }
            }
        });

        // Events for add to cart button
        holder.removeFromCartButton.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION && currentPosition < dataSet.size()) {
                userCartHelper.removeRowFromUserCart(username, dataSet.get(currentPosition).getItemId())
                        .addOnSuccessListener(aVoid -> {
                            // Remove the item from the dataSet
                            dataSet.remove(currentPosition);

                            // Notify the adapter about the item removal
                            notifyItemRemoved(currentPosition);

                            // Update total price in the cart
                            updateTotalPrice();
                        })
                        .addOnFailureListener(e -> Log.d("UserCartAdapter", "Failed to remove item: " + e.getMessage()));
            }
        });
    }

    /**
     * Sets the initial view, based on values of given jewelryItem and userCartItem
     * @param holder            The ViewHolder for the item.
     * @param jewelryItem       The jewelry item to display.
     * @param userCartItem      The user cart item containing the quantity.
     */
    public void initialDisplayJewelryItem(UserCartAdapter.MyViewHolder holder, JewelryItem jewelryItem, UserCartItem userCartItem)
    {
        // Set the jewelry item name
        String name = jewelryItem.getName();
        holder.textViewItemName.setText(name);

        // Calculate and display the total price for this item
        double totalPrice = jewelryItem.getPrice() * userCartItem.getQuantity();
        String totalPriceString = '$' + String.valueOf(totalPrice);
        holder.textViewItemTotalPrice.setText(totalPriceString);

        // Set the item quantity
        int quantity = userCartItem.getQuantity();
        holder.quantityTextInput.setText(String.valueOf(quantity));

        // Set the item image
        // 1. Get the resource name stored in the JewelryItem
        String imageName = jewelryItem.getImageResName();
        // 2. Look up the drawable resource ID from the map
        Integer resId = Utils.jewelryResourceMap().get(imageName);
        if (resId != null) {
            // If resource ID exists in the map, set the image
            Drawable drawable = ContextCompat.getDrawable(holder.itemView.getContext(), resId);
            holder.imageView.setImageDrawable(drawable);
        } else {
            // If resource not found, set a default image or handle error
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_launcher_background)); // default image
        }
    }

    /**
     * Updates the total price of all the items in the cart.
     *  - if the dataset is empty- display "$0.0"
     */
    public void updateTotalPrice() {
        double[] totalPrice = {0.0}; // Use an array as a mutable container
        if (!dataSet.isEmpty()) {
            for (UserCartItem item : dataSet) {
                jewelryRepository.fetchJewelryDetailsByID(item.getItemId())
                        .addOnSuccessListener(jewelryItem -> {
                            if (jewelryItem != null) {
                                totalPrice[0] += jewelryItem.getPrice() * item.getQuantity();
                                String priceText = "$" + totalPrice[0];
                                textViewTotalCartPrice.setText(priceText);
                            }
                        })
                        .addOnFailureListener(e -> System.err.println("Error calculating total price: " + e.getMessage()));
            }
        }
        else
        {
            String priceText = "$0.0";
            textViewTotalCartPrice.setText(priceText);
        }
    }

    /**
     * @return How many members in total will be in our adapter (which is the size of the array it received).
     * in this way the adapter knows how many lines to display.
     */
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}


