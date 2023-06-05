package com.shifuu.diabeticsdiary.mainactivity.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.mainactivity.foodsbase.FoodsbaseViewModel;
import com.shifuu.diabeticsdiary.mainactivity.home.addcal.AddCalDialogFragment;
import com.shifuu.diabeticsdiary.mainactivity.home.addreminder.AddReminderDialogFragment;
import com.shifuu.diabeticsdiary.mainactivity.home.addsugar.AddSugarDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

public class HomeFragmentAdditionSelector extends DialogFragment {

    //TODO deletion?

    public HomeFragmentAdditionSelector()
    {

    }

    public static HomeFragmentAdditionSelector newInstance()
    {
        return new HomeFragmentAdditionSelector();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        final View layout = getLayoutInflater().inflate(R.layout.dialog_home_selector, null);
        builder.setView(layout);
        AlertDialog dialog = builder.create();
        WindowManager.LayoutParams wm = dialog.getWindow().getAttributes();
        wm.gravity = Gravity.BOTTOM;
        wm.y = 400;

        layout.findViewById(R.id.home_selector_dialog_calories_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddCalDialogFragment().show(getActivity().getSupportFragmentManager(), "AddCalDialog");
                HomeFragmentAdditionSelector.this.dismiss();
            }
        });
        layout.findViewById(R.id.home_selector_dialog_sugar_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddSugarDialogFragment().show(getActivity().getSupportFragmentManager(), "AddCalDialog");
                HomeFragmentAdditionSelector.this.dismiss();
            }
        });

        layout.findViewById(R.id.home_selector_dialog_notification_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddReminderDialogFragment().show(getActivity().getSupportFragmentManager(), "AddReminderDialog");
                HomeFragmentAdditionSelector.this.dismiss();
            }
        });

        return dialog;
    }
}
