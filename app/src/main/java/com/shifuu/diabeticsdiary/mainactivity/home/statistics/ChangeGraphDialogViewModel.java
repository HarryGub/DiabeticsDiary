package com.shifuu.diabeticsdiary.mainactivity.home.statistics;

import android.app.Application;

import java.time.LocalDate;
import java.util.EventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;

public class ChangeGraphDialogViewModel extends AndroidViewModel{

    private LocalDate start;
    private LocalDate end;
    private int graphItemType;
    private Observer<ChangeGraphDialogViewModel> observer;

    public ChangeGraphDialogViewModel(@NonNull Application application) {
        super(application);
    }


    public void setStart(LocalDate start) {
        this.start = start;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public void setGraphItemType(int graphItemType) {
        this.graphItemType = graphItemType;
    }



    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public int getGraphItemType() {
        return graphItemType;
    }

    public Observer<ChangeGraphDialogViewModel> getObserver() {
        return observer;
    }

    public void setObserver(Observer<ChangeGraphDialogViewModel> observer) {
        this.observer = observer;
    }
}
