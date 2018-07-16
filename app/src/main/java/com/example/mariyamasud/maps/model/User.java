package com.example.mariyamasud.maps.model;

public class User {

    private String username;
    private String phoneNumber;
    private String email;
    private String profilePicLocation;

    public User(){

    }

    public User(String name,String email){
        this.username = name;
        this.phoneNumber = phoneNumber;
        this.email = email;

    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public String getEmail() {
        return email;
    }

    public String getProfilePicLocation() {
        return profilePicLocation;
    }

}
