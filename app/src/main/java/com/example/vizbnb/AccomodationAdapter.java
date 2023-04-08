package com.example.vizbnb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AccomodationAdapter extends RecyclerView.Adapter<AccomodationAdapter.ViewHolder> {
    private ArrayList<Accomodation> accomodationsData;
    private Context context;
    private int lastPosition = -1;
    private FirebaseUser user;

    public AccomodationAdapter(Context context, ArrayList<Accomodation> accomodations) {
        this.accomodationsData = accomodations;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_accomodations, parent, false));
    }

    @Override
    public void onBindViewHolder(AccomodationAdapter.ViewHolder holder, int position) {
        Accomodation currentAccomodation = accomodationsData.get(position);
        holder.bindTo(currentAccomodation);

        if (holder.getBindingAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getBindingAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return accomodationsData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView accomodationLocation;
        private TextView accomodationText;
        private TextView accomodationPrice;
        private ImageView accomodationImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            accomodationLocation = itemView.findViewById(R.id.accomodationLocation);
            accomodationText = itemView.findViewById(R.id.accomodationText);
            accomodationPrice = itemView.findViewById(R.id.accomodationPrice);
            accomodationImage = itemView.findViewById(R.id.accomodationImage);
        }

        public void bindTo(Accomodation currentAccomodation) {
            accomodationLocation.setText(currentAccomodation.getLocation());
            accomodationText.setText(currentAccomodation.getDescription());
            accomodationPrice.setText(currentAccomodation.getPriceString());

            Glide.with(context).load(currentAccomodation.getImage()).transform(new RoundedCorners(140)).into(accomodationImage);

            user = FirebaseAuth.getInstance().getCurrentUser();

            Button bookBtn = itemView.findViewById(R.id.bookBtn);

            bookBtn.setOnClickListener(view -> {
                FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
                Fragment fragment;
                if (user != null) {
                    fragment = new BookFragment(currentAccomodation);
                    fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                } else {
                    fragment = new LoginFragment();
                    fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                }
            });
        }
    }
}


