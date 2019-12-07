package com.example.finalproject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class InventoryAdapter extends ArrayAdapter {

    /**Context where you set up the spinner. */
    private Context context;

    /**ArrayList that contains all items in the inventory. */
    private ArrayList<Item> items;

    InventoryAdapter(@NonNull final Context setContext, final int resource, final ArrayList<Item> objects) {
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
    public View getView(final int position, final View convertView, @NotNull final ViewGroup parent) {
        //LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = View.inflate(context, R.layout.inventory_entry, null);
        TextView itemName = view.findViewById(R.id.itemName);
        ImageView icon = view.findViewById(R.id.itemIcon);
        items.get(position).setColor("#000000");
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
    public View getDropDownView(final int position, final View convertView, @NotNull final ViewGroup parent) {
        View view = View.inflate(context, R.layout.inventory_entry, null);
        LinearLayout background = view.findViewById(R.id.background);
        TextView itemName = view.findViewById(R.id.itemName);
        ImageView icon = view.findViewById(R.id.itemIcon);
        if (position == 0) {
            background.setBackground(context.getResources().getDrawable(R.drawable.custom_border));
        }
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
