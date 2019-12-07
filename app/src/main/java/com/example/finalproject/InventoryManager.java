package com.example.finalproject;

import java.util.List;

public interface InventoryManager {

    /**Gets a list of all of the items for use elsewhere.
     * @return the current inventory as a List<Item> */
    List<Item> getAllItems();

    /**Gets a list of all of the targets for use elsewhere.
     * @return the current inventory as a List<Target> */
    List<Target> getAllTargets();

    /** adds an item to the inventory in MainActivity.
     * @param item item to be added to the inventory. */
    void addToInventory(Item item);

    /** gets the inventory when it is needed in another class.
     * @return the current inventory as a List<Item> */
    List<Item> getInventory();
}
