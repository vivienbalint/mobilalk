package com.example.vizbnb;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BookFragment extends Fragment {
    private Accomodation accomodation;
    private TextView currentLocation;
    private TextView currentText;
    private TextView currentPrice;
    private TextView dateText;
    private ArrayList<Date> bookedDates = new ArrayList<>();
    private FirebaseUser user;
    private Button bookBtn;
    private Button datePickerBtn;
    private int price;
    private int numberOfDays;
    private String dateString;
    private NotificationHandler notificationHandler;

    public BookFragment(Accomodation accomodation) {
        this.accomodation = accomodation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        notificationHandler = new NotificationHandler(getContext());

        currentLocation = view.findViewById(R.id.currentLocation);
        currentText = view.findViewById(R.id.currentText);
        currentPrice = view.findViewById(R.id.currentPrice);
        dateText = view.findViewById(R.id.chosenDateText);

        datePickerBtn = view.findViewById(R.id.datePickerBtn);
        bookBtn = view.findViewById(R.id.currentBookBtn);

        bookBtn.setEnabled(false);
        bookBtn.setBackgroundColor(Color.LTGRAY);

        bookBtn.setOnClickListener(v -> createBooking());
        setDate();

        dateText.setText("Válassz dátumot!");
        currentLocation.setText(accomodation.getLocation());
        currentText.setText(accomodation.getDescription());
        currentPrice.setText(accomodation.getPriceString());

        return view;
    }

    private void setDate() {
        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
        materialDateBuilder.setTitleText("select date");
        final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = materialDateBuilder.build();
        datePickerBtn.setOnClickListener(v -> materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER"));

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            bookBtn.setEnabled(true);
            bookBtn.setBackgroundColor(Color.parseColor("#FFC93C"));
            Pair<Long, Long> selectedDates = materialDatePicker.getSelection();
            numberOfDays = (int) TimeUnit.MILLISECONDS.toDays(selectedDates.second - selectedDates.first);
            price = accomodation.getPrice() * numberOfDays;
            dateString = materialDatePicker.getHeaderText();

            Date dateStart = new Date(selectedDates.first);
            Date dateEnd = new Date(selectedDates.second);

            Calendar cal = Calendar.getInstance();
            cal.setTime(dateStart);
            bookedDates.add(dateStart);
            while (cal.getTime().before(dateEnd)) {
                cal.add(Calendar.DATE, 1);
                bookedDates.add(cal.getTime());
            }

            dateText.setText("A választott intervallum: " + dateString);
            currentPrice.setText("Az utazás ára " + numberOfDays + " napra " + price + " Ft");
        });
    }

    private void createBooking() {
        CollectionReference accomodationCollection = FirebaseFirestore.getInstance().collection("Accomodations");
        CollectionReference userCollection = FirebaseFirestore.getInstance().collection("Users");
        CollectionReference bookingCollection = FirebaseFirestore.getInstance().collection("Booked");

        BookedAccomodation bookedAccomodation = new BookedAccomodation(accomodation._getId(), user.getUid(), bookedDates, accomodation.getLocation(), price, numberOfDays, dateString);

        accomodationCollection.whereEqualTo(FieldPath.documentId(), accomodation._getId()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Accomodation queryAccomodation = document.toObject(Accomodation.class);
                for (Date date : bookedDates) {
                    try {
                        queryAccomodation.addNewDate(date);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                accomodationCollection.document(document.getId()).update("bookedDates", queryAccomodation.getBookedDates());
            }

            bookingCollection.add(bookedAccomodation).addOnCompleteListener(task -> {
                String bookingId = task.getResult().getId();

                userCollection.whereEqualTo("id", user.getUid()).get().addOnSuccessListener(queryDocumentSnapshots2 -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots2) {
                        User queryUser = document.toObject(User.class);
                        queryUser.addBookedAccomodation(bookingId);
                        userCollection.document(document.getId()).update("bookedAccomodations", queryUser.getBookedAccomodations());
                    }
                });

                sendNotification(bookedAccomodation);

                Toast.makeText(getContext(), "Utazás sikeresen lefoglalva!", Toast.LENGTH_LONG).show();
                bookBtn.setEnabled(false);
                bookBtn.setBackgroundColor(Color.LTGRAY);
            });
        });
    }

    private void sendNotification(BookedAccomodation bookedAccomodation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            } else {
                notificationHandler.createChannel();
                notificationHandler.send("Utazás sikeresen lefoglalva " + bookedAccomodation.getDateText() + " között, az alábbi helyszínen: " + bookedAccomodation.getLocation());
            }
        } else
            notificationHandler.send("Utazás sikeresen lefoglalva " + bookedAccomodation.getDateText() + " között, az alábbi helyszínen: " + bookedAccomodation.getLocation());
    }
}