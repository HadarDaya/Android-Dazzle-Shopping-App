package com.example.shoppingapp;

import com.example.shoppingapp.model.JewelryItem;
import com.example.shoppingapp.model.UserCartItem;
import com.google.firebase.database.DataSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* Utility class for managing jewelry items and user cart items in the shopping app.
   Includes methods for retrieving and processing data from Firebase and mapping resources.*/

public class Utils {

    public static Map<String, Integer> jewelryResourceMap() {
        /* Creates a map of jewelry resource names to their corresponding drawable resource IDs.
           Used for associating jewelry items with their images.*/
        Map<String, Integer> jewelryResourceMap = new HashMap<>();

        // Add resource mappings for different jewelry types
        // Necklaces
        jewelryResourceMap.put("bluebeachstoneneck", R.drawable.bluebeachstoneneck);
        jewelryResourceMap.put("sparklingsquareneck", R.drawable.sparklingsquareneck);
        jewelryResourceMap.put("rosegoldpearlneck", R.drawable.rosegoldpearlneck);
        jewelryResourceMap.put("rosesilverpearlneck", R.drawable.rosesilverpearlneck);
        jewelryResourceMap.put("rhombusglowneck", R.drawable.rhombusglowneck);
        jewelryResourceMap.put("silverpearlneck", R.drawable.silverpearlneck);
        jewelryResourceMap.put("lightpearlneck", R.drawable.lightpearlneck);
        jewelryResourceMap.put("threepearlneck", R.drawable.threepearlneck);
        jewelryResourceMap.put("starlightlnecklace", R.drawable.starlightlnecklace);
        jewelryResourceMap.put("seapearlnecklace", R.drawable.seapearlnecklace);
        jewelryResourceMap.put("pureloveneck", R.drawable.pureloveneck);
        jewelryResourceMap.put("luxurypearlnecklsilver", R.drawable.luxurypearlnecklsilver);
        jewelryResourceMap.put("luxurypearlneckgold", R.drawable.luxurypearlneckgold);
        jewelryResourceMap.put("classicnecksilver", R.drawable.classicnecksilver);
        jewelryResourceMap.put("classicneckgold", R.drawable.classicneckgold);

        // Bracelets
        jewelryResourceMap.put("silverpearlbracelet", R.drawable.silverpearlbracelet);
        jewelryResourceMap.put("goldpearlbracelet", R.drawable.goldpearlbracelet);
        jewelryResourceMap.put("rhombusglowbracelet", R.drawable.rhombusglowbracelet);
        jewelryResourceMap.put("silverbeadedbracelet", R.drawable.silverbeadedbracelet);
        jewelryResourceMap.put("luxurybraceletm", R.drawable.luxurybraceletm);
        jewelryResourceMap.put("luxurybracelets", R.drawable.luxurybracelets);

        // Earrings
        jewelryResourceMap.put("classicsilverearring", R.drawable.classicsilverearring);
        jewelryResourceMap.put("classicgoldearring", R.drawable.classicgoldearring);
        jewelryResourceMap.put("rosegoldpearlearring", R.drawable.rosegoldpearlearring);
        jewelryResourceMap.put("rosesilverpearlearring", R.drawable.rosesilverpearlearring);
        jewelryResourceMap.put("luxurypearlearringsilver", R.drawable.luxurypearlearringsilver);
        jewelryResourceMap.put("luxurypearlearringgold", R.drawable.luxurypearlearringgold);
        jewelryResourceMap.put("everydayearringgold", R.drawable.everydayearringgold);
        jewelryResourceMap.put("everydayearringsilver", R.drawable.everydayearringsilver);
        jewelryResourceMap.put("heartearring", R.drawable.heartearring);

        // Rings
        jewelryResourceMap.put("bluebeachstonering", R.drawable.bluebeachstonering);
        jewelryResourceMap.put("classicring", R.drawable.classicring);
        jewelryResourceMap.put("luxuryring", R.drawable.luxuryring);
        jewelryResourceMap.put("purplestonering", R.drawable.purplestonering);
        jewelryResourceMap.put("uniqueflowerring", R.drawable.uniqueflowerring);
        jewelryResourceMap.put("shellring", R.drawable.shellring);
        jewelryResourceMap.put("everydayringsilver", R.drawable.everydayringsilver);
        jewelryResourceMap.put("everydayringgold", R.drawable.everydayringgold);

        return jewelryResourceMap;
    }

    /**
     * Adds items from a specific category to the provided dataset.
     * @param categorySnapshot   A snapshot of the category data from Firebase.
     * @param dataSet            The dataset to add with jewelry items.
     */
    public static void addItemsFromDatasetByCategory(DataSnapshot categorySnapshot, ArrayList<JewelryItem> dataSet)
    {
        // Iterate over each item in the category
        for (DataSnapshot itemSnapshot : categorySnapshot.getChildren()) {
            Long id = itemSnapshot.child("id").getValue(long.class);
            String name = itemSnapshot.child("name").getValue(String.class);
            Double price = itemSnapshot.child("price").getValue(Double.class);
            String imageResName = itemSnapshot.child("imageResName").getValue(String.class);
            Integer quantityInStock = itemSnapshot.child("quantityInStock").getValue(Integer.class);

            if (id != null && name != null && price != null && imageResName != null && quantityInStock != null) {
                // Add the item to the dataset
                JewelryItem jewelryItem = new JewelryItem(id, name, categorySnapshot.getKey(), price, quantityInStock, imageResName);
                dataSet.add(jewelryItem);
            }
        }
    }

    /**
     * Iterates over the children of the DataSnapshot and populates the dataSet with UserCartItem objects.
     * @param cartSnapshot  A snapshot of the cart data from Firebase.
     * @param dataSet       The dataset to add with user cart items.
     */
    public static void addUserCartToDataset(DataSnapshot cartSnapshot, ArrayList<UserCartItem> dataSet) {
        for (DataSnapshot itemSnapshot : cartSnapshot.getChildren()) {
            // Extract fields from the snapshot
            Long id = itemSnapshot.child("id").getValue(Long.class);
            Long quantity = itemSnapshot.child("quantity").getValue(Long.class);

            if (id != null && quantity != null) {
                UserCartItem cartItem = new UserCartItem(id, quantity.intValue());
                dataSet.add(cartItem);
            }
        }
    }

}
