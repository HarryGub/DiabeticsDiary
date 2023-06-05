package com.shifuu.diabeticsdiary.loginactivity.registration;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.user.UserEntity;
import com.shifuu.diabeticsdiary.util.Util;

import java.io.IOException;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

public class RegistrationViewModel extends AndroidViewModel {

    private byte[] image;
    AppRepository appRepository;

    Application application;

    public RegistrationViewModel(Application application)
    {
        super(application);
        appRepository = new AppRepository(application);
        this.application = application;
    }

    long registerNewUser(String name, String passwd, double weight)
    {
        if (image == null)
            image = Util.getDefaultUserAvatarBytes(application.getApplicationContext());

        UserEntity user = new UserEntity(0, name, passwd, image, weight);

        return appRepository.insertUser(user);
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
}