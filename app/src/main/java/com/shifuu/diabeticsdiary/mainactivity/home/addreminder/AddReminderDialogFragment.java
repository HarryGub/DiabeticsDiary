package com.shifuu.diabeticsdiary.mainactivity.home.addreminder;

import android.Manifest;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.shifuu.diabeticsdiary.databinding.AddReminderDialogBinding;
import com.shifuu.diabeticsdiary.databinding.AddSugarDialogBinding;
import com.shifuu.diabeticsdiary.mainactivity.home.addsugar.AddSugarDialogFragment;
import com.shifuu.diabeticsdiary.mainactivity.home.addsugar.AddSugarViewModel;
import com.shifuu.diabeticsdiary.util.Util;

import java.time.LocalDateTime;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

public class AddReminderDialogFragment extends DialogFragment {

    private AddReminderViewModel viewModel;
    private AddReminderDialogBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = AddReminderDialogBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AddReminderViewModel.class);

        binding.addReminderDialogTimeVal.setText(Util.timeFormatter.format(viewModel.getTime()));

        binding.addReminderDialogTimeVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        binding.addReminderDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.addReminderDialogNote.getText().toString().isEmpty())
                    viewModel.setNote(binding.addReminderDialogNote.getText().toString());
                viewModel.commitReminderRec();
                dismiss();
            }
        });

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            String[] permissions = new String[1];
            permissions[0] = "android.permission.POST_NOTIFICATIONS";

            Log.d("MainActivity", "requestPermissions");

            ActivityCompat.requestPermissions(getActivity(), permissions, 22);
        }

        return binding.getRoot();
    }


    private void showTimePicker()
    {
        AddSugarDialogFragment.TimePickerFragment fragment = new AddSugarDialogFragment.TimePickerFragment(
                viewModel.getTime().getHour(),
                viewModel.getTime().getMinute());
        fragment.setOnTimeSetListener((timePicker, i, i1) -> {
            viewModel.setTime(LocalDateTime.of(2000, 12, 12, i, i1));
            binding.addReminderDialogTimeVal.setText(viewModel.getTime().format(Util.timeFormatter));
            Log.d("DatePicker", LocalDateTime.of(2000, 12, 12, i, i1).toString());
        });

        fragment.show(getChildFragmentManager(), "datePicker");
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

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            listener.onTimeSet(view, hourOfDay, minute);
        }
    }

}
