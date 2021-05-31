package com.example.skillyouneed.models;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.ArrayList;

public class Skill implements Serializable {

    private String skillUid, skillName, skillDescription, skillDifficulty, skillUrlIcon,
            skillUrlVideo, skillGadgetFK, skillTypeFK;
    private ArrayList<String> skillRoutineFK;

    public Skill() {
    }

    public Skill(String skillUid, String skillName, String skillDescription, String skillDifficulty, String skillUrlIcon, String skillUrlVideo, String skillGadgetFK, String skillTypeFK, ArrayList<String> skillRoutineFK) {
        this.skillUid = skillUid;
        this.skillName = skillName;
        this.skillDescription = skillDescription;
        this.skillDifficulty = skillDifficulty;
        this.skillUrlIcon = skillUrlIcon;
        this.skillUrlVideo = skillUrlVideo;
        this.skillGadgetFK = skillGadgetFK;
        this.skillTypeFK = skillTypeFK;
        this.skillRoutineFK = skillRoutineFK;
    }

    public String getSkillUid() {
        return skillUid;
    }

    public void setSkillUid(String skillUid) {
        this.skillUid = skillUid;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }

    public String getSkillDifficulty() {
        return skillDifficulty;
    }

    public void setSkillDifficulty(String skillDifficulty) {
        this.skillDifficulty = skillDifficulty;
    }

    public String getSkillUrlIcon() {
        return skillUrlIcon;
    }

    public void setSkillUrlIcon(String skillUrlIcon) {
        this.skillUrlIcon = skillUrlIcon;
    }

    public String getSkillUrlVideo() {
        return skillUrlVideo;
    }

    public void setSkillUrlVideo(String skillUrlVideo) {
        this.skillUrlVideo = skillUrlVideo;
    }

    public String getSkillGadgetFK() {
        return skillGadgetFK;
    }

    public void setSkillGadgetFK(String skillGadgetFK) {
        this.skillGadgetFK = skillGadgetFK;
    }

    public String getSkillTypeFK() {
        return skillTypeFK;
    }

    public void setSkillTypeFK(String skillTypeFK) {
        this.skillTypeFK = skillTypeFK;
    }

    public ArrayList<String> getSkillRoutineFK() {
        return skillRoutineFK;
    }

    public void setSkillRoutineFK(ArrayList<String> skillRoutineFK) {
        this.skillRoutineFK = skillRoutineFK;
    }
}
