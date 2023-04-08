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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterFragment extends Fragment {
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText passwordAgain;
    private CollectionReference userCollection;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    public RegisterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        firestore = FirebaseFirestore.getInstance();
        userCollection = firestore.collection("Users");

        Button registerBtn = view.findViewById(R.id.registerBtn);
        (registerBtn).setOnClickListener(this::register);

        email = view.findViewById(R.id.registerEmail);
        firstName = view.findViewById(R.id.registerFirstName);
        lastName = view.findViewById(R.id.registerLastName);
        password = view.findViewById(R.id.registerPassword);
        passwordAgain = view.findViewById(R.id.registerPasswordAgain);

        return view;
    }

    public void register(View view) {

        String registerEmail = email.getText().toString();
        String registerFirstName = firstName.getText().toString();
        String registerLastName = lastName.getText().toString();
        String registerPassword = password.getText().toString();
        String registerPasswordAgain = passwordAgain.getText().toString();

        if (!registerPassword.equals(registerPasswordAgain)) {
            Toast.makeText(getContext(), "A jelszavak nem egyeznek! ", Toast.LENGTH_LONG).show();
            return;
        }

        auth.createUserWithEmailAndPassword(registerEmail, registerPassword)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        User user = new User(registerEmail, registerFirstName, registerLastName);
                        user.setId(auth.getUid());
                        userCollection.add(user);
                        Fragment profileFragment = new ProfileFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
                    } else {
                        Toast.makeText(getContext(), "Nem sikerült a fiók létrehozása: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}