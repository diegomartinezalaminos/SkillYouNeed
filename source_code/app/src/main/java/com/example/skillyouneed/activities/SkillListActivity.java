package com.example.skillyouneed.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.skillyouneed.R;
import com.example.skillyouneed.models.Exercise;
import com.example.skillyouneed.models.Gadget;
import com.example.skillyouneed.models.Skill;
import com.example.skillyouneed.models.Type;
import com.example.skillyouneed.reycles.adapters.ManageAddRoutinesExerciseAdapterRecycler;
import com.example.skillyouneed.reycles.adapters.SkillListAdapterRecycler;
import com.example.skillyouneed.reycles.adapters.SkillListGadgetFilterAdapterRecycler;
import com.example.skillyouneed.reycles.listeners.SkillListOnClickListener;
import com.example.skillyouneed.utilities.Constant;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

/*  En esta actividad mostramos todas las skills disponibles junto con un desplegable donde
    se montrarán la rutina para cada una*/
public class SkillListActivity extends AppCompatActivity {

    //DB
    private Type fatherType = null;
    private FirebaseFirestore myDB;
    private CollectionReference index;
    //Layout
    private RecyclerView recyclerViewOptionsSkillList, recyclerViewGadgetSkillList;
    private SearchView searchViewSearchSkillList;
    private ImageButton imageButtonShowFiltersSkillList;
    private FloatingActionButton floatingActionButtonProfileSkillList;
    //RecyclerView (initRecyclerViewSkill();)
    SkillListAdapterRecycler skillListAdapterRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_list);

        initDataBase();
        initLayout();
        initEvents();
    }
    private void initDataBase() {
        myDB = FirebaseFirestore.getInstance();
        getFatherObject();
        index = myDB.collection("skill");
    }

    private void getFatherObject() {
        Bundle obb = getIntent().getExtras();
        if (obb != null){
            fatherType = (Type) obb.getSerializable(Constant.MAIN_TYPE);
        }else{
            Log.w("Warnign:", "Ha ocurrido un error con el fatherType");
        }
    }

    private void initLayout() {
        recyclerViewOptionsSkillList = (RecyclerView) findViewById(R.id.recyclerViewOptionsSkillList);
        recyclerViewGadgetSkillList = (RecyclerView) findViewById(R.id.recyclerViewGadgetSkillList);
        searchViewSearchSkillList = (SearchView) findViewById(R.id.searchViewSearchSkillList);
        imageButtonShowFiltersSkillList = (ImageButton) findViewById(R.id.imageButtonShowFiltersSkillList);
        floatingActionButtonProfileSkillList = (FloatingActionButton) findViewById(R.id.floatingActionButtonProfileSkillList);
        initRecyclerViewSkill();

    }

    private void initRecyclerViewSkill() {
        Query query = index
                .orderBy("skillName");

        FirestoreRecyclerOptions<Skill> myRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Skill>()
                .setLifecycleOwner(this)
                .setQuery(query, Skill.class)
                .build();

        recyclerViewOptionsSkillList.setLayoutManager(new LinearLayoutManager(this));
        skillListAdapterRecycler = new SkillListAdapterRecycler(myRecyclerOptions);
        skillListAdapterRecycler.notifyDataSetChanged();
        recyclerViewOptionsSkillList.setAdapter(skillListAdapterRecycler);
    }

    private void initEvents() {

    }
}