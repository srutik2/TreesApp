package com.example.finalproject.ui.targets;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TargetsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TargetsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the targets fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}