package com.example.bottomnavviewsactivity.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bottomnavviewsactivity.ModifyUser;
import com.example.bottomnavviewsactivity.databinding.FragmentDashboardBinding;
import com.example.bottomnavviewsactivity.ui.home.User;
import com.example.bottomnavviewsactivity.R;
import com.example.bottomnavviewsactivity.ui.home.UserDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private UserDatabaseHelper dbHelper;
    private ListView listViewUsers;
    private List<User> users = new ArrayList<>(); // Initialize the list to avoid null pointer issues

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot(); // Initialize root here

        // Initialize dbHelper here
        dbHelper = new UserDatabaseHelper(requireContext());
        users = dbHelper.showUsers(); // Initialize users after dbHelper

        listViewUsers = root.findViewById(R.id.listViewUsers);
        setupUserSearchView(root); // Pass root to connect with xml

        binding.listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User clickedUser = (User) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(requireContext(), ModifyUser.class);
                intent.putExtra("id", clickedUser.getId());
                intent.putExtra("name", clickedUser.getName());
                intent.putExtra("lastName", clickedUser.getLastName());
                intent.putExtra("username", clickedUser.getUsername());
                intent.putExtra("email", clickedUser.getEmail());
                intent.putExtra("startDate", clickedUser.getStartDate());
                intent.putExtra("endDate", clickedUser.getEndDate());

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
            users = dbHelper.showUsers();
            UserListAdapter adapter = new UserListAdapter(requireContext(), users);
            listViewUsers.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error retrieving users from database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupUserSearchView(View root) {
        SearchView searchView = root.findViewById(R.id.searchUser);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<User> filteredUsers = new ArrayList<>();

                for (User user : users) {
                    if (user.getUsername().toLowerCase().contains(s.toLowerCase())) {
                        filteredUsers.add(user);
                    }
                }

                if (filteredUsers.isEmpty()) {
                    Toast.makeText(requireContext(), "No users found matching the search criteria.", Toast.LENGTH_SHORT).show();
                }

                UserListAdapter adapter = new UserListAdapter(requireContext(), filteredUsers);
                listViewUsers.setAdapter(adapter);
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
