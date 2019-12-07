package com.example.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class Target {

    /**Name of the target. */
    private String name;
    /**More info about the Target, eg. Scientific Name (shown on marker). */
    private String snippet;
    /**Description of the location: used when displaying info in info page. */
    private String locationDescription;
    /**Description of the target. Used displayed on info page. */
    private String description;

    /**The R.drawable id which represents the icon to represent the Target. */
    private int icon;
    /**LatLng object that represents the Target's location. */
    private LatLng location;
    /**An int representing the color of the Target. Darkish green by default. */
    private int color = Color.parseColor("#000000");

    /**The marker object that represents this target object.*/
    private Marker targetMarker;

    /**Item given as a reward by the target. */
    private Item reward;
    /**An Inventory Manager implemented by MainActivity.*/
    private InventoryManager invMan;

    /** adds info about the Target.
     * Format: name, locDesc, desc, snippet, location, reward
     * For now, I will assume the icon and color will be the same.
     * The reward will be a string, item object will have to be found from that.
     * @param info a string[] info to store.
     * @param everyItem a list of all items, in which to find reward.*/
    public Target(final List<String> info, final List<Item> everyItem) {
        name = info.get(0);
        locationDescription = info.get(1);
        description = info.get(2);
        int num = 2 + 1; //getting around checkstyle, using num for indices 3, 4, and 5.
        snippet = info.get(num);
        location = getLatLng(info.get(++num));
        try {
            reward = findItemByName(info.get(++num), everyItem);
        } catch (IllegalArgumentException ex) {
            System.out.println(info.get(num) + " was not found");
            System.out.println(everyItem);
            ex.printStackTrace();
        }
    }

    /**@param itemName LatLng coordinate as a String "lat,lng".
     * @param items List<> of all items to search.
     * @return the item whose name is passed in. */
    private Item findItemByName(final String itemName, final List<Item> items) {
        for (Item i : items) {
            if (i.getName().equals(itemName)) {
                return i;
            }
        }
        System.out.println("Reward Item does not exist.");
        throw new IllegalArgumentException();
    }

    /**@param coords LatLng coordinate as a String "lat,lng"
     * @return a latlng object that is the same as the string. */
    private LatLng getLatLng(final String coords) {
        String[] each = coords.split(",");
        each[1] = each[1].trim(); //just in case
        return new LatLng(Double.parseDouble(each[0]), Double.parseDouble(each[1]));
    }

    /**Creates a target with lots of info.
     * @param setLocation location on the map.
     * @param setName name to be displayed
     * @param addSnippet snippet displayed under name.
     * @param setIcon R.drawable id to represent Target on map.
     * @param setColor color of target on map. */
    public Target(final LatLng setLocation, final String setName, final String addSnippet, final int setIcon, final String setColor) {
        location = setLocation;
        name = setName;
        snippet = addSnippet;
        icon = setIcon;
        color = Color.parseColor(setColor);
    }

    /**Constructor that uses default color.
     * @param setLocation location on map.
     * @param setName display name.
     * @param addSnippet under name.
     * @param setIcon R.drawable id. */
    public Target(final LatLng setLocation, final String setName, final String addSnippet, final int setIcon) {
        location = setLocation;
        name = setName;
        snippet = addSnippet;
        icon = setIcon;
    }

    /** Target with default icon.
     * @param setLocation location on map.
     * @param setName display name.
     * @param addSnippet description. */
    public Target(final LatLng setLocation, final String setName, final String addSnippet) {
        location = setLocation;
        name = setName;
        snippet = addSnippet;
    }

    /** basic constructor for map.
     * @param setLocation map location.
     * @param setName display name.
     */
    public Target(final LatLng setLocation, final String setName) {
        location = setLocation;
        name = setName;
    }

    /**creates a marker on the given map using what fields exist.
     * reulting marker is stored for future use in a global variable.
     * @param map map on which to draw marker.
     * @param context passed directly to vectorToBitmap() to get resources.
     */
    public void setMarker(final GoogleMap map, final Context context) {
        MarkerOptions options = new MarkerOptions();
        options.position(location).title(name);
        if (snippet != null && !(snippet.equals(""))) {
            options.snippet(snippet);
        }
        if (icon != 0) {
            options.icon(vectorToBitmap(icon, context));
        }
        targetMarker = map.addMarker(options);
    }

    /** workaround function that converts a vector image to BitMapDescriptor.
     * @param id resource Id where desired vector image is.
     * @param context activity context, in order to get resources.
     * @return a BitMapDescriptor which imitates the indicated vector image. */
    private BitmapDescriptor vectorToBitmap(@DrawableRes final int id, final Context context) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), id, null);
        Bitmap bitmap;
        if (vectorDrawable != null) {
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        } else {
            return BitmapDescriptorFactory.defaultMarker(color);
        }
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /**@return the marker which represents the target. */
    public Marker getMarker() {
        return targetMarker;
    }

    /**@return the target's name. */
    public String getName() {
        return name;
    }

    /**@return the locaton description - for info page. */
    public String getLocationDescription() {
        return locationDescription;
    }

    /**@return the description of the target - for info page. */
    public String getDescription() {
        return description;
    }

    /**@param newName set a new name for the target. */
    void setName(final String newName) {
        name = newName;
    }

    /**@param item the item which will be set as the reward for this target. */
    public void setReward(final Item item) {
        reward = item;
    }

    /**Put the reward associated with this target in the player's inventory. */
    public void reward() {
        System.out.println(reward.getName());
        invMan.addToInventory(reward);
    }
}
