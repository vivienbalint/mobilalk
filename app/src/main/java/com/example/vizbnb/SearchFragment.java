package com.example.vizbnb;

import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private RecyclerView recyclerView;
    private ArrayList<Accomodation> accomodationList;
    private AccomodationAdapter adapter;
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

        initData();

        favoriteBtn = inflater.inflate(R.layout.list_accomodations, container, false).findViewById(R.id.favoriteBtn);
        favoriteBtn.setChecked(false);
        favoriteBtn.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {


            } else {

            }
        });
        return view;
    }

    private void initData() {
        String[] accomodationCity = getResources().getStringArray(R.array.accomodation_city);
        String[] accomodationCountry = getResources().getStringArray(R.array.accomodation_country);
        String[] accomodationDesc = getResources().getStringArray(R.array.accomodation_description);
        String[] accomodationPrice = getResources().getStringArray(R.array.accomodation_price);
        TypedArray accomodationImage = getResources().obtainTypedArray(R.array.accomodation_image);

        accomodationList.clear();

        for(int i = 0; i < accomodationCity.length; i++) {
            accomodationList.add(new Accomodation(accomodationCity[i], accomodationCountry[i], accomodationDesc[i], Integer.parseInt(accomodationPrice[i]), accomodationImage.getResourceId(i, 0)));
        }

        accomodationImage.recycle();

        adapter.notifyDataSetChanged();
    }
}