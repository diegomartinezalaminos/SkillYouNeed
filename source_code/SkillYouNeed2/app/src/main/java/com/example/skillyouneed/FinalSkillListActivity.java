package com.example.skillyouneed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.example.skillyouneed.models.FinalSkill;
import com.example.skillyouneed.models.OptionItem;
import com.example.skillyouneed.reycles.FinalSkillListAdapterRecycler;
import com.example.skillyouneed.reycles.FinalSkillListOnClickListener;
import com.example.skillyouneed.reycles.GadgetListAdapterRecycler;
import com.example.skillyouneed.utilities.Constant;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/*  En esta actividad mostramos todas las skills disponibles junto con un desplegable donde
    se montrarán la rutina para cada una*/
public class FinalSkillListActivity extends AppCompatActivity implements FinalSkillListOnClickListener {

    //Variables
    private OptionItem fatherOptionItem = null;
    private RecyclerView recyclerViewOptionsFinalSkillList;
    private FloatingActionButton floatingActionButtonProfileFinalSkillList;
    private SearchView searchViewSearchFinalSkillList;
    private FirebaseFirestore myFirestoreDb;
    private CollectionReference index;
    private FinalSkillListAdapterRecycler finalSkillListAdapterRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_skill_list);

        initDataBase();
        initLayout();
    }

    private void initLayout() {
        floatingActionButtonProfileFinalSkillList = (FloatingActionButton) findViewById(R.id.floatingActionButtonProfileGadgeList);
        searchViewSearchFinalSkillList = (SearchView) findViewById(R.id.searchViewSearchGadgetList);

        recyclerViewOptionsFinalSkillList = (RecyclerView) findViewById(R.id.recyclerViewOptionsFinalSkillList);

        recyclerViewOptionsFinalSkillList.setLayoutManager(new LinearLayoutManager(this));
        //Query para seleccionar los datos de la bbdd firestore
        Query query = myFirestoreDb
                .collection(index.getPath());
        //Lista parametro "lista" para recyclerView
        FirestoreRecyclerOptions<FinalSkill> finalSkillFirestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<FinalSkill>()
                .setLifecycleOwner(this)
                .setQuery(query, FinalSkill.class)
                .build();

        //Instanciamos adapter
        finalSkillListAdapterRecycler = new FinalSkillListAdapterRecycler(finalSkillFirestoreRecyclerOptions,this);
        //Para que este escuchando y se modifique la lista ne tiempo real si hay algun cambio en la bbd
        finalSkillListAdapterRecycler.notifyDataSetChanged();
        //Lamamos al recycler
        recyclerViewOptionsFinalSkillList.setAdapter(finalSkillListAdapterRecycler);



    }

    private void initDataBase() {
        myFirestoreDb = FirebaseFirestore.getInstance();
        getFatherObject();
        //TODO El index comentado da erro ya que el serialicer tiene problemas al pasar el tipo de dato DocumentReference (pendiente de solucion) (Duda para Antonio)
        //index = myFirestoreDb.collection(fatherOptionItem.getReference().toString() + "/" + fatherOptionItem.getChildColection());
        //Como el campo reference no funciona se lo pongo a mano para poder seguir trabajando con la app
        index = myFirestoreDb.collection("main/skill/gadget/chinUpBar/" + fatherOptionItem.getChildColection());
    }

    private void getFatherObject() {
        Bundle obb = getIntent().getExtras();
        if (obb != null){
            fatherOptionItem = (OptionItem) obb.getSerializable(Constant.GADGETLIST_OPTION_ITEM);

            //TODO Los Snackbar comentados dan error (pendiente de solucion)
            //Snackbar.make(editTextSearchGadgetList, fatherOptionItem.getUid(), Snackbar.LENGTH_SHORT).show();

        }else{
            //Snackbar.make(editTextSearchGadgetList, "Ha ocurrido un error pasado el obb", Snackbar.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onIntemClick(FinalSkill finalSkill, int position) {
        Log.d("Item_Click", "click item : " + position + " and the id is : " + finalSkill.getFinalSkillUid());
    }
}