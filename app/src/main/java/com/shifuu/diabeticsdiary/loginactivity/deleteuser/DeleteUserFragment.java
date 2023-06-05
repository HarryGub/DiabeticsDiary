package com.shifuu.diabeticsdiary.loginactivity.deleteuser;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shifuu.diabeticsdiary.database.user.UserEntity;
import com.shifuu.diabeticsdiary.databinding.FragmentDeleteUserBinding;
import com.shifuu.diabeticsdiary.util.Util;

public class DeleteUserFragment extends Fragment implements View.OnClickListener {


    private FragmentDeleteUserBinding binding;
    DeleteUserViewModel viewModel;

    public static DeleteUserFragment newInstance() {
        return new DeleteUserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDeleteUserBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DeleteUserViewModel.class);
        long uId = DeleteUserFragmentArgs.fromBundle(getArguments()).getUserId();

        if (viewModel.getAlertDialog() != null)
            viewModel.getAlertDialog().show();

        viewModel.retrieveUser(uId).observe(this.getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity user) {

                viewModel.updateUser(user);

                binding.fragDeleteAvatar.setImageBitmap(BitmapFactory.decodeByteArray(
                        user.getAvatar(),
                        0,
                        user.getAvatar().length,
                        new BitmapFactory.Options()));
                binding.fragDeleteName.setText(user.getName());

                binding.fragmentUserDeleteButtonDelete.setOnClickListener(DeleteUserFragment.this);
            }
        });

    }
    @Override
    public void onClick(View view) {

        if (binding.fragDeleteEditTextPasswd.getText().toString().equals(viewModel.getUser().getPasswd()))
        {
            NavController navController = Navigation.findNavController(view);
            Util.hideKeyboardFrom(getContext(), this.getView());
            new UserDeletionInsuranceDialog(navController, viewModel).showAlertDialog(view, getLayoutInflater());
        }
        else
        {
            binding.fragDeleteWrongPasswd.setVisibility(View.VISIBLE);
        }
    }


}