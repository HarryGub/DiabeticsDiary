package com.shifuu.diabeticsdiary.loginactivity.importuser;

import android.net.Uri;

import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.database.food_rec.FoodRecEntity;
import com.shifuu.diabeticsdiary.database.reminder.ReminderEntity;
import com.shifuu.diabeticsdiary.database.sugar_rec.SugarRecEntity;
import com.shifuu.diabeticsdiary.database.user.UserEntity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class ImportBundle implements Serializable {

    UserEntity user;
    List<ReminderEntity> reminderEntities;
    List<FoodEntity> foodEntities;
    List<FoodRecEntity> foodRecEntities;
    List<SugarRecEntity> sugarRecEntities;


    private static void insertFromBundle(ImportBundle importBundle)
    {

    }

    public static ImportBundle createBundleFromUserId(int id)
    {
        return new ImportBundle();
    }

    private ImportBundle()
    {

    }

    public static void insertFromUri(Uri uri) throws IllegalArgumentException
    {
        FileOutputStream fout = null;
        ImportBundle importBundle = null;
        try {
            fout = new FileOutputStream(uri.getPath());
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(importBundle);
            fout.close();
            oos.close();
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        insertFromBundle(importBundle);

    }

}
