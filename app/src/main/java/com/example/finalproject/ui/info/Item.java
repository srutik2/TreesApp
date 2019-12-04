package com.example.finalproject.ui.info;

import android.graphics.drawable.Drawable;

import java.util.List;

public class Item {

    private String name;

    private int quantity;

    private int icon;

    private String locationDescription;

    private String description;

    public Item(final String setName, final String setLocationDescription, final String setDescription, final int setQuantity) {
        name = setName;
        locationDescription = setLocationDescription;
        description = setDescription;
        quantity = setQuantity;
    }

    public Item(final String setName, final int setQuantity, final int setIcon) {
        name = setName;
        quantity = setQuantity;
        icon = setIcon;
        locationDescription = "";
        description = "";
    }

    public Item(final String setName, final int setQuantity) {
        name = setName;
        quantity = setQuantity;
        locationDescription = "";
        description = "";
    }

    public Item(final List allInfo) {
        if (allInfo == null) {
            return;
        }
        if (allInfo.get(0) != null) name = allInfo.get(0).toString();
        if (allInfo.get(1) != null) locationDescription = allInfo.get(1).toString();
        if (allInfo.get(2) != null) description = allInfo.get(2).toString();
    }

    public String getName() {
        return name;
    }

    String getLocationDescription() {
        return locationDescription;
    }

    String getDescription() {
        return description;
    }

    void setName(final String newName) {
        name = newName;
    }

    public void setLocationDescription(final String newLocationDescription) {
        locationDescription = newLocationDescription;
    }

    public void setDescription(final String newDescription) {
        description = newDescription;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getImageResource() {
        return icon;
    }
}
