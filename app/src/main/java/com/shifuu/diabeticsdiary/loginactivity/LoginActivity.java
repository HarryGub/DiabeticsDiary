package com.shifuu.diabeticsdiary.loginactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;

import android.os.Bundle;

import com.shifuu.diabeticsdiary.R;

public class LoginActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_login);
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.fragment_welcome)
        {
            super.onBackPressed();
        }
        else
        {
            navController.popBackStack();
        }
    }
}