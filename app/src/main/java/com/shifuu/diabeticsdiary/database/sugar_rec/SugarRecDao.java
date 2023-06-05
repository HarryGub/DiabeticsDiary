package com.shifuu.diabeticsdiary.database.sugar_rec;

import com.shifuu.diabeticsdiary.database.food.FoodEntity;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Dao
public interface SugarRecDao {

    @Delete
    void delete(SugarRecEntity entity);

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    long insert(SugarRecEntity entity);

    @Query("SELECT * FROM sugar_rec ORDER BY id")
    LiveData<List<SugarRecEntity>> getAllRecsSortedById();

    @Query("SELECT * FROM sugar_rec WHERE userId = :userId ORDER BY id")
    LiveData<List<SugarRecEntity>> getAllRecsByUserSortedById(long userId);

    @Query("SELECT * FROM sugar_rec WHERE date = :date ORDER BY time")
    LiveData<List<SugarRecEntity>> getRecsByDateSortedByTime(LocalDate date);

    @Query("SELECT * FROM sugar_rec WHERE date = :date AND userId = :userId ORDER BY time")
    LiveData<List<SugarRecEntity>> getRecsByDateAndUserSortedByTime(LocalDate date, long userId);

    @Query("SELECT * FROM sugar_rec WHERE userId = :userId ORDER BY id ASC")
    List<SugarRecEntity> getAllEntitiesSync(long userId);

}
