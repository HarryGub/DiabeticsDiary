package com.shifuu.diabeticsdiary.loginactivity.importuser;

import android.app.Application;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.util.ArrayMap;
import android.util.Log;

import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.WholeDataHolder;
import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.database.food_rec.FoodRecEntity;
import com.shifuu.diabeticsdiary.database.notes.NoteEntity;
import com.shifuu.diabeticsdiary.database.reminder.ReminderEntity;
import com.shifuu.diabeticsdiary.database.sugar_rec.SugarRecEntity;
import com.shifuu.diabeticsdiary.database.user.UserEntity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

public class ImportUserViewModel extends AndroidViewModel {

    private AppRepository appRepository;
    private Application application;
    private Uri uri = null;
    public ImportUserViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        this.application = application;
    }

    public void setDdtFileUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public int commitFileToDatabase() throws IOException, ClassNotFoundException {

        return new WriteAsyncTask().doInBackground(readSerial(uri));

    }

    private WholeDataHolder readSerial(Uri uri) throws IOException, ClassNotFoundException {

        ParcelFileDescriptor fileDescriptor = application.getApplicationContext().getContentResolver().openFileDescriptor(uri, "r");

        FileInputStream fis = new FileInputStream(fileDescriptor.getFileDescriptor());
        ObjectInputStream ois = new ObjectInputStream(fis);
        WholeDataHolder wholeDataHolder = (WholeDataHolder) ois.readObject();
        fileDescriptor.close();
        fis.close();
        ois.close();

        return wholeDataHolder;
    }


    private class WriteAsyncTask extends AsyncTask<WholeDataHolder, Integer, Integer>
    {

        @Override
        protected Integer doInBackground(WholeDataHolder... wholeDataHolders) {
            try
            {
                WholeDataHolder wholeDataHolder = wholeDataHolders[0];

                List<FoodEntity> foodEntities = wholeDataHolder.getFoods();
                List<FoodRecEntity> foodRecEntities = wholeDataHolder.getFoodRecs();
                List<ReminderEntity> reminderEntities = wholeDataHolder.getReminders();
                List<SugarRecEntity> sugarRecEntities = wholeDataHolder.getSugars();
                List<UserEntity> userEntities = wholeDataHolder.getUsers();
                List<NoteEntity> noteEntities = wholeDataHolder.getNotes();

                long[] uid = new long[1];
                Map<Long, Long> foodIdMap = new ArrayMap<>();



                userEntities.forEach(new Consumer<UserEntity>() {
                    @Override
                    public void accept(UserEntity user) {
                        Log.d("ImportUser", user.toString());

                        byte[] avatar = user.getAvatar();

                        UserEntity u = new UserEntity(0, user.getName(), user.getPasswd(), avatar, user.getWeight());

                        uid[0] = appRepository.insertUser(u);
                    }
                });

                foodEntities.forEach(new Consumer<FoodEntity>() {
                    @Override
                    public void accept(FoodEntity food) {
                        FoodEntity f = new FoodEntity(
                                0,
                                food.getName(),
                                food.getPortion(),
                                food.getKal(),
                                food.getNote(),
                                uid[0],
                                food.getCarbs(),
                                food.getProtein(),
                                food.getFat());

                        Log.d("ImportUser", food.toString());
                        long newId = appRepository.insertFood(f);

                        foodIdMap.put(food.getId(), newId);
                    }
                });

                foodRecEntities.forEach(new Consumer<FoodRecEntity>() {
                    @Override
                    public void accept(FoodRecEntity foodRecEntity) {
                        FoodRecEntity f = new FoodRecEntity(
                                0,
                                foodRecEntity.getDate(),
                                foodRecEntity.getTime(),
                                foodRecEntity.getNote(),
                                foodIdMap.get(foodRecEntity.getFoodId()),
                                foodRecEntity.getMass(),
                                uid[0]);

                        Log.d("ImportUser", foodRecEntity.toString());

                        appRepository.insertFoodRec(f);
                    }
                });


                reminderEntities.forEach(new Consumer<ReminderEntity>() {
                    @Override
                    public void accept(ReminderEntity entity) {
                        ReminderEntity r = new ReminderEntity(
                                0,
                                entity.getTime(),
                                entity.getNote(),
                                entity.isSet(),
                                uid[0]);
                        Log.d("ImportUser", entity.toString());

                        appRepository.insertReminder(r);
                    }
                });

                sugarRecEntities.forEach(new Consumer<SugarRecEntity>() {
                    @Override
                    public void accept(SugarRecEntity sugarRecEntity) {
                        SugarRecEntity s = new SugarRecEntity(
                                0,
                                sugarRecEntity.getDate(),
                                sugarRecEntity.getTime(),
                                sugarRecEntity.getNote(),
                                sugarRecEntity.getValue(),
                                sugarRecEntity.isBeforeAfter(),
                                uid[0]);

                        Log.d("ImportUser", sugarRecEntity.toString());

                        appRepository.insertSugarRec(s);
                    }
                });

                noteEntities.forEach(new Consumer<NoteEntity>() {
                    @Override
                    public void accept(NoteEntity note) {
                        NoteEntity n = new NoteEntity(
                                0,
                                note.getNote(),
                                note.getDate(),
                                note.getTime(),
                                uid[0]);

                        Log.d("ImportUser", note.toString());

                        appRepository.insertNote(n);
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return 1;
            }

            return 0;
        }
    }
}