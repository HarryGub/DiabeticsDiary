package com.shifuu.diabeticsdiary.loginactivity.passwordlogin;

import android.app.Application;

import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.reminder.ReminderEntity;
import com.shifuu.diabeticsdiary.database.user.UserEntity;
import com.shifuu.diabeticsdiary.util.NotificationRegistry;

import java.util.List;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class PasswordLoginViewModel extends AndroidViewModel {

    private UserEntity user;
    private AppRepository appRepository;
    private Application application;

    public PasswordLoginViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        this.application = application;
    }

    public LiveData<UserEntity> retrieveUser(long id)
    {
        return appRepository.getUserById(id);
    }

    public void updateUser(UserEntity user) {
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }

    public void registerUsersReminders() {
        LiveData<List<ReminderEntity>> ld = appRepository.getReminderEntitiesByUser(user.getId());
        NotificationRegistry registry = NotificationRegistry.getInstance(application);

        ld.observeForever(new Observer<List<ReminderEntity>>() {
            @Override
            public void onChanged(List<ReminderEntity> reminderEntities) {
                reminderEntities.forEach(new Consumer<ReminderEntity>() {
                    @Override
                    public void accept(ReminderEntity entity) {
                        registry.registerAlarm(application.getApplicationContext(), entity.getTime(), entity.getNote());
                    }
                });
                ld.removeObserver(this);
            }
        });
    }
}