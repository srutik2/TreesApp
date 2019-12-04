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

import java.util.ArrayList;

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

        final Spinner inventory = findViewById(R.id.inventory);
        inventory.setOnItemSelectedListener(this);
        setUpInventory();

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                final int infoPageID = 2131230886;
                if (destination.getId() == infoPageID) {
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

    private void addToInventory(Item item) {

    }

    private void setUpInventory() {
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
