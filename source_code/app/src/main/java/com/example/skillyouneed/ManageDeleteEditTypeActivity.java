package com.example.skillyouneed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.skillyouneed.models.Type;
import com.example.skillyouneed.reycles.adapters.ManageDeleteEditTypeAdapterRecycler;
import com.example.skillyouneed.utilities.SentencesFirestore;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ManageDeleteEditTypeActivity extends AppCompatActivity {

    private final int EDIT_CODE = 222;

    private Type selectObb;
    private ManageDeleteEditTypeAdapterRecycler manageDeleteEditTypeAdapterRecycler;
    private TextInputEditText textInputEditTextNameManageDeleteEditType, textInputEditTextUrlIconManageDeleteEditType;
    private ImageButton imageButtonSaveManageDeleteEditType;
    private FirebaseFirestore myDB;
    private CollectionReference index;
    private RecyclerView recyclerViewManageDeleteEditType;
    private LinearLayout linearLayoutEditVisibilityManageDeleteEditType;
    private SentencesFirestore sentence = new SentencesFirestore();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_delete_edit_type_actyvity);

        initDB();
        initLayout();
        events();
    }
    private void initDB() {
        myDB = FirebaseFirestore.getInstance();
        index = myDB.collection("type");
    }

    private void initLayout() {
        textInputEditTextNameManageDeleteEditType = (TextInputEditText) findViewById(R.id.textInputEditTextNameManageDeleteEditType);
        textInputEditTextUrlIconManageDeleteEditType = (TextInputEditText) findViewById(R.id.textInputEditTextUrlIconManageDeleteEditType);
        imageButtonSaveManageDeleteEditType = (ImageButton) findViewById(R.id.imageButtonSaveManageDeleteEditType);
        recyclerViewManageDeleteEditType = (RecyclerView) findViewById(R.id.recyclerViewManageDeleteEditType);
        linearLayoutEditVisibilityManageDeleteEditType = (LinearLayout) findViewById(R.id.linearLayoutEditVisibilityManageDeleteEditType);
        initRecyclerView();
    }

    private void events() {
        imageButtonSaveManageDeleteEditType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "", iconUrl = "";
                name = textInputEditTextNameManageDeleteEditType.getText().toString();
                iconUrl = textInputEditTextUrlIconManageDeleteEditType.getText().toString();

                if (!name.isEmpty() && !iconUrl.isEmpty()){
                    Type typeObb = new Type(null, name, iconUrl);
                    if (selectObb != null){
                        DocumentReference reference = index.document(selectObb.getTypeUid());
                        sentence.updateDocument(reference, "typeName", name);
                        sentence.updateDocument(reference, "typeUrlIcon", iconUrl);
                        clearAllfile();
                    }
                }else {
                    Snackbar.make(v,"Los campos son obligatorios", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initRecyclerView() {
        Query query = myDB
                .collection(index.getPath())
                .orderBy("typeName");

        FirestoreRecyclerOptions<Type> myRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Type>()
                .setLifecycleOwner(this)
                .setQuery(query, Type.class)
                .build();

        recyclerViewManageDeleteEditType.setLayoutManager(new LinearLayoutManager(this));
        manageDeleteEditTypeAdapterRecycler = new ManageDeleteEditTypeAdapterRecycler(myRecyclerOptions, (model, cod) -> doAction(model, cod));
        manageDeleteEditTypeAdapterRecycler.notifyDataSetChanged();
        recyclerViewManageDeleteEditType.setAdapter(manageDeleteEditTypeAdapterRecycler);
    }

    private void doAction(Type model, int cod) {
        switch (cod){
            case EDIT_CODE:
                linearLayoutEditVisibilityManageDeleteEditType.setVisibility(View.VISIBLE);
                textInputEditTextNameManageDeleteEditType.setText(model.getTypeName());
                textInputEditTextUrlIconManageDeleteEditType.setText(model.getTypeUrlIcon());
                selectObb = model;
                break;
        }
    }

    private void clearAllfile() {
        textInputEditTextNameManageDeleteEditType.setText("");
        textInputEditTextUrlIconManageDeleteEditType.setText("");
    }
}