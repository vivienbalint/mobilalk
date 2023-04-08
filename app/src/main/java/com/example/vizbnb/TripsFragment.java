package com.example.vizbnb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class TripsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<BookedAccomodation> accomodationList;
    private FirebaseFirestore firestore;
    private FirebaseUser user;
    private CollectionReference bookedCollection;
    private BookedAccomodationAdapter adapter;

    public TripsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        recyclerView = view.findViewById(R.id.searchRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        accomodationList = new ArrayList<>();

        adapter = new BookedAccomodationAdapter(getContext(), accomodationList);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        bookedCollection = firestore.collection("Booked");

        user = FirebaseAuth.getInstance().getCurrentUser();

        queryData();

        return view;
    }

    private void queryData() {
        accomodationList.clear();

        bookedCollection.whereEqualTo("userId", user.getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                BookedAccomodation accomodation = document.toObject(BookedAccomodation.class);
                accomodation.setId(document.getId());
                accomodationList.add(accomodation);
            }
            adapter.notifyDataSetChanged();
        });
    }
}