package com.shifuu.diabeticsdiary.mainactivity.home.calories;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.location.GnssAntennaInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.database.food_rec.CollapsedFoodRec;
import com.shifuu.diabeticsdiary.database.food_rec.FoodRecEntity;
import com.shifuu.diabeticsdiary.database.sugar_rec.SugarRecEntity;
import com.shifuu.diabeticsdiary.databinding.FragmentCaloriesBinding;
import com.shifuu.diabeticsdiary.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

public class CaloriesFragment extends Fragment {

    private CaloriesViewModel viewModel;
    private FragmentCaloriesBinding binding;
    private CaloriesListRecyclerAdapter adapter;

    private final Observer<List<FoodRecEntity>> finalObserver = new Observer<List<FoodRecEntity>>() {
        @Override
        public void onChanged(List<FoodRecEntity> foodRecEntities) {
            adapter.submitList(foodRecEntities);

            long[] totalCal = new long[1];

            foodRecEntities.forEach(new Consumer<FoodRecEntity>() {
                @Override
                public void accept(FoodRecEntity foodRecEntity) {

                    if (foodRecEntity.equals(CaloriesListRecyclerAdapter.blankFoodRec))
                        return;
                    viewModel.getFoodEntityById(foodRecEntity.getFoodId()).observe(getViewLifecycleOwner(), new Observer<FoodEntity>() {
                        @Override
                        public void onChanged(FoodEntity food) {
                            totalCal[0] = totalCal[0] + Math.round(food.getKal() * foodRecEntity.getMass());
                            binding.fragCalTodaysCal.setText("За весь день: " + totalCal[0] + " ккал");
                        }
                    });

                }
            });
        }
    };


    public static CaloriesFragment newInstance() {
        return new CaloriesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCaloriesBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(this).get(CaloriesViewModel.class);

        adapter = new CaloriesListRecyclerAdapter(viewModel);


        RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);


        binding.fragCaloriesRecyclerAdapter.setLayoutManager(manager);
        binding.fragCaloriesRecyclerAdapter.setAdapter(adapter);


        updateListByViewModelDate();

        binding.fragCalTextDate.setText(viewModel.getDate().format(Util.dateFormatter));

        binding.fragCalLeftIcon.setOnClickListener(view -> {
            viewModel.lessenDate();
            updateListByViewModelDate();
            binding.fragCalTextDate.setText(viewModel.getDate().format(Util.dateFormatter));
        });
        binding.fragCalRightIcon.setOnClickListener(view -> {
            viewModel.greaterDate();
            updateListByViewModelDate();
            binding.fragCalTextDate.setText(viewModel.getDate().format(Util.dateFormatter));
        });
        binding.fragCalTextDate.setOnClickListener(view -> showDatePicker());

        return binding.getRoot();
    }

    private void updateListByViewModelDate()
    {
        binding.fragCalTodaysCal.setText("За весь день: " + 0 + " ккал");
        viewModel.updateLiveDataWithObserver(getViewLifecycleOwner(), finalObserver);
    }

    private void showDatePicker() {
        DatePickerFragment fragment = new DatePickerFragment(
                viewModel.getDate().getYear(),
                viewModel.getDate().getMonthValue() + 1,
                viewModel.getDate().getDayOfMonth());

        fragment.setOnDatePickedListener((datePicker, i, i1, i2) -> {
            viewModel.setDate(LocalDate.of(i, i1 - 1, i2));
            updateListByViewModelDate();
            binding.fragCalTextDate.setText(viewModel.getDate().format(Util.dateFormatter));
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