package com.example.skillyouneed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.skillyouneed.adapters.SpinnerDifficultyAdapter;
import com.example.skillyouneed.models.Dyfficulty;
import com.example.skillyouneed.models.Exercise;
import com.example.skillyouneed.models.Routine;
import com.example.skillyouneed.reycles.adapters.ManageAddRoutinesExerciseAdapterRecycler;
import com.example.skillyouneed.reycles.adapters.ManageDeleteEditRoutineExerciseListAdapter;
import com.example.skillyouneed.reycles.adapters.ManageDeleteEditRoutineRoutineListAdapter;
import com.example.skillyouneed.reycles.listeners.ManageRoutinesExerciseOnClickListener;
import com.example.skillyouneed.utilities.SentencesFirestore;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.function.LongFunction;

public class ManageDeleteEditRoutineActivity extends AppCompatActivity implements ManageRoutinesExerciseOnClickListener {

    //Spinner
    private ArrayList<Dyfficulty> difficultyArrayList;
    private SpinnerDifficultyAdapter spinnerDifficultyAdapter;
    private String difficulty = "";

    //Code
    private final int EDIT_CODE = 222;
    private final int REFRESH_CODE = 333;
    private final int ADD_CODE = 444;

    //Add exercise
    private String seletExerciseUid = "";
    private ArrayList<String> exerciseUidList = new ArrayList<>();
    private ArrayList<Integer> repList = new ArrayList<>();
    private ArrayList<Integer> timeList = new ArrayList<>();
    private ArrayList<Integer> roundList = new ArrayList<>();

