package com.shifuu.diabeticsdiary.database;

import android.app.Application;
import android.util.ArrayMap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.shifuu.diabeticsdiary.database.food.FoodDao;
import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.database.food_rec.CollapsedFoodRec;
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

import java.sql.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.IntFunction;

public class AppRepository {

    private final UserDao userDao;
    private final FoodDao foodDao;
    private final SugarRecDao sugarRecDao;
    private final FoodRecDao foodRecDao;
    private final ReminderDao reminderDao;
    private final NoteDao noteDao;
    private final AppRoomDatabase db;
    private Application application;
    private final LiveData<List<UserEntity>> users;

    public AppRepository(Application application)
    {
        db = AppRoomDatabase.getDatabase(application);
        userDao = db.userDao();
        foodDao = db.foodDao();
        sugarRecDao = db.sugarDao();
        foodRecDao = db.foodRecDao();
        reminderDao = db.reminderDao();
        noteDao = db.noteDao();
        this.application = application;

        users = getUserListSortedById();
    }

    public LiveData<List<UserEntity>> getUserListSortedById()
    {
        return userDao.getUserListSortedById();
    }
    public long insertUser(UserEntity user)
    {
        Callable<Long> insertCallable = () -> userDao.insert(user);
        long id = 0;

        Future<Long> future = AppRoomDatabase.databaseWriteExecutor.submit(insertCallable);

        try {
            id = future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return id;
    }
    public void deleteUser(UserEntity user)
    {

        AppRoomDatabase.databaseWriteExecutor.execute(() -> userDao.delete(user));
    }

    public void deleteFood(FoodEntity food)
    {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> foodDao.delete(food));
    }
    public long insertFood(FoodEntity food)
    {

        Callable<Long> insertCallable = () -> foodDao.insert(food);
        long id = 0;

        Future<Long> future = AppRoomDatabase.databaseWriteExecutor.submit(insertCallable);

        try {
            id = future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return id;

    }
    public LiveData<List<FoodEntity>> getAllFoodEntitiesSortedByName()
    {
        return foodDao.getAllFoodEntitiesSortedByName();
    }
    public LiveData<List<FoodEntity>> getFoodEntitiesByUserByName(String foodsName, long uid)
    {
        return foodDao.getFoodEntitiesByUserByName(foodsName, uid);
    }

    public long insertFoodRec(FoodRecEntity food)
    {

        Callable<Long> insertCallable = () -> foodRecDao.insert(food);
        long id = 0;

        Future<Long> future = AppRoomDatabase.databaseWriteExecutor.submit(insertCallable);

        try {
            id = future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return id;
    }

    public long insertNote(NoteEntity note)
    {
        Callable<Long> insertCallable = () -> noteDao.insert(note);
        long id = 0;

        Future<Long> future = AppRoomDatabase.databaseWriteExecutor.submit(insertCallable);

        try {
            id = future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return id;
    }

    public void deleteNoteById(long id)
    {
        AppRoomDatabase.databaseWriteExecutor.execute(() ->
                noteDao.delete(new NoteEntity(id, "", LocalDate.now(),LocalDateTime.now(), 0)));
    }

    public void deleteNote(NoteEntity note)
    {
        AppRoomDatabase.databaseWriteExecutor.execute(() ->
                noteDao.delete(note));
    }

    public void updateNote(NoteEntity note)
    {
        AppRoomDatabase.databaseWriteExecutor.execute(() ->
                noteDao.update(note));
    }

    public LiveData<List<NoteEntity>> getAllUsersNotes(long uid)
    {
        return noteDao.getAllUsersNotes(uid);
    }

    public LiveData<List<NoteEntity>> getUsersNotesByDate(long uid, LocalDate localDate)
    {
        return noteDao.getUsersNotesByDateSortedByTime(uid, localDate);
    }
    public void deleteFoodRec(FoodRecEntity food)
    {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> foodRecDao.delete(food));

    }
    public LiveData<List<FoodRecEntity>> getAllFoodRecsSortedByTime()
    {
        return foodRecDao.getAllRecsSortedByTime();
    }

    public long insertReminder(ReminderEntity reminder)
    {

        Callable<Long> insertCallable = () -> reminderDao.insert(reminder);
        long id = 0;

        Future<Long> future = AppRoomDatabase.databaseWriteExecutor.submit(insertCallable);

        try {
            id = future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return id;

    }
    public void deleteReminder(ReminderEntity reminder)
    {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> reminderDao.delete(reminder));
    }
    public LiveData<List<ReminderEntity>> getAllReminderRecsSortedByTime()
    {
        return reminderDao.getAllRecsSortedByTime();
    }

    public long insertSugarRec(SugarRecEntity sugarRec)
    {
        Callable<Long> insertCallable = () -> sugarRecDao.insert(sugarRec);
        long id = 0;

        Future<Long> future = AppRoomDatabase.databaseWriteExecutor.submit(insertCallable);

        try {
            id = future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return id;
    }
    public void deleteSugarRec(SugarRecEntity sugarRecEntity)
    {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> sugarRecDao.delete(sugarRecEntity));
        ;
    }
    public LiveData<List<SugarRecEntity>> getAllSugarRecsSortedById()
    {
        return sugarRecDao.getAllRecsSortedById();
    }
    public LiveData<List<SugarRecEntity>> getSugarRecsByDateSortedByTime(LocalDate date)
    {
        return sugarRecDao.getRecsByDateSortedByTime(date);
    }

    public void deleteUserById(long id)
    {
        AppRoomDatabase.databaseWriteExecutor.execute(() ->
                userDao.delete(new UserEntity(id, "", "", new byte[10], 0.0)));
    }

    public LiveData<UserEntity> getUserById(long id)
    {
        return userDao.getUserById(id);
    }

    public LiveData<FoodEntity> getFoodById(long foodId) {
        return foodDao.getFoodEntityById(foodId);
    }

    public LiveData<List<FoodEntity>> getAllFoodEntitiesByUserSortedByName(long userId)
    {
        return foodDao.getAllFoodEntitiesByUserSortedByName(userId);
    }

    public void deleteFoodById(long id) {

        AppRoomDatabase.databaseWriteExecutor.execute(() ->
                foodDao.delete(new FoodEntity(id, "", "", 0, "", 0, 0, 0, 0)));
    }

    public void updateUser(UserEntity user)
    {
        AppRoomDatabase.databaseWriteExecutor.execute(() ->
                userDao.update(user));
    }

    public LiveData<List<FoodRecEntity>> getFoodRecEntitiesByDateSortedByTime(LocalDate date, long uId)
    {
        return foodRecDao.getAllUserRecsByDateSortedByTime(date, uId);
    }

    public LiveData<List<FoodRecEntity>> getAllRecsByUserSortedByTime(long uid)
    {
        return foodRecDao.getAllRecsByUserSortedByTime(uid);
    }

    public void updateFoodRec(FoodRecEntity foodRecEntity)
    {
        AppRoomDatabase.databaseWriteExecutor.execute(() ->
                foodRecDao.update(foodRecEntity));
    }

    public void deleteFoodRecById(long id) {
        AppRoomDatabase.databaseWriteExecutor.execute(() ->
                foodRecDao.delete(new FoodRecEntity(id, null,null,"", 0, 0.0, 0)));
    }

    public LiveData<List<SugarRecEntity>> getSugarRecsByDateByUser(LocalDate date, long uId) {
        return sugarRecDao.getRecsByDateAndUserSortedByTime(date, uId);
    }

    public LiveData<List<ReminderEntity>> getReminderEntitiesByUser(long uId) {
        return reminderDao.getAllRecsByUserSortedByTime(uId);
    }

    public LiveData<ReminderEntity> getReminderEntityById(long id)
    {
        return reminderDao.getReminderEntityById(id);
    }

    public void deleteReminderById(long id) {
        reminderDao.delete(new ReminderEntity(id,LocalDateTime.now(), "", false, 0));
    }

    public List<FoodEntity> getAllFoodEntitiesSync(long uId)
    {
        return foodDao.getAllEntitiesSync(uId);
    }

    public List<FoodRecEntity> getAllFoodRecEntitiesSync(long uId)
    {
        return foodRecDao.getAllEntitiesSync(uId);
    }

    public List<ReminderEntity> getAllRemindersSync(long uId)
    {
        return reminderDao.getAllEntitiesSync(uId);
    }

    public List<SugarRecEntity> getAllSugarRecsSync(long uId)
    {
        return sugarRecDao.getAllEntitiesSync(uId);
    }

    public List<UserEntity> getAllUsersSync(long uId)
    {
        return userDao.getAllEntitiesSync(uId);
    }

    public List<NoteEntity> getAllUsersNotesSync(long uId) {
        return noteDao.getAllUsersNotesSync(uId);
    }
}








