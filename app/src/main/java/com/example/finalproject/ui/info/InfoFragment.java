package com.example.finalproject.ui.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finalproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class InfoFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private List<Target> targets = new ArrayList<>();

    private List<Item> items = new ArrayList<>();

    private int selectedOption = 0;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        root = inflater.inflate(R.layout.fragment_info, container, false);

        Spinner targetSelector = root.findViewById(R.id.targetOptions);
        targetSelector.setOnItemSelectedListener(this);
        //I/System.out: androidx.appcompat.widget.AppCompatTextView{d7e1669 V.ED..... ........ 0,4-882,80 #1020014 android:id/text1}

        Spinner itemSelector = root.findViewById(R.id.itemOptions);
        itemSelector.setOnItemSelectedListener(this);

        setUpTargets();
        ArrayAdapter<String> targetListFiller = new ArrayAdapter<>(Objects.requireNonNull(this.getContext()),
                android.R.layout.simple_spinner_item, getTargetNames());
        targetListFiller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        targetSelector.setAdapter(targetListFiller);

        setUpItemList();
        ArrayAdapter<String> itemListFiller = new ArrayAdapter<>(Objects.requireNonNull(this.getContext()),
                android.R.layout.simple_spinner_item, getItemNames());
        itemListFiller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemSelector.setAdapter(itemListFiller);
        return root;
    }


    private void setUpTargets() {
        InputStream in = null;
        BufferedReader br = null;
        try {
            in = Objects.requireNonNull(getActivity()).getAssets().open("targetData.txt");
            br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String thisLine;
            while((thisLine = br.readLine()) != null) {
                List<String> splitLine = Arrays.asList(thisLine.split("\\+_\\+"));
                //String[] splitLine = thisLine.split(",");
                targets.add(new Target(splitLine));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpItemList() {
        items.add(new Item("one"));
        items.add(new Item("two"));
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//        if (!firstTargetSelected) {
//            targetNames.remove(0);
//            firstTargetSelected = true;
//        }
        System.out.println(arg1.toString());
        selectedOption = position;
        LinearLayout infoPanel = root.findViewById(R.id.targetInfoContainer);
        TextView location = root.findViewById(R.id.targetInfoLocationDescription);
        TextView description = root.findViewById(R.id.targetInfoDescription);
        TextView name = root.findViewById(R.id.targetInfoName);
        name.setText(targets.get(position).getName());
        location.setText(targets.get(position).getLocationDescription());
        description.setText(targets.get(position).getDescription());
        infoPanel.setVisibility(View.VISIBLE);
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

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Toast.makeText(this.getContext(), "Nothing Selected", Toast.LENGTH_LONG).show();
        // TODO Auto-generated method stub
        //honestly don't know what this method does.
    }
}