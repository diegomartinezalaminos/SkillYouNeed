package com.example.skillyouneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.skillyouneed.models.Type;
import com.example.skillyouneed.reycles.MainActivityOnClickListener;
import com.example.skillyouneed.reycles.MainAdapterRecycler;
import com.example.skillyouneed.utilities.Constant;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
/*  Actividad muestra una lista con las opciones principale para clasificar las skills*/
public class MainActivity extends AppCompatActivity implements MainActivityOnClickListener {

    //Variables
    private RecyclerView myRecyclerView;
    private FloatingActionButton buttonProfile;
    private MainAdapterRecycler myAdapter;
    private FirebaseFirestore myDDBB;
    private CollectionReference index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();
        initLayout();
        initEvents();
    }

    private void initEvents() {

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initFirebase() {

        myDDBB = FirebaseFirestore.getInstance();
        index = myDDBB.collection("type");
    }

    private void initLayout() {
        myRecyclerView = findViewById(R.id.recycleViewOptionsMain);
        buttonProfile = findViewById(R.id.floatingActionButtonProfileMain);
        recyclerView();
    }

    private void recyclerView() {
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = myDDBB
                .collection(index.getPath());

        FirestoreRecyclerOptions<Type> myOptionsList = new FirestoreRecyclerOptions
                .Builder<Type>()
                .setLifecycleOwner(this)
                .setQuery(query, Type.class)
                .build();

        myAdapter = new MainAdapterRecycler(myOptionsList, this);
        myAdapter.notifyDataSetChanged();
        myRecyclerView.setAdapter(myAdapter);
    }

    private void loginOut(){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

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

    @Override
    public void onIntemClick(Type model, int position) {

            Intent intent = new Intent(MainActivity.this, SkillListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.MAIN_TYPE, model);
            intent.putExtras(bundle);
           startActivity(intent);
    }
}