package com.shifuu.diabeticsdiary.mainactivity.home;

import android.app.AlertDialog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private AlertDialog alertDialog = null;
    private boolean isShowingDialog = false;

    public HomeViewModel() {

    }

    public void setAlertDialog(AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

    public boolean isDialogNull()
    {
        return alertDialog == null;
    }

    public void setShowingDialog(boolean showingDialog) {
        isShowingDialog = showingDialog;
    }

    public boolean isShowingDialog() {
        return isShowingDialog;
    }
}