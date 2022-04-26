package com.example.agapelife.user_ui.user_records;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserHealthRecordsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserHealthRecordsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Your history will appear here");
    }

    public LiveData<String> getText() {
        return mText;
    }
}