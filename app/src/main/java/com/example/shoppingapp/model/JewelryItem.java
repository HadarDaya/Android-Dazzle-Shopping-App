package com.example.shoppingapp.model;

/* Represents an item of jewelry in the store.
   Each item includes details such as its unique ID, name, category, price, stock quantity, and image resource for display.*/

public class JewelryItem {
    private long id; // Unique identifier for the jewelry item.
    private String name;
    private String categoryName;
    private double price;
    private int quantityInStock;
    private String imageResName; // Name of the image resource for displaying the item in the app.

    /* Default constructor
       Initializes a new instance of the JewelryItem class with default values.*/
    public JewelryItem() {
    }

    /**
     *
     * @param id                Unique identifier for the item.
     * @param name              Name of the item.
     * @param categoryName      Category name of the item.
     * @param price             Price of the item.
     * @param quantityInStock   Quantity of the item in stock.
     * @param imageResName      Name of the image resource for the item.
     */
    public JewelryItem(long id, String name, String categoryName, double price, int quantityInStock, String imageResName) {
        this.id = id;
        this.name = name;
        this.categoryName = categoryName;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.imageResName = imageResName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public String getImageResName() {
        return imageResName;
    }

    public void setImageResName(String imageResName) {
        this.imageResName = imageResName;
    }
}