    private MaterialAlertDialogBuilder madb;
    private Routine selectRoutine;
    private ManageDeleteEditRoutineRoutineListAdapter manageDeleteEditRoutineRoutineListAdapter;
    private ManageDeleteEditRoutineExerciseListAdapter manageDeleteEditRoutineExerciseListAdapter;
    private CollectionReference index;
    private FirebaseFirestore myDB;
    private TextInputEditText textInputNameManageDeleteEditRoutine, textInputDescriptionManageDeleteEditRoutine,
            textInputRepeManageDeleteEditRoutine, textInputRoundManageDeleteEditRoutine, textInputTimeManageDeleteEditRoutine;
    private SearchView searchViewRoutineManageDeleteEditRoutine;
    private RecyclerView recyclerViewRoutineManageDeleteEditRoutine, recyclerViewExercise;
    private ImageButton imageButtonSaveRoutineManageDeleteEditRoutine;
    private ConstraintLayout contraintLayoutVisibilityRepRoundTimeManageDeleteEditRoutine;
    private LinearLayout linearLayoutVisibilityEditRoutineManageDeleteEditRoutine;
    private Spinner spinnerDifficultyManageDeleteEditRoutine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_delete_edit_routine);

        intiDataBase();
        initLayout();
        event();
    }

    private void intiDataBase() {
        myDB = FirebaseFirestore.getInstance();
        index = myDB.collection("routine");
    }

    private void initLayout() {
        textInputNameManageDeleteEditRoutine = (TextInputEditText) findViewById(R.id.textInputNameManageDeleteEditRoutine);
        textInputDescriptionManageDeleteEditRoutine = (TextInputEditText) findViewById(R.id.textInputDescriptionManageDeleteEditRoutine);
        textInputRepeManageDeleteEditRoutine = (TextInputEditText) findViewById(R.id.textInputRepeManageDeleteEditRoutine);
        textInputRoundManageDeleteEditRoutine = (TextInputEditText) findViewById(R.id.textInputRoundManageDeleteEditRoutine);
        textInputTimeManageDeleteEditRoutine = (TextInputEditText) findViewById(R.id.textInputTimeManageDeleteEditRoutine);
        searchViewRoutineManageDeleteEditRoutine = (SearchView) findViewById(R.id.searchViewRoutineManageDeleteEditRoutine);
        recyclerViewRoutineManageDeleteEditRoutine = (RecyclerView) findViewById(R.id.recyclerViewRoutineManageDeleteEditRoutine);
        recyclerViewExercise = (RecyclerView) findViewById(R.id.recyclerViewExerciseManageDeleteEditRoutine);
        imageButtonSaveRoutineManageDeleteEditRoutine = (ImageButton) findViewById(R.id.imageButtonSaveRoutineManageDeleteEditRoutine);
        contraintLayoutVisibilityRepRoundTimeManageDeleteEditRoutine = (ConstraintLayout) findViewById(R.id.contraintLayoutVisibilityRepRoundTimeManageDeleteEditRoutine);
        linearLayoutVisibilityEditRoutineManageDeleteEditRoutine = (LinearLayout) findViewById(R.id.linearLayoutVisibilityEditRoutineManageDeleteEditRoutine);
        spinnerDifficultyManageDeleteEditRoutine = (Spinner) findViewById(R.id.spinnerDifficultyManageDeleteEditRoutine);

        llenarLista();
        initRoutineRecyclerView();
    }

    public void llenarLista(){
        difficultyArrayList = new ArrayList<>();
        difficultyArrayList.add(new Dyfficulty(0, "-- Select Difficulty --"));
        difficultyArrayList.add(new Dyfficulty(R.drawable.ic_easy, "Facil"));
        difficultyArrayList.add(new Dyfficulty(R.drawable.ic_midel, "Medio"));
        difficultyArrayList.add(new Dyfficulty(R.drawable.ic_difficult, "Dificil"));
    }

    private  void initSpinner(){

        int pos = 0;

        switch (selectRoutine.getRoutineDifficulty()){
            case "Medio":
                pos = 2;
                break;
            case "Facil":
                pos = 1;
                break;
            case "Dificil":
                pos = 3;
                break;
            default:
                pos = 0;
                break;
        }
        difficulty = difficultyArrayList.get(pos).getName();

        spinnerDifficultyAdapter = new SpinnerDifficultyAdapter(this, difficultyArrayList, pos);
        spinnerDifficultyManageDeleteEditRoutine.setAdapter(spinnerDifficultyAdapter);
        spinnerDifficultyManageDeleteEditRoutine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void initRoutineRecyclerView() {
        Query query = myDB
                .collection(index.getPath());

        FirestoreRecyclerOptions<Routine> myRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Routine>()
                .setLifecycleOwner(this)
                .setQuery(query, Routine.class)
                .build();

        recyclerViewRoutineManageDeleteEditRoutine.setLayoutManager(new LinearLayoutManager(this));
        manageDeleteEditRoutineRoutineListAdapter = new ManageDeleteEditRoutineRoutineListAdapter(myRecyclerOptions, (model, cod) -> doActionRoutineList(model, cod));
        manageDeleteEditRoutineRoutineListAdapter.notifyDataSetChanged();
        recyclerViewRoutineManageDeleteEditRoutine.setAdapter(manageDeleteEditRoutineRoutineListAdapter);
    }

    private void doActionRoutineList(Routine model, int cod) {
        selectRoutine = model;
        initSpinner();
        switch (cod){
            case EDIT_CODE:
                linearLayoutVisibilityEditRoutineManageDeleteEditRoutine.setVisibility(View.VISIBLE);
                textInputNameManageDeleteEditRoutine.setText(model.getRoutineName());
                textInputDescriptionManageDeleteEditRoutine.setText(model.getRoutineDescription());
                initExerciseRecyclerView();
                break;
            case ADD_CODE:
                addExerciseToRoutine(model);
        }
    }

    private void addExerciseToRoutine(Routine model) {
        madb = new MaterialAlertDialogBuilder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_exercise_manage_delete_edit_routine, null);

        ImageButton save = dialogView.findViewById(R.id.imageButtonSaveDialogAddExerciseManageDeleteEditRoutine);
        TextInputEditText rep = dialogView.findViewById(R.id.textInputEditTextRepDialogAddExerciseManageDeleteEditRoutine);
        TextInputEditText time = dialogView.findViewById(R.id.textInputEditTextTimeDialogAddExerciseManageDeleteEditRoutine);
        TextInputEditText round = dialogView.findViewById(R.id.textInputEditTextRoundDialogAddExerciseManageDeleteEditRoutine);
        RecyclerView recyclerViewExercise = dialogView.findViewById(R.id.recyclerViewExerciseDialogAddExerciseManageDeleteEditRoutine);
        ManageAddRoutinesExerciseAdapterRecycler myAdapter;

        Query query = myDB
                .collection("exercise")
                .orderBy("exerciseName");

        FirestoreRecyclerOptions<Exercise> myRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Exercise>()
                .setLifecycleOwner(this)
                .setQuery(query, Exercise.class)
                .build();

        recyclerViewExercise.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new ManageAddRoutinesExerciseAdapterRecycler(myRecyclerOptions, this);
        myAdapter.notifyDataSetChanged();
        recyclerViewExercise.setAdapter(myAdapter);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String repS = rep.getText().toString(), timeS = time.getText().toString(), roundS = round.getText().toString();
                if (!repS.isEmpty() && !timeS.isEmpty() && !roundS.isEmpty() && !seletExerciseUid.isEmpty() && !difficulty.isEmpty()){
                    Integer repI = Integer.parseInt(repS), timeI = Integer.parseInt(timeS), roundI = Integer.parseInt(roundS);
                    if ((repI > 0 && roundI > 0 && timeI == 0) || (repI == 0 && roundI > 0 && timeI > 0)){
                        if (selectRoutine.getRoutineExerciseFK().contains(seletExerciseUid)){
                            Snackbar.make(rep, "Ese ejercicio ya esta en la rutina, no se pueden añadir mas de un ejercicio de cada tipo", Snackbar.LENGTH_SHORT).show();
                        }else {
                            SentencesFirestore sentences = new SentencesFirestore();
                            DocumentReference reference = myDB.collection("routine").document(selectRoutine.getRoutineUid());

                            exerciseUidList = selectRoutine.getRoutineExerciseFK();
                            exerciseUidList.add(seletExerciseUid);
                            sentences.updateDocument(reference, "routineExerciseFK", exerciseUidList);
                            repList = selectRoutine.getRoutineRep();
                            repList.add(repI);
                            sentences.updateDocument(reference, "routineRep", repList);
                            timeList = selectRoutine.getRoutineTime();
                            timeList.add(timeI);
                            sentences.updateDocument(reference, "routineTime", timeList);
                            roundList = selectRoutine.getRoutineSet();
                            roundList.add(roundI);
                            sentences.updateDocument(reference, "routineSet", roundList);

                            rep.setText("");
                            time.setText("");
                            round.setText("");
                            seletExerciseUid = "";
                            myAdapter.notifyDataSetChanged();
                        }

                    } else if ((repI > 0 && roundI <= 0 && timeI == 0) || (repI == 0 && roundI <= 0 && timeI > 0)){
                        Snackbar.make(rep, "Las series tiene que ser superior a 0", Snackbar.LENGTH_SHORT).show();
                    }else {
                        Snackbar.make(rep, "No pueden existir dos valores superiro a 0 simultaneamente en el campo rep y time", Snackbar.LENGTH_SHORT).show();
                    }

                }else {
                    Snackbar.make(rep, "Todos los campos son obligatorios", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        madb.setView(dialogView).setCancelable(true);
        madb.create().show();
    }

    private void editExerciseToRoutine(int position) {
        madb = new MaterialAlertDialogBuilder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_exercise_manage_delete_edit_routine, null);

        TextInputEditText rep = dialogView.findViewById(R.id.textInputEditTextRepDialogEditExerciseManageDeleteEditRoutine);
        TextInputEditText round = dialogView.findViewById(R.id.textInputEditTextRoundDialogEditExerciseManageDeleteEditRoutine);
        TextInputEditText time = dialogView.findViewById(R.id.textInputEditTextTimeDialogEditExerciseManageDeleteEditRoutine);

        rep.setText(selectRoutine.getRoutineRep().get(position).toString());
        time.setText(selectRoutine.getRoutineTime().get(position).toString());
        round.setText(selectRoutine.getRoutineSet().get(position).toString());

        madb.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String repS = rep.getText().toString(), timeS = time.getText().toString(), roundS = round.getText().toString();
                if (!repS.isEmpty() && !timeS.isEmpty() && !roundS.isEmpty()){
                    Integer repI = Integer.parseInt(repS), timeI = Integer.parseInt(timeS), roundI = Integer.parseInt(roundS);
                    if ((repI > 0 && roundI > 0 && timeI == 0) || (repI == 0 && roundI > 0 && timeI > 0)){

                        SentencesFirestore sentences = new SentencesFirestore();
                        DocumentReference reference = myDB.collection("routine").document(selectRoutine.getRoutineUid());

                        repList = selectRoutine.getRoutineRep();
                        repList.set(position, repI);
                        sentences.updateDocument(reference, "routineRep", repList);

                        timeList = selectRoutine.getRoutineTime();
                        timeList.set(position,timeI);
                        sentences.updateDocument(reference, "routineTime", timeList);

                        roundList = selectRoutine.getRoutineSet();
                        roundList.set(position, roundI);
                        sentences.updateDocument(reference, "routineSet", roundList);

/*                        rep.setText("");
                        time.setText("");
                        round.setText("");
                        repList.clear();
                        timeList.clear();
                        roundList.clear();*/

                    }else if ((repI > 0 && roundI <= 0 && timeI == 0) || (repI == 0 && roundI <= 0 && timeI > 0)){
                        Snackbar.make(rep, "Las series tiene que ser superior a 0", Snackbar.LENGTH_SHORT).show();

                    }else {
                        Snackbar.make(rep, "No pueden existir dos valores superiro a 0 simultaneamente en el campo rep y time", Snackbar.LENGTH_SHORT).show();
                    }

                }else {
                    Snackbar.make(rep, "Todos los campos son obligatorios", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        madb.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        madb.setView(dialogView).setCancelable(true);
        madb.create().show();

    }

    private void doActionExerciseList(Exercise model, int cod, int position){
        switch (cod){
            case EDIT_CODE:
                editExerciseToRoutine(position);
                break;
            case REFRESH_CODE:
                initExerciseRecyclerView();
                break;
        }
    }

    private void initExerciseRecyclerView() {
        recyclerViewExercise.setVisibility(View.VISIBLE);
        Query query = myDB
                .collection("exercise")
                .whereIn("exerciseUid", selectRoutine.getRoutineExerciseFK());

        FirestoreRecyclerOptions<Exercise> myRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Exercise>()
                .setLifecycleOwner(this)
                .setQuery(query, Exercise.class)
                .build();

        recyclerViewExercise.setLayoutManager(new LinearLayoutManager(this));
        manageDeleteEditRoutineExerciseListAdapter = new ManageDeleteEditRoutineExerciseListAdapter(myRecyclerOptions, (model, cod, position) -> doActionExerciseList(model, cod, position), selectRoutine);
        manageDeleteEditRoutineExerciseListAdapter.notifyDataSetChanged();
        recyclerViewExercise.setAdapter(manageDeleteEditRoutineExerciseListAdapter);
    }

    private void event() {
        imageButtonSaveRoutineManageDeleteEditRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, description;
                description = textInputDescriptionManageDeleteEditRoutine.getText().toString();
                name = textInputNameManageDeleteEditRoutine.getText().toString();
                if (!name.isEmpty() && !description.isEmpty() && !difficulty.isEmpty()){
                    Routine routineObb = new Routine(null, name, description, difficulty, repList, roundList, timeList, exerciseUidList);
                    SentencesFirestore sentence = new SentencesFirestore();

                    DocumentReference reference = myDB.collection("routine").document(selectRoutine.getRoutineUid());
                    sentence.updateDocument(reference, "routineName", name);
                    sentence.updateDocument(reference, "routineDescription", description);
                    sentence.updateDocument(reference, "routineDifficulty", difficulty);

                    textInputNameManageDeleteEditRoutine.setText("");
                    textInputDescriptionManageDeleteEditRoutine.setText("");

                }else {
                    Snackbar.make(v, "Los campos son obligatorio", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onItemClick(Exercise model, int position) {
        seletExerciseUid = model.getExerciseUid();
    }
}