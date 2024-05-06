package com.example.bottomnavviewsactivity.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bottomnavviewsactivity.ModifyUser;
import com.example.bottomnavviewsactivity.databinding.FragmentDashboardBinding;
import com.example.bottomnavviewsactivity.ui.home.User;
import com.example.bottomnavviewsactivity.R;
import com.example.bottomnavviewsactivity.ui.home.UserDatabaseHelper;

import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private UserDatabaseHelper dbHelper;
    private ListView listViewUsers;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listViewUsers = root.findViewById(R.id.listViewUsers);

        binding.listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the User object associated with the clicked row
                User clickedUser = (User) adapterView.getItemAtPosition(i);

                // Now you can access the properties of the clickedUser and display them
                int id = clickedUser.getId();
                String name = clickedUser.getName();
                String lastName = clickedUser.getLastName();
                String username = clickedUser.getUsername();
                String email = clickedUser.getEmail();

                Intent intent = new Intent(requireContext(), ModifyUser.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("lastName", lastName);
                intent.putExtra("username", username);
                intent.putExtra("email", email);

                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserList();
    }

    private void loadUserList() {
        try {
            dbHelper = new UserDatabaseHelper(requireContext());
            List<User> users = dbHelper.showUsers();
            UserListAdapter adapter = new UserListAdapter(requireContext(), users);
            listViewUsers.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error retrieving users from database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
