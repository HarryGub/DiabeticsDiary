package com.shifuu.diabeticsdiary.loginactivity.deleteuser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.shifuu.diabeticsdiary.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

public class UserDeletionInsuranceDialog {

    NavController navController;
    DeleteUserViewModel viewModel;
    AlertDialog dialog = null;

    public UserDeletionInsuranceDialog(NavController navController, DeleteUserViewModel viewModel)
    {
        this.navController = navController;
        this.viewModel = viewModel;
    }

    public void showAlertDialog(View view, LayoutInflater layoutInflater) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        final View layout = layoutInflater.inflate(R.layout.alert_user_deletion, null);
        builder.setView(layout);

        Button deleteButton = layout.findViewById(R.id.user_deletion_dialog_confirm);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.deleteCurrentUser();
                Toast.makeText(view.getContext(), R.string.successfull_deletion_deletion_dialog, Toast.LENGTH_LONG).show();
                dialog.onBackPressed();
                navController.popBackStack();

            }
        });

        Button cancelButton = layout.findViewById(R.id.user_deletion_dialog_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), R.string.cancel_deletion_deletion_dialog, Toast.LENGTH_LONG).show();
                dialog.onBackPressed();
                navController.popBackStack();

            }
        });

        dialog = builder.create();
        viewModel.setAlertDialog(dialog);

        dialog.show();
    }
}
