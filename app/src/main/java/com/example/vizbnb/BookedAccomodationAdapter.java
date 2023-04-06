package com.example.vizbnb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;

public class BookedAccomodationAdapter extends RecyclerView.Adapter<BookedAccomodationAdapter.ViewHolder> {

    private ArrayList<BookedAccomodation> accomodationsData;
    private Context context;
    private int lastPosition = -1;

    public BookedAccomodationAdapter(Context context, ArrayList<BookedAccomodation> accomodations) {
        this.accomodationsData = accomodations;
        this.context = context;
    }

    @Override
    public BookedAccomodationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookedAccomodationAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_booked_accomodations, parent, false));
    }

    @Override
    public void onBindViewHolder(BookedAccomodationAdapter.ViewHolder holder, int position) {
        BookedAccomodation currentAccomodation = accomodationsData.get(position);
        holder.bindTo(currentAccomodation);

        if (holder.getBindingAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getBindingAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return accomodationsData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView bookedAccomodationLocation;
        private TextView bookedAccomodationDate;
        private TextView bookedAccomodationPrice;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookedAccomodationLocation = itemView.findViewById(R.id.bookedAccomodationLocation);
            bookedAccomodationDate = itemView.findViewById(R.id.bookedAccomodationDate);
            bookedAccomodationPrice = itemView.findViewById(R.id.bookedAccomodationPrice);
        }

        public void bindTo(BookedAccomodation currentAccomodation) {
            bookedAccomodationLocation.setText(currentAccomodation.getLocation());
            bookedAccomodationDate.setText(currentAccomodation._getText());
            bookedAccomodationPrice.setText(currentAccomodation._getPriceString());

            Button removeBtn = itemView.findViewById(R.id.removeBtn);
            removeBtn.setOnClickListener(view -> {
                deleteFromDb(currentAccomodation);
                accomodationsData.remove(getBindingAdapterPosition());
                notifyItemRemoved(getBindingAdapterPosition());
                notifyItemRangeChanged(getBindingAdapterPosition(), accomodationsData.size() - getBindingAdapterPosition());
            });
        }

        private void deleteFromDb(BookedAccomodation currentAccomodation) {
            CollectionReference accomodationCollection = FirebaseFirestore.getInstance().collection("Accomodations");
            CollectionReference bookedCollection = FirebaseFirestore.getInstance().collection("Booked");
            CollectionReference userCollection = FirebaseFirestore.getInstance().collection("Users");

            DocumentReference bookedRef = bookedCollection.document(currentAccomodation._getId());
            bookedRef.delete().addOnSuccessListener(unused -> {
                DocumentReference accomodationRef = accomodationCollection.document(currentAccomodation.getAccomodationId());
                accomodationRef.get().addOnCompleteListener(task -> {
                    Accomodation accomodation = task.getResult().toObject(Accomodation.class);
                    ArrayList<Date> dates = accomodation.getBookedDates();
                    for (Date date : currentAccomodation.getBookedDates()) {
                        dates.remove(date);
                    }
                    accomodationRef.update("bookedDates", dates);
                });
                userCollection.whereEqualTo("id", currentAccomodation.getUserId()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        User user = document.toObject(User.class);
                        user.removeBookedAccomodation(currentAccomodation._getId());
                        userCollection.document(document.getId()).update("bookedAccomodations", user.getBookedAccomodations());
                    }
                });
                Toast.makeText(context, "Utazás sikeresen törölve!", Toast.LENGTH_LONG).show();
            });
        }
    }
}
