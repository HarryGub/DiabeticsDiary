package com.shifuu.diabeticsdiary.mainactivity.usertransfer;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shifuu.diabeticsdiary.databinding.FragmentUserTransferBinding;
import com.shifuu.diabeticsdiary.util.Util;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserTransferFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 1276;
    private static final int OPEN_FOLDER_DDT = 7891;
    private static final int OPEN_FOLDER_XLSX = 7891124;
    private UserTransferViewModel viewModel;

    private FragmentUserTransferBinding binding;

    public static UserTransferFragment newInstance() {
        return new UserTransferFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(UserTransferViewModel.class);

        binding = FragmentUserTransferBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fragUserTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDir(Uri.parse(File.separator), OPEN_FOLDER_DDT);
            }
        });

        binding.fragUserTransferButtonXslx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDir(Uri.parse(File.separator), OPEN_FOLDER_XLSX);
            }
        });


    }






    private void openDir(Uri pickerInitialUri, int code) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_TITLE,
                File.separator +
                        "Мобильный_дневник_диабетика_" +
                        Util.dateFormatter.format(LocalDateTime.now()) +
                        "_" +
                        Util.timeFormatter.format(LocalDateTime.now()) +
                        (code == OPEN_FOLDER_DDT ? ".ddt" : ".xlsx"));

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        Log.d("UserTransfer", "onActivityResult");

        if (resultCode == Activity.RESULT_OK && resultData != null)
        {
            Uri uri = resultData.getData();
            String s = "";
            if (requestCode == OPEN_FOLDER_DDT)
            {
                s = viewModel.writeUserImportBundle(uri);
            }
            else if (requestCode == OPEN_FOLDER_XLSX)
            {
                s = viewModel.writeUserImportBundleXlsx(uri);
            }
            Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
        }
    }


}