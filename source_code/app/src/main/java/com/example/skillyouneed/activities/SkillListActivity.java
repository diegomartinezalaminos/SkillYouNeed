package com.example.skillyouneed.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.skillyouneed.R;
import com.example.skillyouneed.models.Skill;
import com.example.skillyouneed.models.Type;
import com.example.skillyouneed.reycles.SkillListAdapterRecycler;
import com.example.skillyouneed.reycles.SkillListOnClickListener;
import com.example.skillyouneed.utilities.Constant;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/*  En esta actividad mostramos todas las skills disponibles junto con un desplegable donde
    se montrarán la rutina para cada una*/
public class SkillListActivity extends AppCompatActivity implements SkillListOnClickListener {

    //Variables
    private Type fatherType = null;
    private RecyclerView myRecyclerView;
    private FloatingActionButton myButtonProfile;
    private SearchView mySearchView;
    private FirebaseFirestore myDDBB;
    private CollectionReference index;
    private SkillListAdapterRecycler myAdapter;
    private ChipGroup chipGroupFilterGadget;
    private ImageButton imageButtonShowFiltersFinalSkillList, imageButtonGadgetChinUpBar, imageButtonGadgetFloor, imageButtonGadgetGymnasticRings,
            imageButtonGadgetParallel, imageButtonGadgetRack;
    private Boolean pushShowButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_list);

        initDataBase();
        initLayout();
        initEvents();
    }

    private void initLayout() {
        imageButtonShowFiltersFinalSkillList = (ImageButton) findViewById(R.id.imageButtonShowFiltersFinalSkillList);
        chipGroupFilterGadget = (ChipGroup) findViewById(R.id.chipGroupFilterButtonFinalSkillList);
        imageButtonGadgetChinUpBar = (ImageButton) findViewById(R.id.imageButtonGadgetChinUpBar);
        imageButtonGadgetFloor = (ImageButton) findViewById(R.id.imageButtonGadgetFloor);
        imageButtonGadgetGymnasticRings = (ImageButton) findViewById(R.id.imageButtonGadgetGymnasticRings);
        imageButtonGadgetParallel = (ImageButton) findViewById(R.id.imageButtonGadgetParallel);
        imageButtonGadgetRack = (ImageButton) findViewById(R.id.imageButtonGadgetRack);
        mySearchView = (SearchView) findViewById(R.id.searchViewSearchFinalSkillList);
        myRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewOptionsFinalSkillList);
        myButtonProfile = (FloatingActionButton) findViewById(R.id.floatingActionButtonProfileFinalSkillList);
        recyclerView();
    }

    private void initDataBase() {
        myDDBB = FirebaseFirestore.getInstance();
        getFatherObject();
        index = myDDBB.collection("skill");
    }

    private void initEvents() {
        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFilter(query, "skillName");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFilter(newText, "skillName");
                return false;
            }
        });

        imageButtonShowFiltersFinalSkillList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushShowButton = !pushShowButton;
                if (pushShowButton){
                    chipGroupFilterGadget.setVisibility(View.VISIBLE);
                }else {
                    chipGroupFilterGadget.setVisibility(View.GONE);
                }
            }
        });

        imageButtonGadgetChinUpBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageButtonGadgetFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageButtonGadgetGymnasticRings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageButtonGadgetParallel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageButtonGadgetRack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        myButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SkillListActivity.this, UserProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getFatherObject() {
        Bundle obb = getIntent().getExtras();
        if (obb != null){
            fatherType = (Type) obb.getSerializable(Constant.MAIN_TYPE);
            //TODO Los Snackbar comentados dan error (pendiente de solucion)
        }else{
            Snackbar.make(myButtonProfile, "Ha ocurrido un error pasado el obb", Snackbar.LENGTH_SHORT).show();

        }
    }

    private  void recyclerView(){
        Query query = myDDBB
                .collection(index.getPath())
                .whereEqualTo("skillTypeFK", fatherType.getTypeUid())
                .orderBy("skillName");

        FirestoreRecyclerOptions<Skill> myRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Skill>()
                .setLifecycleOwner(this)
                .setQuery(query, Skill.class)
                .build();

        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new SkillListAdapterRecycler(myRecyclerOptions,this);
        myAdapter.notifyDataSetChanged();
        myRecyclerView.setAdapter(myAdapter);
    }

    private void searchFilter(String strSearch, String fieldName){
        Query query = myDDBB
                .collection(index.getPath())
                .whereEqualTo("skillTypeFK", fatherType.getTypeUid())
                .orderBy(fieldName)
                .startAt(strSearch)
                .endAt(strSearch + "\uf8ff");

        FirestoreRecyclerOptions<Skill> myRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Skill>()
                .setLifecycleOwner(this)
                .setQuery(query, Skill.class)
                .build();

        myAdapter = new SkillListAdapterRecycler(myRecyclerOptions,this);
        myAdapter.notifyDataSetChanged();
        myRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onIntemClick(Skill model, int position) {
        Log.d("Item_Click", "click item : " + position + " and the id is : " + model.getSkillUid());
    }


}