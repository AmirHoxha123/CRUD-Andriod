package com.example.bottomnavviewsactivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bottomnavviewsactivity.ui.home.User;
import com.example.bottomnavviewsactivity.ui.home.UserDatabaseHelper;

public class ModifyUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.modify_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String name = getIntent().getStringExtra("name");
        String lastName = getIntent().getStringExtra("lastName");
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");

        // Inflate the layout containing titleTextView
        View headerView = LayoutInflater.from(this).inflate(R.layout.action_bar_layout, null);

        TextView titleTextView = headerView.findViewById(R.id.titleTextView);

        titleTextView.setText("Modify " + name);


        ActionBar actionBar = getSupportActionBar(); // or getActionBar() depending on your setup
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(headerView);
        }

        // Find TextViews in the layout
        TextView textViewName = findViewById(R.id.nameEt);
        TextView textViewLastName = findViewById(R.id.last_nameEt);
        TextView textViewEmail = findViewById(R.id.emailEt);
        TextView textViewUsername = findViewById(R.id.usernameEt);

        // Set the text of TextViews with received data
        textViewName.setText(name);
        textViewLastName.setText(lastName);
        textViewUsername.setText(username);
        textViewEmail.setText(email);

        Button btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUser.this);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete " + name);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Retrieve user information
                        int userID = getIntent().getIntExtra("id", -1);

                        // Initialize the database helper
                        UserDatabaseHelper dbHelper = new UserDatabaseHelper(ModifyUser.this);

                        // Delete the user from the database
                        boolean isDeleted = dbHelper.deleteUserById(userID);

                        if (isDeleted) {
                            // Show a toast indicating successful deletion
                            Toast.makeText(ModifyUser.this, "User deleted successfully", Toast.LENGTH_SHORT).show();

                            // Finish the activity
                            finish();
                        } else {
                            Toast.makeText(ModifyUser.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        Button btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = getIntent().getIntExtra("id", -1);
                // Retrieve the updated information from EditText or other input fields
                EditText editTextName = findViewById(R.id.nameEt);
                EditText editTextLastName = findViewById(R.id.last_nameEt);
                EditText editTextEmail = findViewById(R.id.emailEt);
                EditText editTextUsername = findViewById(R.id.usernameEt);

                String newName = editTextName.getText().toString();
                String newLastName = editTextLastName.getText().toString();
                String newEmail = editTextEmail.getText().toString();
                String newUsername = editTextUsername.getText().toString();
                if (newName.isEmpty() || newLastName.isEmpty() || newEmail.isEmpty() || newUsername.isEmpty()) {
                    Toast.makeText(ModifyUser.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(newEmail)) {
                    Toast.makeText(ModifyUser.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUser.this);
                    builder.setTitle("Confirm Update");
                    builder.setMessage("Are you sure you want to update " + name);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Create a new User object with the updated information
                            User updatedUser = new User(userId, newName, newLastName, newEmail, newUsername);

                            // Initialize the database helper
                            UserDatabaseHelper dbHelper = new UserDatabaseHelper(ModifyUser.this);

                            // Update the user in the database
                            boolean isUpdated = dbHelper.updateUser(updatedUser);

                            if (isUpdated) {
                                // Show a toast indicating successful update
                                Toast.makeText(ModifyUser.this, "User updated successfully", Toast.LENGTH_SHORT).show();

                                // Optionally, refresh the UI to reflect the updated user information
                                // For example, update the TextViews with the new values
                                textViewName.setText(newName);
                                textViewLastName.setText(newLastName);
                                textViewEmail.setText(newEmail);
                                textViewUsername.setText(newUsername);
                            } else {
                                // Show a toast indicating update failure
                                Toast.makeText(ModifyUser.this, "Failed to update user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });




        // Handle back button click
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}