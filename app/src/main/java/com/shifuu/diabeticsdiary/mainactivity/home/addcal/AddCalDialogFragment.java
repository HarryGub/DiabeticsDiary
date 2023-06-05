package com.shifuu.diabeticsdiary.mainactivity.home.addcal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.databinding.AddCalDialogBinding;
import com.shifuu.diabeticsdiary.mainactivity.home.calories.CaloriesFragment;
import com.shifuu.diabeticsdiary.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

public class AddCalDialogFragment extends DialogFragment {

    public static String TAG = "AddCalDialog";
    private AddCalViewModel viewModel;
    private AddCalDialogBinding binding;
    private AddCalRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(AddCalViewModel.class);

        binding = AddCalDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new AddCalRecyclerAdapter(viewModel);
        binding.fragAddCalScrollView.setAdapter(adapter);
        binding.fragAddCalScrollView.setLayoutManager(new LinearLayoutManager(getContext()));

        Log.d(TAG, "onViewCreated");

        binding.addCalDialogFragmentTimeValue.setText(viewModel.getTime().format(Util.timeFormatter));
        binding.addCalDialogFragmentDateValue.setText(viewModel.getDate().format(Util.dateFormatter));
        binding.addCalDialogFragmentTotalValue.setText(viewModel.getTotalCal() + " ккал");

        binding.addCalDialogFragmentDateValue.setOnClickListener(view1 -> showDatePicker());
        binding.addCalDialogFragmentTimeValue.setOnClickListener(view1 -> showTimePicker());

        adapter.submitList(viewModel.getFoodEntries());

        binding.fragAddCalButtonAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodsbaseFoodSelectionFragment fragment = new FoodsbaseFoodSelectionFragment();
                fragment.setObserver(new Observer<AddCalFoodEntry>() {
                    @Override
                    public void onChanged(AddCalFoodEntry entry) {
                        viewModel.addFoodEntry(entry);

                        binding.addCalDialogFragmentTotalValue.setText(viewModel.getTotalCal() + " ккал");
                        adapter.submitList(viewModel.getFoodEntries());
//                        viewModel.getFoodEntries().forEach(new Consumer<AddCalFoodEntry>() {
//                            @Override
//                            public void accept(AddCalFoodEntry entry) {
//                                Log.d("AddCalDialogFrag", entry.getFoodEntity().getName() + " " +
//                                        entry.getMass() + " " +
//                                        entry.getTotalCal());
//                                adapter.submitList(viewModel.getFoodEntries());
//                            }
//                        });
                    }
                });

                FragmentManager fragmentManager = AddCalDialogFragment.this.getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setReorderingAllowed(true);
                fragmentTransaction.addToBackStack("AddCalDialogFragment");
                fragmentTransaction.replace(R.id.add_cal_dialog_root, fragment, null);
                fragmentTransaction.commit();

            }
        });

        binding.fragAddCalButtonAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    viewModel.setNote(binding.addCalDialogFragNote.getText().toString());
                    viewModel.commitFoodRec();
                    Toast.makeText(getContext(), "Добавлено успешно", Toast.LENGTH_LONG).show();

                    AddCalDialogFragment.this.dismiss();
                }
                catch (IllegalArgumentException e)
                {
                    Toast.makeText(requireContext(), "Требуется хотя бы один продукт", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
        this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("AddCalFrag", "OnKeyEvent");
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK &&
                        keyEvent.getAction() == KeyEvent.ACTION_UP &&
                        getChildFragmentManager().getBackStackEntryCount() != 0) {

                    Log.d("AddCalFrag", "KEY_BACK");
                    getChildFragmentManager().popBackStack();
                    return true;

                }
                return false;
            }
        });

    }


    private void showDatePicker() {
        AddCalDialogFragment.DatePickerFragment fragment = new AddCalDialogFragment.DatePickerFragment(
                viewModel.getDate().getYear(),
                viewModel.getDate().getMonthValue() - 1,
                viewModel.getDate().getDayOfMonth());

        fragment.setOnDatePickedListener((datePicker, i, i1, i2) -> {
            viewModel.setDate(LocalDate.of(i, i1 + 1, i2));
            binding.addCalDialogFragmentDateValue.setText(viewModel.getDate().format(Util.dateFormatter));
            Log.d("DatePicker", LocalDate.of(i, i1 + 1, i2).toString());
        });

        fragment.show(getChildFragmentManager(), "datePicker");
    }

    private void showTimePicker()
    {
        AddCalDialogFragment.TimePickerFragment fragment = new AddCalDialogFragment.TimePickerFragment(
                viewModel.getTime().getHour(),
                viewModel.getTime().getMinute());
        fragment.setOnTimeSetListener((timePicker, i, i1) -> {
            viewModel.setTime(LocalDateTime.of(2000, 12, 12, i, i1));
            binding.addCalDialogFragmentTimeValue.setText(viewModel.getTime().format(Util.timeFormatter));
            Log.d("DatePicker", LocalDateTime.of(2000, 12, 12, i, i1).toString());
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
