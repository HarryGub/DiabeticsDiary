package com.shifuu.diabeticsdiary.mainactivity.home.addsugar;

import android.app.Application;

import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.sugar_rec.SugarRecEntity;
import com.shifuu.diabeticsdiary.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AddSugarViewModel extends AndroidViewModel {

    private LocalDateTime time;
    private LocalDate date;

    private String note;
    private long uId;

    private double value;
    private boolean isBefore = true;


    AppRepository appRepository;

    public AddSugarViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        uId = Util.getSharedPrefUserId(application);
        date = LocalDate.now();
        time = LocalDateTime.now();

    }

    public void commitSugarRec()
    {
        appRepository.insertSugarRec(new SugarRecEntity(0, date, time, note, value, isBefore, uId));
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getuId() {
        return uId;
    }

    public double getValue() {
        return value;
    }

    public void setuId(long uId) {
        this.uId = uId;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setBefore(boolean before) {
        isBefore = before;
    }
}
