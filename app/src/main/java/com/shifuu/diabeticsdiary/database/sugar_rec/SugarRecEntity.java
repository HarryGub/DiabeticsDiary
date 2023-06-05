package com.shifuu.diabeticsdiary.database.sugar_rec;

import com.shifuu.diabeticsdiary.database.user.UserEntity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity (tableName = "sugar_rec",
        foreignKeys = @ForeignKey(entity = UserEntity.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE))
public class SugarRecEntity implements Serializable {

    public static final long serialVersionUID = 65250982677690L;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    @NonNull
    private LocalDate date;

    @NonNull
    private LocalDateTime time;

    private String note;

    @NonNull
    private double value;

    private boolean beforeAfter;

    @NonNull
    private long userId;

    public String getNote() {
        return note;
    }

    public long getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public long getUserId() {
        return userId;
    }

    public boolean isBeforeAfter() {
        return beforeAfter;
    }

    @Override
    public boolean equals(@NonNull Object obj) {
        return date == ((SugarRecEntity) obj).date &&
                time == ((SugarRecEntity) obj).time &&
                value == ((SugarRecEntity) obj).value &&
                note.equals(((SugarRecEntity) obj).note);
    }

    public SugarRecEntity(long id, @NonNull LocalDate date, @NonNull LocalDateTime time, String note, double value, boolean beforeAfter, long userId) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.note = note;
        this.value = value;
        this.beforeAfter = beforeAfter;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SugarRecEntity{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", note='" + note + '\'' +
                ", value=" + value +
                ", beforeAfter=" + beforeAfter +
                ", userId=" + userId +
                '}';
    }
}
