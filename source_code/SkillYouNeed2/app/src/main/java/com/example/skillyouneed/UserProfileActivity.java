package com.example.skillyouneed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/*Mostramos y modificamos nombre de usuario, email, password(solo modificar pero no mostrar)
* la opcción de cerrar sesión y eliminar cuenta de la bbdd.
* En el caso de que el usuario logueado sea admin, a demás de todoo lo anterior pordra eliminar o dar
* de alta a ortros usuario, ejercicios, rutinas, skills en la bbdd */
public class UserProfileActivity extends AppCompatActivity {

    //Variables
    private FirebaseFirestore myDB;
    private FirebaseAuth myAuth;
    private FirebaseUser currentUser = null;
    private String Uid = "";
    private DocumentReference referenceUserProfile;
    private ArrayList<DocumentReference> userSkills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        intiDataBase();
        initLayout();
        event();
    }
    private void intiDataBase() {
        myAuth = FirebaseAuth.getInstance();
        myDB = FirebaseFirestore.getInstance();
        currentUser = myAuth.getCurrentUser();
        Uid = myAuth.getCurrentUser().getUid();
        referenceUserProfile = myDB.collection("user")
                .document(Uid);
    }

    private void initLayout() {
    }

    private void event() {

    }
}