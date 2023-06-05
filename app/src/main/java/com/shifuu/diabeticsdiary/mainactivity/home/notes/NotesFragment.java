package com.shifuu.diabeticsdiary.mainactivity.home.notes;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.database.notes.NoteEntity;
import com.shifuu.diabeticsdiary.database.sugar_rec.SugarRecEntity;
import com.shifuu.diabeticsdiary.databinding.FragmentNotesBinding;
import com.shifuu.diabeticsdiary.mainactivity.home.calories.CaloriesFragment;
import com.shifuu.diabeticsdiary.mainactivity.home.calories.CaloriesListRecyclerAdapter;
import com.shifuu.diabeticsdiary.util.Util;

import java.time.LocalDate;
import java.util.List;

public class NotesFragment extends Fragment {

    private NotesViewModel viewModel;
    private FragmentNotesBinding binding;
    private NotesListRecyclerAdapter adapter;

    private final Observer<List<NoteEntity>> finalObserver = new Observer<List<NoteEntity>>() {
        @Override
        public void onChanged(List<NoteEntity> noteEntities) {
            adapter.submitList(noteEntities);
        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNotesBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        adapter = new NotesListRecyclerAdapter(viewModel, getActivity().getSupportFragmentManager());


        RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);


        binding.fragNotesRecyclerAdapter.setLayoutManager(manager);
        binding.fragNotesRecyclerAdapter.setAdapter(adapter);


        updateListByViewModelDate();


        binding.fragNotesTextDate.setText(viewModel.getDate().format(Util.dateFormatter));

        binding.fragNotesLeftIcon.setOnClickListener(view1 -> {
            viewModel.lessenDate();
            updateListByViewModelDate();
            binding.fragNotesTextDate.setText(viewModel.getDate().format(Util.dateFormatter));
        });
        binding.fragNotesRightIcon.setOnClickListener(view1 -> {
            viewModel.greaterDate();
            updateListByViewModelDate();
            binding.fragNotesTextDate.setText(viewModel.getDate().format(Util.dateFormatter));
        });
        binding.fragNotesTextDate.setOnClickListener(view1 -> showDatePicker());

        return binding.getRoot();
    }


    private void updateListByViewModelDate()
    {

        viewModel.updateLiveDataWithObserver(getViewLifecycleOwner(), finalObserver);

    }

    private void showDatePicker() {
        NotesFragment.DatePickerFragment fragment = new NotesFragment.DatePickerFragment(
                viewModel.getDate().getYear(),
                viewModel.getDate().getMonthValue() + 1,
                viewModel.getDate().getDayOfMonth());

        fragment.setOnDatePickedListener((datePicker, i, i1, i2) -> {
            viewModel.setDate(LocalDate.of(i, i1 - 1, i2));
            binding.fragNotesTextDate.setText(viewModel.getDate().format(Util.dateFormatter));
            Log.d("DatePicker", LocalDate.of(i, i1 + 1, i2).toString());
        });

        fragment.show(getChildFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        DatePickerDialog.OnDateSetListener listener;
        int year;
        int month;
        int day;

        public DatePickerFragment(int year, int month, int day)
        {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }

        public void setOnDatePickedListener(DatePickerDialog.OnDateSetListener listener)
        {
            this.listener = listener;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            listener.onDateSet(view, year, month, day);
        }
    }

}