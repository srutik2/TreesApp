package com.example.finalproject.ui.targets;

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

public class TargetsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private TargetsViewModel targetsViewModel;

    private List<List<String>> targets = new ArrayList<>();

    private List<String> targetNames = new ArrayList<>();

    private int selectedOption = 0;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        root = inflater.inflate(R.layout.fragment_targets, container, false);
        Spinner targetList = root.findViewById(R.id.targetOptions);
        targetList.setOnItemSelectedListener(this);

        setUpTargets();
        ArrayAdapter<String> listFiller = new ArrayAdapter<>(Objects.requireNonNull(this.getContext()),
                android.R.layout.simple_spinner_item, targetNames);
        listFiller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        targetList.setAdapter(listFiller);
        targetList.setPrompt("Please Select an Option:");
        targetList.setSelection(selectedOption);

//        targetsViewModel =
//                ViewModelProviders.of(this).get(TargetsViewModel.class);
//        final TextView textView = root.findViewById(R.id.text_targets);
//        targetsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }


    private void setUpTargets() {
//        targetNames.add("Please Select an Option");
        InputStream in = null;
        BufferedReader br = null;
        try {
            in = Objects.requireNonNull(getActivity()).getAssets().open("targetData.txt");
            br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String thisLine;
            while((thisLine = br.readLine()) != null) {
                System.out.println(thisLine);
                List<String> splitLine = Arrays.asList(thisLine.split("\\+_\\+"));
                //String[] splitLine = thisLine.split(",");
                targets.add(splitLine);
                targetNames.add(splitLine.get(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//        if (!firstTargetSelected) {
//            targetNames.remove(0);
//            firstTargetSelected = true;
//        }
        selectedOption = position;
        LinearLayout infoPanel = root.findViewById(R.id.targetInfoContainer);
        TextView location = root.findViewById(R.id.targetInfoLocationDescription);
        TextView description = root.findViewById(R.id.targetInfoDescription);
        TextView name = root.findViewById(R.id.targetInfoName);
        name.setText(targets.get(position).get(0));
        location.setText(targets.get(position).get(1));
        description.setText(targets.get(position).get(2));
        infoPanel.setVisibility(View.VISIBLE);
        Toast.makeText(this.getContext(), targetNames.get(position), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Toast.makeText(this.getContext(), "Nothing Selected", Toast.LENGTH_LONG).show();
        // TODO Auto-generated method stub
        //honestly don't know what this method does.
    }
}