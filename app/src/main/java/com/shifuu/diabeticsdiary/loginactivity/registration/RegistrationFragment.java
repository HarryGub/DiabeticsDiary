package com.shifuu.diabeticsdiary.loginactivity.registration;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.database.AppRoomDatabase;
import com.shifuu.diabeticsdiary.database.user.UserEntity;
import com.shifuu.diabeticsdiary.databinding.FragmentRegistrationBinding;
import com.shifuu.diabeticsdiary.mainactivity.MainActivity;
import com.shifuu.diabeticsdiary.util.Util;

import java.time.LocalDateTime;

public class RegistrationFragment extends Fragment {


    private FragmentRegistrationBinding binding;
    private RegistrationViewModel viewModel;
    NavController navController;
    private static final int PICK_IMAGE_FILE = 2;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.runAsyncTaskThread(() -> binding.regFragmentLoadAvatarImage.
                setImageBitmap(Util.getBitmapFromByteArray(viewModel.getImage())));

        setOnClickListeners(view);

    }


    private void setOnClickListeners(View view)
    {
         navController = Navigation.findNavController(view);

        binding.regFragTextButtonImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_registrationFragment_to_importUserFragment);
            }
        });

        binding.fragmentRegistrationButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long uid = registerNewUser();

                if (uid == 0)
                    return;

                AppRoomDatabase.databaseWriteExecutor.execute(() -> addDefaultFoodDatabase(uid));

                Toast.makeText(RegistrationFragment.this.getContext(), R.string.reg_frag_toast, Toast.LENGTH_LONG).show();


                SharedPreferences sp = getContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putLong("uId", uid);
                editor.commit();

                Intent i = new Intent(getContext(), MainActivity.class);
                getActivity().startActivity(i);
                RegistrationFragment.this.getActivity().finish();


            }
        });

        binding.regFragmentLoadAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile(Uri.parse("/"));
            }
        });

    }

    private void addDefaultFoodDatabase(long uid) {
        new DefaultDatabaseInserter(getActivity().getApplication()).insert(uid);
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

            binding.regFragmentLoadAvatarImage.setImageBitmap(Util.getBitmapFromUriJPEG(this.getContext(), uri));

            viewModel.loadImageFromURI(uri, getContext());
        }
    }

    private long registerNewUser()
    {
        binding.regFragmentYourNameNotification.setVisibility(View.GONE);
        binding.regFragmentPasswdNotification.setVisibility(View.GONE);
        binding.regFragmentPasswdConfirmNotification.setVisibility(View.GONE);


        String name = binding.regFragEditTextName.getText().toString();
        if (name.isEmpty())
        {
            binding.regFragmentYourNameNotification.setText(R.string.name_must_not_be_empty);
            binding.regFragmentYourNameNotification.setVisibility(View.VISIBLE);
            return 0;
        }
        if (name.contains(";") || name.contains(",") || name.contains("@") || name.contains(":") ||
                name.contains("?") || name.contains("^"))
        {
            binding.regFragmentYourNameNotification.setText(R.string.reg_fragment_your_name_notification);
            binding.regFragmentYourNameNotification.setVisibility(View.VISIBLE);
            return 0;
        }

        String passwd = binding.regFragEditTextPasswd.getText().toString();
        String confirmPasswd = binding.regFragEditTextPasswdConfirm.getText().toString();

        if (passwd.isEmpty())
        {
            binding.regFragmentPasswdNotification.setText(R.string.passwd_must_not_be_empty);
            binding.regFragmentPasswdNotification.setVisibility(View.VISIBLE);
            return 0;
        }
        if (!passwd.equals(confirmPasswd))
        {
            binding.regFragmentPasswdConfirmNotification.setText(R.string.passwds_not_equal);
            binding.regFragmentPasswdConfirmNotification.setVisibility(View.VISIBLE);
            return 0;
        }

        double weight = 0;

        try {
            weight = Double.parseDouble(binding.fragAccountWeightEditText.getText().toString());
        }
        catch (Throwable throwable)
        {
            Toast.makeText(getContext(), "Введите ваш вес", Toast.LENGTH_LONG).show();
            return 0;
        }


        return viewModel.registerNewUser(name, passwd, weight);

        //navController.popBackStack();
    }

}