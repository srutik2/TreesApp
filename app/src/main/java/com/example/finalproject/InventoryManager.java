package com.example.finalproject;

import java.util.List;

public interface InventoryManager {
    void addToInventory(Item item);
    List<Item> getInventory();
}
