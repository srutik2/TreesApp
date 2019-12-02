package com.example.finalproject.ui.info;

import java.util.List;

class Target {

    private String name;

    private String locationDescription;

    private String description;

    Target(final String setName, final String setLocationDescription, final String setDescription) {
        name = setName;
        locationDescription = setLocationDescription;
        description = setDescription;
    }

    Target(final List allInfo) {
        if (allInfo == null) {
            return;
        }
        if (allInfo.get(0) != null) name = allInfo.get(0).toString();
        if (allInfo.get(1) != null) locationDescription = allInfo.get(1).toString();
        if (allInfo.get(2) != null) description = allInfo.get(2).toString();
    }

    String getName() {
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

    void setLocationDescription(final String newLocationDescription) {
        locationDescription = newLocationDescription;
    }

    void setDescription(final String newDescription) {
        description = newDescription;
    }
}
