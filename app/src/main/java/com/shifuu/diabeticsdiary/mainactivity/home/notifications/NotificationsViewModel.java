package com.shifuu.diabeticsdiary.mainactivity.home.notifications;

import android.app.Application;

import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.reminder.ReminderEntity;
import com.shifuu.diabeticsdiary.util.NotificationRegistry;
import com.shifuu.diabeticsdiary.util.Util;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class NotificationsViewModel extends AndroidViewModel {

    private AppRepository repository;
    private Application application;
    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        this.application = application;
    }

    public void deleteReminderRec(ReminderEntity entity) {

        repository.deleteReminder(entity);
        NotificationRegistry.getInstance(application).unregisterAlarm(entity.getTime());
    }

    public LiveData<List<ReminderEntity>> getReminderEntitiesByUser()
    {
        return repository.getReminderEntitiesByUser(Util.getSharedPrefUserId(application));
    }

    public void commitReminderActive(ReminderEntity entity, boolean isActive)
    {
        repository.insertReminder(new ReminderEntity(entity.getId(), entity.getTime(), entity.getNote(), isActive, entity.getUserId()));

        if (isActive)
            NotificationRegistry.getInstance(application).registerAlarm(application.getApplicationContext(), entity.getTime(), entity.getNote());
        else
            NotificationRegistry.getInstance(application).unregisterAlarm(entity.getTime());

    }
}