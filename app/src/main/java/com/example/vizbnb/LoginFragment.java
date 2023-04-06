package com.example.vizbnb;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int RC_SIGN_IN = 549;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private EditText email;
    private EditText password;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);

        Button loginBtn = view.findViewById(R.id.loginBtn);
        (loginBtn).setOnClickListener(this::login);

        Button loginWithGoogleBtn = view.findViewById(R.id.loginWithGoogleBtn);
        (loginWithGoogleBtn).setOnClickListener(this::loginWithGoogle);

        Button registerBtn = view.findViewById(R.id.registerBtn);
        (registerBtn).setOnClickListener(this::register);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                //Todo
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential cred = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(cred).addOnCompleteListener(getActivity(), task -> {
            if(task.isSuccessful()) {
//                User user = new User(registerEmail, registerFirstName, registerLastName);
                Fragment profileFragment = new ProfileFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
            } else {
                Toast.makeText(getContext(), "A bejelentkezés nem sikerült: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void login(View view) {
        String loginEmail = email.getText().toString();
        String loginPassword = password.getText().toString();
        auth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(getActivity(), task -> {
            if(task.isSuccessful()) {
                Fragment profileFragment = new ProfileFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
            } else {
                Toast.makeText(getContext(), "A bejelentkezés nem sikerült: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loginWithGoogle(View view) {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivity(signInIntent);
    }

    public void register(View view) {
        Fragment registerFragment = new RegisterFragment();
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, registerFragment).commit();
    }


}