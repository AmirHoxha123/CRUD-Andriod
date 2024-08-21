package com.example.bottomnavviewsactivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bottomnavviewsactivity.ui.home.User;
import com.example.bottomnavviewsactivity.ui.home.UserDatabaseHelper;
import java.util.Calendar;

public class ModifyUser extends AppCompatActivity {

    private EditText editTextStartDate, editTextEndDate;
    private Calendar startDateCalendar, endDateCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_user);

        String name = getIntent().getStringExtra("name");
        String lastName = getIntent().getStringExtra("lastName");
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");

        // Inflate the layout containing titleTextView
        View headerView = LayoutInflater.from(this).inflate(R.layout.action_bar_layout, null);
        TextView titleTextView = headerView.findViewById(R.id.titleTextView);
        titleTextView.setText("Modify " + name);

        // for adding the custom action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(headerView);
        }

        // Find TextViews and EditTexts in the layout
        EditText textViewName = findViewById(R.id.nameEt);
        EditText textViewLastName = findViewById(R.id.last_nameEt);
        EditText textViewEmail = findViewById(R.id.emailEt);
        EditText textViewUsername = findViewById(R.id.usernameEt);
        editTextStartDate = findViewById(R.id.startDateEt);
        editTextEndDate = findViewById(R.id.endDateEt);
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();

        // Set the text of EditTexts with received data
        textViewName.setText(name);
        textViewLastName.setText(lastName);
        textViewUsername.setText(username);
        textViewEmail.setText(email);
        editTextStartDate.setText(startDate);
        editTextEndDate.setText(endDate);

        // Set OnClickListener for the start and end date EditTexts
        editTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editTextStartDate, startDateCalendar, true);
            }
        });

        editTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editTextEndDate, endDateCalendar, false);
            }
        });

        Button btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUser.this);
            builder.setTitle("Confirm Delete");
            builder.setMessage("Are you sure you want to delete " + name);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                int userID = getIntent().getIntExtra("id", -1);
                UserDatabaseHelper dbHelper = new UserDatabaseHelper(ModifyUser.this);
                boolean isDeleted = dbHelper.deleteUserById(userID);
                if (isDeleted) {
                    Toast.makeText(ModifyUser.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ModifyUser.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        Button btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(v -> {
            UserDatabaseHelper dbHelper = new UserDatabaseHelper(ModifyUser.this);
            int userId = getIntent().getIntExtra("id", -1);
            String newName = textViewName.getText().toString();
            String newLastName = textViewLastName.getText().toString();
            String newEmail = textViewEmail.getText().toString();
            String newUsername = textViewUsername.getText().toString();
            String newStartDate = editTextStartDate.getText().toString();
            String newEndDate = editTextEndDate.getText().toString();

            if (newName.isEmpty() || newLastName.isEmpty() || newEmail.isEmpty() || newUsername.isEmpty() || newStartDate.isEmpty() || newEndDate.isEmpty()) {
                Toast.makeText(ModifyUser.this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else if (!isValidEmail(newEmail)) {
                Toast.makeText(ModifyUser.this, "Invalid email format", Toast.LENGTH_SHORT).show();
            } else if (!isDateRangeValid()) {
                Toast.makeText(ModifyUser.this, "End date cannot be before start date", Toast.LENGTH_SHORT).show();
            }else if (dbHelper.doesUsernameExist(newUsername, userId)){
                Toast.makeText(ModifyUser.this, "Username already exists", Toast.LENGTH_SHORT).show();
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUser.this);
                builder.setTitle("Confirm Update");
                builder.setMessage("Are you sure you want to update " + name);
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    User updatedUser = new User(userId, newName, newLastName, newEmail, newUsername, newStartDate, newEndDate);
                    boolean isUpdated = dbHelper.updateUser(updatedUser);

                    if (isUpdated) {
                        Toast.makeText(ModifyUser.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                        textViewName.setText(newName);
                        textViewLastName.setText(newLastName);
                        textViewEmail.setText(newEmail);
                        textViewUsername.setText(newUsername);
                        editTextStartDate.setText(newStartDate);
                        editTextEndDate.setText(newEndDate);
                        finish();
                    } else {
                        Toast.makeText(ModifyUser.this, "Failed to update user", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void showDatePickerDialog(final EditText editText, final Calendar calendar, final boolean isStartDate) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ModifyUser.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        editText.setText(selectedDate);

                        if (isStartDate) {
                            startDateCalendar = calendar;
                            // Update end date's minimum date only, without clearing the field
                            if (editTextEndDate.getText().length() > 0 && !isDateRangeValid()) {
                                Toast.makeText(ModifyUser.this, "End date cannot be before start date", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            endDateCalendar = calendar;
                        }
                    }
                }, year, month, day);

        // Set minimum date for the end date if it's the end date picker
        if (!isStartDate && startDateCalendar != null) {
            datePickerDialog.getDatePicker().setMinDate(startDateCalendar.getTimeInMillis());
        }

        datePickerDialog.show();
    }

    private boolean isDateRangeValid() {
        return startDateCalendar.compareTo(endDateCalendar) <= 0;
    }

    private boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
