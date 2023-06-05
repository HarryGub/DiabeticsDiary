package com.shifuu.diabeticsdiary.loginactivity.login;

import android.app.Application;

import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.user.UserEntity;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends AndroidViewModel {
    private final LiveData<List<UserEntity>> users;
    private AppRepository repository;

    public LoginViewModel (Application application)
    {
        super(application);
        repository = new AppRepository(application);
        users = repository.getUserListSortedById();
    }

    public LiveData<List<UserEntity>> getAllUserEntities()
    {
        return repository.getUserListSortedById();
    }

    public void insert(UserEntity user)
    {
        repository.insertUser(user);
    }
}