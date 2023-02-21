package com.example.senderemailaut.Model;

public class Receiver {
    String fullName;
    String email;
    String city;

    public Receiver() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Receiver(String date) {
        this.date = date;
    }

    String date;

    public Receiver(String fullName, String email, String city) {
        this.fullName = fullName;
        this.email = email;
        this.city = city;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
