package com.example.agapelife.user_ui.user_home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agapelife.utils.PreferenceStorage;

public class UserHomeViewModel extends ViewModel {
    PreferenceStorage preferenceStorage;

    private MutableLiveData<String> mText;

    public UserHomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the Home page");
    }

    public LiveData<String> getText() {
        return mText;
    }
}