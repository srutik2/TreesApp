package com.example.finalproject;

import java.util.List;

public class Target {

    private String name;

    private String locationDescription;

    private String description;

    public Target(final List allInfo) {
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

    public String getLocationDescription() {
        return locationDescription;
    }

    public String getDescription() {
        return description;
    }

    void setName(final String newName) {
        name = newName;
    }
}
