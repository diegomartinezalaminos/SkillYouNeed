package com.example.skillyouneed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.example.skillyouneed.models.OptionItem;
import com.example.skillyouneed.reycles.GadgetListAdapterRecycler;
import com.example.skillyouneed.reycles.GadgetListOnClickListener;
import com.example.skillyouneed.utilities.Constant;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
//En esta actividad mostramos un listado de las distintas apartos(opciones) para clasificar las skills
public class GadgetListActivity extends AppCompatActivity implements GadgetListOnClickListener {

    //Variables
    private OptionItem fatherOptionItem = null;
    private RecyclerView recyclerViewOptionsGadgetList;
    private FloatingActionButton floatingActionButtonProfileGadgeList;
    private SearchView searchViewSearchGadgetList;
    private FirebaseFirestore myFirestoreDb;
    private CollectionReference index;
    private GadgetListAdapterRecycler myGadgetListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gadget_list);

        initDataBase();
        initLayout();
    }

    private void initLayout() {
        floatingActionButtonProfileGadgeList = (FloatingActionButton) findViewById(R.id.floatingActionButtonProfileGadgeList);
        searchViewSearchGadgetList = (SearchView) findViewById(R.id.searchViewSearchGadgetList);
        recyclerViewOptionsGadgetList = (RecyclerView) findViewById(R.id.recyclerViewOptionsGadgetList);

        recyclerViewOptionsGadgetList.setLayoutManager(new LinearLayoutManager(this));
        //Query para seleccionar los datos de la bbdd firestore
        Query query = myFirestoreDb
                .collection(index.getPath());


        //Lista parametro "lista" para recyclerView
        FirestoreRecyclerOptions<OptionItem> myFirestoreRecyclerOptionsList = new FirestoreRecyclerOptions
                .Builder<OptionItem>()
                .setLifecycleOwner(this)
                .setQuery(query, OptionItem.class)
                .build();

        //Instanciamos adapter
        myGadgetListAdapter = new GadgetListAdapterRecycler(myFirestoreRecyclerOptionsList, this);
        //Para que este escuchando y se modifique la lista ne tiempo real si hay algun cambio en la bbd
        myGadgetListAdapter.notifyDataSetChanged();
        //Lamamos al recycler
        recyclerViewOptionsGadgetList.setAdapter(myGadgetListAdapter);
    }

    private void initDataBase() {
        myFirestoreDb = FirebaseFirestore.getInstance();
        getFatherObject();
        //index = myFirestoreDb.collection(fatherOptionItem.getReference().toString() + "/" + fatherOptionItem.getChildColection());
        index = myFirestoreDb.collection("main/skill/" + fatherOptionItem.getChildColection());
    }

    private void getFatherObject() {
        Log.i("Prueba", "FatherObb");

        Bundle obb = getIntent().getExtras();
        if (obb != null){
            fatherOptionItem = (OptionItem) obb.getSerializable(Constant.MAIN_OPTION_ITEM);
            //Snackbar.make(editTextSearchGadgetList, fatherOptionItem.getUid(), Snackbar.LENGTH_SHORT).show();

        }else{
            //Snackbar.make(editTextSearchGadgetList, "Ha ocurrido un error pasado el obb", Snackbar.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onIntemClick(OptionItem optionItem, int position) {
        Log.d("Item_Click", "click item : " + position + " and the id is : " + optionItem.getUid());

        if (optionItem.getChildColection().equals(Constant.GADGETLIST_CHIL_COLECTION_NAME)){
            Intent intent = new Intent(GadgetListActivity.this, FinalSkillListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.GADGETLIST_OPTION_ITEM, optionItem);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

/*    @Override
    protected void onStart() {
        super.onStart();
        myGadgetListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myGadgetListAdapter.stopListening();
    }*/
}