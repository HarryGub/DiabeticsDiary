package com.shifuu.diabeticsdiary.mainactivity.home.addnote;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shifuu.diabeticsdiary.database.notes.NoteEntity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EditNoteDialogFragment extends AddNoteDialogFragment{

    private NoteEntity note;

    public EditNoteDialogFragment(NoteEntity note)
    {
        this.note = note;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (viewModel.getNote() == null)
            viewModel.setNote(note);
        else
            note = viewModel.getNote();

        binding.addNoteDialogNote.setText(note.getNote());
        binding.addNoteDialogButton.setText("Редактировать");

        binding.addNoteDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = binding.addNoteDialogNote.getText().toString();
                if (s.isEmpty())
                    Toast.makeText(getContext(), "Введите текст заметки", Toast.LENGTH_LONG).show();
                else
                {
                    Log.d("EditNote", note.toString());
                    note.setNote(s);
                    viewModel.updateNote(note);
                    Toast.makeText(getContext(), "Успешно сохранено", Toast.LENGTH_LONG).show();
                    viewModel.setNote(null);
                    dismiss();
                }
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        viewModel.setNote(null);
    }
}
