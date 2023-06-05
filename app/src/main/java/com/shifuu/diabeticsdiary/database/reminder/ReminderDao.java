package com.shifuu.diabeticsdiary.database.reminder;

import com.shifuu.diabeticsdiary.database.food.FoodEntity;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface ReminderDao {

    @Delete
    void delete(ReminderEntity entity);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    long insert(ReminderEntity entity);

    @Query("SELECT * FROM reminder ORDER BY time")
    LiveData<List<ReminderEntity>> getAllRecsSortedByTime();

    @Query("SELECT * FROM reminder WHERE userId = :userId ORDER BY time")
    LiveData<List<ReminderEntity>> getAllRecsByUserSortedByTime(long userId);
    @Query("SELECT * FROM reminder WHERE id = :id ORDER BY time")
    LiveData<ReminderEntity> getReminderEntityById(long id);

    @Query("SELECT * FROM reminder WHERE userId = :userId ORDER BY id ASC")
    List<ReminderEntity> getAllEntitiesSync(long userId);
}
