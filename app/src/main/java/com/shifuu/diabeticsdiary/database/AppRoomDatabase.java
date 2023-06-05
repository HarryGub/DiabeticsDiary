package com.shifuu.diabeticsdiary.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.AutoMigrationSpec;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.shifuu.diabeticsdiary.database.food.FoodDao;
import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.database.food_rec.FoodRecDao;
import com.shifuu.diabeticsdiary.database.food_rec.FoodRecEntity;
import com.shifuu.diabeticsdiary.database.notes.NoteDao;
import com.shifuu.diabeticsdiary.database.notes.NoteEntity;
import com.shifuu.diabeticsdiary.database.reminder.ReminderDao;
import com.shifuu.diabeticsdiary.database.reminder.ReminderEntity;
import com.shifuu.diabeticsdiary.database.sugar_rec.SugarRecDao;
import com.shifuu.diabeticsdiary.database.sugar_rec.SugarRecEntity;
import com.shifuu.diabeticsdiary.database.user.UserDao;
import com.shifuu.diabeticsdiary.database.user.UserEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        SugarRecEntity.class,
        FoodEntity.class,
        UserEntity.class,
        FoodRecEntity.class,
        ReminderEntity.class,
        NoteEntity.class},
        version = 11)

@TypeConverters({Converter.class})
public abstract class AppRoomDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract SugarRecDao sugarDao();
    public abstract FoodDao foodDao();
    public abstract FoodRecDao foodRecDao();
    public abstract ReminderDao reminderDao();
    public abstract NoteDao noteDao();

    private static volatile AppRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppRoomDatabase.class,
                                    "app_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        databaseWriteExecutor.execute(() -> {


        });

        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block

        }
    };

}






















