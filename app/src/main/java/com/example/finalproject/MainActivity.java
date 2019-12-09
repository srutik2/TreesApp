package com.example.finalproject;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements InventoryManager {

    /** ArrayList to store all items. */
    private ArrayList<Item> allItems = new ArrayList<>();

    /** ArrayList to store all Targets. */
    private ArrayList<Target> allTargets = new ArrayList<>();
    /** ArrayList to store items in inventory. */
    private ArrayList<Item> invContents = new ArrayList<>();

    /** ArrayList to store items in inventory. */
    private InventoryAdapter adapter;

    /**Media Player for background music. */
    private MediaPlayer player;
    /**volume for background music (L and R same). */
    private final int VOL = 20;

    /**Sets up the navigation fragment and inventory.
     * @param savedInstanceState I want to know what this does. */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_map, R.id.navigation_info).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //inventory spinner set up with InventoryAdapter and custom layout.
        final Spinner inventory = findViewById(R.id.inventory);
        inventory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**When anything is selected in inventory, keep selection at 0.
             * @param adapterView adapter that controls spinner.
             * @param view view that contains spinner.
             * @param i position of selected item.
             * @param l id of selected object. */
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {
                adapterView.setSelection(0);
            }

            /**Do nothing when nothing is selected.
             * @param adapterView spinner adapter that did nothing. */
            @Override
            public void onNothingSelected(final AdapterView<?> adapterView) { }
        });
        setUpList("targetData.txt");
        setUpInventory();

        if (player != null) {
            System.out.println("player still exists!");
        }
        Switch playMusic = findViewById(R.id.musicPlayer);
        playMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                if (b) {
                    startPlayer();
                } else {
                    stopPlayer();
                }
            }
        });
        //playMusic.setChecked(true);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull final NavController controller, @NonNull final NavDestination destination, @Nullable final Bundle arguments) {
                assert destination.getLabel() != null;
                if (destination.getLabel().equals(getResources().getString(R.string.title_info))) {
                    inventory.setVisibility(View.GONE);
                } else {
                    inventory.setVisibility(View.VISIBLE);
                }
            }
        });

        adapter = new InventoryAdapter(this, R.layout.inventory_entry, invContents, this);
        adapter.setDropDownViewResource(R.layout.inventory_entry);

        inventory.setAdapter(adapter);
    }

    /**Create a new MediaPlayer with background music. */
    private void startPlayer() {
        if (player == null) {
            player = MediaPlayer.create(getApplicationContext(), R.raw.background);
        }
        player.start();
        player.setVolume(VOL, VOL);
        player.setLooping(true);
    }

    /**Release the player and set it to null. */
    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    /**When the player stops, end everything. */
    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    /**Make sure you stop the player when the app closes. */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlayer();
    }

    /**Takes a filename and sets up the list whose values are stored inside.
     * @param source name of the file from which to draw the list of things. */
    private void setUpList(final String source) {
        InputStream in;
        BufferedReader br;
        try {
            //creates buffered reader to read each line of the source file.
            in = this.getAssets().open(source);
            br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            //splits each line into parts, fills corresponding list.
            String thisLine;
            while ((thisLine = br.readLine()) != null) {
                List<String> splitLine = new ArrayList<>();
                Collections.addAll(splitLine, thisLine.split("\\+_\\+"));
                if (source.equals("targetData.txt")) {
                    allTargets.add(new Target(splitLine, getAllItems()));
                }
                if (source.equals("itemInfo.txt")) {
                    int icon = findIconByName(splitLine.remove(splitLine.size() - 1));
                    allItems.add(new Item(splitLine, icon));
                }
            }
        } catch (IOException e) {
            //something went wrong, likely in getting the file.
            e.printStackTrace();
        }
    }

    /**Find the R.drawable id of an icon by name.
     * returns alert icon by default.
     * @param name the name of the icon to find.
     * @return the R.drawable id of the chosen icon. */
    public int findIconByName(final String name) {
        String iconName = "ic_" + name;
        int resId = getResources().getIdentifier(iconName, "drawable", getPackageName());
        if (resId == 0) {
            System.out.println("alert! icon name not found!");
            return R.drawable.ic_alert;
        }
        System.out.println("Icon id: " + resId);
        return resId;
    }

    /**Gets a list of all of the items for use elsewhere.
     * @return the current inventory as a List<Item> */
    @Override
    public List<Item> getAllItems() {
        if (allItems.isEmpty()) {
            setUpList("itemInfo.txt");
        }
        return allItems;
    }

    /**Gets a list of all of the targets for use elsewhere.
     * @return the current inventory as a List<Target> */
    @Override
    public List<Target> getAllTargets() {
        if (allTargets.isEmpty()) {
            setUpList("targetData.txt");
        }
        return allTargets;
    }

    /**Adds an item to the inventory. implements InventoryManager.
     * @param item item to be added to the inventory. */
    @Override
    public void addToInventory(final Item item) {
        if (invContents.contains(item)) {
            item.increment();
            System.out.println("increment " + item.getName());
        } else {
            item.increment();
            System.out.println("add new " + item.getName());
            invContents.add(item);
        }
    }

    @Override
    public void sell(final Item item) {
        System.out.println("sell: " + item.getName());
        //add the value of the item sold to the amount of money (inv at 1).
        invContents.get(1).add(item.getValue());
        //remove 1 item from inventory. delete it if there are 0.
        item.decrement();
        if (item.getQuantity() <= 0) {
            invContents.remove(item);
        }
        adapter.notifyDataSetChanged();
    }

    /** finds an item from the inventory and returns it.
     * @param itemName item to be found.
     * @return the item with the given name. */
    @Override
    public Item getItem(final String itemName) throws IllegalArgumentException {
        for (Item i : allItems) {
            if (i.getName().equalsIgnoreCase(itemName)) {
                return i;
            }
        }
        throw new IllegalArgumentException();
    }

    /**@return a List of items in the inventory. */
    @Override
    public List<Item> getInventory() {
        return invContents;
    }

    /**Fills the inventory with example items.
     *Eventually just the first line will be kept. */
    private void setUpInventory() {
        invContents.add(new Item(new ArrayList<>(Arrays.asList("Inventory", " ", " ", "1")), R.drawable.ic_inventory));
        invContents.add(new Item(new ArrayList<>(Arrays.asList("Cash Money", " ", " ", "0")), R.drawable.ic_coin));
        for (Item i : allItems) {
            if (i.getQuantity() != 0) {
                invContents.add(i);
            }
        }
    }
}
