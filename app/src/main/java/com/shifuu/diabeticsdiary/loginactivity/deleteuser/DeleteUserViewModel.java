package com.shifuu.diabeticsdiary.loginactivity.deleteuser;

import android.app.AlertDialog;
import android.app.Application;

import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.user.UserEntity;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class DeleteUserViewModel extends AndroidViewModel {

    UserEntity user;
    AppRepository appRepository;

    AlertDialog alertDialog;

    public DeleteUserViewModel(Application application)
    {
        super(application);
        appRepository = new AppRepository(application);
    }

    public LiveData<UserEntity> retrieveUser(long id)
    {
        return appRepository.getUserById(id);
    }

    public void deleteCurrentUser()
    {
        appRepository.deleteUserById(user.getId());
    }

    public UserEntity getUser() {
        return user;
    }

    public void updateUser(UserEntity user) {
        this.user = user;
    }

    public void setAlertDialog(AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }
}