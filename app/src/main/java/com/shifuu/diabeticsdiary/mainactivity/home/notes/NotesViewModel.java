package com.shifuu.diabeticsdiary.mainactivity.home.notes;

import android.app.Application;

import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.notes.NoteEntity;
import com.shifuu.diabeticsdiary.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class NotesViewModel extends AndroidViewModel {

    private AppRepository appRepository;
    private LocalDate date;
    private Application application;
    private LiveData<List<NoteEntity>> currentLiveData;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        date = LocalDate.now();
        this.application = application;
    }

    public void deleteNote(NoteEntity note) {
        appRepository.deleteNote(note);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void lessenDate() {
        date = date.minusDays(1);
    }

    public void greaterDate() {
        date = date.plusDays(1);
    }

    public LiveData<List<NoteEntity>> getNotesListByCurrentDate() {
        return appRepository.getUsersNotesByDate(Util.getSharedPrefUserId(application), date);
    }

    public void updateLiveDataWithObserver(LifecycleOwner owner, Observer<List<NoteEntity>> finalObserver) {
        if (currentLiveData != null)
            currentLiveData.removeObservers(owner);
        currentLiveData = getNotesListByCurrentDate();
        currentLiveData.observe(owner, finalObserver);
    }
}