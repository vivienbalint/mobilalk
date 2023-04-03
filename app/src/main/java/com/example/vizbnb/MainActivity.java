package com.example.vizbnb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser user;
    private final static String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();

        BottomNavigationView nav = findViewById(R.id.navigation);
        nav.setOnItemSelectedListener(navListener);
        Fragment searchFragment = new SearchFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, searchFragment).commit();
    }

    private final BottomNavigationView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.search) {
            selectedFragment = new SearchFragment();
        }
        if (itemId == R.id.favorites) {
            if(user != null) {
                selectedFragment = new FavoritesFragment();
            } else {
                selectedFragment = new LoginFragment();
            }
        }
        if (itemId == R.id.trips) {
            if(user != null) {
                selectedFragment = new TripsFragment();
            } else {
                selectedFragment = new LoginFragment();
            }
        }
        if (itemId == R.id.profile) {
            if(user != null) {
                selectedFragment = new ProfileFragment();
            } else {
                selectedFragment = new LoginFragment();
            }
        }
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }
        return true;
    };
}