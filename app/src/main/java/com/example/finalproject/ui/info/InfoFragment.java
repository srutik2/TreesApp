package com.example.finalproject.ui.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finalproject.Item;
import com.example.finalproject.R;
import com.example.finalproject.Target;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class InfoFragment extends Fragment {

    private List<Target> targets = new ArrayList<>();
    private List<Item> items = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final View root = inflater.inflate(R.layout.fragment_info, container, false);

        Spinner targetSelector = root.findViewById(R.id.targetOptions);
        targetSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                targetSelected(i, root);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        Spinner itemSelector = root.findViewById(R.id.itemOptions);
        itemSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemSelected(i, root);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        setUpList("targetData.txt");
        ArrayAdapter<String> targetListFiller = new ArrayAdapter<>(Objects.requireNonNull(this.getContext()),
                android.R.layout.simple_spinner_item, getTargetNames());
        targetListFiller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        System.out.println();
        targetSelector.setAdapter(targetListFiller);

        setUpList("itemInfo.txt");
        ArrayAdapter<String> itemListFiller = new ArrayAdapter<>(Objects.requireNonNull(this.getContext()),
                android.R.layout.simple_spinner_item, getItemNames());
        itemListFiller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemSelector.setAdapter(itemListFiller);

        SeekBar pageSelector = root.findViewById(R.id.modeSelector);
        pageSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                selectPage(root, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                TextView info = root.findViewById(R.id.sliderInfo);
                switch (seekBar.getProgress()) {
                    case 0: info.setText(R.string.slider_info_default);
                        break;
                    case 1: info.setText(R.string.slider_info_targets);
                        break;
                    case 2: info.setText(R.string.slider_info_items);
                }
            }
        });

        return root;
    }

    private void setUpList(final String source) {
        InputStream in;
        BufferedReader br;
        try {
            in = Objects.requireNonNull(getActivity()).getAssets().open(source);
            br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String thisLine;
            while((thisLine = br.readLine()) != null) {
                List<String> splitLine = Arrays.asList(thisLine.split("\\+_\\+"));
                if (source.equals("targetData.txt")) targets.add(new Target(splitLine));
                if (source.equals("itemInfo.txt")) items.add(new Item(splitLine));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private void itemSelected(final int position, final View root) {
        LinearLayout infoPanel = root.findViewById(R.id.itemInfoContainer);
        TextView location = root.findViewById(R.id.itemInfoLocationDescription);
        TextView description = root.findViewById(R.id.itemInfoDescription);
        TextView name = root.findViewById(R.id.itemInfoName);

        name.setText(items.get(position).getName());
        location.setText(items.get(position).getLocationDescription());
        description.setText(items.get(position).getDescription());
        infoPanel.setVisibility(View.VISIBLE);
    }

    private void selectPage(final View root, final int page) {
        TextView welcome = root.findViewById(R.id.welcomeToTargetPage);
        Spinner targets = root.findViewById(R.id.targetOptions);
        LinearLayout targetInfo = root.findViewById(R.id.targetInfoContainer);
        Spinner items = root.findViewById(R.id.itemOptions);
        LinearLayout itemInfo = root.findViewById(R.id.itemInfoContainer);

        welcome.setVisibility(View.GONE);
        targets.setVisibility(View.GONE);
        targetInfo.setVisibility(View.GONE);
        items.setVisibility(View.GONE);
        itemInfo.setVisibility(View.GONE);
        switch (page) {
            case 0: welcome.setVisibility(View.VISIBLE);
                break;
            case 1: targets.setVisibility(View.VISIBLE);
                targetInfo.setVisibility(View.VISIBLE);
                break;
            case 2: items.setVisibility(View.VISIBLE);
                itemInfo.setVisibility(View.VISIBLE);
        }
    }

    private String[] getTargetNames() {
        String[] names = new String[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            names[i] = targets.get(i).getName();
        }
        return names;
    }

    private String[] getItemNames() {
        String[] names = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            names[i] = items.get(i).getName();
        }
        return names;
    }
}