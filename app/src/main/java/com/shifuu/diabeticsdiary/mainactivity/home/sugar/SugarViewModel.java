package com.shifuu.diabeticsdiary.mainactivity.home.sugar;

import android.app.Application;

import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.food_rec.FoodRecEntity;
import com.shifuu.diabeticsdiary.database.sugar_rec.SugarRecEntity;
import com.shifuu.diabeticsdiary.util.Util;

import java.time.LocalDate;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class SugarViewModel extends AndroidViewModel {

    Application application;
    AppRepository appRepository;

    private LocalDate date;
    private long uId;

    private LiveData<List<SugarRecEntity>> currentLiveData;

    public SugarViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        this.application = application;
        uId = Util.getSharedPrefUserId(application.getApplicationContext());
        date = LocalDate.now();
    }

    private LiveData<List<SugarRecEntity>> getSugarRecEntitiesByCurrentDateByCurrentUser()
    {
        return appRepository.getSugarRecsByDateByUser(date, uId);
    }

    public void updateLiveDataWithObserver(LifecycleOwner owner, Observer<List<SugarRecEntity>> observer)
    {
        if (currentLiveData != null)
            currentLiveData.removeObservers(owner);
        currentLiveData = getSugarRecEntitiesByCurrentDateByCurrentUser();
        currentLiveData.observe(owner, observer);
    }

    public void lessenDate() {
        date = date.minusDays(1);
    }

    public void greaterDate() {
        date = date.plusDays(1);
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public LocalDate getDate() {
        return date;
    }
    public void deleteSugarRec(SugarRecEntity sugarRecEntity) {
        appRepository.deleteSugarRec(sugarRecEntity);
    }
}