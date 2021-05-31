package com.example.skillyouneed.models;

import java.io.Serializable;
import java.util.ArrayList;

// Modelo de user de la bbdd
public class User implements Serializable {

    private String userUid, userEmail, userName;
    private ArrayList<String> userSkill;

    public User() {
    }

    public User(String userUid, String userEmail, String userName) {
        this.userUid = userUid;
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public User(String userUid, String userEmail, String userName, ArrayList<String> userSkill) {
        this.userUid = userUid;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userSkill = userSkill;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
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

    public ArrayList<String> getUserSkill() {
        return userSkill;
    }

    public void setUserSkill(ArrayList<String> userSkill) {
        this.userSkill = userSkill;
    }
}
