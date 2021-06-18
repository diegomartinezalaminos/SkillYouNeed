package com.example.skillyouneed.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.example.skillyouneed.ManageDeleteEditExerciseActivity;
import com.example.skillyouneed.ManageDeleteEditGadgetActivity;
import com.example.skillyouneed.ManageDeleteEditRoutineActivity;
import com.example.skillyouneed.ManageDeleteEditSkillActivity;
import com.example.skillyouneed.ManageDeleteEditTypeActivity;
import com.example.skillyouneed.ManageAddExerciseActivity;
import com.example.skillyouneed.ManageAddGadgetActivity;
import com.example.skillyouneed.ManageAddRoutineActivity;
import com.example.skillyouneed.ManageAddSkillActivity;
import com.example.skillyouneed.ManageAddTypeActivity;
import com.example.skillyouneed.R;
import com.example.skillyouneed.models.User;
import com.example.skillyouneed.utilities.SentencesFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
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
    private SentencesFirestore sentence;
    private User user; //(getUserObb)
    private MaterialAlertDialogBuilder madb;
    private FirebaseFirestore myDB;
    private FirebaseAuth myAuth;
    private FirebaseUser currentUser = null;
    private String Uid = "";
    private DocumentReference referenceUserProfile;
    private ArrayList<DocumentReference> userSkills;
    private ScrollView scrollViewAdminSectionUserProfile;
    private Button buttonDeleteOwnUserUserProfile, buttonLoginOutUserProfile, buttonAddExerciseUserProfile,
            buttonDeleteEditGadgetUserProfile, buttonDeleteEditExerciseUserProfile, buttonDeleteEditRoutineUserProfile,
            buttonDeleteEditSkillUserProfile, buttonEditDeleteTypeUserProfile,
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
        buttonAddExerciseUserProfile = (Button) findViewById(R.id.buttonAddExerciseUserProfile);
        buttonAddRoutineUserProfile = (Button) findViewById(R.id.buttonAddRoutineUserProfile);
        buttonAddTypeUserProfile = (Button) findViewById(R.id.buttonAddTypeUserProfile);
        buttonAddGadgetUserProfile = (Button) findViewById(R.id.buttonAddGadgetUserProfile);
        buttonAddSkillUserProfile = (Button) findViewById(R.id.buttonAddSkillUserProfile);
        textInputOwnUserNameUserProfile = (TextInputEditText) findViewById(R.id.textInputOwnUserNameUserProfile);
        textInputOwnGmailUserProfile = (TextInputEditText) findViewById(R.id.textInputOwnGmailUserProfile);
        buttonEditDeleteTypeUserProfile = (Button) findViewById(R.id.buttonEditDeleteTypeUserProfile);
        buttonDeleteEditGadgetUserProfile = (Button) findViewById(R.id.buttonDeleteEditGadgetUserProfile);
        buttonDeleteEditExerciseUserProfile = (Button) findViewById(R.id.buttonDeleteEditExerciseUserProfile);
        buttonDeleteEditRoutineUserProfile = (Button) findViewById(R.id.buttonDeleteEditRoutineUserProfile);
        buttonDeleteEditSkillUserProfile = (Button) findViewById(R.id.buttonDeleteEditSkillUserProfile);

        textInputOwnGmailUserProfile.setText(currentUser.getEmail());
        DocumentReference reference = myDB.collection("user").document(currentUser.getUid());
        reference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        textInputOwnUserNameUserProfile.setText(snapshot.toObject(User.class).getUserName());
                    }
                });

        DocumentReference referenceAdmin = myDB.collection("user").document(currentUser.getUid());
        referenceAdmin.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        Boolean admin = snapshot.toObject(User.class).getUserAdmin();
                        if(admin){
                            scrollViewAdminSectionUserProfile.setVisibility(View.VISIBLE);
                        }
                    }
                });
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

        buttonAddExerciseUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ManageAddExerciseActivity.class);
                startActivity(i);
            }
        });

        buttonDeleteEditExerciseUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ManageDeleteEditExerciseActivity.class);
                startActivity(i);
            }
        });

        buttonAddRoutineUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ManageAddRoutineActivity.class);
                startActivity(i);
            }
        });

        buttonDeleteEditRoutineUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ManageDeleteEditRoutineActivity.class);
                startActivity(i);
            }
        });

        buttonAddTypeUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ManageAddTypeActivity.class);
                startActivity(i);
            }
        });

        buttonEditDeleteTypeUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ManageDeleteEditTypeActivity.class);
                startActivity(i);
            }
        });

        buttonAddGadgetUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ManageAddGadgetActivity.class);
                startActivity(i);
            }
        });

        buttonDeleteEditGadgetUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ManageDeleteEditGadgetActivity.class);
                startActivity(i);
            }
        });

        buttonAddSkillUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ManageAddSkillActivity.class);
                startActivity(i);
            }
        });

        buttonDeleteEditSkillUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ManageDeleteEditSkillActivity.class);
                startActivity(i);
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
/*                    DocumentReference reference = myDB.collection("user").document(currentUser.getUid());
                    sentence.deleteUser(myAuth.getCurrentUser(), reference);
                    Intent i = new Intent(UserProfileActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();*/

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