package com.example.vizbnb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
    private FirebaseAuth auth;
    private EditText email;
    private EditText password;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);

        Button loginBtn = view.findViewById(R.id.loginBtn);
        (loginBtn).setOnClickListener(this::login);

        Button registerBtn = view.findViewById(R.id.registerBtn);
        (registerBtn).setOnClickListener(this::register);

        return view;
    }

    public void login(View view) {
        String loginEmail = email.getText().toString();
        String loginPassword = password.getText().toString();
        auth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful()) {
                Fragment profileFragment = new ProfileFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
            } else {
                Toast.makeText(getContext(), "A bejelentkezés nem sikerült: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void register(View view) {
        Fragment registerFragment = new RegisterFragment();
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, registerFragment).commit();
    }
}