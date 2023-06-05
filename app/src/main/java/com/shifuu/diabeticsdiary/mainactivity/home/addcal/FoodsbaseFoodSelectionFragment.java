package com.shifuu.diabeticsdiary.mainactivity.home.addcal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.databinding.FragmentFoodsbaseBinding;
import com.shifuu.diabeticsdiary.mainactivity.foodsbase.FoodListRecyclerAdapter;
import com.shifuu.diabeticsdiary.mainactivity.foodsbase.FoodsbaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

public class FoodsbaseFoodSelectionFragment extends FoodsbaseFragment {

    Observer<AddCalFoodEntry> observer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //view.findViewById(R.id.frag_foodsbase_fab).setVisibility(View.GONE);
        RecyclerView recyclerView = view.findViewById(R.id.frag_foodsbase_recycler_adapter);
        recyclerView.setAdapter(adapter);

        adapter.setItemViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                FoodEntity food = adapter.getFoodEntityByPos(recyclerView.getChildViewHolder(view).getAdapterPosition());
                Log.d("FoodsbaseFoodSelectionFrag", "Selected: " + food.getName());
                MassSelectionDialogFragment fragment = new MassSelectionDialogFragment(food);
                fragment.setObserver(new Observer<Double>() {
                    @Override
                    public void onChanged(Double aDouble) {
                        Log.d("FoodsbaseFoodSelectionFragment", aDouble.toString());
                        observer.onChanged(new AddCalFoodEntry(food, aDouble, Math.round(food.getKal() * aDouble)));
                        fm.popBackStack();
                    }
                });

                fragment.show(fm, "MassSelectionDialogFragment");

            }
        });

    }

    public void setObserver(Observer<AddCalFoodEntry> observer) {
        this.observer = observer;
    }
}
