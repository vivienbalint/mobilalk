package com.example.vizbnb;

import java.util.ArrayList;
import java.util.Date;

public class BookedAccomodation {
    private String id;
    private ArrayList<Date> bookedDates;

    public BookedAccomodation() {
    }

    public BookedAccomodation(String id, ArrayList<Date> bookedDates) {
        this.id = id;
        this.bookedDates = bookedDates;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Date> getBookedDates() {
        return bookedDates;
    }
}
