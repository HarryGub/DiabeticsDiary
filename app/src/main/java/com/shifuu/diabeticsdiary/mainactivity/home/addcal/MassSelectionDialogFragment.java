package com.shifuu.diabeticsdiary.mainactivity.home.addcal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.databinding.AddCalDialogBinding;
import com.shifuu.diabeticsdiary.databinding.MassSelectionDialogFragBinding;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MassSelectionDialogFragment extends DialogFragment {

    private MassSelectionDialogFragBinding binding;
    private double mass = 1;

    private FoodEntity food;

    private Observer<Double> o;

    MassSelectionDialogFragment()
    {

    }

    MassSelectionDialogFragment(FoodEntity food)
    {
        this.food = food;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = MassSelectionDialogFragBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.massSelectionDialogFragPortion.setText(food.getPortion());
        binding.massSelectionDialogFragButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.massSelectionDialogFragEditText.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Введите массу", Toast.LENGTH_LONG).show();
                    return;
                }

                mass = Double.parseDouble(binding.massSelectionDialogFragEditText.getText().toString());
                o.onChanged(mass);
                MassSelectionDialogFragment.this.dismissNow();
            }
        });
    }

    public void setObserver(Observer<Double> o)
    {
        this.o = o;
    }
}
