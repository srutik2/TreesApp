package com.example.finalproject.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.finalproject.InventoryManager;
import com.example.finalproject.Item;
import com.example.finalproject.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private InventoryManager inventoryManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button add = root.findViewById(R.id.addAThing);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item tree = new Item("tree", 1, R.drawable.ic_pine_tree);
                List<Item> inv = inventoryManager.getInventory();
                if (inv.contains(tree)) {
                    tree = inv.get(inv.indexOf(tree));
                    tree.increment();
                } else {
                    inventoryManager.addToInventory(tree);
                }
            }
        });

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            inventoryManager = (InventoryManager) context;
        } catch (ClassCastException castException) {
            System.out.println("The activity does not implement the listener.");
            castException.printStackTrace();
        }
    }
}