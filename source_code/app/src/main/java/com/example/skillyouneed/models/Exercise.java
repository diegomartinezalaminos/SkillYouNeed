package com.example.skillyouneed.models;

import java.io.Serializable;

//Modelo Exercise de la bbdd
public class Exercise implements Serializable {

    private String exerciseUid, exerciseName, exerciseDescription ,exerciseDifficulty,
            exerciseUrlIcon, exerciseUrlVideo;

    public Exercise() {
    }

    public Exercise(String exerciseUid, String exerciseName, String exerciseDescription, String exerciseDifficulty, String exerciseUrlIcon, String exerciseUrlVideo) {
        this.exerciseUid = exerciseUid;
        this.exerciseName = exerciseName;
        this.exerciseDescription = exerciseDescription;
        this.exerciseDifficulty = exerciseDifficulty;
        this.exerciseUrlIcon = exerciseUrlIcon;
        this.exerciseUrlVideo = exerciseUrlVideo;
    }

    public String getExerciseUid() {
        return exerciseUid;
    }

    public void setExerciseUid(String exerciseUid) {
        this.exerciseUid = exerciseUid;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseDescription() {
        return exerciseDescription;
    }

    public void setExerciseDescription(String exerciseDescription) {
        this.exerciseDescription = exerciseDescription;
    }

    public String getExerciseDifficulty() {
        return exerciseDifficulty;
    }

    public void setExerciseDifficulty(String exerciseDifficulty) {
        this.exerciseDifficulty = exerciseDifficulty;
    }

    public String getExerciseUrlIcon() {
        return exerciseUrlIcon;
    }

    public void setExerciseUrlIcon(String exerciseUrlIcon) {
        this.exerciseUrlIcon = exerciseUrlIcon;
    }

    public String getExerciseUrlVideo() {
        return exerciseUrlVideo;
    }

    public void setExerciseUrlVideo(String exerciseUrlVideo) {
        this.exerciseUrlVideo = exerciseUrlVideo;
    }
}
