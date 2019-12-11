package com.example.finalproject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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

    /**Allows sale of item. */
    private InventoryManager invMan;

    InventoryAdapter(@NonNull final Context setContext, final int resource, final ArrayList<Item> objects, final InventoryManager im) {
        super(setContext, resource, objects);
        invMan = im;
        context = setContext;
        items = objects;
    }

    /**@return number of items. */
    @Override
    public int getCount() {
        return super.getCount();
    }

    /**Sets up the spinner when closed with an item selected.
     * @param position position in the spinner that was selected.
     * @param convertView view of selected thing maybe.
     * @param parent parent viewGroup.
     * @return the view to be displayed. */
    @NotNull
    @Override
    public View getView(final int position, final View convertView, @NotNull final ViewGroup parent) {
        //LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = View.inflate(context, R.layout.inventory_entry, null);
        TextView itemName = view.findViewById(R.id.itemName);
        ImageView icon = view.findViewById(R.id.itemIcon);
        items.get(position).setColor("#000000");
        Item current = items.get(position);
        itemName.setText(current.toString());
        icon.setImageResource(current.getImageResource());
        return view;
    }

    /**Creates a view for each member of the dropdown list.
     * @param position position in dropdown being created.
     * @param convertView view to be converted if it exists.
     * @param parent parent ViewGroup.
     * @return a view to populate the list. */
    @Override
    public View getDropDownView(final int position, final View convertView, @NotNull final ViewGroup parent) {
        View view = View.inflate(context, R.layout.inventory_dropdown_entry, null);
        LinearLayout background = view.findViewById(R.id.background);
        TextView itemName = view.findViewById(R.id.itemName);
        ImageView icon = view.findViewById(R.id.itemIcon);
        ImageButton sell = view.findViewById(R.id.sellItem);

        if (position == 0) {
            background.setBackground(context.getResources().getDrawable(R.drawable.custom_border));
        }
        final Item current = items.get(position);
        if (position > 1) {
            sell.setVisibility(View.VISIBLE);
            sell.setOnClickListener(new View.OnClickListener() {
                /**sells an item if you click the sell button.
                 * @param view the parent view of the button.*/
                @Override
                public void onClick(final View view) {
                    //eventually, should I make a pop-up confirmation box? might be annoying for the user.
                    invMan.sell(current);
                }
            });
        }
        itemName.setText(current.toString());
        icon.setImageResource(current.getImageResource());
        return view;
    }
}
