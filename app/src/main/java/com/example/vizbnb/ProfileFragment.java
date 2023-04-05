package com.example.vizbnb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    // TODO: Rename and change types of parameters
    private User user;
    private String mParam2;

    public ProfileFragment() {}
    public ProfileFragment(User user) {
        this.user = user;
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment LoginFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ProfileFragment profileFragment(User user, String param2) {
//        ProfileFragment fragment = new ProfileFragment();
//        Bundle args = new Bundle();
//        args.put
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button registerBtn = view.findViewById(R.id.logoutBtn);
        (registerBtn).setOnClickListener(this::logout);
        return view;
    }

    private void logout(View view) {
        auth.signOut();
//        googleSignInClient.signOut().addOnCompleteListener(getActivity(), task -> {
////            Log.d("profile", auth.getCurrentUser().getEmail());
//            Fragment loginFragment = new LoginFragment();
//            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, loginFragment).commit();
//        });
        Fragment loginFragment = new LoginFragment();
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, loginFragment).commit();

    }

}