package com.example.vizbnb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            selectedFragment = new FavoritesFragment();
        }
        if (itemId == R.id.trips) {
            selectedFragment = new TripsFragment();
        }
        if (itemId == R.id.profile) {
            selectedFragment = new ProfileFragment();
        }
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }
        return true;
    };
}