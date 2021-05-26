package com.example.skillyouneed.models;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;


//Modelo skill bbdd
public class FinalSkill {

    private String finalSkillDescription, finalSkillDificulty,
            finalSkillName, finalSkillUid, finalSkillUrlIcon, finalSkillUrlVideo;
    private DocumentReference finalSkillReference;
    private ArrayList<DocumentReference> finalSkillRoutine;

    public FinalSkill() {
    }

    public FinalSkill(String finalSkillDescription, String finalSkillDificulty, String finalSkillName, String finalSkillUid, String finalSkillUrlIcon, String finalSkillUrlVideo, DocumentReference finalSkillReference, ArrayList<DocumentReference> finalSkillRoutine) {
        this.finalSkillDescription = finalSkillDescription;
        this.finalSkillDificulty = finalSkillDificulty;
        this.finalSkillName = finalSkillName;
        this.finalSkillUid = finalSkillUid;
        this.finalSkillUrlIcon = finalSkillUrlIcon;
        this.finalSkillUrlVideo = finalSkillUrlVideo;
        this.finalSkillReference = finalSkillReference;
        this.finalSkillRoutine = finalSkillRoutine;
    }

    public String getFinalSkillDescription() {
        return finalSkillDescription;
    }

    public void setFinalSkillDescription(String finalSkillDescription) {
        this.finalSkillDescription = finalSkillDescription;
    }

    public String getFinalSkillDificulty() {
        return finalSkillDificulty;
    }

    public void setFinalSkillDificulty(String finalSkillDificulty) {
        this.finalSkillDificulty = finalSkillDificulty;
    }

    public String getFinalSkillName() {
        return finalSkillName;
    }

    public void setFinalSkillName(String finalSkillName) {
        this.finalSkillName = finalSkillName;
    }

    public String getFinalSkillUid() {
        return finalSkillUid;
    }

    public void setFinalSkillUid(String finalSkillUid) {
        this.finalSkillUid = finalSkillUid;
    }

    public String getFinalSkillUrlIcon() {
        return finalSkillUrlIcon;
    }

    public void setFinalSkillUrlIcon(String finalSkillUrlIcon) {
        this.finalSkillUrlIcon = finalSkillUrlIcon;
    }

    public String getFinalSkillUrlVideo() {
        return finalSkillUrlVideo;
    }

    public void setFinalSkillUrlVideo(String finalSkillUrlVideo) {
        this.finalSkillUrlVideo = finalSkillUrlVideo;
    }

    public DocumentReference getFinalSkillReference() {
        return finalSkillReference;
    }

    public void setFinalSkillReference(DocumentReference finalSkillReference) {
        this.finalSkillReference = finalSkillReference;
    }

    public ArrayList<DocumentReference> getFinalSkillRoutine() {
        return finalSkillRoutine;
    }

    public void setFinalSkillRoutine(ArrayList<DocumentReference> finalSkillRoutine) {
        this.finalSkillRoutine = finalSkillRoutine;
    }
}
