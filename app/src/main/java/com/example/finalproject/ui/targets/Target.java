package com.example.finalproject.ui.targets;

class Target {

    private String name;

    private String locationDescription;

    private String description;

    public Target(final String setName, final String setLocationDescription, final String setDescription) {
        name = setName;
        locationDescription = setLocationDescription;
        description = setDescription;
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
