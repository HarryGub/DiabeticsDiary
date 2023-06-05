package com.shifuu.diabeticsdiary.database.food_rec;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

public class CollapsedFoodRec {


    private LocalDate date;
    private LocalDateTime time;
    private String note;
    private Map<Long, Double> foodMassMap;
    private Map<Long, String> foodFoodNameMap;
    private Map<Long, String> foodPortionNameMap;
    private Map<Long, Integer> foodCaloriesByPortionMap;
    private long totalCalories;
    private List<Long> foodRecIds;
    private long userId;

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getNote() {
        return note;
    }

    public Map<Long, Double> getFoodMassMap() {
        return foodMassMap;
    }

    public Map<Long, String> getFoodFoodNameMap() {
        return foodFoodNameMap;
    }

    public Map<Long, String> getFoodPortionNameMap() {
        return foodPortionNameMap;
    }

    public Map<Long, Integer> getFoodCaloriesByPortionMap() {
        return foodCaloriesByPortionMap;
    }

    public long getTotalCalories() {
        return totalCalories;
    }

    public List<Long> getFoodRecIds() {
        return foodRecIds;
    }

    public long getUserId() {
        return userId;
    }

    public CollapsedFoodRec(LocalDate date, LocalDateTime time, String note, Map<Long, Double> foodMassMap, Map<Long, String> foodFoodNameMap, Map<Long, String> foodPortionNameMap, Map<Long, Integer> foodCaloriesByPortionMap, long totalCalories, List<Long> foodRecIds, long userId) {
        this.date = date;
        this.time = time;
        this.note = note;
        this.foodMassMap = foodMassMap;
        this.foodFoodNameMap = foodFoodNameMap;
        this.foodPortionNameMap = foodPortionNameMap;
        this.foodCaloriesByPortionMap = foodCaloriesByPortionMap;
        this.totalCalories = totalCalories;
        this.foodRecIds = foodRecIds;
        this.userId = userId;
    }
}
