package com.shifuu.diabeticsdiary.mainactivity.foodsbase;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.loginactivity.login.LoginFragmentDirections;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class FoodListRecyclerAdapter extends ListAdapter<FoodEntity, FoodListRecyclerAdapter.FoodEntityViewHolder> {

    public static final DiffUtil.ItemCallback<FoodEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<FoodEntity>()
    {

        @Override
        public boolean areItemsTheSame(@NonNull FoodEntity oldItem, @NonNull FoodEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull FoodEntity oldItem, @NonNull FoodEntity newItem) {
            return oldItem.equals(newItem);
        }
    };

    private List<FoodEntity> list;
    private final FoodsbaseViewModel viewModel;

    private View.OnClickListener listener;
    private boolean isAltListener;


    public FoodListRecyclerAdapter(FoodsbaseViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.list = new ArrayList<>();
        this.viewModel = viewModel;
    }


    @NonNull
    @Override
    public FoodListRecyclerAdapter.FoodEntityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_food, parent, false);
        return new FoodListRecyclerAdapter.FoodEntityViewHolder(rootView, LayoutInflater.from(parent.getContext()), viewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListRecyclerAdapter.FoodEntityViewHolder holder, int position) {
        holder.food = list.get(position);
        holder.bindFoodData();
        if (isAltListener)
            holder.itemView.setOnClickListener(listener);
        Log.d("FoodsBase", "onBindViewHolder " + position);
    }

    public void setItemViewOnClickListener(View.OnClickListener listener)
    {
        this.listener = listener;
        isAltListener = true;
        notifyDataSetChanged();
    }

    @Override
    public void submitList(@Nullable List<FoodEntity> list) {
        this.list = list;
        list.forEach(food -> Log.d("FoodsBase", String.valueOf(food.getId())));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public FoodEntity getFoodEntityByPos(int pos)
    {
        return list.get(pos);
    }

    public static class FoodEntityViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener

    {

        FoodEntity food;
        TextView name;
        TextView value;
        TextView portion;
        TextView protein;
        TextView fat;
        TextView carbs;
        TextView XE;
        FoodsbaseViewModel viewModel;
        LayoutInflater inflater;

        public FoodEntityViewHolder(@NonNull View itemView, LayoutInflater inflater, FoodsbaseViewModel viewModel) {
            super(itemView);
            name = itemView.findViewById(R.id.frag_food_name);
            value = itemView.findViewById(R.id.frag_food_value);
            portion = itemView.findViewById(R.id.frag_food_dimen_value);
            protein = itemView.findViewById(R.id.frag_food_nutrition_protein);
            fat = itemView.findViewById(R.id.frag_food_nutrition_fat);
            carbs = itemView.findViewById(R.id.frag_food_nutrition_carb);
            XE = itemView.findViewById(R.id.frag_food_xe_val);
            this.viewModel = viewModel;
            this.inflater = inflater;

            itemView.setOnClickListener(this);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showPopup(view);
                    return true;
                }
            });

        }

        private void showPopup(View view)
        {
            PopupMenu popupMenu = new PopupMenu(this.itemView.getContext(), view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.food_deletion_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    viewModel.deleteFoodById(food.getId());
                    Toast.makeText(view.getContext(), R.string.successfull_deletion_food_dialog, Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            popupMenu.show();
        }

        @Override
        public void onClick(View view) {
            new FoodFragmentAlterationDialog(viewModel).showAlertDialog(food, view, inflater);
        }

        void bindFoodData()
        {
            name.setText(food.getName());
            value.setText(String.valueOf(food.getKal()));
            portion.setText(food.getPortion());

            protein.setText(String.valueOf(food.getProtein()));
            fat.setText(String.valueOf(food.getFat()));
            carbs.setText(String.valueOf(food.getCarbs()));
            XE.setText(String.valueOf(((double) food.getCarbs())/10));
        }

        public FoodEntity getFoodEntity() {
            return food;
        }
    }
}
