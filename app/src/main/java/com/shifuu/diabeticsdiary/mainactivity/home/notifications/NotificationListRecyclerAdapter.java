package com.shifuu.diabeticsdiary.mainactivity.home.notifications;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.database.reminder.ReminderEntity;
import com.shifuu.diabeticsdiary.util.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationListRecyclerAdapter extends
        ListAdapter<ReminderEntity, NotificationListRecyclerAdapter.NotificationEntityViewHolder> {

    public static final DiffUtil.ItemCallback<ReminderEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<ReminderEntity>()
    {

        @Override
        public boolean areItemsTheSame(@NonNull ReminderEntity oldItem, @NonNull ReminderEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ReminderEntity oldItem, @NonNull ReminderEntity newItem) {
            return oldItem.equals(newItem);
        }
    };

    private NotificationsViewModel viewModel;
    private List<ReminderEntity> list;

    protected NotificationListRecyclerAdapter(NotificationsViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.list = new ArrayList<>();
        this.viewModel = viewModel;
    }


    @NonNull
    @Override
    public NotificationListRecyclerAdapter.NotificationEntityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_reminder, parent, false);
        return new NotificationListRecyclerAdapter.NotificationEntityViewHolder(rootView, LayoutInflater.from(parent.getContext()), viewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationListRecyclerAdapter.NotificationEntityViewHolder holder, int position) {
        holder.reminderEntity = list.get(position);
        Log.d("NotificationsFrag", "onBindViewHolder");
        holder.bindData();
    }

    @Override
    public void submitList(@Nullable List<ReminderEntity> list) {

        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NotificationEntityViewHolder
            extends RecyclerView.ViewHolder
    {

        ReminderEntity reminderEntity;
        TextView time;
        TextView note;
        SwitchMaterial switchMaterial;
        NotificationsViewModel viewModel;
        LayoutInflater inflater;

        public NotificationEntityViewHolder(@NonNull View itemView, LayoutInflater inflater, NotificationsViewModel viewModel) {
            super(itemView);
            time = itemView.findViewById(R.id.frag_reminder_time);
            note = itemView.findViewById(R.id.frag_reminder_note);
            switchMaterial = itemView.findViewById(R.id.frag_reminder_switch);
            this.viewModel = viewModel;
            this.inflater = inflater;

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showPopup(view);
                    return true;
                }
            });

            switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (reminderEntity.isSet() != b)
                        viewModel.commitReminderActive(reminderEntity, b);
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
                    viewModel.deleteReminderRec(reminderEntity);
                    Toast.makeText(view.getContext(), R.string.successfull_deletion_food_dialog, Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            popupMenu.show();
        }


        void bindData()
        {
            time.setText(Util.timeFormatter.format(reminderEntity.getTime()));
            note.setText(reminderEntity.getNote());
            switchMaterial.setChecked(reminderEntity.isSet());
        }
    }
}
