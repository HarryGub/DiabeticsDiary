package com.shifuu.diabeticsdiary.mainactivity.home.addsugar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.shifuu.diabeticsdiary.databinding.AddCalDialogBinding;
import com.shifuu.diabeticsdiary.databinding.AddSugarDialogBinding;
import com.shifuu.diabeticsdiary.mainactivity.home.addcal.AddCalDialogFragment;
import com.shifuu.diabeticsdiary.mainactivity.home.addcal.AddCalRecyclerAdapter;
import com.shifuu.diabeticsdiary.mainactivity.home.addcal.AddCalViewModel;
import com.shifuu.diabeticsdiary.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

public class AddSugarDialogFragment extends DialogFragment {

    private AddSugarViewModel viewModel;
    private AddSugarDialogBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(AddSugarViewModel.class);

        binding = AddSugarDialogBinding.inflate(inflater, container, false);

        binding.addCalSugarDialogDateVal.setText(Util.dateFormatter.format(viewModel.getDate()));
        binding.addCalSugarDialogTimeVal.setText(Util.timeFormatter.format(viewModel.getTime()));

        binding.addCalSugarDialogDateVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        binding.addCalSugarDialogTimeVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        binding.addCalSugarDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.addCalSugarDialogValVal.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Введите значение калорий", Toast.LENGTH_LONG).show();
                    return;
                }
                viewModel.setValue(Double.parseDouble(binding.addCalSugarDialogValVal.getText().toString()));
                viewModel.setNote(binding.addCalSugarDialogNote.getText().toString());
                viewModel.setBefore(binding.addCalSugarDialogIsBeforeSwitch.isChecked());
                viewModel.commitSugarRec();
                dismiss();
            }
        });

        return binding.getRoot();
    }





    private void showDatePicker() {
        AddSugarDialogFragment.DatePickerFragment fragment = new AddSugarDialogFragment.DatePickerFragment(
                viewModel.getDate().getYear(),
                viewModel.getDate().getMonthValue() - 1,
                viewModel.getDate().getDayOfMonth());

        fragment.setOnDatePickedListener((datePicker, i, i1, i2) -> {
            viewModel.setDate(LocalDate.of(i, i1 + 1, i2));
            viewModel.setTime(viewModel.getDate().atTime(viewModel.getTime().toLocalTime()));
            binding.addCalSugarDialogDateVal.setText(viewModel.getDate().format(Util.dateFormatter));
            Log.d("DatePicker", LocalDate.of(i, i1 + 1, i2).toString());
        });

        fragment.show(getChildFragmentManager(), "datePicker");
    }

    private void showTimePicker()
    {
        AddSugarDialogFragment.TimePickerFragment fragment = new AddSugarDialogFragment.TimePickerFragment(
                viewModel.getTime().getHour(),
                viewModel.getTime().getMinute());
        fragment.setOnTimeSetListener((timePicker, i, i1) -> {
            LocalDateTime dateTime = LocalDateTime.now();
            viewModel.setTime(LocalDateTime.of(viewModel.getDate().getYear(), viewModel.getDate().getMonthValue(), viewModel.getDate().getDayOfMonth(), i, i1));
            binding.addCalSugarDialogTimeVal.setText(viewModel.getTime().format(Util.timeFormatter));
            Log.d("DatePicker", LocalDateTime.of(viewModel.getDate().getYear(), viewModel.getDate().getMonthValue(), viewModel.getDate().getDayOfMonth(), i, i1).toString());
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

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        int hour;
        int minute;
        TimePickerDialog.OnTimeSetListener listener;

        public TimePickerFragment(int hour, int minute)
        {
            this.hour = hour;
            this.minute = minute;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LocalDateTime now = LocalDateTime.now();

            return new TimePickerDialog(
                    requireContext(),
                    this,
                    now.getHour(),
                    now.getMinute(),
                    true);
        }

        public void setOnTimeSetListener(TimePickerDialog.OnTimeSetListener listener)
        {
            this.listener = listener;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            listener.onTimeSet(view, hourOfDay, minute);
        }
    }
}
