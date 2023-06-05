package com.shifuu.diabeticsdiary.mainactivity.home.notes;

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
import com.shifuu.diabeticsdiary.database.notes.NoteEntity;
import com.shifuu.diabeticsdiary.mainactivity.home.addnote.EditNoteDialogFragment;
import com.shifuu.diabeticsdiary.util.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class NotesListRecyclerAdapter  extends ListAdapter<NoteEntity, NotesListRecyclerAdapter.NoteEntityViewHolder> {

    public static final DiffUtil.ItemCallback<NoteEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<NoteEntity>()
    {

        @Override
        public boolean areItemsTheSame(@NonNull NoteEntity oldItem, @NonNull NoteEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull NoteEntity oldItem, @NonNull NoteEntity newItem) {
            return oldItem.equals(newItem);
        }
    };



    private List<NoteEntity> list;
    private final NotesViewModel viewModel;
    private final FragmentManager fragmentManager;

    protected NotesListRecyclerAdapter(NotesViewModel viewModel, FragmentManager manager) {
        super(DIFF_CALLBACK);
        this.list = new ArrayList<>();
        this.viewModel = viewModel;
        this.fragmentManager = manager;
    }

    @NonNull
    @Override
    public NotesListRecyclerAdapter.NoteEntityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_note, parent, false);
        return new NotesListRecyclerAdapter.NoteEntityViewHolder(rootView, LayoutInflater.from(parent.getContext()), viewModel, fragmentManager);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesListRecyclerAdapter.NoteEntityViewHolder holder, int position) {
        holder.note = list.get(position);
        holder.bindNoteData();
        Log.d("Notes", "onBindViewHolder " + holder.note.toString());
    }

    @Override
    public void submitList(@Nullable List<NoteEntity> list) {

        this.list = list;
        notifyDataSetChanged();
        Log.d("finalObserver", list.toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NoteEntityViewHolder
            extends RecyclerView.ViewHolder

    {
        NoteEntity note;
        TextView textView;
        TextView time;
        NotesViewModel viewModel;
        LayoutInflater inflater;
        FragmentManager fragmentManager;

        public NoteEntityViewHolder(@NonNull View itemView, LayoutInflater inflater,
                                    NotesViewModel viewModel, FragmentManager fragmentManager) {
            super(itemView);
            textView = itemView.findViewById(R.id.frag_note_note);
            time = itemView.findViewById(R.id.frag_note_time);
            this.viewModel = viewModel;
            this.inflater = inflater;
            this.fragmentManager = fragmentManager;


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
            inflater.inflate(R.menu.note_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    switch (menuItem.getItemId())
                    {
                        case R.id.action_alter_note: new EditNoteDialogFragment(note).show(fragmentManager, "EditNoteDialog"); break;
                        case R.id.action_delete_note: viewModel.deleteNote(note);
                        Toast.makeText(view.getContext(), "Удаление успешно", Toast.LENGTH_LONG).show();
                        break;
                    }
                    bindNoteData();
                    return true;
                }
            });
            popupMenu.show();
        }


        void bindNoteData()
        {
            textView.setText(note.getNote());
            time.setText(Util.timeFormatter.format(note.getTime()));
        }
    }
}
