package com.example.skillyouneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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
import com.example.skillyouneed.reycles.listeners.ManageRoutinesExerciseOnClickListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class ManageAddRoutineActivity extends AppCompatActivity implements ManageRoutinesExerciseOnClickListener/*, AdapterView.OnItemSelectedListener*/{

    private ImageView contentImgSpinner;
    private TextView contentTxtSpinner;
    private LinearLayout contentLinearLayoutSpinner;
    private ArrayList<Dyfficulty> difficultyArrayList;
    private SpinnerDifficultyAdapter spinnerDifficultyAdapter;


    private Boolean pushShowButton = false;
    private ArrayList<String> exerciseUidList = new ArrayList<>();
    private ArrayList<Integer> repList = new ArrayList<>();
    private ArrayList<Integer> timeList = new ArrayList<>();
    private ArrayList<Integer> roundList = new ArrayList<>();
    private String seletExerciseUid = "";

    private ManageAddRoutinesExerciseAdapterRecycler myAdapter;
    private CollectionReference index;
    private String difficulty;
    private FirebaseFirestore myDB;
    private TextInputEditText textInputNameManageRoutine, textInputDescriptionManageRoutine,
            textInputRepeManageRoutine, textInputRoundManageRoutine, textInputTimeManageRoutine;
    private ImageButton imageButtonAddExerciseManageRoutine, imageButtonShowAddExerciseManageRoutine, imageButtonSaveRoutineManageRoutine;
    private SearchView searchViewExerciseManageRoutine;
    private RecyclerView recyclerViewExerciseManageRoutine;
    private Spinner spinnerDifficultyManageRoutine;
    private TextInputLayout textInputLayoutRepManageRoutine, textInputLayoutRoundManageRoutine, textInputLayoutTimeManageRoutine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_routine);

        intiDataBase();
        initLayout();
        event();
    }

    private void intiDataBase() {
        myDB = FirebaseFirestore.getInstance();
        index = myDB.collection("routine");
    }

    private void initLayout() {
        textInputNameManageRoutine = (TextInputEditText) findViewById(R.id.textInputNameManageRoutine);
        textInputDescriptionManageRoutine = (TextInputEditText) findViewById(R.id.textInputDescriptionManageRoutine);
        textInputRepeManageRoutine = (TextInputEditText) findViewById(R.id.textInputRepeManageRoutine);
        textInputRoundManageRoutine = (TextInputEditText) findViewById(R.id.textInputRoundManageRoutine);
        textInputTimeManageRoutine = (TextInputEditText) findViewById(R.id.textInputTimeManageRoutine);
        imageButtonAddExerciseManageRoutine = (ImageButton) findViewById(R.id.imageButtonAddExerciseManageRoutine);
        imageButtonShowAddExerciseManageRoutine = (ImageButton) findViewById(R.id.imageButtonShowAddExerciseManageRoutine);
        imageButtonSaveRoutineManageRoutine = (ImageButton) findViewById(R.id.imageButtonSaveRoutineManageRoutine);
        searchViewExerciseManageRoutine = (SearchView) findViewById(R.id.searchViewExerciseManageRoutine);
        recyclerViewExerciseManageRoutine = (RecyclerView) findViewById(R.id.recyclerViewExerciseManageRoutine);
        spinnerDifficultyManageRoutine = (Spinner) findViewById(R.id.spinnerDifficultyManageRoutine);
        textInputLayoutRepManageRoutine = (TextInputLayout) findViewById(R.id.textInputLayoutRepManageRoutine);
        textInputLayoutRoundManageRoutine = (TextInputLayout) findViewById(R.id.textInputLayoutRoundManageRoutine);
        textInputLayoutTimeManageRoutine = (TextInputLayout) findViewById(R.id.textInputLayoutTimeManageRoutine);
        initRecyclerView();
        contentImgSpinner = (ImageView) findViewById(R.id.contentImgSpinner);
        contentTxtSpinner = (TextView) findViewById(R.id.contentTxtSpinner);
        contentLinearLayoutSpinner = (LinearLayout) findViewById(R.id.contentLinearLayoutSpinner);
        contentLinearLayoutSpinner.setVisibility(View.GONE);
        llenarLista();
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

        spinnerDifficultyAdapter = new SpinnerDifficultyAdapter(this, difficultyArrayList);
        spinnerDifficultyManageRoutine.setAdapter(spinnerDifficultyAdapter);
        spinnerDifficultyManageRoutine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Dyfficulty difficultyObb = difficultyArrayList.get(position);
                difficulty = difficultyObb.getName();
                if (position == 0){
                    contentLinearLayoutSpinner.setVisibility(View.GONE);
                }

                if (position != 0){
                    contentLinearLayoutSpinner.setVisibility(View.VISIBLE);
                    contentImgSpinner.setImageResource(difficultyObb.getIcon());
                    contentTxtSpinner.setText(difficultyObb.getName());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





/*
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.difficulty_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerDifficultyManageRoutine.setAdapter(spinnerAdapter);
        spinnerDifficultyManageRoutine.setOnItemSelectedListener(this);*/
        Log.d("DebugManageRoutine: ", "initSpinner");
    }

    private void initRecyclerView() {
        Query query = myDB
                .collection("exercise")
                .orderBy("exerciseName");

        FirestoreRecyclerOptions<Exercise> myRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Exercise>()
                .setLifecycleOwner(this)
                .setQuery(query, Exercise.class)
                .build();

        recyclerViewExerciseManageRoutine.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new ManageAddRoutinesExerciseAdapterRecycler(myRecyclerOptions, this);
        myAdapter.notifyDataSetChanged();
        recyclerViewExerciseManageRoutine.setAdapter(myAdapter);
        Log.d("DebugManageRoutine: ", "Lista de ejercicios iniciada");
    }

    private void event() {
        imageButtonShowAddExerciseManageRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushShowButton = !pushShowButton;
                if (pushShowButton){
                    searchViewExerciseManageRoutine.setVisibility(View.VISIBLE);
                    recyclerViewExerciseManageRoutine.setVisibility(View.VISIBLE);
                    textInputLayoutRepManageRoutine.setVisibility(View.VISIBLE);
                    textInputLayoutRoundManageRoutine.setVisibility(View.VISIBLE);
                    textInputLayoutTimeManageRoutine.setVisibility(View.VISIBLE);
                    imageButtonAddExerciseManageRoutine.setVisibility(View.VISIBLE);

                }else {
                    searchViewExerciseManageRoutine.setVisibility(View.GONE);
                    recyclerViewExerciseManageRoutine.setVisibility(View.GONE);
                    textInputLayoutRepManageRoutine.setVisibility(View.GONE);
                    textInputLayoutRoundManageRoutine.setVisibility(View.GONE);
                    textInputLayoutTimeManageRoutine.setVisibility(View.GONE);
                    imageButtonAddExerciseManageRoutine.setVisibility(View.GONE);
                }

            }
        });

        searchViewExerciseManageRoutine.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFilter(query, "exerciseName");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFilter(newText, "exerciseName");
                return false;
            }
        });

        imageButtonAddExerciseManageRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String repString, roundString, timeString;
                repString = textInputRepeManageRoutine.getText().toString();
                roundString = textInputRoundManageRoutine.getText().toString();
                timeString = textInputTimeManageRoutine.getText().toString();

                Log.d("DebugManageRoutine: ", repString);
                Log.d("DebugManageRoutine: ", roundString);
                Log.d("DebugManageRoutine: ", timeString);
                Log.d("DebugManageRoutine: ", seletExerciseUid);

                if (!repString.isEmpty() && !roundString.isEmpty() && !timeString.isEmpty() && !seletExerciseUid.isEmpty()){

                    Log.d("DebugManageRoutine: ", "Se han recogido todos los campos de ejercicios");
                    int repInt = Integer.parseInt(repString),
                            roundInt = Integer.parseInt(roundString), timeInt = Integer.parseInt(timeString);

                    if ((repInt == 0 && roundInt > 0 && timeInt > 0) || (repInt > 0 && roundInt > 0 && timeInt == 0)){

                        Log.d("DebugManageRoutine: ", "Se llama a funcion aniadir elementos a la lista y borrar texto");
                        if (!exerciseUidList.contains(seletExerciseUid)){
                            addExerciseElementToList(repInt, timeInt, roundInt);
                            clearExerceseFile();
                        }else {
                            Snackbar.make(v, "No pueden haber ejercicios repetidos en una misma rutina", Snackbar.LENGTH_SHORT).show();
                        }


                    } else if ((repInt == 0 && roundInt <= 0 && timeInt > 0) && (repInt > 0 && roundInt <= 0 && timeInt == 0)){
                        //Mostrar error en el formulario los round nunca pueden ser 0 o inferior
                        Snackbar.make(imageButtonAddExerciseManageRoutine, "Los round nunca pueden ser 0 o inferior", Snackbar.LENGTH_SHORT).show();
                    } else {
                        //Mostrar error "El campo time y rep no pueden tener simultanemante un valor superior a 0
                        Snackbar.make(imageButtonAddExerciseManageRoutine, "El campo time y rep no pueden tener simultanemante un valor superior a 0", Snackbar.LENGTH_SHORT).show();

                    }

                }else {
                    Snackbar.make(imageButtonAddExerciseManageRoutine, "Los campos son obligatorio", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        imageButtonSaveRoutineManageRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, description;
                name = textInputNameManageRoutine.getText().toString();
                description = textInputDescriptionManageRoutine.getText().toString();

                if (!name.isEmpty() && !description.isEmpty() && !difficulty.isEmpty()){
                    Routine routineObb = new Routine(null, name, description, difficulty, repList, roundList, timeList, exerciseUidList);
                    Log.d("DebugManageRoutine: ", "objeto rutina creado");
                    addRoutineToDB(routineObb);
                    clearAllFile();

                }else {
                    //Error campos obligatorios
                    Snackbar.make(imageButtonAddExerciseManageRoutine, "Los campos son obligatorio", Snackbar.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void addRoutineToDB(Routine routineObb) {
        myDB.collection(index.getPath())
                .add(routineObb)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        documentReference.update("routineUid", documentReference.getId())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("DebugManageRoutine: ", "El campo routineUid se ha actualizado correctamente");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("WarningManageRoutine: ", "Error al actualizar el campo routineUid del documento", e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("WarningManageRoutine: ", "Error aniadendo el documento", e);
                    }
                });
    }

    private void addExerciseElementToList(int rep, int time, int round) {
        exerciseUidList.add(seletExerciseUid);
        repList.add(rep);
        roundList.add(round);
        timeList.add(time);
        Log.d("DebugManageRoutine: ", "Se han aniadido los elementos a lista");
    }

    private void clearExerceseFile() {
        textInputRepeManageRoutine.setText("");
        textInputRoundManageRoutine.setText("");
        textInputTimeManageRoutine.setText("");
        Log.d("DebugManageRoutine: ", "Limpiando texto");
    }

    private void clearAllFile(){
        textInputNameManageRoutine.setText("");
        textInputDescriptionManageRoutine.setText("");
        textInputRepeManageRoutine.setText("");
        textInputRoundManageRoutine.setText("");
        textInputTimeManageRoutine.setText("");
        exerciseUidList.clear();
        repList.clear();
        timeList.clear();
        roundList.clear();
        Log.d("DebugManageRoutine: ", "Limpiando todo el texto");
    }

    private void searchFilter(String strSearch, String fieldName){
        Query query = myDB
                .collection("exercise")
                .orderBy(fieldName)
                .startAt(strSearch)
                .endAt(strSearch + "\uf8ff");

        FirestoreRecyclerOptions<Exercise> myRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Exercise>()
                .setLifecycleOwner(this)
                .setQuery(query, Exercise.class)
                .build();

        recyclerViewExerciseManageRoutine.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new ManageAddRoutinesExerciseAdapterRecycler(myRecyclerOptions, this);
        myAdapter.notifyDataSetChanged();
        recyclerViewExerciseManageRoutine.setAdapter(myAdapter);
    }

/*    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        difficulty = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/

    @Override
    public void onItemClick(Exercise model, int position) {
        seletExerciseUid = model.getExerciseUid();
        Log.d("DebugManageRoutine:", "Obtener el id del ejercicio pulsado");
    }
}