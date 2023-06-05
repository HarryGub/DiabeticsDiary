package com.shifuu.diabeticsdiary.mainactivity.home.statistics;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.databinding.ChangeGraphDialogBinding;
import com.shifuu.diabeticsdiary.mainactivity.home.sugar.SugarViewModel;
import com.shifuu.diabeticsdiary.util.Util;

import java.time.LocalDate;
import java.util.EventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class ChangeGraphDialogFragment extends DialogFragment {

    private ChangeGraphDialogViewModel viewModel;
    private ChangeGraphDialogBinding binding;

    private LocalDate start;
    private LocalDate end;
    private int graphItemType;

    private Observer<ChangeGraphDialogViewModel> observer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(ChangeGraphDialogViewModel.class);

        if (start != null && end != null && observer != null)
        {
            viewModel.setStart(start);
            viewModel.setEnd(end);
            viewModel.setGraphItemType(graphItemType);
            viewModel.setObserver(observer);
        }

        binding = ChangeGraphDialogBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fragChangeGraphPeriodStartVal.setText(Util.dateFormatter.format(viewModel.getStart()));
        binding.fragChangeGraphPeriodEndVal.setText(Util.dateFormatter.format(viewModel.getEnd()));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.graph_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.fragChangeGraphToggle.setAdapter(adapter);

        switch (viewModel.getGraphItemType()){
            case StatisticsViewModel.CALORIES_ID: binding.fragChangeGraphToggle.setSelection(0); break;
            case StatisticsViewModel.SUGAR_ID: binding.fragChangeGraphToggle.setSelection(1); break;
            case StatisticsViewModel.XE_ID: binding.fragChangeGraphToggle.setSelection(2); break;
        }


        binding.fragChangeGraphToggle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("GraphSelected", adapterView.getItemAtPosition(i).toString());
                switch (i){
                    case 0: viewModel.setGraphItemType(StatisticsViewModel.CALORIES_ID);break;
                    case 1: viewModel.setGraphItemType(StatisticsViewModel.SUGAR_ID);break;
                    case 2: viewModel.setGraphItemType(StatisticsViewModel.XE_ID);break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        binding.fragChangeGraphPeriodStartVal.setOnClickListener(view1 -> showStartDatePicker());

        binding.fragChangeGraphPeriodEndVal.setOnClickListener(view12 -> showEndDatePicker());

        binding.fragChangeGraphButton.setOnClickListener(view13 -> dismiss());

        binding.fragChangeGraphButton.setOnClickListener(view14 -> {
            LocalDate start = viewModel.getStart();
            LocalDate end = viewModel.getEnd();

            if (start.isAfter(end))
            {
                viewModel.setStart(end);
                viewModel.setEnd(start);
            }

            viewModel.getObserver().onChanged(viewModel);
            dismiss();
        });
    }

    public void showWithArgs(FragmentManager manager, String tag, StatisticsViewModel statisticsViewModel, Observer<ChangeGraphDialogViewModel> listener)
    {

        super.show(manager, tag);

        end = statisticsViewModel.getEnd();
        start = statisticsViewModel.getStart();
        graphItemType = statisticsViewModel.getGraphItemType();
        this.observer = listener;
    }

    private void showStartDatePicker() {
        ChangeGraphDialogFragment.DatePickerFragment fragment = new ChangeGraphDialogFragment.DatePickerFragment(
                viewModel.getStart().getYear(),
                viewModel.getStart().getMonthValue() - 1,
                viewModel.getStart().getDayOfMonth());

        fragment.setOnDatePickedListener((datePicker, i, i1, i2) -> {
            viewModel.setStart(LocalDate.of(i, i1 + 1, i2));
            binding.fragChangeGraphPeriodStartVal.setText(viewModel.getStart().format(Util.dateFormatter));
            Log.d("DatePicker", LocalDate.of(i, i1 + 1, i2).toString());
        });

        fragment.show(getChildFragmentManager(), "datePicker");
    }

    private void showEndDatePicker() {
        ChangeGraphDialogFragment.DatePickerFragment fragment = new ChangeGraphDialogFragment.DatePickerFragment(
                viewModel.getEnd().getYear(),
                viewModel.getEnd().getMonthValue() - 1,
                viewModel.getEnd().getDayOfMonth());

        fragment.setOnDatePickedListener((datePicker, i, i1, i2) -> {
            viewModel.setEnd(LocalDate.of(i, i1 + 1, i2));
            binding.fragChangeGraphPeriodEndVal.setText(viewModel.getEnd().format(Util.dateFormatter));
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
