package com.shifuu.diabeticsdiary.loginactivity.login;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.database.user.UserEntity;
import com.shifuu.diabeticsdiary.loginactivity.login.LoginFragmentDirections;
import com.shifuu.diabeticsdiary.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class UserListRecyclerAdapter extends ListAdapter<UserEntity, UserListRecyclerAdapter.UserEntityViewHolder> {




    public static final DiffUtil.ItemCallback<UserEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<UserEntity>()
    {

        @Override
        public boolean areItemsTheSame(@NonNull UserEntity oldItem, @NonNull UserEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserEntity oldItem, @NonNull UserEntity newItem) {
            return oldItem.equals(newItem);
        }
    };
    List<UserEntity> userList;

    public UserListRecyclerAdapter()
    {
        super(DIFF_CALLBACK);
        this.userList = new ArrayList<UserEntity>();
    }

    public static class UserEntityViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener

    {

        UserEntity user;
        TextView name;
        ImageView avatar;

        public UserEntityViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_fragment_name_text);
            avatar = itemView.findViewById(R.id.user_fragment_image_avatar);
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
            inflater.inflate(R.menu.user_deletion_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    NavController navController = Navigation.findNavController(view);

                    com.shifuu.diabeticsdiary.loginactivity.login.
                            LoginFragmentDirections.ActionLoginFragmentToDeleteUserFragment action
                            = LoginFragmentDirections.actionLoginFragmentToDeleteUserFragment();

                    action.setUserId(user.getId());

                    navController.navigate(action);
                    return true;
                }
            });
            popupMenu.show();
        }

        @Override
        public void onClick(View view) {
            NavController navController = Navigation.findNavController(view);

            com.shifuu.diabeticsdiary.loginactivity.login.
                    LoginFragmentDirections.ActionLoginFragmentToPasswordLoginFragment action
                    = LoginFragmentDirections.actionLoginFragmentToPasswordLoginFragment();
            action.setUserId(user.getId());

            navController.navigate(action);


        }

        void bindUserData()
        {
            name.setText(user.getName());

            try {
                avatar.setImageBitmap(new ByteDecoderAsyncTask().execute().get()); //TODO window cursor issue
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        private class ByteDecoderAsyncTask extends AsyncTask<Void, Void, Bitmap>
        {

            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap bitmap;
                try
                {
                    bitmap = BitmapFactory.decodeByteArray(
                            user.getAvatar(),
                            0,
                            user.getAvatar().length,
                            new BitmapFactory.Options());
                }
                catch (NullPointerException e)
                {
                    byte[] b = Util.getDefaultUserAvatarBytes(name.getContext());

                    bitmap = BitmapFactory.decodeByteArray(
                            b,
                            0,
                            b.length,
                            new BitmapFactory.Options());
                }

                return bitmap;
            }
        }
    }

    @NonNull
    @Override
    public UserListRecyclerAdapter.UserEntityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_user, parent, false);
        return new UserEntityViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserEntityViewHolder holder, int position) {
        UserEntity user = userList.get(position);
        holder.user = user;
        holder.bindUserData();
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void submitList(List<UserEntity> list)
    {
        userList = list;
        notifyDataSetChanged();
    }


}
