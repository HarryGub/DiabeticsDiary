package com.shifuu.diabeticsdiary.mainactivity.home.sugar;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.shifuu.diabeticsdiary.database.sugar_rec.SugarRecEntity;
import com.shifuu.diabeticsdiary.databinding.FragmentSugarBinding;
import com.shifuu.diabeticsdiary.mainactivity.home.calories.CaloriesFragment;
import com.shifuu.diabeticsdiary.util.Util;

import java.time.LocalDate;
import java.util.List;

public class SugarFragment extends Fragment {

    private SugarViewModel viewModel;
    private FragmentSugarBinding binding;
    private SugarRecListRecyclerAdapter adapter;
    private final Observer<List<SugarRecEntity>> finalObserver = new Observer<List<SugarRecEntity>>() {
        @Override
        public void onChanged(List<SugarRecEntity> sugarRecEntities) {
            adapter.submitList(sugarRecEntities);
        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSugarBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(SugarViewModel.class);

        adapter = new SugarRecListRecyclerAdapter(viewModel);
        binding.fragSugarRecyclerAdapter.setAdapter(adapter);
        binding.fragSugarRecyclerAdapter.setLayoutManager(new LinearLayoutManager(getContext()));

        setCurrentDateSugarRecsObserver();

        binding.fragSugarTextDate.setText(viewModel.getDate().format(Util.dateFormatter));

        binding.fragSugarLeftIcon.setOnClickListener(view -> {
            viewModel.lessenDate();
            setCurrentDateSugarRecsObserver();
            binding.fragSugarTextDate.setText(viewModel.getDate().format(Util.dateFormatter));
        });
        binding.fragSugarRightIcon.setOnClickListener(view -> {
            viewModel.greaterDate();
            setCurrentDateSugarRecsObserver();
            binding.fragSugarTextDate.setText(viewModel.getDate().format(Util.dateFormatter));
        });
        binding.fragSugarTextDate.setOnClickListener(view -> showDatePicker());


        return binding.getRoot();
    }


    private void setCurrentDateSugarRecsObserver() {
        viewModel.updateLiveDataWithObserver(getViewLifecycleOwner(), finalObserver);
    }


    private void showDatePicker() {
        CaloriesFragment.DatePickerFragment fragment = new CaloriesFragment.DatePickerFragment(
                viewModel.getDate().getYear(),
                viewModel.getDate().getMonthValue() - 1,
                viewModel.getDate().getDayOfMonth());

        fragment.setOnDatePickedListener((datePicker, i, i1, i2) -> {

            viewModel.setDate(LocalDate.of(i, i1 + 1, i2));
            setCurrentDateSugarRecsObserver();

            binding.fragSugarTextDate.setText(viewModel.getDate().format(Util.dateFormatter));
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