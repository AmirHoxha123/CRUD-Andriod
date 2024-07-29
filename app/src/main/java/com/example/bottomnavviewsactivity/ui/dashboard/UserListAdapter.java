package com.example.bottomnavviewsactivity.ui.dashboard;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bottomnavviewsactivity.R;
import com.example.bottomnavviewsactivity.ui.home.User;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<User> {
    private Context mContext;
    private List<User> mUsers;

    public UserListAdapter(Context context, List<User> users) {
        super(context, 0, users);
        mContext = context;
        mUsers = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item_user, parent, false);
        }

        User currentUser = mUsers.get(position);

        TextView textViewUsername = listItem.findViewById(R.id.textViewUsername);
        textViewUsername.setText(currentUser.getUsername());

        return listItem;
    }
}
