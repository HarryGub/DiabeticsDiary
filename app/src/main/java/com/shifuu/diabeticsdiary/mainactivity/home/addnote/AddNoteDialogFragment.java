package com.shifuu.diabeticsdiary.mainactivity.home.addnote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shifuu.diabeticsdiary.databinding.AddNoteDialogBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

public class AddNoteDialogFragment extends DialogFragment {

    protected AddNoteDialogBinding binding;
    protected AddNoteViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddNoteDialogBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AddNoteViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addNoteDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = binding.addNoteDialogNote.getText().toString();
                if (s.isEmpty())
                    Toast.makeText(getContext(), "Введите текст заметки", Toast.LENGTH_LONG).show();
                else
                {
                    viewModel.saveNote(s);
                    Toast.makeText(getContext(), "Успешно сохранено", Toast.LENGTH_LONG).show();
                    dismiss();
                }
            }
        });
    }
}
