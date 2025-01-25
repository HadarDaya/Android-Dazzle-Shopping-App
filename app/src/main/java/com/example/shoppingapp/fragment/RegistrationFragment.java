package com.example.shoppingapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoppingapp.MainActivity;
import com.example.shoppingapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Registration Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewScreen = inflater.inflate(R.layout.fragment_registeration, container, false);

        // Find views
        EditText username = viewScreen.findViewById(R.id.regFrag_username);
        EditText password = viewScreen.findViewById(R.id.regFrag_password);
        EditText verifyPassword = viewScreen.findViewById(R.id.regFrag_verifyPassword);
        EditText phone = viewScreen.findViewById(R.id.regFrag_phone);
        Button register = viewScreen.findViewById(R.id.regFrag_signUpButton);

        // Set click listener for the register button
        register.setOnClickListener(v -> {
            String string_username = username.getText().toString().trim();
            String string_password = password.getText().toString().trim();
            String string_verifyPassword = verifyPassword.getText().toString().trim();
            String string_phone = phone.getText().toString().trim();

            if (validateFields(string_username, string_password, string_verifyPassword, string_phone)) {
                /* Proceed with registration.
                   For demonstration, we use a Toast.
                   When everything is ok- we need do to the registration. */
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null)
                    mainActivity.registerFunc(viewScreen); // Validating the data the user has typed
            }
        });
        Button loginButton= viewScreen.findViewById(R.id.regFrag_login);
        // Set click listener for the register button
        loginButton.setOnClickListener(v -> Navigation.findNavController(viewScreen).navigate(R.id.action_registerationFragment_to_entranceFragment));
        return viewScreen;
    }

    /**
     * @param username          the username the user chose.
     * @param password          the password the user chose.
     * @param verifyPassword    the password again for verify.
     * @param phone             the user's phone number
     * @return true if all fields have been filled in correctly, otherwise- return false
     */
    private boolean validateFields(String username, String password, String verifyPassword, String phone) {
        // Check if username is empty
        if (username.isEmpty()) {
            Toast.makeText(getContext(), "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if password is empty
        if (password.isEmpty()) {
            Toast.makeText(getContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if verify password is empty
        if (verifyPassword.isEmpty()) {
            Toast.makeText(getContext(), "Verify Password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.isEmpty()) {
            Toast.makeText(getContext(), "Phone cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if password and verify password match
        if (!password.equals(verifyPassword)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if password is at least 6 characters
        if (password.length() < 6) {
            Toast.makeText(getContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.length()!=10) {
            Toast.makeText(getContext(), "Phone must be 10 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        // All checks passed
        return true;
    }
}