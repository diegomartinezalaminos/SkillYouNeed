package com.example.skillyouneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.skillyouneed.models.Type;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ManageAddTypeActivity extends AppCompatActivity {

    private TextInputEditText textInputEditTextNameManageType, textInputEditTextUrlIconManageType;
    private ImageButton imageButtonSaveManageType;
    private FirebaseFirestore myDB;
    private CollectionReference index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_type);

        initDB();
        initLayout();
        events();
    }

    private void initDB() {
        myDB = FirebaseFirestore.getInstance();
        index = myDB.collection("type");
    }

    private void initLayout() {
        textInputEditTextNameManageType = (TextInputEditText) findViewById(R.id.textInputEditTextNameManageType);
        textInputEditTextUrlIconManageType = (TextInputEditText) findViewById(R.id.textInputEditTextUrlIconManageType);
        imageButtonSaveManageType = (ImageButton) findViewById(R.id.imageButtonSaveManageType);
    }

    private void events() {
        imageButtonSaveManageType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "", iconUrl = "";
                name = textInputEditTextNameManageType.getText().toString();
                iconUrl = textInputEditTextUrlIconManageType.getText().toString();

                if (!name.isEmpty() && !iconUrl.isEmpty()){
                    Type typeObb = new Type(null, name, iconUrl);
                    addObbToDB(typeObb, "typeUid", index);
                    clearAllfile();
                }else {
                    Snackbar.make(v,"Los campos son obligatorios", Snackbar.LENGTH_SHORT).show();
                }
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

    private void clearAllfile() {
        textInputEditTextNameManageType.setText("");
        textInputEditTextUrlIconManageType.setText("");
    }
}