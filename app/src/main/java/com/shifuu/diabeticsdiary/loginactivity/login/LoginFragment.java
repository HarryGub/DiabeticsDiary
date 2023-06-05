package com.shifuu.diabeticsdiary.loginactivity.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.database.user.UserEntity;
import com.shifuu.diabeticsdiary.databinding.FragmentLoginBinding;

import java.util.List;

public class LoginFragment extends Fragment {

    private LoginViewModel viewModel;

    private FragmentLoginBinding binding;
    private UserListRecyclerAdapter adapter;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        adapter = new UserListRecyclerAdapter();
        binding.loginFragmentRecycleView.setAdapter(adapter);
        binding.loginFragmentRecycleView.setLayoutManager(
                new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));

        viewModel.getAllUserEntities().observe(this.getViewLifecycleOwner(), new Observer<List<UserEntity>>() {
            @Override
            public void onChanged(List<UserEntity> userEntities) {
                adapter.submitList(userEntities);
            }
        });

//        binding.loginFragButtonReg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registrationFragment);
//            }
//        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}