package com.shifuu.diabeticsdiary.mainactivity.home.calories;


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
import com.shifuu.diabeticsdiary.database.food_rec.FoodRecEntity;
import com.shifuu.diabeticsdiary.util.Util;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class CaloriesListRecyclerAdapter extends ListAdapter<FoodRecEntity, CaloriesListRecyclerAdapter.FoodRecEntityViewHolder> {

    public static final DiffUtil.ItemCallback<FoodRecEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<FoodRecEntity>()
    {

        @Override
        public boolean areItemsTheSame(@NonNull FoodRecEntity oldItem, @NonNull FoodRecEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull FoodRecEntity oldItem, @NonNull FoodRecEntity newItem) {
            return oldItem.equals(newItem);
        }
    };

    private List<FoodRecEntity> list;
    private final CaloriesViewModel viewModel;
    final static FoodRecEntity blankFoodRec = new FoodRecEntity(0, LocalDate.MAX, LocalDateTime.MAX, "", 0, 0, 0);


    protected CaloriesListRecyclerAdapter(CaloriesViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.list = new ArrayList<>();
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public FoodRecEntityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_food_rec, parent, false);
        return new FoodRecEntityViewHolder(rootView, LayoutInflater.from(parent.getContext()), viewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodRecEntityViewHolder holder, int position) {
        holder.foodRec = list.get(position);
        holder.bindFoodData();
        Log.d("Calories", "onBindViewHolder");
    }

    @Override
    public void submitList(@Nullable List<FoodRecEntity> list) {

        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class FoodRecEntityViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener

    {

        FoodRecEntity foodRec;
        TextView time;
        TextView kal;
        TextView foods;
        TextView note;
        TextView protein;
        TextView fat;
        TextView carbs;
        TextView XE;
        CaloriesViewModel viewModel;
        LayoutInflater inflater;

        public FoodRecEntityViewHolder(@NonNull View itemView, LayoutInflater inflater, CaloriesViewModel viewModel) {
            super(itemView);
            time = itemView.findViewById(R.id.frag_food_rec_time);
            kal = itemView.findViewById(R.id.frag_food_rec_cal);
            foods = itemView.findViewById(R.id.frag_food_rec_foods);
            note = itemView.findViewById(R.id.frag_food_rec_note);
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

                    viewModel.deleteFoodRecById(foodRec.getId());
                    bindFoodData();
                    Toast.makeText(view.getContext(), R.string.successfull_deletion_food_dialog, Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            popupMenu.show();
        }

        @Override
        public void onClick(View view) {

        }

        void bindFoodData()
        {
            if (foodRec.equals(blankFoodRec))
            {
                makeBlank();
                return;
            }

            LiveData<FoodEntity> f = viewModel.getFoodDataById(foodRec.getFoodId());
            f.observeForever(new Observer<FoodEntity>() {
                @Override
                public void onChanged(FoodEntity food) {
                    time.setText(Util.timeFormatter.format(foodRec.getTime()));
                    Log.d("Time", foodRec.getTime().toString());
                    Log.d("Date", foodRec.getDate().toString());
                    kal.setText(Math.round(food.getKal() * foodRec.getMass()) + " ккал");
                    note.setText(foodRec.getNote());

                    protein.setText(String.valueOf(Math.round(food.getProtein() * foodRec.getMass())));
                    fat.setText(String.valueOf(Math.round(food.getFat() * foodRec.getMass())));
                    carbs.setText(String.valueOf(Math.round(food.getCarbs() * foodRec.getMass())));
                    DecimalFormat decimalFormat = new DecimalFormat("0.##");
                    XE.setText(decimalFormat.format(((double) food.getCarbs())/10  * foodRec.getMass()));

                    StringBuilder sb = new StringBuilder("");
                    sb.append(food.getName());
                    sb.append(" - ");
                    sb.append(foodRec.getMass());
                    sb.append(" x ");
                    sb.append(food.getPortion());

                    foods.setText(sb.toString());
                    f.removeObserver(this);
                }
            });


        }

        public void makeBlank() {
            time.setVisibility(View.INVISIBLE);
            kal.setVisibility(View.INVISIBLE);
            foods.setVisibility(View.INVISIBLE);
            note.setVisibility(View.INVISIBLE);
        }
    }
}
