package com.shifuu.diabeticsdiary.loginactivity.passwordlogin;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.database.user.UserEntity;
import com.shifuu.diabeticsdiary.databinding.FragmentPasswordLoginBinding;
import com.shifuu.diabeticsdiary.loginactivity.deleteuser.DeleteUserFragment;
import com.shifuu.diabeticsdiary.loginactivity.deleteuser.DeleteUserFragmentArgs;
import com.shifuu.diabeticsdiary.loginactivity.deleteuser.DeleteUserViewModel;
import com.shifuu.diabeticsdiary.loginactivity.deleteuser.UserDeletionInsuranceDialog;
import com.shifuu.diabeticsdiary.mainactivity.MainActivity;
import com.shifuu.diabeticsdiary.util.Util;

public class PasswordLoginFragment extends Fragment implements View.OnClickListener {

    private PasswordLoginViewModel viewModel;

    private FragmentPasswordLoginBinding binding;

    public static PasswordLoginFragment newInstance() {
        return new PasswordLoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPasswordLoginBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PasswordLoginViewModel.class);
        long uId = PasswordLoginFragmentArgs.fromBundle(getArguments()).getUserId();

        Log.d("PasswdLogin", String.valueOf(uId));
        viewModel.retrieveUser(uId).observe(this.getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity user) {

                viewModel.updateUser(user);

                binding.fragPasswdLoginAvatar.setImageBitmap(BitmapFactory.decodeByteArray(
                        user.getAvatar(),
                        0,
                        user.getAvatar().length,
                        new BitmapFactory.Options()));

                binding.fragPasswdLoginName.setText(user.getName());

                binding.fragmentPasswdLoginButtonEnter.setOnClickListener(PasswordLoginFragment.this);
            }
        });





        setOnClickListeners(view);
    }

    private void setOnClickListeners(View view)
    {
        binding.fragmentPasswdLoginButtonEnter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (binding.fragPasswdLoginEditTextPasswd.getText().toString().equals(viewModel.getUser().getPasswd()))
        {
            Util.hideKeyboardFrom(getContext(), this.getView());

            SharedPreferences sp = getContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putLong("uId", viewModel.getUser().getId());
            editor.commit();
            viewModel.registerUsersReminders();

            Intent i = new Intent(getContext(), MainActivity.class);
            getActivity().startActivity(i);
            getActivity().finish();
        }
        else
        {
            binding.fragPasswdLoginWrongPasswd.setVisibility(View.VISIBLE);
        }
    }
}