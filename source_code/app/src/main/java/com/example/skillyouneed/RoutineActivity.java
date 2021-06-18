package com.example.skillyouneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.skillyouneed.models.Exercise;
import com.example.skillyouneed.models.Routine;
import com.example.skillyouneed.models.Skill;
import com.example.skillyouneed.reycles.adapters.RoutineListAdapterRecycler;
import com.example.skillyouneed.reycles.adapters.RoutineListWarmingRoutineAdapterRecycler;
import com.example.skillyouneed.utilities.Constant;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

public class RoutineActivity extends AppCompatActivity {

    private MaterialAlertDialogBuilder madb;

    //initDataBase()
    private Skill skillObb;
    private FirebaseFirestore myDB;
    //Layout
    private ImageView imageViewSkillIconRoutine;
    private ImageButton imageButtonTestRoutine, imageButtonInfoSkillRoutine;
    private RecyclerView recyclerViewGeneralWarmingRoutine, recyclerViewRoutineRoutine;
    private TextView textViewNameSkillRoutine;
    //Adapter initGeneralWarmingRecyclerView(); initRoutineRecyclerView();
    private RoutineListAdapterRecycler routineListAdapterRecycler;
    private RoutineListWarmingRoutineAdapterRecycler routineListWarmingRoutineAdapterRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        initDataBase();
        initLayout();
        initEvents();
    }

    private void initDataBase() {
        myDB = FirebaseFirestore.getInstance();
        Bundle obb = getIntent().getExtras();
        if (obb != null){
            skillObb = (Skill) obb.getSerializable(Constant.SKILL_LIST);
        }else{
            Log.w("Warnign:", "Ha ocurrido un error con el getSkillObb()");
        }
    }

    private void initLayout() {
        imageViewSkillIconRoutine = (ImageView) findViewById(R.id.imageViewSkillIconRoutine);
        imageButtonTestRoutine = (ImageButton) findViewById(R.id.imageButtonTestRoutine);
        imageButtonInfoSkillRoutine = (ImageButton) findViewById(R.id.imageButtonInfoSkillRoutine);
        recyclerViewGeneralWarmingRoutine = (RecyclerView) findViewById(R.id.recyclerViewGeneralWarmingRoutine);
        recyclerViewRoutineRoutine = (RecyclerView) findViewById(R.id.recyclerViewRoutineRoutine);
        textViewNameSkillRoutine = (TextView) findViewById(R.id.textViewNameSkillRoutine);

        textViewNameSkillRoutine.setText(skillObb.getSkillName());
        Picasso.get().load(skillObb.getSkillUrlIcon())
                .resize(90, 90)
                .centerCrop()
                .into(imageViewSkillIconRoutine);

        initRoutineRecyclerView(0);
        initGeneralWarmingRecyclerView();

    }

    private void initEvents() {
        imageButtonInfoSkillRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                madb = new MaterialAlertDialogBuilder(v.getContext());
                madb.setTitle("Descripcion:");
                madb.setMessage(skillObb.getSkillDescription());
                madb.create().show();
            }
        });

        imageViewSkillIconRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                madb = new MaterialAlertDialogBuilder(v.getContext());
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_video, null);
                madb.setView(dialogView).setCancelable(true);
                madb.create().show();
                TextView descripcion;
                descripcion = dialogView.findViewById(R.id.textViewDescripcionDialogVideo);
                YouTubePlayerView video;
                video = dialogView.findViewById(R.id.youtube_player_viewVideoDialogVideo);
                descripcion.setText(skillObb.getSkillDescription());

                getLifecycle().addObserver(video);
                video.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        String videoId = skillObb.getSkillUrlVideo();
                        youTubePlayer.loadVideo(videoId, 0);
                    }

                    @Override
                    public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError error) {
                        String videoId = skillObb.getSkillUrlVideo();
                        youTubePlayer.loadVideo(videoId, 0);
                    }
                });
            }
        });

    }

    private void initRoutineRecyclerView(int positionRoutine) {
        DocumentReference reference = myDB.collection("routine").document(skillObb.getSkillRoutineFK().get(positionRoutine));
        reference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        Routine routineObb = snapshot.toObject(Routine.class);
                        recyclerViewRoutineRoutine.setLayoutManager(new LinearLayoutManager(RoutineActivity.this));
                        Query query = myDB
                                .collection("exercise")
                                .whereIn("exerciseUid", routineObb.getRoutineExerciseFK());
                        FirestoreRecyclerOptions<Exercise> myOptionsList = new FirestoreRecyclerOptions
                                .Builder<Exercise>()
                                .setLifecycleOwner(RoutineActivity.this)
                                .setQuery(query, Exercise.class)
                                .build();
                        routineListAdapterRecycler = new RoutineListAdapterRecycler(myOptionsList, routineObb);
                        routineListAdapterRecycler.notifyDataSetChanged();
                        recyclerViewRoutineRoutine.setAdapter(routineListAdapterRecycler);

                    }
                });
    }
    private void initGeneralWarmingRecyclerView() {
        DocumentReference reference = myDB.collection("routine").document("aRFiNRKlKKlg8xP68BKq");
        reference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        Routine routineObb = snapshot.toObject(Routine.class);
                        recyclerViewGeneralWarmingRoutine.setLayoutManager(new LinearLayoutManager(RoutineActivity.this));
                        Query query = myDB
                                .collection("exercise")
                                .whereIn("exerciseUid", routineObb.getRoutineExerciseFK());
                        FirestoreRecyclerOptions<Exercise> myOptionsList = new FirestoreRecyclerOptions
                                .Builder<Exercise>()
                                .setLifecycleOwner(RoutineActivity.this)
                                .setQuery(query, Exercise.class)
                                .build();
                        routineListWarmingRoutineAdapterRecycler = new RoutineListWarmingRoutineAdapterRecycler(myOptionsList, routineObb);
                        routineListWarmingRoutineAdapterRecycler.notifyDataSetChanged();
                        recyclerViewGeneralWarmingRoutine.setAdapter(routineListWarmingRoutineAdapterRecycler);
                    }
                });
    }
}