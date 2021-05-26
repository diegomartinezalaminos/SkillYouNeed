package com.example.skillyouneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.skillyouneed.models.OptionItem;
import com.example.skillyouneed.reycles.MainActivityOnClickListener;
import com.example.skillyouneed.reycles.MainAdapterRecycler;
import com.example.skillyouneed.utilities.Constant;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
/*  Actividad muestra una lista con las opciones principale para clasificar las skills*/
public class MainActivity extends AppCompatActivity implements MainActivityOnClickListener {

    //Variables
    private RecyclerView myMainOptionRecycler;
    private MainAdapterRecycler myMainOptionsAdapter;
    private FirebaseFirestore myFirestoreDb;
    private CollectionReference main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();
        initLayout();
    }

    private void initFirebase() {

        myFirestoreDb = FirebaseFirestore.getInstance();
        main = myFirestoreDb.collection("main");
    }

    private void initLayout() {
        myMainOptionRecycler = findViewById(R.id.recycleViewOptionsMain);
        myMainOptionRecycler.setLayoutManager(new LinearLayoutManager(this));

        //Query para seleccionar los datos de la bbdd firestore
        Query query = myFirestoreDb
                .collection("main");

        //Lista parametro "lista" para recyclerView
        FirestoreRecyclerOptions<OptionItem> myFirestoreRecyclerOptionsList = new FirestoreRecyclerOptions
                .Builder<OptionItem>()
                .setLifecycleOwner(this)
                .setQuery(query, OptionItem.class)
                .build();

        //Instanciamos adapter
        myMainOptionsAdapter = new MainAdapterRecycler(myFirestoreRecyclerOptionsList, this);

        //Para que este escuchando y se modifique la lista ne tiempo real si hay algun cambio en la bbd
        myMainOptionsAdapter.notifyDataSetChanged();
        //Lamamos al recycler
        myMainOptionRecycler.setAdapter(myMainOptionsAdapter);
    }


/*    @Override
    protected void onStart() {
        super.onStart();
        myMainOptionsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myMainOptionsAdapter.stopListening();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflador = getMenuInflater() ;
        inflador.inflate(R.menu.mainmenu, menu) ;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.MainmenuLoginOunt:
                loginOut();
                break ;
            default :
                return super.onOptionsItemSelected(item);
        }

        return true ;
    }

    private void loginOut(){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onIntemClick(OptionItem optionItem, int position) {
        Log.d("Item_Click", "click item : " + position + " and the id is : " + optionItem.getUid());

        if (optionItem.getChildColection().equals(Constant.MAIN_CHIL_COLECTION_NAME)){
            Intent intent = new Intent(MainActivity.this, GadgetListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.MAIN_OPTION_ITEM, optionItem);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}