package com.example.bottomnavviewsactivity.ui.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bottomnavviewsactivity.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private EditText editTextName, editTextLastName, editTextEmail, editTextUsername;
    private Button submitButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        editTextName = binding.editTextName;
        editTextLastName = binding.editTextLastName;
        editTextEmail = binding.editTextEmail;
        editTextUsername = binding.editTextUsername;
        submitButton = binding.submitButton;

        clearFormFields();

        // Set OnClickListener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve text from EditText fields
                String name = editTextName.getText().toString();
                String lastName = editTextLastName.getText().toString();
                String email = editTextEmail.getText().toString();
                String username = editTextUsername.getText().toString();

                if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty()) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    Toast.makeText(requireContext(), "Invalid email format", Toast.LENGTH_SHORT).show();
                } else {
                    // All fields are filled and email format is valid, proceed to save to database
                    User user = new User();
                    user.setName(name);
                    user.setLastName(lastName);
                    user.setEmail(email);
                    user.setUsername(username);

                    // Save user to database
                    UserDatabaseHelper dbHelper = new UserDatabaseHelper(requireContext());
                    dbHelper.addUser(user);

                    // Optionally, you can show a success message to the user
                    Toast.makeText(requireContext(), "User saved successfully", Toast.LENGTH_SHORT).show();

                    // Clear EditText fields after submission
                    clearFormFields();
                }
            }
        });

        return root;
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void clearFormFields() {
        editTextName.setText(null);
        editTextLastName.setText(null);
        editTextEmail.setText(null);
        editTextUsername.setText(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearFormFields();
        binding = null;
    }
}
