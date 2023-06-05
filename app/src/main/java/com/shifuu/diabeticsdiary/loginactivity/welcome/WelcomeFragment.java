package com.shifuu.diabeticsdiary.loginactivity.welcome;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.databinding.FragmentHomeBinding;
import com.shifuu.diabeticsdiary.databinding.FragmentWelcomeBinding;

public class WelcomeFragment extends Fragment {

    private WelcomeViewModel mViewModel;

    FragmentWelcomeBinding binding;

    public static WelcomeFragment newInstance() {
        return new WelcomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentWelcomeBinding.inflate(inflater, container, false);



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setButtonOnClickListener(view);
    }

    private void setButtonOnClickListener(View view) {

        NavController navController =
                Navigation.findNavController(view);

        binding.fragmentWelcomeButtonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_fragment_welcome_to_loginFragment);
            }
        });

        binding.welcomeFragButtonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_fragment_welcome_to_registrationFragment);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WelcomeViewModel.class);
        // TODO: Use the ViewModel
    }

}