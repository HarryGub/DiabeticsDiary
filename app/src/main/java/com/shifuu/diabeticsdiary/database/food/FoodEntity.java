package com.shifuu.diabeticsdiary.database.food;

import com.shifuu.diabeticsdiary.database.user.UserEntity;

import java.io.Serializable;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;



@Entity (tableName = "food",
        foreignKeys = @ForeignKey(entity = UserEntity.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE))
public class FoodEntity implements Serializable {

    public static final long serialVersionUID = 6529685098290L;

    @PrimaryKey (autoGenerate = true)
    @NonNull
    private long id;

    private String note;

    @NonNull
    private String name;

    @NonNull
    private int kal;

    @NonNull
    private String portion;

    @NonNull
    private long userId;

    private int carbs;
    private int protein;
    private int fat;

    @Override
    public String toString() {
        return "FoodEntity{" +
                "id=" + id +
                ", note='" + note + '\'' +
                ", name='" + name + '\'' +
                ", kal=" + kal +
                ", portion='" + portion + '\'' +
                ", userId=" + userId +
                ", carbs=" + carbs +
                ", protein=" + protein +
                ", fat=" + fat +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodEntity food = (FoodEntity) o;
        return id == food.id && kal == food.kal && userId == food.userId && carbs == food.carbs && protein == food.protein && fat == food.fat && Objects.equals(note, food.note) && name.equals(food.name) && portion.equals(food.portion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, note, name, kal, portion, userId, carbs, protein, fat);
    }

    public FoodEntity(long id, String note, @NonNull String name, int kal, @NonNull String portion, long userId, int carbs, int protein, int fat) {
        this.id = id;
        this.note = note;
        this.name = name;
        this.kal = kal;
        this.portion = portion;
        this.userId = userId;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getKal() {
        return kal;
    }

    public void setKal(int kal) {
        this.kal = kal;
    }

    @NonNull
    public String getPortion() {
        return portion;
    }

    public void setPortion(@NonNull String portion) {
        this.portion = portion;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }
}
