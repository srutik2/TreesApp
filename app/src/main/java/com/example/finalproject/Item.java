package com.example.finalproject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item {

    /**Display name. */
    private String name;
    /**Quantity in inventory.. */
    private int quantity;
    /**R.drawable vector image that represents item. */
    private int icon;
    /**Where to find item, used in info page. */
    private String locationDescription;
    /**Item description used in info page. */
    private String description;

    /**Value of the the item when sold. default value is 1. Can change constructor later. */
    private int value = 1;

    /** A list of Item names that pluralize oddly. */
    private final static Map<String, String> pluralName = new HashMap<String, String>(){{
        put("Leaf", "Leaves");
    }};

    /**Used to create an item used in the info page.
     * will update soon to include other parameters.
     * format: name, locDesc, desc, iconName
     * @param allInfo a String[] containing attributes.
     * @param itemIcon R.drawable id for the icon.
     */
    public Item(final List<String> allInfo, final int itemIcon) {
        if (allInfo == null) {
            return;
        }
        int i = -1;
        name = allInfo.get(++i);
        locationDescription = allInfo.get(++i);
        description = allInfo.get(++i);
        quantity = Integer.parseInt(allInfo.get(++i));
        icon = itemIcon;
    }

    /** Two items are equal if they have the same name and icon.
     * @param other other item to compare.
     * @return whether the other Item is equal to this one. */
    public boolean equals(final Object other) {
        if (!(other instanceof Item)) {
            return false;
        }
        Item item = (Item) other;
        return name.equals(item.getName()) && icon == item.getImageResource();
    }

    /**@return the item as a string representing name and amount. */
    @NotNull
    @Override
    public String toString() {
        if (quantity == 1) {
            return name;
        }
        if (pluralName.containsKey(name)) {
            return "" + quantity + " " + pluralName.get(name);
        }
        return "" + quantity + " " + name + "s";
    }

    /**@return the hashCode based on the super implementation.
     * Checkstyle wants me to override this if I override equals(other). */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**@return the item name. */
    public String getName() {
        return name;
    }

    /**@return the location description. */
    public String getLocationDescription() {
        return locationDescription;
    }

    /**@return the description. */
    public String getDescription() {
        return description;
    }

    /**@return the quantity. */
    int getQuantity() {
        return quantity;
    }

    /**@return the id of the R.drawable file with this icon. */
    public int getImageResource() {
        return icon;
    }

    /**Increase the quantity by one. */
    void increment() {
        quantity++;
    }

    /**Decrease the quantity by one. */
    void decrement() {
        quantity--;
    }

    /**@param amt amount to add to quantity. */
    public void add(final int amt) {
        quantity += amt;
    }

    /**Set the color of the icon.
     * @param setColor new color for icon. */
    public void setColor(final String setColor) {
        //change icon color manually.
    }

    /**@return the value of the Item. */
    public int getValue() {
        return value;
    }
}
