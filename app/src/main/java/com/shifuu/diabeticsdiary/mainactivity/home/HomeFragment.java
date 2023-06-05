package com.shifuu.diabeticsdiary.mainactivity.home;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.databinding.FragmentHomeBinding;
import com.shifuu.diabeticsdiary.mainactivity.home.addcal.AddCalDialogFragment;
import com.shifuu.diabeticsdiary.mainactivity.home.addnote.AddNoteDialogFragment;
import com.shifuu.diabeticsdiary.mainactivity.home.addreminder.AddReminderDialogFragment;
import com.shifuu.diabeticsdiary.mainactivity.home.addsugar.AddSugarDialogFragment;

public class HomeFragment extends Fragment implements NavigationBarView.OnItemSelectedListener {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        binding.bottomBarHome.bottomBarNavView.setSelectedItemId(R.id.nav_bottom_sugar);

        binding.bottomBarHome.bottomBarNavView.setOnItemSelectedListener(this);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        NavController navController =
                Navigation.findNavController(binding.contentHome.navHostFragmentContentHome);

        if (navController.getCurrentDestination().getId() == R.id.nav_bottom_statistics)
            binding.fab.setVisibility(View.GONE);
    }


    private void setFabOnClickListener() {
        NavController navController =
                Navigation.findNavController(binding.contentHome.navHostFragmentContentHome);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                HomeFragmentAdditionSelector.newInstance().show(getChildFragmentManager(), "HomeFragAdditionSelector");
                switch (navController.getCurrentDestination().getId())
                {
                    case R.id.nav_bottom_sugar:
                        new AddSugarDialogFragment().show(getActivity().getSupportFragmentManager(), "AddSugDialog"); break;
                    case R.id.nav_bottom_calories:
                        new AddCalDialogFragment().show(getActivity().getSupportFragmentManager(), "AddCalDialog"); break;
                    case R.id.nav_bottom_notifications:
                        new AddReminderDialogFragment().show(getActivity().getSupportFragmentManager(), "AddReminderDialog"); break;
                    case R.id.nav_bottom_notes:
                        new AddNoteDialogFragment().show(getActivity().getSupportFragmentManager(), "AddNoteDialog"); break;
                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        setFabOnClickListener();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

//        Log.d ("Home Fragment", binding.contentHome.navHostFragmentContentHome.getFragment().getClass().toString());

        NavController navController =
                Navigation.findNavController(binding.contentHome.navHostFragmentContentHome);
        navController.navigate(id);

        binding.fab.setVisibility(id == R.id.nav_bottom_statistics ? View.GONE : View.VISIBLE);

        return true;
    }


}