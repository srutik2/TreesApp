package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

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
import java.util.List;

public class MainActivity extends AppCompatActivity implements InventoryManager {

    /** ArrayList to store all items. */
    private ArrayList<Item> allItems = new ArrayList<>();

    /** ArrayList to store all Targets. */
    private ArrayList<Target> allTargets = new ArrayList<>();

    /** ArrayList to store items in inventory. */
    private ArrayList<Item> invContents = new ArrayList<>();

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
        setUpInventory();

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

        InventoryAdapter adapter = new InventoryAdapter(this, R.layout.inventory_entry, invContents);
        adapter.setDropDownViewResource(R.layout.inventory_entry);

        inventory.setAdapter(adapter);
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
                List<String> splitLine = Arrays.asList(thisLine.split("\\+_\\+"));
                if (source.equals("targetData.txt")) {
                    allTargets.add(new Target(splitLine));
                }
                if (source.equals("itemInfo.txt")) {
                    allItems.add(new Item(splitLine));
                }
            }
        } catch (IOException e) {
            //something went wrong, likely in getting the file.
            e.printStackTrace();
        }
    }

    /**Gets a list of all of the items for use elsewhere.
     * @return the current inventory as a List<Item> */
    @Override
    public List<Item> getAllItems() {
        setUpList("itemInfo.txt");
        return allItems;
    }

    /**Gets a list of all of the targets for use elsewhere.
     * @return the current inventory as a List<Target> */
    @Override
    public List<Target> getAllTargets() {
        setUpList("targetData.txt");
        return allTargets;
    }

    /**Adds an item to the inventory. implements InventoryManager.
     * @param item item to be added to the inventory. */
    @Override
    public void addToInventory(final Item item) {
        if (invContents.contains(item)) {
            System.out.println("increment " + item.getName());
            item.increment();
        } else {
            System.out.println("add new " + item.getName());
            invContents.add(item);
        }
    }

    /**@return a List of items in the inventory. */
    @Override
    public List<Item> getInventory() {
        return invContents;
    }

    /**Fills the inventory with example items.
     *Eventually just the first line will be kept. */
    private void setUpInventory() {
        invContents.add(new Item("Inventory", R.drawable.ic_inventory));
        invContents.add(new Item("Ice Cream", R.drawable.ic_ice_cream));
        invContents.add(new Item("Pineapple", 13, R.drawable.ic_pineapple));
        invContents.add(new Item("Book", 147, R.drawable.ic_info_black_24dp));
    }
}
