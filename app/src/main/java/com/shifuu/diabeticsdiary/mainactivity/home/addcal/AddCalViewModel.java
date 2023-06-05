package com.shifuu.diabeticsdiary.mainactivity.home.addcal;

import android.app.Application;
import android.app.ApplicationErrorReport;
import android.util.Log;

import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.food_rec.FoodRecEntity;
import com.shifuu.diabeticsdiary.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AddCalViewModel extends AndroidViewModel {

    private final AppRepository appRepository;
    private Application application;
    private LocalDateTime time;
    private LocalDate date;
    private List<AddCalFoodEntry> foodEntries;
    private String note;
    private long uId;
    private long totalCal;


    public AddCalViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        this.application = application;
        time = LocalDateTime.now();
        date = LocalDate.now();
        foodEntries = new ArrayList<>();
        uId = Util.getSharedPrefUserId(application.getApplicationContext());
    }

    public void addFoodEntry(AddCalFoodEntry entry)
    {
        foodEntries.add(entry);
        totalCal = totalCal + entry.getTotalCal();
    }

    public void deleteFoodEntry(AddCalFoodEntry entry)
    {
        foodEntries.remove(entry);
        totalCal = totalCal - entry.getTotalCal();
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void commitFoodRec() throws IllegalArgumentException
    {
        if (foodEntries.isEmpty())
            throw new IllegalArgumentException();
        else
            foodEntries.forEach(entry -> appRepository.insertFoodRec(
                    new FoodRecEntity(0, date, time, note, entry.getFoodEntity().getId(), entry.getMass(), uId)));
        Log.d("TimeAddFrag", time.toString());
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getTotalCal() {
        return totalCal;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<AddCalFoodEntry> getFoodEntries() {
        return foodEntries;
    }
}
