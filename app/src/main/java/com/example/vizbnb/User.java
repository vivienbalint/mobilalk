package com.example.vizbnb;

import java.util.ArrayList;

public class User {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private ArrayList<String> bookedAccomodations;

    public User() {
    }

    public User(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public ArrayList<String> getBookedAccomodations() {
        return bookedAccomodations;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void addBookedAccomodation(String accomodation) {
        bookedAccomodations.add(accomodation);
    }

    public void removeBookedAccomodation(String accomodation) {
        bookedAccomodations.remove(accomodation);
    }
}
