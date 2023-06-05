package com.shifuu.diabeticsdiary.database.reminder;

import com.shifuu.diabeticsdiary.database.user.UserEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity (tableName = "reminder",
        foreignKeys = @ForeignKey(entity = UserEntity.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE))
public class ReminderEntity implements Serializable {

    public static final long serialVersionUID = 65098267757690L;

    @PrimaryKey (autoGenerate = true)
    @NonNull
    private long id;

    @NonNull
    private LocalDateTime time;

    private String note;

    private boolean isSet;

    @NonNull
    private long userId;

    public ReminderEntity(long id, LocalDateTime time, String note, boolean isSet, long userId) {
        this.id = id;
        this.time = time;
        this.note = note;
        this.isSet = isSet;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReminderEntity that = (ReminderEntity) o;
        return id == that.id && time == that.time && isSet == that.isSet && userId == that.userId && Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, note, isSet, userId);
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getNote() {
        return note;
    }

    public boolean isSet() {
        return isSet;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "ReminderEntity{" +
                "id=" + id +
                ", time=" + time +
                ", note='" + note + '\'' +
                ", isSet=" + isSet +
                ", userId=" + userId +
                '}';
    }
}
