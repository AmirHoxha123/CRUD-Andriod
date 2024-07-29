package com.example.bottomnavviewsactivity.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bottomnavviewsactivity.R;
import com.example.bottomnavviewsactivity.ui.home.UserDatabaseHelper;

public class LoginFragment extends Fragment {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().toLowerCase();
                String password = etPassword.getText().toString();

                UserDatabaseHelper dbHelper = new UserDatabaseHelper(requireContext());
                if (dbHelper.isValidCredentials(username, password)) {
                    Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();

                    // Navigate to HomeFragment using action ID
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_loginFragment_to_navigation_home);
                } else {
                    Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}