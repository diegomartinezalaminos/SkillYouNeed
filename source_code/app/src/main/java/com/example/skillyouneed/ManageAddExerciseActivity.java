package com.example.skillyouneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.skillyouneed.adapters.SpinnerDifficultyAdapter;
import com.example.skillyouneed.models.Dyfficulty;
import com.example.skillyouneed.models.Exercise;
import com.example.skillyouneed.models.Skill;
import com.example.skillyouneed.utilities.SentencesFirestore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ManageAddExerciseActivity extends AppCompatActivity{

    //Spinner
    private ArrayList<Dyfficulty> difficultyArrayList;
    private SpinnerDifficultyAdapter spinnerDifficultyAdapter;
    private String difficulty;
    //Dialog
    private MaterialAlertDialogBuilder madb;
    //DB
    private FirebaseFirestore myDB;
    private CollectionReference index;
    //Layout
    private TextInputEditText textInputNameManageExercise, textInputDescriptionManageExercise,
            textInputUrlIconManageExercise, textInputUrlVideoManageExercise;
    private ImageButton imageButtonAddExerciseManageExercise;
    private Spinner spinnerDifficultyManageExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_exercise);

        initDDBB();
        initLayout();
        events();
    }

    private void initDDBB() {
        myDB = FirebaseFirestore.getInstance();
        index = myDB.collection("exercise");
    }

    private void initLayout() {
        textInputNameManageExercise = (TextInputEditText) findViewById(R.id.textInputNameManageExercise);
        textInputDescriptionManageExercise = (TextInputEditText) findViewById(R.id.textInputDescriptionManageExercise);
        textInputUrlIconManageExercise = (TextInputEditText) findViewById(R.id.textInputUrlIconManageExercise);
        textInputUrlVideoManageExercise = (TextInputEditText) findViewById(R.id.textInputUrlVideoManageExercise);
        imageButtonAddExerciseManageExercise = (ImageButton) findViewById(R.id.imageButtonAddExerciseManageExercise);
        spinnerDifficultyManageExercise = (Spinner) findViewById(R.id.spinnerDifficultyManageExercise);
        llenarLista();
        initSpinner();
    }


    private void events() {

        imageButtonAddExerciseManageExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "", description = "", urlIcon = "", urlVideo = "";
                name = textInputNameManageExercise.getText().toString();
                description = textInputDescriptionManageExercise.getText().toString();
                urlIcon = textInputUrlIconManageExercise.getText().toString();
                urlVideo = textInputUrlVideoManageExercise.getText().toString();


                if (!name.isEmpty() && !description.isEmpty() && !difficulty.isEmpty() && !urlIcon.isEmpty() && !urlVideo.isEmpty()){
                    SentencesFirestore sentence = new SentencesFirestore();
                    String solution = sentence.stringUrlFormat(urlVideo);
                    if (solution.equals("error")){
                        Snackbar.make(v, "La url tiene que ser de YT y válida", Snackbar.LENGTH_SHORT).show();
                    }else {
                        urlVideo = solution;
                        Exercise exerciseObb = new Exercise( null ,name, description, difficulty, urlIcon, urlVideo);
                        addObbToDB(exerciseObb, "exerciseUid", index);
                        clearFild();
                    }

                } else {
                    Snackbar.make(v, "Los campos son obligatorios", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearFild() {
        textInputNameManageExercise.setText("");
        textInputDescriptionManageExercise.setText("");
        textInputUrlIconManageExercise.setText("");
        textInputUrlVideoManageExercise.setText("");
    }

    public void llenarLista(){
        difficultyArrayList = new ArrayList<>();
        difficultyArrayList.add(new Dyfficulty(0, "-- Select Difficulty --"));
        difficultyArrayList.add(new Dyfficulty(R.drawable.ic_easy, "Facil"));
        difficultyArrayList.add(new Dyfficulty(R.drawable.ic_midel, "Medio"));
        difficultyArrayList.add(new Dyfficulty(R.drawable.ic_difficult, "Dificil"));
    }

    private  void initSpinner(){

        spinnerDifficultyAdapter = new SpinnerDifficultyAdapter(this, difficultyArrayList, 0);
        spinnerDifficultyManageExercise.setAdapter(spinnerDifficultyAdapter);
        spinnerDifficultyManageExercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void addObbToDB(Object obb, String fileUid, CollectionReference index){

        myDB.collection(index.getPath())
                .add(obb)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        documentReference.update(fileUid, documentReference.getId())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Debug addObbToDB: ", "El campo '" + fileUid + "' se ha actualizado correctamente");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Warning addObbToDB: ", "Error al actualizar el campo '" + fileUid + "' typeUid del documento", e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Warning addObbToDB: ", "Error añadiendo el documento", e);
                    }
                });
    }
}