package com.example.mariyamasud.maps.register;

import android.widget.CheckBox;

/**
 * Created by mariyamasud on 19.02.18.
 */

public class Register {
    private String id;
    private String name;
    private String dateOfBirth;
    private String language;
    private String address;
    private String phone;
    private String email;
    private String group;
    private String occupation;
    private String password;

    public Register() {
        //this constructor is required
    }

    public Register(String id, String name, String dateOfBirth, String language, String address, String phone, String email, String group,String occupation,String password) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.language = language;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.group = group;
        this.occupation = occupation;
        this.password = password;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public String getLanguage() {
        return language;
    }
    public String getAddress() {
        return address;
    }
    public String getPhone() {
        return phone;
    }
    public String getEmail() {
        return email;
    }
    public String getGroup() {
        return group;
    }
    public String getOccupation() {
        return occupation;
    }
    public String getPassword() {
        return password;
    }
}
