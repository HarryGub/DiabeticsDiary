package com.shifuu.diabeticsdiary.mainactivity.home.addnote;

import android.app.Application;

import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.AppRoomDatabase;
import com.shifuu.diabeticsdiary.database.notes.NoteEntity;
import com.shifuu.diabeticsdiary.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AddNoteViewModel extends AndroidViewModel {

    private NoteEntity note;
    private AppRepository appRepository;
    private Application application;

    public AddNoteViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        this.application = application;
    }

    long saveNote(String note)
    {
        return appRepository.insertNote(
                new NoteEntity(
                        0,
                        note,
                        LocalDate.now(),
                        LocalDateTime.now(),
                        Util.getSharedPrefUserId(application)));
    }

    void updateNote(NoteEntity note)
    {
        appRepository.updateNote(note);
    }

    public NoteEntity getNote() {
        return note;
    }

    public void setNote(NoteEntity note) {
        this.note = note;
    }
}
