package com.example.bottomnavviewsactivity.ui.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bottomnavviewsactivity.databinding.FragmentHomeBinding;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private EditText editTextName, editTextLastName, editTextEmail, editTextUsername;
    private EditText editTextStartDate;
    private EditText editTextEndDate;
    private Calendar startDateCalendar, endDateCalendar;
    private Button submitButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        editTextName = binding.editTextName;
        editTextLastName = binding.editTextLastName;
        editTextEmail = binding.editTextEmail;
        editTextUsername = binding.editTextUsername;
        editTextStartDate = binding.editTextStartDate;
        editTextEndDate = binding.editTextEndDate;
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();
        submitButton = binding.submitButton;

        clearFormFields();

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

        // Set OnClickListener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve text from EditText fields
                String name = editTextName.getText().toString();
                String lastName = editTextLastName.getText().toString();
                String email = editTextEmail.getText().toString();
                String username = editTextUsername.getText().toString();
                String startDate = editTextStartDate.getText().toString();
                String endDate = editTextEndDate.getText().toString();

                if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    Toast.makeText(requireContext(), "Invalid email format", Toast.LENGTH_SHORT).show();
                } else if (!isDateRangeValid()) {
                    Toast.makeText(requireContext(), "End date cannot be before start date", Toast.LENGTH_SHORT).show();
                } else {
                    UserDatabaseHelper dbHelper = new UserDatabaseHelper(requireContext());

                    if (dbHelper.doesUsernameExist(username, -1)) {
                        Toast.makeText(requireContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                    }else{
                        // All fields are filled and email format is valid, proceed to save to database
                        User user = new User();
                        user.setName(name);
                        user.setLastName(lastName);
                        user.setEmail(email);
                        user.setUsername(username);
                        user.setStartDate(startDate);
                        user.setEndDate(endDate);

                        // Save user to database
                        dbHelper.addUser(user);

                        // Optionally, you can show a success message to the user
                        Toast.makeText(requireContext(), "User saved successfully", Toast.LENGTH_SHORT).show();

                        // Clear EditText fields after submission
                        clearFormFields();
                    }
                }
            }
        });

        return root;
    }

    private void showDatePickerDialog(final EditText editText, final Calendar calendar, final boolean isStartDate) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        // we add because because javas calendar are 0 based
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        editText.setText(selectedDate);

                        if (isStartDate) {
                            startDateCalendar = calendar;
                            // Update end date's minimum date only, without clearing the field
                            if (editTextEndDate.getText().length() > 0 && !isDateRangeValid()) {
                                Toast.makeText(requireContext(), "End date cannot be before start date", Toast.LENGTH_SHORT).show();
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

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void clearFormFields() {
        editTextName.setText(null);
        editTextLastName.setText(null);
        editTextEmail.setText("");
        editTextUsername.setText(null);
        editTextStartDate.setText(null);
        editTextEndDate.setText(null);
        // Reset the calendars
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Clear the form fields every time the fragment is resumed
        clearFormFields();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearFormFields();
        binding = null;
    }
}
