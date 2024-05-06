package com.example.bottomnavviewsactivity.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bottomnavviewsactivity.R; // Assuming R.layout.list_item_user exists in your project
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
            listItem = LayoutInflater.from(mContext).inflate(R.layout.fragment_dashboard, parent, false);
        }

        User currentUser = mUsers.get(position);

//        TextView textViewName = listItem.findViewById(R.id.textViewName);
//        textViewName.setText(currentUser.getName() + " " + currentUser.getLastName());
//
//        TextView textViewEmail = listItem.findViewById(R.id.textViewEmail);
//        textViewEmail.setText(currentUser.getEmail());

        TextView textViewUsername = listItem.findViewById(R.id.textViewUsername);
        textViewUsername.setText(currentUser.getUsername());

        return listItem;
    }
}
