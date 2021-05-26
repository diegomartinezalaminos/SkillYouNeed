package com.example.skillyouneed.models;

import com.google.firebase.firestore.DocumentReference;

//Modelo Exercise de la bbdd
public class Exercise {

    private String exerciseDescription,exerciseDifficulty,
            exerciseName, exerciseUid, exerciseUrlIcon, exerciseUrlVideo;
    private DocumentReference exerciseReference;

    public Exercise() {
    }

    public Exercise(String exerciseDescription, String exerciseDifficulty, String exerciseName, String exerciseUid, String exerciseUrlIcon, String exerciseUrlVideo, DocumentReference exerciseReference) {
        this.exerciseDescription = exerciseDescription;
        this.exerciseDifficulty = exerciseDifficulty;
        this.exerciseName = exerciseName;
        this.exerciseUid = exerciseUid;
        this.exerciseUrlIcon = exerciseUrlIcon;
        this.exerciseUrlVideo = exerciseUrlVideo;
        this.exerciseReference = exerciseReference;
    }

    public DocumentReference getExerciseReference() {
        return exerciseReference;
    }

    public void setExerciseReference(DocumentReference exerciseReference) {
        this.exerciseReference = exerciseReference;
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

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseUid() {
        return exerciseUid;
    }

    public void setExerciseUid(String exerciseUid) {
        this.exerciseUid = exerciseUid;
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
