package com.example.finalproject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OptionsAdapter extends ArrayAdapter {

    /**Name of the options. */
    private List<String> options;

    /**Name of the options. */
    private OptionsManager opMan;

    /**Context of the Spinner. */
    private Context context;

    /**Ions for the different options. */
    private Map<String, Integer> icons = new HashMap<String, Integer>() { {
        put("Settings", R.drawable.ic_gear);
        put("Music on", R.drawable.ic_music_on);
        put("Music off", R.drawable.ic_music_off);
    }};

    /**Sets up an Options Adapter.
     * @param setContext context of spinner.
     * @param resource layout to show when closed.
     * @param objects List<String> of option names. */
    public OptionsAdapter(@NonNull final Context setContext, final int resource, @NonNull final List<String> objects, OptionsManager om) {
        super(setContext, resource, objects);
        opMan = om;
        options = objects;
        context = setContext;
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
        View view = View.inflate(context, R.layout.options_dropdown, null);
        Switch s = view.findViewById(R.id.optionSwitch);
        ImageView i = view.findViewById(R.id.optionIcon);
        s.setVisibility(View.GONE);
        i.setVisibility(View.GONE);
        return view;
    }

    /**Creates a view for each member of the dropdown list.
     * @param position position in dropdown being created.
     * @param convertView view to be converted if it exists.
     * @param parent parent ViewGroup.
     * @return a view to populate the list. */
    @Override
    public View getDropDownView(final int position, final View convertView, @NotNull final ViewGroup parent) {
        View view = View.inflate(context, R.layout.options_dropdown, null);
        final ImageView icon = view.findViewById(R.id.optionIcon);
        final Switch option = view.findViewById(R.id.optionSwitch);

        final String name = options.get(position);
        String nameWithSpace = name + " ";
        option.setText(nameWithSpace);
        option.setChecked(shouldCheck(name));
        if (option.isChecked()) {
            icon.setImageResource(icons.get(name + " on"));
        } else {
            icon.setImageResource(icons.get(name + " off"));
        }

        option.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton cb, final boolean b) {
                String text = "Turn " + name;
                if (b) {
                    icon.setImageResource(icons.get(name + " on"));
                    switchOn(name, cb);
                } else {
                    icon.setImageResource(icons.get(name + " off"));
                    switchOff(name, cb);
                }
            }
        });

        return view;
    }

    //When adding a new option, add switch functions in these methods.

    /**@param name name of the option to turn on. */
    private boolean shouldCheck(final String name) {
        if (name.equals(options.get(0))) {
            return opMan.musicPlaying();
        } else {
            return false;
        }
    }

    /**@param name name of the option to turn on.
     * @param cb button to switch in default case. */
    private void switchOn(final String name, final CompoundButton cb) {
        if (name.equals(options.get(0))) {
            opMan.startPlayer();
        } else {
            System.out.println("Could not find option");
            Toast.makeText(context, "Could not toggle option", Toast.LENGTH_SHORT).show();
            cb.setChecked(false);
        }
    }

    /**@param name name of the option to turn off.
     * @param cb switch to change in default case. */
    private void switchOff(final String name, final CompoundButton cb) {
        if (name.equals(options.get(0))) {
            opMan.stopPlayer();
        } else {
            System.out.println("Could not find option");
            Toast.makeText(context, "Could not toggle option", Toast.LENGTH_SHORT).show();
            cb.setChecked(true);
        }
    }
}
