package com.shifuu.diabeticsdiary.mainactivity.home.statistics;

import android.app.Application;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.database.food_rec.FoodRecEntity;
import com.shifuu.diabeticsdiary.database.sugar_rec.SugarRecEntity;
import com.shifuu.diabeticsdiary.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class StatisticsViewModel extends AndroidViewModel {

    static final int CALORIES_ID = 1234356321;
    static final int SUGAR_ID = 903451234;
    static final int XE_ID = 30893456;

    private AppRepository appRepository;
    private Application application;
    private long uId;

    private LocalDate start;
    private LocalDate end;
    private int graphItemType;


    public StatisticsViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        this.application = application;
        uId = Util.getSharedPrefUserId(application);
        start = LocalDate.now().minusDays(7);
        // End day not included
        end = LocalDate.now().plusDays(1);
        graphItemType = CALORIES_ID;
    }

    public int getCurrentPeriodInDays()
    {
        return end.getDayOfYear() - start.getDayOfYear();
    }

    public List<LiveData<List<SugarRecEntity>>> getSugarRecsForCurrentViewModelPeriod()
    {
        ArrayList<LiveData<List<SugarRecEntity>>> list = new ArrayList<>();

        for (LocalDate i = start; i.isBefore(end) ; i = i.plusDays(1)) {
            Log.d("StatViewModel", "enteredLoop");
            list.add(appRepository.getSugarRecsByDateByUser(i, uId));
        }

        return list;
    }

    public List<LiveData<List<FoodRecEntity>>> getFoodRecsForCurrentViewModelPeriod()
    {
        ArrayList<LiveData<List<FoodRecEntity>>> list = new ArrayList<>();

        for (LocalDate i = start; i.isBefore(end) ; i = i.plusDays(1)) {
            Log.d("StatViewModel", "enteredLoop");
            list.add(appRepository.getFoodRecEntitiesByDateSortedByTime(i, uId));
        }

        return list;
    }

    public LiveData<FoodEntity> getFoodEntityById(long id)
    {
        return appRepository.getFoodById(id);
    }

    public int getGraphItemType() {
        return graphItemType;
    }

    public void setGraphItemType(int graphItemType) {
        if (graphItemType != SUGAR_ID && graphItemType != CALORIES_ID && graphItemType != XE_ID)
            return;
        this.graphItemType = graphItemType;
    }


    public void setStart(LocalDate start) {
        this.start = start;
    }

    public void setEnd(LocalDate end) {
        this.end = end.plusDays(1);
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end.minusDays(1);
    }
}