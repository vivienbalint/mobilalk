package com.example.vizbnb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class AccomodationAdapter extends RecyclerView.Adapter<AccomodationAdapter.ViewHolder> implements Filterable {
    private ArrayList<Accomodation> accomodationsData;
    private ArrayList<Accomodation> accomodationsDataAll;
    private Context context;
    private int lastPosition = -1;

    public AccomodationAdapter(Context context, ArrayList<Accomodation> accomodations) {
        this.accomodationsData = accomodations;
        this.accomodationsDataAll = accomodations;
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

        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return accomodationsData.size();
    }

    @Override
    public Filter getFilter() {
        return accomodationFilter;
    }

    private Filter accomodationFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Accomodation> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0) {
                results.count = accomodationsDataAll.size();
                results.values = accomodationsDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Accomodation accomodation : accomodationsDataAll) {
                    if(accomodation.getCity().toLowerCase().contains(filterPattern)
                            || accomodation.getCountry().toLowerCase().contains(filterPattern)
                            || accomodation.getDescription().toLowerCase().contains(filterPattern)) {
                        filteredList.add(accomodation);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            accomodationsData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

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
            Glide.with(context)
                    .load(currentAccomodation.getImage())
                    .transform(new RoundedCorners(140))
                    .into(accomodationImage);
        }
    }
}


