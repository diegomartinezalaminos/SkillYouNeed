package com.example.skillyouneed.models;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

// Modelo routine de la bbdd
public class Routine {
    private String routineDescription, routineDifficulty, routineName;
    private ArrayList<DocumentReference> routineExercise;
    private  ArrayList<Number> routineRep;
    private ArrayList<Number> routineSet;
    private ArrayList<Number> routineTime;
    private DocumentReference routineReference;

    public Routine() {
    }

    public Routine(String routineDescription, String routineDifficulty, String routineName, ArrayList<DocumentReference> routineExercise, ArrayList<Number> routineRep, ArrayList<Number> routineSet, ArrayList<Number> routineTime, DocumentReference routineReference) {
        this.routineDescription = routineDescription;
        this.routineDifficulty = routineDifficulty;
        this.routineName = routineName;
        this.routineExercise = routineExercise;
        this.routineRep = routineRep;
        this.routineSet = routineSet;
        this.routineTime = routineTime;
        this.routineReference = routineReference;
    }

    public DocumentReference getRoutineReference() {
        return routineReference;
    }

    public void setRoutineReference(DocumentReference routineReference) {
        this.routineReference = routineReference;
    }

    public String getRoutineDescription() {
        return routineDescription;
    }

    public void setRoutineDescription(String routineDescription) {
        this.routineDescription = routineDescription;
    }

    public String getRoutineDifficulty() {
        return routineDifficulty;
    }

    public void setRoutineDifficulty(String routineDifficulty) {
        this.routineDifficulty = routineDifficulty;
    }

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }

    public ArrayList<DocumentReference> getRoutineExercise() {
        return routineExercise;
    }

    public void setRoutineExercise(ArrayList<DocumentReference> routineExercise) {
        this.routineExercise = routineExercise;
    }

    public ArrayList<Number> getRoutineRep() {
        return routineRep;
    }

    public void setRoutineRep(ArrayList<Number> routineRep) {
        this.routineRep = routineRep;
    }

    public ArrayList<Number> getRoutineSet() {
        return routineSet;
    }

    public void setRoutineSet(ArrayList<Number> routineSet) {
        this.routineSet = routineSet;
    }

    public ArrayList<Number> getRoutineTime() {
        return routineTime;
    }

    public void setRoutineTime(ArrayList<Number> routineTime) {
        this.routineTime = routineTime;
    }
}
