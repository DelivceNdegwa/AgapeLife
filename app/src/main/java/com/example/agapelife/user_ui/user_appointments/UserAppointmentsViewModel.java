package com.example.agapelife.user_ui.user_appointments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserAppointmentsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserAppointmentsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}