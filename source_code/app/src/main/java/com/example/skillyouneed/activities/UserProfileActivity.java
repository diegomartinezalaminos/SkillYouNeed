package com.example.skillyouneed.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.skillyouneed.ManageExercisesActivity;
import com.example.skillyouneed.ManageGadgetsActivity;
import com.example.skillyouneed.ManageRoutinesActivity;
import com.example.skillyouneed.ManageSkillsActivity;
import com.example.skillyouneed.ManageTypesActivity;
import com.example.skillyouneed.ManageUsersActivity;
import com.example.skillyouneed.R;
import com.example.skillyouneed.models.Routine;
import com.example.skillyouneed.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/*Mostramos y modificamos nombre de usuario, email, password(solo modificar pero no mostrar)
* la opcción de cerrar sesión y eliminar cuenta de la bbdd.
* En el caso de que el usuario logueado sea admin, a demás de todoo lo anterior pordra eliminar o dar
* de alta a ortros usuario, ejercicios, rutinas, skills en la bbdd */
public class UserProfileActivity extends AppCompatActivity {

    //Variables
    private User user; //(getUserObb)
    private MaterialAlertDialogBuilder madb;
    private FirebaseFirestore myDB;
    private FirebaseAuth myAuth;
    private FirebaseUser currentUser = null;
    private String Uid = "";
    private DocumentReference referenceUserProfile;
    private ArrayList<DocumentReference> userSkills;
    private ScrollView scrollViewAdminSectionUserProfile;
    private Button buttonDeleteOwnUserUserProfile, buttonLoginOutUserProfile,
            buttonDeleteUserUserProfile, buttonAddExerciseUserProfile,
            buttonAddRoutineUserProfile, buttonAddTypeUserProfile, buttonAddGadgetUserProfile, buttonAddSkillUserProfile;
    private TextInputEditText textInputOwnUserNameUserProfile, textInputOwnGmailUserProfile;
    private ImageButton buttonSaveChangeUserProfile;

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

        scrollViewAdminSectionUserProfile = (ScrollView) findViewById(R.id.scrollViewAdminSectionUserProfile);
        buttonDeleteOwnUserUserProfile = (Button) findViewById(R.id.buttonDeleteOwnUserUserProfile);
        buttonLoginOutUserProfile = (Button) findViewById(R.id.buttonLoginOutUserProfile);
        buttonSaveChangeUserProfile = (ImageButton) findViewById(R.id.buttonSaveChangeUserProfile);
        buttonDeleteUserUserProfile = (Button) findViewById(R.id.buttonDeleteUserUserProfile);
        buttonAddExerciseUserProfile = (Button) findViewById(R.id.buttonAddExerciseUserProfile);
        buttonAddRoutineUserProfile = (Button) findViewById(R.id.buttonAddRoutineUserProfile);
        buttonAddTypeUserProfile = (Button) findViewById(R.id.buttonAddTypeUserProfile);
        buttonAddGadgetUserProfile = (Button) findViewById(R.id.buttonAddGadgetUserProfile);
        buttonAddSkillUserProfile = (Button) findViewById(R.id.buttonAddSkillUserProfile);
        textInputOwnUserNameUserProfile = (TextInputEditText) findViewById(R.id.textInputOwnUserNameUserProfile);
        textInputOwnGmailUserProfile = (TextInputEditText) findViewById(R.id.textInputOwnGmailUserProfile);

        textInputOwnGmailUserProfile.setText(currentUser.getEmail());
        //getUserObb(currentUser.getUid());
        //textInputOwnGmailUserProfile.setText(user.getUserName());
    }

    private void event() {
        buttonDeleteOwnUserUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(currentUser);
            }
        });

        buttonLoginOutUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOut();
            }
        });

        buttonSaveChangeUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "";
                String gmail = "";

                name = textInputOwnUserNameUserProfile.getText().toString();
                gmail = textInputOwnGmailUserProfile.getText().toString();

                if (!name.isEmpty() && !gmail.isEmpty()){

                    updateGmail(gmail, currentUser);
                    updateStringField(currentUser.getUid(), "userName", name);

                }else {
                    textInputOwnUserNameUserProfile.setError(getText(R.string.ErrorEmptyField));
                    textInputOwnGmailUserProfile.setError(getText(R.string.ErrorEmptyField));
                }
            }
        });

        buttonDeleteUserUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ManageUsersActivity.class);
                startActivity(i);
                //finish();
            }
        });

        buttonAddExerciseUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ManageExercisesActivity.class);
                startActivity(i);
                //finish();
            }
        });

        buttonAddRoutineUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ManageRoutinesActivity.class);
                startActivity(i);
                //finish();
            }
        });

        buttonAddTypeUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ManageTypesActivity.class);
                startActivity(i);
                //finish();
            }
        });

        buttonAddGadgetUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ManageGadgetsActivity.class);
                startActivity(i);
                //finish();
            }
        });

        buttonAddSkillUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ManageSkillsActivity.class);
                startActivity(i);
                //finish();
            }
        });
    }

    private void deleteUser(FirebaseUser currentUser) {
        madb = new MaterialAlertDialogBuilder(this);
        madb.setTitle("BORRAR USUARIO")
            .setMessage("¿ Desea eliminar el usuario " + currentUser.getEmail() + " ?")
            .setPositiveButton("CONTINUAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    currentUser.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent i = new Intent(UserProfileActivity.this, LoginActivity.class);
                                        startActivity(i);
                                        finish();
                                    }else{
                                        Snackbar.make(buttonLoginOutUserProfile, "Ha ocurrido un error al eliminar el ususario", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    myDB.collection("user").document(currentUser.getUid())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("DebugUserProfile:", "El documento se ha eliminado correctamente");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("WarningUserProfile:", "Error borrando el documento", e);
                                }
                            });
                }
            }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
                .setCancelable(false)
                .create().show();
    }

    private void loginOut(){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(UserProfileActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void getUserObb(String uid) {

        myDB.collection("user").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        user = snapshot.toObject(User.class);
                    }
                });
        Log.d("Pruebaaa:", user.getUserName());
    }

    //Modifica un campo de tipo string de la bbdd (Firestore)
    private void updateStringField(String documentUid, String nameField, String value){
        myDB.collection("user").document(documentUid)
                .update(nameField, value)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Debug: ", "Campo actualizado correctamente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Warning: ", "Error actualizando el campo", e);
                    }
                });
    }

    //Actualiza el gmail para la autenticacion (Authentication)
    private void updateGmail(String newGemail, FirebaseUser currentUser){
        currentUser.updateEmail(newGemail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d("Debug: ", "Gmail actualizado correctamente");
                            updateStringField(currentUser.getUid(), "userEmail", newGemail);
                        } else {
                            Log.w("Warning", "Ha ocurrido un erro al actualizar el gamil");
                        }
                    }
                });
    }

}