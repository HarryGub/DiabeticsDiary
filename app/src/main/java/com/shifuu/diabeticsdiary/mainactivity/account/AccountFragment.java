package com.shifuu.diabeticsdiary.mainactivity.account;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.database.AppRoomDatabase;
import com.shifuu.diabeticsdiary.database.user.UserEntity;
import com.shifuu.diabeticsdiary.databinding.FragmentAccountBinding;
import com.shifuu.diabeticsdiary.loginactivity.LoginActivity;
import com.shifuu.diabeticsdiary.loginactivity.deleteuser.DeleteUserFragment;
import com.shifuu.diabeticsdiary.loginactivity.deleteuser.UserDeletionInsuranceDialog;
import com.shifuu.diabeticsdiary.loginactivity.registration.RegistrationFragment;
import com.shifuu.diabeticsdiary.mainactivity.MainActivity;
import com.shifuu.diabeticsdiary.util.NotificationRegistry;
import com.shifuu.diabeticsdiary.util.Util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static android.content.Context.MODE_PRIVATE;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private FragmentAccountBinding binding;
    private AccountViewModel viewModel;
    private static final int PICK_IMAGE_FILE = 2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Util.runAsyncTaskThread(() -> binding.accountFragmentLoadAvatarImage.
                setImageBitmap(Util.getBitmapFromByteArray(viewModel.getImage())));


        long uId = Util.getSharedPrefUserId(this.getContext());


        viewModel.retrieveUser(uId).observe(this.getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity user) {

                Log.d("AccountFrag", user.toString());

                viewModel.updateUser(user);

                binding.fragAccountAvatar.setImageBitmap(BitmapFactory.decodeByteArray(
                        user.getAvatar(),
                        0,
                        user.getAvatar().length,
                        new BitmapFactory.Options()));


                binding.fragAccountName.setText(user.getName());
                binding.fragAccountWeightEditText.setText(String.valueOf(user.getWeight()));

                binding.fragmentAccountLogoutButton.setOnClickListener(AccountFragment.this);
            }
        });

        setOnClickListeners(view);
    }


    @Override
    public void onClick(View view) {

        Util.setSharedPrefUserId(getContext(), 0);
        Util.setSharedPrefLoggedIn(getContext(), false);
        viewModel.unregisterAllUserAlarms();

        Intent i = new Intent(getContext(), LoginActivity.class);
        getActivity().startActivity(i);
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setOnClickListeners(View view)
    {

        binding.accountFragmentLoadAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile(Uri.parse("/"));
            }
        });

        binding.fragmentAccountChangeAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    viewModel.alterUserAvatar();
                Toast.makeText(AccountFragment.this.getContext(), R.string.avatar_change_succsess, Toast.LENGTH_LONG).show();
            }
        });

        binding.fragmentAccountChangeWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.alterUserWeight(Double.parseDouble(binding.fragAccountWeightEditText.getText().toString()));
                Toast.makeText(AccountFragment.this.getContext(), "Вес успешно изменён", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");

        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, PICK_IMAGE_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_FILE && resultCode == Activity.RESULT_OK)
        {
            Uri uri = data.getData();

            binding.accountFragmentLoadAvatarImage.setImageBitmap(Util.getBitmapFromUriJPEG(this.getContext(), uri));

            viewModel.loadImageFromURI(uri, getContext());
        }
    }


}