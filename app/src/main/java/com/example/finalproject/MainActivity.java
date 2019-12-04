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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.google.android.gms.common.util.ArrayUtils;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ArrayAdapter<String> adapter;

    private Map<String, Integer> inventoryContents  = new HashMap<>();

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

        //This section displays a message when you switch tabs at the bottom. Mostly for testing
        //might be helpful in the future idk, can delete safely if not.
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.navigation_home) {
                    //Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_SHORT).show();
                } else if(destination.getId() == R.id.navigation_map) {
                    //Toast.makeText(getApplicationContext(), "map", Toast.LENGTH_SHORT).show();
                } else if(destination.getId() == R.id.navigation_info) {
                    //Toast.makeText(getApplicationContext(), "info", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Spinner inventory = findViewById(R.id.inventory);
        inventory.setOnItemSelectedListener(this);
        testInventory();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getFormattedInventory());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        updateInventory();
        inventory.setAdapter(adapter);
    }

    private void updateInventory() {
        adapter.clear();
        adapter.add("Inventory");
        for (String item : getFormattedInventory()) {
            adapter.add(item);
        }
    }

    private List<String> getFormattedInventory() {
        try {
            List<String> items = new ArrayList<>(inventoryContents.keySet());
            for (int i = 0; i < items.size(); i++) {
                int quantity = inventoryContents.get(items.get(i));
                if (quantity > 1) {
                    items.set(i, quantity + " " + items.get(i) + "s");
                } else items.remove(i);
            }
            System.out.println("Done Formatting: " + items.toString());
            return items;
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error in getting inventory", Toast.LENGTH_SHORT).show();
        }
        return new ArrayList<String>(0);
    }

    private String[] remove(String[] array, int index) {
        String[] smaller = new String[array.length - 1];
        List<String> list = new ArrayList<>(Arrays.asList(array));
        list.remove(index);
        list.toArray(smaller);
        return smaller;
    }

    private void testInventory() {
        System.out.println("Putting test items in inventory.");
        inventoryContents.put("stick", 6);
        inventoryContents.put("stone", 18);
        inventoryContents.put("broken bone", 0);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        adapterView.setSelection(0);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }
}
