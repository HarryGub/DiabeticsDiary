package com.shifuu.diabeticsdiary.mainactivity.account;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.reminder.ReminderEntity;
import com.shifuu.diabeticsdiary.database.user.UserEntity;
import com.shifuu.diabeticsdiary.util.NotificationRegistry;
import com.shifuu.diabeticsdiary.util.Util;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class AccountViewModel extends AndroidViewModel {

    private LiveData<UserEntity> liveData;
    private UserEntity user;
    private AppRepository appRepository;
    private byte[] image;
    Application application;

    public AccountViewModel(Application application) {
        super(application);
        appRepository = new AppRepository(application);
        this.application = application;
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

    public void loadImageFromURI(Uri uri, Context context) {

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

            image = Util.getBytesFromBitmapJPEG(bitmap);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getImage() {
        return image == null ?
                Util.getDefaultUserAvatarBytes(application.getApplicationContext()) :
                image;
    }

    public void alterUserAvatar()
    {
        UserEntity newUser = new UserEntity(user.getId(), user.getName(), user.getPasswd(), getImage(), user.getWeight());
        appRepository.updateUser(newUser);
        Util.setSharedPrefUserId(application.getApplicationContext(), user.getId());
    }

    public void unregisterAllUserAlarms() {
        LiveData<List<ReminderEntity>> ld = appRepository.getReminderEntitiesByUser(user.getId());
        NotificationRegistry registry = NotificationRegistry.getInstance(application);

        ld.observeForever(new Observer<List<ReminderEntity>>() {
            @Override
            public void onChanged(List<ReminderEntity> reminderEntities) {
                reminderEntities.forEach(new Consumer<ReminderEntity>() {
                    @Override
                    public void accept(ReminderEntity entity) {
                        registry.unregisterAlarm(entity.getTime());
                    }
                });
                ld.removeObserver(this);
            }
        });
    }

    public void alterUserWeight(double weight) {
        UserEntity newUser = new UserEntity(user.getId(), user.getName(), user.getPasswd(), user.getAvatar(), weight);
        appRepository.updateUser(newUser);
        Util.setSharedPrefUserId(application.getApplicationContext(), user.getId());
    }
}