package com.shifuu.diabeticsdiary.database.notes;

import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.database.user.UserEntity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "note",
        foreignKeys =
                {@ForeignKey(
                        entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE),
                })
public class NoteEntity implements Serializable {

    public static final long serialVersionUID = 652968967757690L;

    @PrimaryKey (autoGenerate = true)
    private long id;

    @NonNull
    private String note;

    @NonNull
    private LocalDate date;

    @NonNull
    private LocalDateTime time;
    private long userId;

    public NoteEntity(long id, @NonNull String note, @NonNull LocalDate date, @NonNull LocalDateTime time, long userId) {
        this.id = id;
        this.note = note;
        this.date = date;
        this.time = time;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        NoteEntity that = (NoteEntity) o;
        return id == that.id && userId == that.userId && note.equals(that.note) && date.equals(that.date) && time.equals(that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, note, date, time, userId);
    }

    @NonNull
    public LocalDate getDate() {
        return date;
    }

    public void setDate(@NonNull LocalDate date) {
        this.date = date;
    }

    @NonNull
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(@NonNull LocalDateTime time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getNote() {
        return note;
    }

    public void setNote(@NonNull String note) {
        this.note = note;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", note='" + note + '\'' +
                "\n";
    }
}
