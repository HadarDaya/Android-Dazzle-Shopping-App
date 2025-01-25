package com.example.shoppingapp.model;

/* Represents a single item in a user's shopping cart.
    This class includes the item's unique ID and the quantity of the item added to the cart, which are stored in the database. */
public class UserCartItem {

    private long itemId;
    private int quantity;

    /* Default constructor.
       Initializes a new instance of the UserCartItem class with default values. */
    public UserCartItem () {

    }

    /**
     * Initializes a new instance of the UserCartItem class with the specified item ID and quantity.
     * @param itemId    The unique identifier of the item.
     * @param quantity  The quantity of the item in the cart.
     */
    public UserCartItem(long itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
