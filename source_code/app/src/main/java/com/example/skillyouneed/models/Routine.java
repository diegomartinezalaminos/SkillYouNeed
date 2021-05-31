package com.example.skillyouneed.models;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.ArrayList;

// Modelo routine de la bbdd
public class Routine implements Serializable {
    private String routineUid, routineName, routineDescription, routineDifficulty;
    private ArrayList<Integer> routineRep;
    private ArrayList<Integer> routineSet;
    private ArrayList<Integer> routineTime;
    private ArrayList<String> routineExerciseFK;

    public Routine() {
    }

    public Routine(String routineUid, String routineName, String routineDescription, String routineDifficulty, ArrayList<Integer> routineRep, ArrayList<Integer> routineSet, ArrayList<Integer> routineTime, ArrayList<String> routineExerciseFK) {
        this.routineUid = routineUid;
        this.routineName = routineName;
        this.routineDescription = routineDescription;
        this.routineDifficulty = routineDifficulty;
        this.routineRep = routineRep;
        this.routineSet = routineSet;
        this.routineTime = routineTime;
        this.routineExerciseFK = routineExerciseFK;
    }

    public String getRoutineUid() {
        return routineUid;
    }

    public void setRoutineUid(String routineUid) {
        this.routineUid = routineUid;
    }

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
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

    public ArrayList<Integer> getRoutineRep() {
        return routineRep;
    }

    public void setRoutineRep(ArrayList<Integer> routineRep) {
        this.routineRep = routineRep;
    }

    public ArrayList<Integer> getRoutineSet() {
        return routineSet;
    }

    public void setRoutineSet(ArrayList<Integer> routineSet) {
        this.routineSet = routineSet;
    }

    public ArrayList<Integer> getRoutineTime() {
        return routineTime;
    }

    public void setRoutineTime(ArrayList<Integer> routineTime) {
        this.routineTime = routineTime;
    }

    public ArrayList<String> getRoutineExerciseFK() {
        return routineExerciseFK;
    }

    public void setRoutineExerciseFK(ArrayList<String> routineExerciseFK) {
        this.routineExerciseFK = routineExerciseFK;
    }
}
