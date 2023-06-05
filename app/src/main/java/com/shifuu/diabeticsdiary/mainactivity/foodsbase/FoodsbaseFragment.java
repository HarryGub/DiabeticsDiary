package com.shifuu.diabeticsdiary.mainactivity.foodsbase;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.databinding.FragmentFoodsbaseBinding;
import com.shifuu.diabeticsdiary.util.Util;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;


public class FoodsbaseFragment extends Fragment {

    private FragmentFoodsbaseBinding binding;
    protected FoodsbaseViewModel viewModel;
    protected FoodListRecyclerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFoodsbaseBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(FoodsbaseViewModel.class);

        adapter = new FoodListRecyclerAdapter(viewModel);

        binding.fragFoodsbaseRecyclerAdapter.setAdapter(adapter);
        binding.fragFoodsbaseRecyclerAdapter.setLayoutManager(
                new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));

        LiveData<List<FoodEntity>> liveData =
                viewModel.getAllFoodEntitiesByUserSortedByName(
                        viewModel.getUId(getContext()));

        liveData.observe(this.getViewLifecycleOwner(),
                foodEntities -> adapter.submitList(foodEntities));



        binding.fragFoodsbaseFab.setOnClickListener(
                view -> {
                    new FoodFragmentAdditionDialog(viewModel).showAlertDialog(view, getLayoutInflater());
                });

        binding.fragFoodsbaseSearchHint.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER || keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK)
                {
                    Util.hideKeyboardFrom(getContext(), view);
                    binding.fragFoodsbaseSearchHint.clearFocus();
                }
                return true;
            }
        });

        binding.fragFoodsbaseSearchHint.addTextChangedListener(new TextWatcher() {

            LiveData<List<FoodEntity>> listLiveData = null;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                liveData.removeObservers(FoodsbaseFragment.this.getViewLifecycleOwner());
                if (listLiveData != null)
                    listLiveData.removeObservers(FoodsbaseFragment.this.getViewLifecycleOwner());

                listLiveData =
                        viewModel.getFoodEntitiesByUserByName(
                                binding.fragFoodsbaseSearchHint.getText().toString(),
                                viewModel.getUId(getContext()));

                listLiveData.observe(FoodsbaseFragment.this.getViewLifecycleOwner(),
                        foodEntities -> adapter.submitList(foodEntities));

                if (binding.fragFoodsbaseSearchHint.getText().toString().isEmpty())
                    Log.d("search", "empty");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}