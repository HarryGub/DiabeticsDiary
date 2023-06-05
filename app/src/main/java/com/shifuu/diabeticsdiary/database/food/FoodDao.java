package com.shifuu.diabeticsdiary.database.food;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FoodDao {

    @Delete
    void delete (FoodEntity food);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    long insert(FoodEntity food);

    @Query("SELECT * FROM food ORDER BY name ASC")
    LiveData<List<FoodEntity>> getAllFoodEntitiesSortedByName();

    @Query("SELECT * FROM food WHERE userId = :userId ORDER BY name ASC")
    LiveData<List<FoodEntity>> getAllFoodEntitiesByUserSortedByName(long userId);

    @Query("SELECT * FROM food WHERE name LIKE '%' || :foodsName || '%' ORDER BY name ASC")
    LiveData<List<FoodEntity>> getFoodEntitiesByUserByName(String foodsName);

    @Query("SELECT * FROM food WHERE name LIKE '%' || :foodsName || '%' AND userId = :userId ORDER BY name ASC")
    LiveData<List<FoodEntity>> getFoodEntitiesByUserByName(String foodsName, long userId);
    @Query("SELECT * FROM food WHERE id = :foodId")
    LiveData<FoodEntity> getFoodEntityById(long foodId);

    @Query("SELECT * FROM food WHERE userId = :userId ORDER BY id ASC")
    List<FoodEntity> getAllEntitiesSync(long userId);
}
