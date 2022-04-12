package com.example.agapelife.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agapelife.utils.PreferenceStorage;

public class HomeViewModel extends ViewModel {
    PreferenceStorage preferenceStorage;

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(preferenceStorage.getUserName());
    }

    public LiveData<String> getText() {
        return mText;
    }
}