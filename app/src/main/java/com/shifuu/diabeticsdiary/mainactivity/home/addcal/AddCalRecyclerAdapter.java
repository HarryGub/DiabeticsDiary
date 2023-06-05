package com.shifuu.diabeticsdiary.mainactivity.home.addcal;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.shifuu.diabeticsdiary.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class AddCalRecyclerAdapter extends ListAdapter<AddCalFoodEntry, AddCalRecyclerAdapter.AddCalRecyclerAdapterViewHolder> {

    public static final DiffUtil.ItemCallback<AddCalFoodEntry> DIFF_CALLBACK = new DiffUtil.ItemCallback<AddCalFoodEntry>()
    {

        @Override
        public boolean areItemsTheSame(@NonNull AddCalFoodEntry oldItem, @NonNull AddCalFoodEntry newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull AddCalFoodEntry oldItem, @NonNull AddCalFoodEntry newItem) {
            return oldItem.equals(newItem);
        }
    };

    private List<AddCalFoodEntry> list;
    private AddCalViewModel viewModel;

    protected AddCalRecyclerAdapter(AddCalViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.list = new ArrayList<>();
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public AddCalRecyclerAdapter.AddCalRecyclerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_cal_dialog_fragment_food_item, parent, false);
        return new AddCalRecyclerAdapter.AddCalRecyclerAdapterViewHolder(rootView, LayoutInflater.from(parent.getContext()), viewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull AddCalRecyclerAdapter.AddCalRecyclerAdapterViewHolder holder, int position) {
        holder.entry = list.get(position);
        holder.bindFoodData();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public void submitList(@Nullable List<AddCalFoodEntry> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class AddCalRecyclerAdapterViewHolder extends RecyclerView.ViewHolder {

        AddCalFoodEntry entry;
        TextView name;
        TextView massXPortion;
        TextView totalCal;

        AddCalViewModel viewModel;

        public AddCalRecyclerAdapterViewHolder(@NonNull View itemView, LayoutInflater inflater, AddCalViewModel viewModel) {
            super(itemView);
            name = itemView.findViewById(R.id.add_cal_dialog_frag_food_item_name);
            massXPortion = itemView.findViewById(R.id.add_cal_dialog_frag_food_item_mass_x_portion);
            totalCal = itemView.findViewById(R.id.add_cal_dialog_frag_food_item_cal);
            this.viewModel = viewModel;

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showPopup(view);
                    return true;
                }
            });

        }


        public void bindFoodData() {
            name.setText(entry.getFoodEntity().getName());
            massXPortion.setText(entry.getMass() + " x " + entry.getFoodEntity().getPortion());
            totalCal.setText(entry.getTotalCal() + " ккал");
        }

        private void showPopup(View view)
        {
            PopupMenu popupMenu = new PopupMenu(this.itemView.getContext(), view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.food_deletion_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    viewModel.deleteFoodEntry(entry);
                    AddCalRecyclerAdapterViewHolder.this.bindFoodData();
                    Toast.makeText(view.getContext(), R.string.successfull_deletion_food_dialog, Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            popupMenu.show();
        }
    }

}
