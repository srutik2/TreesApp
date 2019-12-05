package com.example.finalproject;

import java.util.List;

public interface InventoryManager {
    /** adds an item to the inventory in MainActivity.
     * @param item item to be added to the inventory. */
    void addToInventory(Item item);

    /** gets the inventory when it is needed in another class.
     * @return the current inventory as a List<Item> */
    List<Item> getInventory();
}
