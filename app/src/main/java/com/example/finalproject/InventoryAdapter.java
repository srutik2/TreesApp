package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.finalproject.ui.info.Item;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class InventoryAdapter extends ArrayAdapter {

    private Context context;

    private ArrayList<Item> items;

    InventoryAdapter(@NonNull Context setContext, int resource, ArrayList<Item> objects) {
        super(setContext, resource, objects);
        context = setContext;
        items = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NotNull
    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        //LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = View.inflate(context, R.layout.inventory_entry, null);
        TextView itemName = view.findViewById(R.id.itemName);
        ImageView icon = view.findViewById(R.id.itemIcon);
        Item current = items.get(position);
        if (current.getQuantity() > 1) {
            itemName.setText((current.getQuantity() + " " + current.getName() + "s"));
        } else {
            itemName.setText(current.getName());
        }
        icon.setImageResource(current.getImageResource());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent) {
        View view = View.inflate(context, R.layout.inventory_entry, null);
        LinearLayout background = view.findViewById(R.id.background);
        TextView itemName = view.findViewById(R.id.itemName);
        ImageView icon = view.findViewById(R.id.itemIcon);
            if (position != 0) background.setBackgroundColor(context.getResources().getColor(R.color.primaryBackground));
        Item current = items.get(position);
        if (current.getQuantity() > 1) {
            itemName.setText((current.getQuantity() + " " + current.getName() + "s"));
        } else {
            itemName.setText(current.getName());
        }
        icon.setImageResource(current.getImageResource());
        return view;
    }
}
