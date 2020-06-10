package com.kitchenworks.chefclient.ui.comenta;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ComentaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ComentaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is comenta fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}