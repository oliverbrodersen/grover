package com.example.grover.ui.addplant;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddPlantViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddPlantViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Add plants here!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}