package com.shifuu.diabeticsdiary.mainactivity.home.notifications;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.database.reminder.ReminderEntity;
import com.shifuu.diabeticsdiary.databinding.FragmentNotificationsBinding;
import com.shifuu.diabeticsdiary.util.NotificationRegistry;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel viewModel;
    private FragmentNotificationsBinding binding;
    private NotificationListRecyclerAdapter adapter;

    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        adapter = new NotificationListRecyclerAdapter(viewModel);
        binding.fragNotificationsRecyclerView.setAdapter(adapter);
        binding.fragNotificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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

        viewModel.getReminderEntitiesByUser().observe(getViewLifecycleOwner(), new Observer<List<ReminderEntity>>() {
            @Override
            public void onChanged(List<ReminderEntity> reminderEntities) {
                Log.d("NotificationsFrag", "dataSetChanged");
                reminderEntities.forEach(new Consumer<ReminderEntity>() {
                    @Override
                    public void accept(ReminderEntity reminderEntity) {
                        Log.d("NotificationsFrag", reminderEntity.getNote());
                    }
                });
                adapter.submitList(reminderEntities);
            }
        });

        return binding.getRoot();
    }
}