package com.shifuu.diabeticsdiary.database.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.database.user.UserEntity;

import java.util.List;

@Dao
public interface UserDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    long insert(UserEntity user);
    @Update
    void update(UserEntity user);

    @Delete
    void delete(UserEntity user);

    @Query("SELECT * FROM users ORDER BY id ASC")
    LiveData<List<UserEntity>> getUserListSortedById();

    @Delete
    void deleteUserById(UserEntity user);

    @Query("SELECT * FROM users WHERE id = :id")
    LiveData<UserEntity> getUserById(int id);

    @Query("SELECT * FROM users WHERE id = :id")
    LiveData<UserEntity> getUserById(long id);

    @Query("SELECT * FROM users WHERE id = :userId ORDER BY id ASC")
    List<UserEntity> getAllEntitiesSync(long userId);
}
