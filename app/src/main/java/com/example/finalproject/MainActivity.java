package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.finalproject.ui.info.Item;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

//import com.google.android.gms.common.util.ArrayUtils;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ArrayList<Item> invContents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        Spinner inventory = findViewById(R.id.inventory);
        inventory.setOnItemSelectedListener(this);
        testInventory();

        InventoryAdapter adapter = new InventoryAdapter(this, R.layout.inventory_entry, invContents);
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getFormattedInventory());
        adapter.setDropDownViewResource(R.layout.inventory_entry);

//        updateInventory();
        inventory.setAdapter(adapter);
    }

//    private void updateInventory() {
//        adapter.clear();
//        adapter.add("Inventory");
//        for (String item : getFormattedInventory()) {
//            adapter.add(item);
//        }
//    }

//    private List<String> getFormattedInventory() {
//        try {
//            List<String> items = new ArrayList<>(inventoryContents.keySet());
//            for (int i = 0; i < items.size(); i++) {
//                int quantity = inventoryContents.get(items.get(i));
//                if (quantity > 1) {
//                    items.set(i, quantity + " " + items.get(i) + "s");
//                } else if (quantity != 1) items.remove(i);
//            }
//            System.out.println("Done Formatting: " + items.toString());
//            return items;
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error in getting inventory", Toast.LENGTH_SHORT).show();
//        }
//        return new ArrayList<>(0);
//    }

    private void testInventory() {
        invContents.add(new Item("Inventory", 1 ,R.drawable.ic_inventory));
        invContents.add(new Item("Ice Cream", 1 ,R.drawable.ic_ice_cream));
        invContents.add(new Item("Pineapple", 13 ,R.drawable.ic_pineapple));
        invContents.add(new Item("Book", 147 ,R.drawable.ic_info_black_24dp));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        adapterView.setSelection(0);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }
}
