package com.example.skillyouneed.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.skillyouneed.R;
import com.example.skillyouneed.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/*  Esta actividad se necarga de dar de altas a los usuarios en la bbdd*/
public class RegisterActivity extends AppCompatActivity {

    //Variables
    private EditText editTextRegisterName, editTextRegisterEmail, editTextRegisterPassword, editTextRegisterPasswordRepeat;
    private Button buttonRegisterRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initLayout();
        initDataBase();
        events();
    }

    private void initLayout() {
        editTextRegisterName = (EditText) findViewById(R.id.editTextRegisterName);
        editTextRegisterEmail = (EditText) findViewById(R.id.editTextRegisterEmail);
        editTextRegisterPassword = (EditText) findViewById(R.id.editTextRegisterPassword);
        editTextRegisterPasswordRepeat = (EditText) findViewById(R.id.editTextRegisterPasswordRepeat);
        buttonRegisterRegister = (Button) findViewById(R.id.buttonRegisterRegister);
    }

    private void initDataBase() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private void events() {

        buttonRegisterRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = editTextRegisterName.getText().toString();
                String email = editTextRegisterEmail.getText().toString();
                String password = editTextRegisterPassword.getText().toString();
                String passwordRepeat = editTextRegisterPasswordRepeat.getText().toString();

                if (!user.isEmpty() && !email.isEmpty() && !password.isEmpty() && !passwordRepeat.isEmpty()){

                    if (password.equals(passwordRepeat)){
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){

                                            //Aniadimos informacion del usuario a la bbdd
                                            userUID = mAuth.getCurrentUser().getUid();
                                            addUserToDDBB(userUID, email, user, v);

                                            currentUser = mAuth.getCurrentUser();
                                            updateUI(currentUser);
                                        }else{
                                            updateUI(null);
                                        }
                                    }
                                });

                    }else {
                        editTextRegisterPasswordRepeat.setError(getText(R.string.NotEqualsPassword));
                    }

                } else {
                    Snackbar.make(v, R.string.ErrorEmptyField, Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    //Aniadimos información del usuario al la bbdd
    private void addUserToDDBB(String userUID, String email, String name, View v) {
        //Obb del usuario que registramos
        User userObb = new User(userUID, email, name, new ArrayList<String>());

        db.collection("user")
                .document(userUID)
                .set(userObb)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(v, "El usuario se ha agregado a la BBDD correctamente", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(v, "Ha ocurrido un error al crear el usuario en la BBDD", Snackbar.LENGTH_LONG).show();
                    }
                });
    }


    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}