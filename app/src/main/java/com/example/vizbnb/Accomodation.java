package com.example.vizbnb;

import java.util.ArrayList;
import java.util.Date;

public class Accomodation {
    private String id;
    private String city;
    private String country;
    private String description;
    private int price;
    private int image;
    private ArrayList<Date> bookedDates;
    public Accomodation() {
    }

    public Accomodation(String city, String country, String description, int price, int image) {
        this.city = city;
        this.country = country;
        this.description = description;
        this.price = price;
        this.image = image;
        this.bookedDates = new ArrayList<>();
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getLocation() {
        return city + ", " + country;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getPriceString() {
        return price + " Ft/nap";
    }

    public int getImage() {
        return image;
    }

    public String _getId() {
        return id;
    }

    public ArrayList<Date> getBookedDates() {
        return bookedDates;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addNewDate(Date date) throws Exception {
        if (bookedDates.contains(date)) {
            throw new Exception("A kiválasztott dátumon nincs szabad szoba!");
        } else bookedDates.add(date);
    }
}
