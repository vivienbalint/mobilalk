package com.example.vizbnb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText passwordAgain;

    private FirebaseAuth auth;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

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

        if(!registerPassword.equals(registerPasswordAgain)) {
            Toast.makeText(getContext(), "A jelszavak nem egyeznek! ", Toast.LENGTH_LONG).show();
            return;
        }

        auth.createUserWithEmailAndPassword(registerEmail, registerPassword)
                .addOnCompleteListener(getActivity(), task -> {
            if(task.isSuccessful()) {
                User user = new User(registerEmail, registerFirstName, registerLastName);
                Fragment profileFragment = new ProfileFragment(user);
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
            } else {
                Toast.makeText(getContext(), "Nem sikerült a fiók létrehozása: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}