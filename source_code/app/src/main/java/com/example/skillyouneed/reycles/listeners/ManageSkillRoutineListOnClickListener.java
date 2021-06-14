package com.example.skillyouneed.reycles.listeners;

import com.example.skillyouneed.models.Routine;

import java.util.ArrayList;

public interface ManageSkillRoutineListOnClickListener {
    void onItemClick(Routine model, int position, ArrayList<String> routineUidArrayList);
}
