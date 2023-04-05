package com.example.vizbnb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private CollectionReference accomodationsCollection;

    private RecyclerView recyclerView;
    private ArrayList<Accomodation> accomodationList;
    private AccomodationAdapter adapter;

    private int queryLimit = 10;

    private ToggleButton favoriteBtn;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        accomodationList = new ArrayList<>();

        adapter = new AccomodationAdapter(getContext(), accomodationList);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        accomodationsCollection = firestore.collection("Accomodations");

        queryData();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        getContext().registerReceiver(powerReceiver, filter);

        return view;
    }

    BroadcastReceiver powerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            switch (action) {
                case Intent.ACTION_POWER_CONNECTED:
                    queryLimit = 10;
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    queryLimit = 5;
                    break;
            }
            queryData();
        }
    };

    private void queryData() {
        accomodationList.clear();

        accomodationsCollection.limit(queryLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Accomodation accomodation = document.toObject(Accomodation.class);
                accomodation.setId(document.getId());
                accomodationList.add(accomodation);
            }
            if(accomodationList.size() == 0) {
                initData();
                queryData();
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void initData() {
        String[] accomodationCity = getResources().getStringArray(R.array.accomodation_city);
        String[] accomodationCountry = getResources().getStringArray(R.array.accomodation_country);
        String[] accomodationDesc = getResources().getStringArray(R.array.accomodation_description);
        String[] accomodationPrice = getResources().getStringArray(R.array.accomodation_price);
        TypedArray accomodationImage = getResources().obtainTypedArray(R.array.accomodation_image);

        for(int i = 0; i < accomodationCity.length; i++) {
            accomodationsCollection.add(new Accomodation(accomodationCity[i],
                    accomodationCountry[i],
                    accomodationDesc[i],
                    Integer.parseInt(accomodationPrice[i]),
                    accomodationImage.getResourceId(i, 0)));
        }
        accomodationImage.recycle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(powerReceiver);
    }
}