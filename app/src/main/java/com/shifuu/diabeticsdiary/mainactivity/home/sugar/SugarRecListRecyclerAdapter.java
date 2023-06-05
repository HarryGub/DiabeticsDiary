package com.shifuu.diabeticsdiary.mainactivity.home.sugar;

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
import com.shifuu.diabeticsdiary.database.sugar_rec.SugarRecEntity;
import com.shifuu.diabeticsdiary.mainactivity.home.calories.CaloriesViewModel;
import com.shifuu.diabeticsdiary.util.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


public class SugarRecListRecyclerAdapter extends ListAdapter<SugarRecEntity, SugarRecListRecyclerAdapter.SugarRecEntityViewHolder> {

    public static final DiffUtil.ItemCallback<SugarRecEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<SugarRecEntity>()
    {

        @Override
        public boolean areItemsTheSame(@NonNull SugarRecEntity oldItem, @NonNull SugarRecEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull SugarRecEntity oldItem, @NonNull SugarRecEntity newItem) {
            return oldItem.equals(newItem);
        }
    };

    private List<SugarRecEntity> list;
    private final SugarViewModel viewModel;


    protected SugarRecListRecyclerAdapter(SugarViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.list = new ArrayList<>();
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public SugarRecListRecyclerAdapter.SugarRecEntityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_sugar_rec, parent, false);
        return new SugarRecListRecyclerAdapter.SugarRecEntityViewHolder(rootView, LayoutInflater.from(parent.getContext()), viewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull SugarRecListRecyclerAdapter.SugarRecEntityViewHolder holder, int position) {
        holder.sugarRecEntity = list.get(position);

        holder.bindFoodData();
        Log.d("Calories", "onBindViewHolder");
    }

    @Override
    public void submitList(@Nullable List<SugarRecEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SugarRecEntityViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener

    {

        SugarRecEntity sugarRecEntity;
        TextView time;
        TextView val;
        TextView beforeAfter;
        TextView note;
        SugarViewModel viewModel;
        LayoutInflater inflater;

        public SugarRecEntityViewHolder(@NonNull View itemView, LayoutInflater inflater, SugarViewModel viewModel) {
            super(itemView);

            this.viewModel = viewModel;
            this.inflater = inflater;
            time = itemView.findViewById(R.id.frag_sugar_rec_time);
            val = itemView.findViewById(R.id.frag_sugar_rec_val);
            beforeAfter = itemView.findViewById(R.id.frag_sugar_rec_isBefore);
            note = itemView.findViewById(R.id.frag_sugar_rec_note);

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

                    viewModel.deleteSugarRec(sugarRecEntity);
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
            time.setText(Util.timeFormatter.format(sugarRecEntity.getTime()));
            note.setText(sugarRecEntity.getNote());
            beforeAfter.setText(sugarRecEntity.isBeforeAfter() ? "До еды" : "После еды");
            val.setText(sugarRecEntity.getValue() + " ммоль/л");
        }
    }
}
