package com.example.skillyouneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.skillyouneed.models.Gadget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ManageAddGadgetActivity extends AppCompatActivity {

    private FirebaseFirestore myDB;
    private CollectionReference index;
    private TextInputEditText textInputEditTextNameManageGadget, textInputEditTextUrlIconManageGadget;
    private ImageButton imageButtonSaveManageGadget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_gadget);

        intiDataBase();
        initLayout();
        event();
    }

    private void intiDataBase() {
        myDB = FirebaseFirestore.getInstance();
        index = myDB.collection("gadget");

    }

    private void initLayout() {
        textInputEditTextNameManageGadget = (TextInputEditText) findViewById(R.id.textInputEditTextNameManageGadget);
        textInputEditTextUrlIconManageGadget = (TextInputEditText) findViewById(R.id.textInputEditTextUrlIconManageGadget);
        imageButtonSaveManageGadget = (ImageButton) findViewById(R.id.imageButtonSaveManageGadget);
    }

    private void event() {
        imageButtonSaveManageGadget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "", urlIcon = "";
                name = textInputEditTextNameManageGadget.getText().toString();
                urlIcon = textInputEditTextUrlIconManageGadget.getText().toString();

                if (!name.isEmpty() && !urlIcon.isEmpty()){
                    Gadget gadgetObb = new Gadget(null, name, urlIcon);
                    addObbToDB(gadgetObb, "gadgetUid", index);
                    clearAllFile();
                }else {
                    Snackbar.make(v, "Los campos son obligatorios", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearAllFile() {
        textInputEditTextNameManageGadget.setText("");
        textInputEditTextUrlIconManageGadget.setText("");
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