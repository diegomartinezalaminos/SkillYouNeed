package com.example.skillyouneed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.skillyouneed.models.Gadget;
import com.example.skillyouneed.models.Type;
import com.example.skillyouneed.reycles.adapters.ManageDeleteEditGadgetAdapterRecycler;
import com.example.skillyouneed.reycles.adapters.ManageDeleteEditTypeAdapterRecycler;
import com.example.skillyouneed.utilities.SentencesFirestore;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ManageDeleteEditGadgetActivity extends AppCompatActivity {

    private final int EDIT_CODE = 222;

    private Gadget selectObb;
    private ManageDeleteEditGadgetAdapterRecycler manageDeleteEditGadgetAdapterRecycler;
    private TextInputEditText textInputEditTextNameManageDeleteEditGadget, textInputEditTextUrlIconManageDeleteEditGadget;
    private ImageButton imageButtonSaveManageDeleteEditGadget;
    private FirebaseFirestore myDB;
    private CollectionReference index;
    private RecyclerView recyclerViewManageDeleteEditGadget;
    private LinearLayout linearLayoutEditVisibilityManageDeleteEditGadget;
    private SentencesFirestore sentence = new SentencesFirestore();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_delete_edit_gadget);

        initDB();
        initLayout();
        events();
    }

    private void initDB() {
        myDB = FirebaseFirestore.getInstance();
        index = myDB.collection("gadget");
    }

    private void initLayout() {
        textInputEditTextNameManageDeleteEditGadget = (TextInputEditText) findViewById(R.id.textInputEditTextNameManageDeleteEditGadget);
        textInputEditTextUrlIconManageDeleteEditGadget = (TextInputEditText) findViewById(R.id.textInputEditTextUrlIconManageDeleteEditGadget);
        imageButtonSaveManageDeleteEditGadget = (ImageButton) findViewById(R.id.imageButtonSaveManageDeleteEditGadget);
        recyclerViewManageDeleteEditGadget = (RecyclerView) findViewById(R.id.recyclerViewManageDeleteEditGadget);
        linearLayoutEditVisibilityManageDeleteEditGadget = (LinearLayout) findViewById(R.id.linearLayoutEditVisibilityManageDeleteEditGadget);
        initRecyclerView();
    }

    private void events() {
        imageButtonSaveManageDeleteEditGadget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "", iconUrl = "";
                name = textInputEditTextNameManageDeleteEditGadget.getText().toString();
                iconUrl = textInputEditTextUrlIconManageDeleteEditGadget.getText().toString();

                if (!name.isEmpty() && !iconUrl.isEmpty()){
                    if (selectObb != null){
                        DocumentReference reference = index.document(selectObb.getGadgetUid());
                        sentence.updateDocument(reference, "gadgetName", name);
                        sentence.updateDocument(reference, "gadgetUrlIcon", iconUrl);
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
                .orderBy("gadgetName");

        FirestoreRecyclerOptions<Gadget> myRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Gadget>()
                .setLifecycleOwner(this)
                .setQuery(query, Gadget.class)
                .build();

        recyclerViewManageDeleteEditGadget.setLayoutManager(new LinearLayoutManager(this));
        manageDeleteEditGadgetAdapterRecycler = new ManageDeleteEditGadgetAdapterRecycler(myRecyclerOptions, (model, cod) -> doAction(model, cod));
        manageDeleteEditGadgetAdapterRecycler.notifyDataSetChanged();
        recyclerViewManageDeleteEditGadget.setAdapter(manageDeleteEditGadgetAdapterRecycler);
    }

    private void doAction(Gadget model, int cod) {
        switch (cod){
            case EDIT_CODE:
                linearLayoutEditVisibilityManageDeleteEditGadget.setVisibility(View.VISIBLE);
                textInputEditTextNameManageDeleteEditGadget.setText(model.getGadgetName());
                textInputEditTextUrlIconManageDeleteEditGadget.setText(model.getGadgetUrlIcon());
                selectObb = model;
                break;
        }
    }

    private void clearAllfile() {
        textInputEditTextUrlIconManageDeleteEditGadget.setText("");
        textInputEditTextNameManageDeleteEditGadget.setText("");
    }
}