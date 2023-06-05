package com.shifuu.diabeticsdiary.mainactivity;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.database.user.UserEntity;
import com.shifuu.diabeticsdiary.loginactivity.LoginActivity;
import com.shifuu.diabeticsdiary.util.NotificationRegistry;
import com.shifuu.diabeticsdiary.util.Util;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = "MainActivity";

    NavController navController;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SharedPreferences sp = this.getSharedPreferences("loginInfo", MODE_PRIVATE);
        if (!sp.getBoolean("isLoggedIn", false) || sp.getLong("uId", 0) == 0)
        {
            exitToLogin();
        }
        else {

            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            Log.d("MainActivity", String.valueOf(sp.getLong("uId", 0)));


            navigationView.setNavigationItemSelectedListener(this);

            setDrawerListener(drawer);

        }
    }

    private void exitToLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        this.startActivity(i);
        this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            new AppRepository(getApplication()).getUserById(Util.getSharedPrefUserId(getApplicationContext())).observe(this, new Observer<UserEntity>() {
                @Override
                public void onChanged(UserEntity user) {
                    CircleImageView ciw = navigationView
                            .getHeaderView(0)
                            .findViewById(R.id.drawer_header_avatar);
                    if (ciw != null)
                        ciw.setImageBitmap(Util.getBitmapFromByteArray(user.getAvatar()));

                    TextView tw = navigationView
                            .getHeaderView(0)
                            .findViewById(R.id.drawer_header_name);
                    if (tw != null)
                        tw.setText(user.getName());
                }
            });
        }
        catch (Exception e)
        {
            exitToLogin();
        }


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        navController =
                Navigation.findNavController(findViewById(R.id.nav_host_fragment_content_main));

    }

    private void setDrawerListener(DrawerLayout drawer) {
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Log.i(TAG, "onDrawerSlide");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Log.i(TAG, "onDrawerOpened");
                Log.d(TAG, String.valueOf(Util.getSharedPrefUserId(getApplicationContext())));
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Log.i(TAG, "onDrawerClosed");
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                //Log.i(TAG, "onDrawerStateChanged");
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (navController.getCurrentDestination().getId() == R.id.nav_home) {
                Log.d("MainActivity", "call super.onBackPressed(), nav = " + navController.getCurrentDestination().toString());
                super.onBackPressed();
            }
            else {
                navController.popBackStack(R.id.nav_home, false);
                NavigationView navigationView = findViewById(R.id.nav_view);
                navigationView.getMenu().getItem(0).setChecked(true);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home)
            navController.popBackStack(R.id.nav_home, false);
        else
            navController.navigate(id);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

        Toast.makeText(getApplicationContext(), String.valueOf(Util.getSharedPrefUserId(getApplicationContext())), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}