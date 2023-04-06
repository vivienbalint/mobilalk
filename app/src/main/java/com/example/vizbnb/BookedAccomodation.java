package com.example.vizbnb;

import java.util.ArrayList;
import java.util.Date;

public class BookedAccomodation {
    private String accomodationId;
    private String userId;
    private ArrayList<Date> bookedDates;
    private String location;
    private int price;
    private int numberOfDays;
    private String dateText;
    private String id;

    public BookedAccomodation() {
    }

    public BookedAccomodation(String accomodationId, String userId, ArrayList<Date> bookedDates, String location, int price, int numberOfDays, String dateText) {
        this.accomodationId = accomodationId;
        this.userId = userId;
        this.bookedDates = bookedDates;
        this.location = location;
        this.price = price;
        this.numberOfDays = numberOfDays;
        this.dateText = dateText;
    }

    public String getAccomodationId() {
        return accomodationId;
    }

    public String getUserId() {
        return userId;
    }

    public int getPrice() {
        return price;
    }
    public String _getPriceString() { return "Az utazás ára " + price + " Ft"; }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public String getDateText() {
        return dateText;
    }

    public String _getText() { return "Az utazás időtartama: " + dateText + " ,vagyis " + numberOfDays + " nap"; }

    public String getLocation() {
        return location;
    }
    public String _getId() { return id; }
    public ArrayList<Date> getBookedDates() {
        return bookedDates;
    }

    public void setId(String id) {
        this.id = id;
    }
}
