package com.example.finalproject.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finalproject.InventoryManager;
import com.example.finalproject.Item;
import com.example.finalproject.R;
import com.example.finalproject.Target;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InfoFragment extends Fragment {

    /**List of Targets to show info about. */
    private List<Target> targets = new ArrayList<>();

    /**List of Items to show info about. */
    private List<Item> items = new ArrayList<>();

    /**An Inventory Manager implemented by MainActivity.*/
    private InventoryManager invMan;

    /**Called when fragment is called. Sets up spinners and the slider.
     * @param inflater inflater used to inflate root from parrent.
     * @param container parent of root view.
     * @param save still don't know how to use this at all.
     * @return root view. */
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, final Bundle save) {
        super.onCreateView(inflater, container, save);

        final View root = inflater.inflate(R.layout.fragment_info, container, false);

        //target spinner has options to select a target from list to show info.
        Spinner targetSelector = root.findViewById(R.id.targetOptions);
        targetSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**Calls a helper function when target spinner changes selection.
             * @param adapterView the adapterView that set up the spinner.
             * @param view the view which holds the spinner.
             * @param i the position of the spinner.
             * @param l Id of the selected position I believe. */
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {
                targetSelected(i, root);
            }

            /** unused as of right now.
             * @param adapterView adapterView that fills spinner. */
            @Override
            public void onNothingSelected(final AdapterView<?> adapterView) { }
        });

        //gives options for user to select a item from the list to show info.
        Spinner itemSelector = root.findViewById(R.id.itemOptions);
        itemSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**Calls a helper function when item spinner changes selection.
             * @param adapterView the adapterView that set up the spinner.
             * @param view the view which holds the spinner.
             * @param i the position of the spinner.
             * @param l Id of the selected position I believe. */
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {
                itemSelected(i, root);
            }
            /** unused as of right now.
             * @param adapterView adapterView that fills spinner. */
            @Override
            public void onNothingSelected(final AdapterView<?> adapterView) { }
        });

        //gets the targets from targetData.txt, and sets up an ArrayAdapter to fill the target spinner.
        targets = invMan.getAllTargets();
        ArrayAdapter<String> targetListFiller = new ArrayAdapter<>(Objects.requireNonNull(this.getContext()),
                android.R.layout.simple_spinner_item, getTargetNames());
        targetListFiller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        System.out.println();
        targetSelector.setAdapter(targetListFiller);

        //gets items from itemInfo.txt, sets up ArrayAdapter to fill spinner.
        items = invMan.getAllItems();
        ArrayAdapter<String> itemListFiller = new ArrayAdapter<>(Objects.requireNonNull(this.getContext()),
                android.R.layout.simple_spinner_item, getItemNames());
        itemListFiller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemSelector.setAdapter(itemListFiller);

        //creates a seek bar to go between layouts on the info page.
        SeekBar pageSelector = root.findViewById(R.id.modeSelector);
        pageSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**Selects what info is shown when slider changes position.
             * @param seekBar the seekBar whose position changed.
             * @param i the current position of the seekBar.
             * @param fromUser called: true = manually, false = programmatically. */
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int i, final boolean fromUser) {
                selectPage(root, i);
            }

            /** does nothing (for now at least).
             * @param seekBar seekBar that changes between targets and items.
             */
            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) { }

            /**Changes a textView when user moves slider.
             *  @param seekBar the seekBar obeject which changes pages. */
            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                TextView info = root.findViewById(R.id.sliderInfo);
                switch (seekBar.getProgress()) {
                    case 0: info.setText(R.string.slider_info_default);
                        break;
                    case 1: info.setText(R.string.slider_info_targets);
                        break;
                    case 2: info.setText(R.string.slider_info_items);
                        break;
                    default:
                        info.setText(R.string.slider_info_error);
                }
            }
        });

        return root;
    }

    /** Pretty sure this is called when this fragment is attached to fragment view in MainActivity.
     * This function's purpose is to create the inventory manager based on MainActivity's implementation.
     * @param context main context */
    @Override
    public void onAttach(@NotNull final Context context) {
        super.onAttach(context);
        try {
            invMan = (InventoryManager) context;
        } catch (ClassCastException castException) {
            System.out.println("The activity does not implement the listener.");
            castException.printStackTrace();
        }
    }

    /** updates info displayed when a target is selected by target spinner.
     * @param position of slider, and target index.
     * @param root root view on which to find the views which hold info. */
    private void targetSelected(final int position, final View root) {
        LinearLayout infoPanel = root.findViewById(R.id.targetInfoContainer);
        TextView location = root.findViewById(R.id.targetInfoLocationDescription);
        TextView description = root.findViewById(R.id.targetInfoDescription);
        TextView name = root.findViewById(R.id.targetInfoName);

        name.setText(targets.get(position).getName());
        location.setText(targets.get(position).getLocationDescription());
        description.setText(targets.get(position).getDescription());
        infoPanel.setVisibility(View.VISIBLE);
    }

    /** updates info displayed when an item is selected by item spinner.
     * @param position of slider, and item index.
     * @param root root view on which to find the views which hold info. */
    private void itemSelected(final int position, final View root) {
        LinearLayout infoPanel = root.findViewById(R.id.itemInfoContainer);
        TextView location = root.findViewById(R.id.itemInfoLocationDescription);
        TextView description = root.findViewById(R.id.itemInfoDescription);
        TextView name = root.findViewById(R.id.itemInfoName);
        ImageView itemIcon = root.findViewById(R.id.itemIcon);

        name.setText(items.get(position).getName());
        location.setText(items.get(position).getLocationDescription());
        description.setText(items.get(position).getDescription());
        itemIcon.setImageResource(items.get(position).getImageResource());
        infoPanel.setVisibility(View.VISIBLE);
    }

    /**When slider changes, makes certain views visible to show info.
     * @param root root view on which to find all views.
     * @param page the selected page. */
    private void selectPage(final View root, final int page) {
        //get all views
        TextView welcome = root.findViewById(R.id.welcomeToTargetPage);
        Spinner targetSpinner = root.findViewById(R.id.targetOptions);
        LinearLayout targetInfo = root.findViewById(R.id.targetInfoContainer);
        Spinner itemSpinner = root.findViewById(R.id.itemOptions);
        LinearLayout itemInfo = root.findViewById(R.id.itemInfoContainer);

        //set everything to be gone
        welcome.setVisibility(View.GONE);
        targetSpinner.setVisibility(View.GONE);
        targetInfo.setVisibility(View.GONE);
        itemSpinner.setVisibility(View.GONE);
        itemInfo.setVisibility(View.GONE);

        //make just the applicable views visible.
        switch (page) {
            case 1: targetSpinner.setVisibility(View.VISIBLE);
                targetInfo.setVisibility(View.VISIBLE);
                break;
            case 2: itemSpinner.setVisibility(View.VISIBLE);
                itemInfo.setVisibility(View.VISIBLE);
                break;
            default: welcome.setVisibility(View.VISIBLE);
        }
    }

    /**Makes a String[] from the names of all targets.
     * @return a String[] of all target names. */
    private String[] getTargetNames() {
        String[] names = new String[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            names[i] = targets.get(i).getName();
        }
        return names;
    }

    /**Makes a String[] from the names of all items.
     * @return a String[] of all item names. */
    private String[] getItemNames() {
        String[] names = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            names[i] = items.get(i).getName();
        }
        return names;
    }
}
