package com.example.finalproject.ui.targets;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.finalproject.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class TargetsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private TargetsViewModel targetsViewModel;

    private List<String> targets = new ArrayList<>();

    private boolean firstTargetSelected = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_targets, container, false);
        Spinner targetList = root.findViewById(R.id.targetOptions);
        targetList.setOnItemSelectedListener(this);

        setUpTargets();
        ArrayAdapter<String> listFiller = new ArrayAdapter<>(Objects.requireNonNull(this.getContext()),
                android.R.layout.simple_spinner_item, targets);
        listFiller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        targetList.setAdapter(listFiller);

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

        targets.add("Please Select an Option");
        targets.add("Option1");
        targets.add("Option2");
        targets.add("Option3");
        targets.add("Option4");
        targets.add("Option5");
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        if (!firstTargetSelected) {
            targets.remove(0);
            firstTargetSelected = true;
        }
        Toast.makeText(this.getContext(), targets.get(position), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Toast.makeText(this.getContext(), "Nothing Selected", Toast.LENGTH_LONG).show();
        // TODO Auto-generated method stub
        //honestly don't know what this method does.
    }
}