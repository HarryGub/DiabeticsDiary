package com.shifuu.diabeticsdiary.mainactivity.home.calories;

import android.app.Application;
import android.util.Log;

import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.database.food_rec.CollapsedFoodRec;
import com.shifuu.diabeticsdiary.database.food_rec.FoodRecEntity;
import com.shifuu.diabeticsdiary.database.sugar_rec.SugarRecEntity;
import com.shifuu.diabeticsdiary.util.Util;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class CaloriesViewModel extends AndroidViewModel {

    private AppRepository appRepository;
    private LocalDate date;
    private long uId;

    private LiveData<List<FoodRecEntity>> currentLiveData;

    public CaloriesViewModel(@NonNull Application application) {
        super(application);
        date = LocalDate.now();
        Log.d("CaloriesViewModel", date.toString());
        appRepository = new AppRepository(application);
        uId = Util.getSharedPrefUserId(application.getApplicationContext());
    }

    private LiveData<List<FoodRecEntity>> getCalRecEntitiesByCurrentDateByCurrentUser()
    {
        return appRepository.getFoodRecEntitiesByDateSortedByTime(date, uId);
    }

    public void updateLiveDataWithObserver(LifecycleOwner owner, Observer<List<FoodRecEntity>> observer)
    {
        if (currentLiveData != null)
            currentLiveData.removeObservers(owner);
        currentLiveData = getCalRecEntitiesByCurrentDateByCurrentUser();
        currentLiveData.observe(owner, observer);
    }

    public void deleteFoodRecById(long id) {
        appRepository.deleteFoodRecById(id);
    }

    public LiveData<FoodEntity> getFoodDataById(long id) {

        return appRepository.getFoodById(id);
    }

    public LiveData<List<FoodRecEntity>> getFoodRecEntitiesByCurrentDateByCurrentUser()
    {
        return appRepository.getFoodRecEntitiesByDateSortedByTime(date, uId);
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public LocalDate getDate() {
        return date;
    }

    public void lessenDate() {
        date = date.minusDays(1);
    }

    public void greaterDate() {
        date = date.plusDays(1);
    }

    public LiveData<FoodEntity> getFoodEntityById(long foodId) {
        return appRepository.getFoodById(foodId);
    }
}























