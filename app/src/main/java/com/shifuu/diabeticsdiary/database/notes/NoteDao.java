package com.shifuu.diabeticsdiary.database.notes;

import com.shifuu.diabeticsdiary.database.food_rec.FoodRecEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NoteDao {

    @Delete
    void delete(NoteEntity entity);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(NoteEntity entity);

    @Query("SELECT * FROM note WHERE userId = :uId ORDER BY time")
    LiveData<List<NoteEntity>> getAllUsersNotes(long uId);

    @Query("SELECT * FROM note WHERE date = :ldt AND userId = :uId ORDER BY time")
    LiveData<List<NoteEntity>> getUsersNotesByDateSortedByTime(long uId, LocalDate ldt);

    @Update
    void update(NoteEntity entity);
    @Query("SELECT * FROM note WHERE userId = :uId ORDER BY time")
    List<NoteEntity> getAllUsersNotesSync(long uId);
}
