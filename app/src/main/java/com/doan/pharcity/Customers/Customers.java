package com.doan.pharcity.Customers;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Customers implements Serializable {
    private long id;
    private String name;
    private String phone;
    private String email;
    private String gender;
    private String dateOfBirth;
    private String address;

    public Customers() {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {return dateOfBirth;}

    public void setDateOfBirth(String dateOfBirth) {this.dateOfBirth = dateOfBirth;}

    public String getGender() {return gender;}

    public void setGender(String gender) {this.gender = gender;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}


    @NonNull
    @Override
    public String toString() {
        return name + " - " + phone + " - " + email + " - " + gender + " - " + dateOfBirth + " - " + address;
    }
}
