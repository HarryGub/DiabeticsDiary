package com.shifuu.diabeticsdiary.database.food_rec;

import com.shifuu.diabeticsdiary.database.food.FoodEntity;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Dao
public interface FoodRecDao {

    @Delete
    void delete(FoodRecEntity entity);

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    long insert(FoodRecEntity entity);

    @Query("SELECT * FROM food_rec ORDER BY time")
    LiveData<List<FoodRecEntity>> getAllRecsSortedByTime();

    @Query("SELECT * FROM food_rec WHERE userId = :userId ORDER BY time")
    LiveData<List<FoodRecEntity>> getAllRecsByUserSortedByTime(long userId);

    @Query("SELECT * FROM food_rec WHERE date = :date ORDER BY time")
    LiveData<List<FoodRecEntity>> getAllRecsByDateSortedByTime(LocalDate date);

    @Update
    void update(FoodRecEntity foodRecEntity);
    @Query("SELECT * FROM food_rec WHERE date = :date AND userId = :uId ORDER BY time")
    LiveData<List<FoodRecEntity>> getAllUserRecsByDateSortedByTime(LocalDate date, long uId);

    @Query("SELECT * FROM food_rec WHERE userId = :userId ORDER BY id ASC")
    List<FoodRecEntity> getAllEntitiesSync(long userId);
}
