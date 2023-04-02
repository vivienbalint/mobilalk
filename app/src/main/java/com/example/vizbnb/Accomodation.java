package com.example.vizbnb;

import java.util.Date;
import java.util.HashSet;

public class Accomodation {
    private String city;
    private String country;
    private String description;
    private int price;
    private final int image;
    private HashSet<Date> bookedDates;

    public Accomodation(String city, String country, String description, int price, int image) {
        this.city = city;
        this.country = country;
        this.description = description;
        this.price = price;
        this.image = image;
        this.bookedDates = new HashSet<Date>() {
        };
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

    public String getPriceString() { return price + " Ft/nap"; }

    public int getImage() {
        return image;
    }

    public HashSet<Date> getBookedDates() {
        return bookedDates;
    }

    public void addNewDate(Date date) throws Exception {
        if (bookedDates.contains(date)) {
            throw new Exception("A kiválasztott dátumon nincs szabad szoba!");
        } else {
            bookedDates.add(date);
        }
    }
}
