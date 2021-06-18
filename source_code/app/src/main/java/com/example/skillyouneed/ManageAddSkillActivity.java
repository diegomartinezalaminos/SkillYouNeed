package com.example.skillyouneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.skillyouneed.adapters.SpinnerDifficultyAdapter;
import com.example.skillyouneed.models.Dyfficulty;
import com.example.skillyouneed.models.Gadget;
import com.example.skillyouneed.models.Routine;
import com.example.skillyouneed.models.Skill;
import com.example.skillyouneed.models.Type;
import com.example.skillyouneed.reycles.adapters.ManageSkillGadgetListAdapterRecycler;
import com.example.skillyouneed.reycles.listeners.ManageSkillGadgetListOnClickListener;
import com.example.skillyouneed.reycles.adapters.ManageSkillRoutineListAdapterRecycler;
import com.example.skillyouneed.reycles.listeners.ManageSkillRoutineListOnClickListener;
import com.example.skillyouneed.reycles.adapters.ManageSkillTypeListAdapterRecycler;
import com.example.skillyouneed.reycles.listeners.ManageSkillTypeListOnClickListener;
import com.example.skillyouneed.utilities.SentencesFirestore;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class ManageAddSkillActivity extends AppCompatActivity implements ManageSkillTypeListOnClickListener, ManageSkillGadgetListOnClickListener, ManageSkillRoutineListOnClickListener, AdapterView.OnItemSelectedListener {

    //Spinner
    private ArrayList<Dyfficulty> difficultyArrayList;
    private SpinnerDifficultyAdapter spinnerDifficultyAdapter;
    private String difficulty;
    //DB
    private FirebaseFirestore myDB;
    private CollectionReference index;
    //OnCLick
    private String typeUid = "", gadgetUid = "";
    private ArrayList<String> routineUidArrayList = new ArrayList<>();
    //Adapters Recycler
    private ManageSkillTypeListAdapterRecycler typeAdapter;
    private ManageSkillGadgetListAdapterRecycler gadgetAdapter;
    private ManageSkillRoutineListAdapterRecycler routineAdapter;
    //Layout
    private TextInputEditText textInputEditTextNameManageSkill, textInputEditTextDescriptionManageSkill,
            textInputEditTextUrlIconManageSkill, textInputEditTextUrlVideoManageSkill;
    private Spinner spinnerDifficultyManageSkill;
    private RecyclerView recyclerViewTypeManageSkill, recyclerViewGadgetManageSkill, recyclerViewRoutineManageSkill;
    private ImageButton imageButtonSaveManageSkill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_skill);

        intiDataBase();
        initLayout();
        events();
    }
    private void intiDataBase() {
        myDB = FirebaseFirestore.getInstance();
        index = myDB.collection("skill");

    }

    private void initLayout() {
        textInputEditTextNameManageSkill = (TextInputEditText) findViewById(R.id.textInputEditTextNameManageSkill);
        textInputEditTextDescriptionManageSkill = (TextInputEditText) findViewById(R.id.textInputEditTextDescriptionManageSkill);
        textInputEditTextUrlIconManageSkill = (TextInputEditText) findViewById(R.id.textInputEditTextUrlIconManageSkill);
        textInputEditTextUrlVideoManageSkill = (TextInputEditText) findViewById(R.id.textInputEditTextUrlVideoManageSkill);
        spinnerDifficultyManageSkill = (Spinner) findViewById(R.id.spinnerDifficultyManageSkill);
        recyclerViewTypeManageSkill = (RecyclerView) findViewById(R.id.recyclerViewTypeManageSkill);
        recyclerViewGadgetManageSkill = (RecyclerView) findViewById(R.id.recyclerViewGadgetManageSkill);
        recyclerViewRoutineManageSkill = (RecyclerView) findViewById(R.id.recyclerViewRoutineManageSkill);
        imageButtonSaveManageSkill = (ImageButton) findViewById(R.id.imageButtonSaveManageSkill);
        initRecyclerType();
        initRecyclerGadget();
        initRecyclerRoutine();
        llenarLista();
        initSpinner();
        initSpinner();
    }

    public void llenarLista(){
        difficultyArrayList = new ArrayList<>();
        difficultyArrayList.add(new Dyfficulty(0, "-- Select Difficulty --"));
        difficultyArrayList.add(new Dyfficulty(R.drawable.ic_logo, "Facil"));
        difficultyArrayList.add(new Dyfficulty(R.drawable.ic_logo, "Medio"));
        difficultyArrayList.add(new Dyfficulty(R.drawable.ic_logo, "Dificil"));
    }


    private  void initSpinner(){

        spinnerDifficultyAdapter = new SpinnerDifficultyAdapter(this, difficultyArrayList, 0);
        spinnerDifficultyManageSkill.setAdapter(spinnerDifficultyAdapter);
        spinnerDifficultyManageSkill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Dyfficulty difficultyObb = difficultyArrayList.get(position);
                difficulty = difficultyObb.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRecyclerRoutine() {
        recyclerViewRoutineManageSkill.setLayoutManager(new LinearLayoutManager(this));

        Query query = myDB
                .collection("routine");

        FirestoreRecyclerOptions<Routine> myOptions = new FirestoreRecyclerOptions
                .Builder<Routine>()
                .setLifecycleOwner(this)
                .setQuery(query, Routine.class)
                .build();

        routineAdapter = new ManageSkillRoutineListAdapterRecycler(myOptions, this);
        routineAdapter.notifyDataSetChanged();
        recyclerViewRoutineManageSkill.setAdapter(routineAdapter);
    }

    private void initRecyclerGadget() {
        recyclerViewGadgetManageSkill.setLayoutManager(new LinearLayoutManager(this));

        Query query = myDB
                .collection("gadget");

        FirestoreRecyclerOptions<Gadget> myOptions = new FirestoreRecyclerOptions
                .Builder<Gadget>()
                .setLifecycleOwner(this)
                .setQuery(query, Gadget.class)
                .build();

        gadgetAdapter = new ManageSkillGadgetListAdapterRecycler(myOptions, this);
        gadgetAdapter.notifyDataSetChanged();
        recyclerViewGadgetManageSkill.setAdapter(gadgetAdapter);
    }

    private void initRecyclerType() {
        recyclerViewTypeManageSkill.setLayoutManager(new LinearLayoutManager(this));

        Query query = myDB
                .collection("type");

        FirestoreRecyclerOptions<Type> myOptions = new FirestoreRecyclerOptions
                .Builder<Type>()
                .setLifecycleOwner(this)
                .setQuery(query, Type.class)
                .build();

        typeAdapter = new ManageSkillTypeListAdapterRecycler(myOptions, this);
        typeAdapter.notifyDataSetChanged();
        recyclerViewTypeManageSkill.setAdapter(typeAdapter);
    }

    private void events() {
        imageButtonSaveManageSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "", description = "", urlIcon = "" , urlVideo = "";
                name = textInputEditTextNameManageSkill.getText().toString();
                description = textInputEditTextDescriptionManageSkill.getText().toString();
                urlIcon = textInputEditTextUrlIconManageSkill.getText().toString();
                urlVideo = textInputEditTextUrlVideoManageSkill.getText().toString();

                if (!name.isEmpty() && !description.isEmpty() && !urlIcon.isEmpty() && !urlVideo.isEmpty() && !difficulty.isEmpty() && !typeUid.isEmpty() && !gadgetUid.isEmpty() && !routineUidArrayList.isEmpty()){
                    SentencesFirestore sentence = new SentencesFirestore();
                    String solution = sentence.stringUrlFormat(urlVideo);
                    if (solution.equals("error")){
                        Snackbar.make(textInputEditTextUrlVideoManageSkill, "La url tiene que ser de YT y válida", Snackbar.LENGTH_SHORT).show();
                    }else {
                        urlVideo = solution;
                        Skill skillObb = new Skill(null, name, description, difficulty, urlIcon, urlVideo, gadgetUid, typeUid, routineUidArrayList);
                        addSkill(skillObb);
                        initRecyclerRoutine();
                    }
                }else {
                    Snackbar.make(v, "Todos los campos son obligatorios", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void addSkill(Skill obb){

        myDB.collection(index.getPath())
                .add(obb)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        clearAllFile();
                        documentReference.update("skillUid", documentReference.getId())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.w("WarningManageSkill: ", "El campo skillUid se ha actualizado correctamente");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("WarningManageSkill: ", "Error al actualizar el campo skillUid del documento", e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("WarningManageSkill: ", "Error añadiendo el documento", e);
                    }
                });
    }

    private void clearAllFile() {
        textInputEditTextNameManageSkill.setText("");
        textInputEditTextDescriptionManageSkill.setText("");
        textInputEditTextUrlIconManageSkill.setText("");
        textInputEditTextUrlVideoManageSkill.setText("");
        routineUidArrayList.clear();
        typeUid = "";
        gadgetUid = "";
    }

    @Override
    public void OnItemClick(Type model, int position) {
        typeUid = model.getTypeUid();
        Log.d("DebugManageSkill: ", "typeUid = " + typeUid);
    }

    @Override
    public void onItemClick(Gadget model, int position) {
        gadgetUid = model.getGadgetUid();
        Log.d("DebugManageSkill: ", "gadgetUid = " + gadgetUid);
    }

    @Override
    public void onItemClick(Routine model, int position, ArrayList<String> routineUidArrayList) {
        this.routineUidArrayList = routineUidArrayList;
        Log.d("DebugManageSkill: ", "--------- ArrayRutina clase ManageSkills ---------");
        for (int u = 0; u < this.routineUidArrayList.size(); u++){
            Log.d("DebugManageSkill: ", "[" + u + "] --> "  + this.routineUidArrayList.get(u));
        }
        Log.d("DebugManageSkill: ", "---------------------------------------------------");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        difficulty = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        difficulty = parent.getItemAtPosition(0).toString();
    }
}