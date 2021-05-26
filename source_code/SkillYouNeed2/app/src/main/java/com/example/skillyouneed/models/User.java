package com.example.skillyouneed.models;

import java.io.Serializable;
import java.util.ArrayList;

// Modelo de user de la bbdd
public class User implements Serializable {

    private String userEmail;
    private String userName;
    private String userUID;
    private ArrayList<String> userSkills;

    public User(String userEmail, String userName, String userUID) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userUID = userUID;
    }

    public User() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public ArrayList<String> getUserSkills() {
        return userSkills;
    }

    public void setUserSkills(ArrayList<String> userSkills) {
        this.userSkills = userSkills;
    }
}
