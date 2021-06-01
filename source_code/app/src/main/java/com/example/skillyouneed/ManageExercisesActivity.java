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

import com.example.skillyouneed.models.Exercise;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ManageExercisesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String difficulty;
    private MaterialAlertDialogBuilder madb;
    private FirebaseFirestore myDB;
    private CollectionReference index;
    private TextInputEditText textInputNameManageExercise, textInputDescriptionManageExercise,
            textInputUrlIconManageExercise, textInputUrlVideoManageExercise;
    private ImageButton imageButtonAddExerciseManageExercise;
    private Spinner spinnerDifficultyManageExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_exercises);

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
                    Exercise exerciseObb = new Exercise( null ,name, description, difficulty, urlIcon, urlVideo);
                    addExercise(exerciseObb);
                    clearFild();

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

    private  void initSpinner(){
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.difficulty_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerDifficultyManageExercise.setAdapter(spinnerAdapter);
        spinnerDifficultyManageExercise.setOnItemSelectedListener(this);
    }

    private void addExercise(Exercise obb){

        myDB.collection(index.getPath())
                .add(obb)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        documentReference.update("exerciseUid", documentReference.getId())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.w("WarningManageExercise: ", "El campo exerciseUid se ha actualizado correctamente");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("WarningManageExercise: ", "Error al actualizar el campo exerciseUid del documento", e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("WarningManageExercise: ", "Error aniadendo el documento", e);
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        difficulty = parent.getItemAtPosition(position).toString();
        Snackbar.make(spinnerDifficultyManageExercise, difficulty, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}