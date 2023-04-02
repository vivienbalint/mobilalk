package com.example.vizbnb;

import java.util.ArrayList;

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private ArrayList<Accomodation> favoriteAccomodations;
    private ArrayList<Accomodation> bookedAccomodations;

    public User(String email, String firstName, String lastName, String city, String country) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.country = country;
        this.favoriteAccomodations = new ArrayList<>();
        this.bookedAccomodations = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    public ArrayList<Accomodation> getFavoriteAccomodations() {
        return favoriteAccomodations;
    }

    public ArrayList<Accomodation> getBookedAccomodations() {
        return bookedAccomodations;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void addFavoriteAccomodation(Accomodation accomodation) {
        favoriteAccomodations.add(accomodation);
    }

    public void removeFavoriteAccomodation(Accomodation accomodation) {
        favoriteAccomodations.remove(accomodation);
    }

    public void addBookedAccomodation(Accomodation accomodation) {
        bookedAccomodations.add(accomodation);
    }

    public void removeBookedAccomodation(Accomodation accomodation) {
        bookedAccomodations.remove(accomodation);
    }
}
