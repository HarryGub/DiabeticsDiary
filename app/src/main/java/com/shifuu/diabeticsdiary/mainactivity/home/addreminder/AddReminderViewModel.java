package com.shifuu.diabeticsdiary.mainactivity.home.addreminder;

import android.app.Application;

import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.reminder.ReminderEntity;
import com.shifuu.diabeticsdiary.util.NotificationRegistry;
import com.shifuu.diabeticsdiary.util.Util;

import java.time.LocalDateTime;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AddReminderViewModel extends AndroidViewModel {

    private AppRepository appRepository;
    private Application application;
    private LocalDateTime time;
    private String note = "";


    public AddReminderViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        this.application = application;
        time = LocalDateTime.now();
    }

    public void commitReminderRec()
    {
        appRepository.insertReminder(new ReminderEntity(0, time, note, true, Util.getSharedPrefUserId(application)));
        NotificationRegistry.getInstance(application).registerAlarm(application.getApplicationContext(), time, note);

    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
