package com.example.shoppingapp.adapter;

import android.graphics.drawable.Drawable;
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
import com.example.shoppingapp.model.JewelryItem;
import com.example.shoppingapp.repository.UserCartRepository;
import java.util.ArrayList;

/* CharacterAdapter extends from RecyclerView.Adapter
   This class responsible for connecting the list of jewelry items the user add to the view (RecyclerView). */
public class JewelryAdapter extends RecyclerView.Adapter<JewelryAdapter.MyViewHolder> {
    /* dataSet Holds the data we want to display in the RecyclerView.
       Defined as an ArrayList of JewelryItem objects, which are the jewelry' information (including name, description and image). */
    private ArrayList<JewelryItem> dataSet; // The array of data (retrieved from database)
    private final UserCartRepository userCartRepository = new UserCartRepository(); // Helper for managing Firebase operations related to user cart
    private final String username;  // Username of the currently logged-in user


    /**
     * Constructor for the JewelryAdapter.
     * @param dataSet       List of JewelryItem objects to display in the RecyclerView.
     * @param username      Username of the currently logged-in user.
     */
    public JewelryAdapter(ArrayList<JewelryItem> dataSet, String username) {
        this.dataSet = dataSet;
        this.username = username;
    }

    /* The inner class MyViewHolder
       Role: Holds references to the display elements (TextView, ImageView) that appear in each row of the RecyclerView.
       When creating a new row (new ViewHolder), it looks for view elements by their id.*/
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewPrice;
        ImageView imageView;
        Button addToCartButton;
        TextView outOfStockMsg;

        /**
         * Constructor for MyViewHolder- Pulls out all the organs that exist inside the card according to id
         * @param itemView  The root view of a single row in the RecyclerView.
         */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.row_item_name);
            textViewPrice = itemView.findViewById(R.id.row_item_price);
            imageView = itemView.findViewById(R.id.row_item_image);
            addToCartButton = itemView.findViewById(R.id.row_item_add_to_cart_btn);
            outOfStockMsg = itemView.findViewById(R.id.row_item_out_of_stock_msg);
        }
    }

    /* Lay out the map for us on top of our line.
        The function Creates a new view for each row in the RecyclerView.*/
    @NonNull
    public JewelryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_items_jewerly, parent, false);

        return new MyViewHolder(view);
    }

    /* Build one row inside our recyclerView
       The function connects data to a specific row in the RecyclerView.
       According to the position, extracts information from the list (dataSet) and updates the TextView and the image in the ViewHolder.*/
    @Override
    public void onBindViewHolder(@NonNull JewelryAdapter.MyViewHolder holder, int position) {

        JewelryItem jewelryItem = dataSet.get(position);

        // Set the jewelry name
        String name = jewelryItem.getName();
        holder.textViewName.setText(name);

        // Set the jewelry price
        String priceString = '$' + String.valueOf(jewelryItem.getPrice());
        holder.textViewPrice.setText(priceString);

        // Set the jewelry image
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

        // Handle "out of stock" scenario
        if (jewelryItem.getQuantityInStock() == 0) {
            holder.addToCartButton.setVisibility(View.INVISIBLE);
            holder.outOfStockMsg.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.addToCartButton.setVisibility(View.VISIBLE);
            holder.outOfStockMsg.setVisibility(View.INVISIBLE);
        }

        // Handle clicking on Add To Cart button of a row
        holder.addToCartButton.setOnClickListener(v -> {
            // insert into 'UserCart' table + show message
            userCartRepository.insertIntoUserCart(dataSet.get(position), username, v.getContext());
        });
    }

    /**
     * @return How many members in total will be in our adapter (which is the size of the array it received).
     *          in this way the adapter knows how many lines to display.*/

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}


