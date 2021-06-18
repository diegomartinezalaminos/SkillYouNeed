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
import com.example.skillyouneed.RoutineActivity;
import com.example.skillyouneed.models.Exercise;
import com.example.skillyouneed.models.Gadget;
import com.example.skillyouneed.models.Routine;
import com.example.skillyouneed.models.Skill;
import com.example.skillyouneed.models.Type;
import com.example.skillyouneed.reycles.adapters.ManageAddRoutinesExerciseAdapterRecycler;
import com.example.skillyouneed.reycles.adapters.ManageSkillRoutineListAdapterRecycler;
import com.example.skillyouneed.reycles.adapters.SkillListAdapterRecycler;
import com.example.skillyouneed.reycles.adapters.SkillListGadgetFilterAdapterRecycler;
import com.example.skillyouneed.reycles.listeners.SkillListGadgetFilterOnClickListener;
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
public class SkillListActivity extends AppCompatActivity implements SkillListGadgetFilterOnClickListener, SkillListOnClickListener {

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
    private SkillListAdapterRecycler skillListAdapterRecycler;
    private SkillListGadgetFilterAdapterRecycler SkillListGadgetFilterAdapterRecycler;
    //imageButtonShowFiltersSkillList
    private Boolean pushShowButton = false;
    private Boolean fistPush = false;

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
                .whereEqualTo("skillTypeFK", fatherType.getTypeUid());

        FirestoreRecyclerOptions<Skill> myRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Skill>()
                .setLifecycleOwner(this)
                .setQuery(query, Skill.class)
                .build();

        recyclerViewOptionsSkillList.setLayoutManager(new LinearLayoutManager(this));
        skillListAdapterRecycler = new SkillListAdapterRecycler(myRecyclerOptions, this);
        skillListAdapterRecycler.notifyDataSetChanged();
        recyclerViewOptionsSkillList.setAdapter(skillListAdapterRecycler);
    }

    private void initEvents() {
        searchViewSearchSkillList.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFilterRoutineList(query, "skillName");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFilterRoutineList(newText, "skillName");
                return false;
            }
        });

        imageButtonShowFiltersSkillList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushShowButton = !pushShowButton;
                if (pushShowButton){
                    recyclerViewGadgetSkillList.setVisibility(View.VISIBLE);
                    if (!fistPush){
                        recyclerViewGadgetFilter();
                        fistPush = true;
                    }

                }else {
                    recyclerViewGadgetSkillList.setVisibility(View.GONE);
                }
            }
        });

        floatingActionButtonProfileSkillList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SkillListActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });



    }

    private void searchFilterRoutineList(String strSearch, String fieldName){

        Query query = myDB
                .collection("skill")
                .orderBy(fieldName)
                .startAt(strSearch)
                .endAt(strSearch + "\uf8ff");

        FirestoreRecyclerOptions<Skill> myRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Skill>()
                .setLifecycleOwner(this)
                .setQuery(query, Skill.class)
                .build();

        recyclerViewOptionsSkillList.setLayoutManager(new LinearLayoutManager(this));
        skillListAdapterRecycler = new SkillListAdapterRecycler(myRecyclerOptions, this);
        skillListAdapterRecycler.notifyDataSetChanged();
        recyclerViewOptionsSkillList.setAdapter(skillListAdapterRecycler);
    }

    private void recyclerViewGadgetFilter(){

        Query query = myDB
                .collection("gadget").orderBy("gadgetName");

        FirestoreRecyclerOptions<Gadget> myRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Gadget>()
                .setLifecycleOwner(this)
                .setQuery(query, Gadget.class)
                .build();

        recyclerViewGadgetSkillList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SkillListGadgetFilterAdapterRecycler = new SkillListGadgetFilterAdapterRecycler(myRecyclerOptions, this);
        SkillListGadgetFilterAdapterRecycler.notifyDataSetChanged();
        recyclerViewGadgetSkillList.setAdapter(SkillListGadgetFilterAdapterRecycler);
    }

    @Override
    public void OnItemClick(Gadget model, int position) {

    }

    @Override
    public void onIntemClick(Skill model, int position) {
        Intent intent = new Intent(SkillListActivity.this, RoutineActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.SKILL_LIST, model);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}