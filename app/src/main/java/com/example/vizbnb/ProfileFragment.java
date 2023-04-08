package com.example.vizbnb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ProfileFragment extends Fragment {
    private FirebaseAuth auth;
    private FirebaseUser fbUser;
    private TextView profileGreeting;
    private TextView profileEmail;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        fbUser = auth.getCurrentUser();

        profileGreeting = view.findViewById(R.id.profileGreeting);
        profileEmail = view.findViewById(R.id.profileEmail);

        findUser();

        Button registerBtn = view.findViewById(R.id.logoutBtn);
        (registerBtn).setOnClickListener(this::logout);
        return view;
    }

    private void logout(View view) {
        auth.signOut();
        Fragment loginFragment = new LoginFragment();
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, loginFragment).commit();
    }

    private void findUser() {
        CollectionReference userCollection = FirebaseFirestore.getInstance().collection("Users");
        userCollection.whereEqualTo("id", fbUser.getUid()).get().addOnSuccessListener(queryDocumentSnapshots2 -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots2) {
                User user = document.toObject(User.class);
                profileGreeting.setText("Üdv, " + user.getFirstName() + "!");
                profileEmail.setText(user.getEmail());
            }
        });
    }
}