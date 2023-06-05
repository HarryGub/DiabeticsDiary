package com.shifuu.diabeticsdiary.mainactivity.home.addcal;

import com.shifuu.diabeticsdiary.database.food.FoodEntity;

import java.util.Objects;

public class AddCalFoodEntry {

    private FoodEntity foodEntity;
    private double mass;
    private long totalCal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddCalFoodEntry that = (AddCalFoodEntry) o;
        return Double.compare(that.mass, mass) == 0 && totalCal == that.totalCal && Objects.equals(foodEntity, that.foodEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodEntity, mass, totalCal);
    }

    public FoodEntity getFoodEntity() {
        return foodEntity;
    }

    public double getMass() {
        return mass;
    }

    public long getTotalCal() {
        return totalCal;
    }

    public AddCalFoodEntry(FoodEntity foodEntity, double mass, long totalCal) {
        this.foodEntity = foodEntity;
        this.mass = mass;
        this.totalCal = totalCal;
    }
}
