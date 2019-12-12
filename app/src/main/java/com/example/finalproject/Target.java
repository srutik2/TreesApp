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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Target {

    /**Name of the target. */
    private String name;
    /**More info about the Target, eg. Scientific Name (shown on marker). */
    private String sciName;
    /**Description of the location: used when displaying info in info page. */
    private String locationDescription;
    /**Description of the target. Used displayed on info page. */
    private String description;
    /**Description of the target. Used displayed on info page. */
    private final int descWordsPerLine = 8;

    /**The R.drawable id which represents the icon to represent the Target. */
    private int icon = R.drawable.ic_pine_tree;
    /**LatLng object that represents the Target's location. */
    private LatLng location;
    /**An int representing the color of the Target. Darkish green by default. */
    private int color = Color.parseColor("#000000");

    /**The marker object that represents this target object.*/
    private Marker targetMarker;
    /**Whether or not to use Extended Snippets.*/
    private boolean extendedSnippets = false;

    /**Item given as a reward by the target. */
    private Item reward;


    /** adds info about the Target.
     * Format: name, locDesc, desc, snippet, location, reward, icon
     * For now, I will assume color will be the same.
     * The reward will be a string, item object will have to be found from that.
     * @param info a string[] info to store.
     * @param everyItem a list of all items, in which to find reward.*/
    Target(final List<String> info, final List<Item> everyItem, final int setIcon) {
        name = info.get(0);
        locationDescription = info.get(1);
        description = info.get(2);
        int num = 2 + 1; //getting around checkstyle, using num for indices 3, 4, and 5.
        sciName = info.get(num);
        location = getLatLng(info.get(++num));
        try {
            reward = findItemByName(info.get(++num), everyItem);
            icon = setIcon;
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
            if (i.getName().equalsIgnoreCase(itemName)) {
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

    /**creates a marker on the given map using what fields exist.
     * reulting marker is stored for future use in a global variable.
     * @param map map on which to draw marker.
     * @param context passed directly to vectorToBitmap() to get resources.
     */
    public void setMarker(final GoogleMap map, final Context context) {
        MarkerOptions options = new MarkerOptions();
        options.position(location).title(name);
        if (extendedSnippets) {
            options.snippet(formatDescription());
        } else {
            options.snippet(sciName);
        }
        if (icon != 0) {
            options.icon(vectorToBitmap(icon, context));
        }
        targetMarker = map.addMarker(options);
    }

    /**Set up listeners for the marker, for info window. */
    private void setUpListeners(final GoogleMap map) {
        //has not been called yet, need to figure out how and where.
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

    /**Toggles snippet between description and sciName. */
    void toggleExtendedSnippet() {
        extendedSnippets = !extendedSnippets;
        if (targetMarker != null) {
            if (targetMarker.getSnippet().equals(sciName)) {
                targetMarker.setSnippet(formatDescription());
            } else {
                targetMarker.setSnippet(sciName);
            }
        }
    }

    /**@return whether or not snippets are extended. */
    boolean hasExtendedSnippets() {
        return extendedSnippets;
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

    /**@return the description formatted to fit a snippet. */
    public String formatDescription() {
        String[] wds = description.split(" ");
        LinkedList<String> words = new LinkedList<>(Arrays.asList(wds));
        StringBuilder output = new StringBuilder();
        StringBuilder line;
        for (int i = 0; i < (words.size() / descWordsPerLine) + 1; i++) {
            line = new StringBuilder();
            for (int j = 0; j < descWordsPerLine - 1 || !words.isEmpty(); j++) {
                line.append(words.pop()).append(" ");
            }
            output.append(line.append("\n"));
        }
        return output.toString();
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
    public Item reward() {
        return reward;
    }
}
