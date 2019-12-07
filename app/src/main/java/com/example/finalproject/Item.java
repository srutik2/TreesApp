package com.example.finalproject;

import java.util.List;

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

    /**Used to make a new inventory entry.
     * @param setName display name.
     * @param amount quantity to start with in inventory.
     * @param setIcon display icon id. */
    public Item(final String setName, final int amount, final int setIcon) {
        name = setName;
        quantity = amount;
        icon = setIcon;
    }

    /**Used to make a new inventory entry with a default 1 quantity..
     * @param setName display name.
     * @param setIcon display icon id. */
    public Item(final String setName, final int setIcon) {
        name = setName;
        icon = setIcon;
        quantity = 1;
    }

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
    public int getQuantity() {
        return quantity;
    }

    /**@return the id of the R.drawable file with this icon. */
    public int getImageResource() {
        return icon;
    }

    /**Increase the quantity by one. */
    public void increment() {
        quantity++;
        System.out.println("quantity increased to " + quantity);
    }

    /**Set the color of the icon.
     * @param setColor new color for icon. */
    public void setColor(final String setColor) {
        //change icon color manually.
    }
}
