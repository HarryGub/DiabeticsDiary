package com.shifuu.diabeticsdiary.database.food_rec;

import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.database.user.UserEntity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity (tableName = "food_rec",
        foreignKeys =
                {@ForeignKey(
                        entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE),
                @ForeignKey(
                        entity = FoodEntity.class,
                        parentColumns = "id",
                        childColumns = "foodId",
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.CASCADE)
                })
public class FoodRecEntity implements Serializable {

    public static final long serialVersionUID = 65296850267757690L;

    @PrimaryKey (autoGenerate = true)
    @NonNull
    private long id;

    @NonNull
    private LocalDate date;

    @NonNull
    private LocalDateTime time;

    private String note;

    @NonNull
    private long foodId;

    @NonNull
    private double mass;

    @NonNull
    private long userId;

    public FoodRecEntity(long id, LocalDate date, LocalDateTime time, String note, long foodId, double mass, long userId) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.note = note;
        this.foodId = foodId;
        this.mass = mass;
        this.userId = userId;
    }

    public String getNote() {
        return note;
    }

    public long getId() {
        return id;
    }

    public long getFoodId() {
        return foodId;
    }

    public double getMass() { return mass;}

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(@NonNull Object obj) {
        return date == ((FoodRecEntity) obj).date &&
                time == ((FoodRecEntity) obj).time &&
                mass == ((FoodRecEntity) obj).mass &&
                note.equals(((FoodRecEntity) obj).note) &&
                foodId == ((FoodRecEntity) obj).foodId;
    }

    @Override
    public String toString() {
        return "FoodRecEntity{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", note='" + note + '\'' +
                ", foodId=" + foodId +
                ", mass=" + mass +
                ", userId=" + userId +
                '}';
    }
}
