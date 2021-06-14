package com.example.skillyouneed.reycles.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillyouneed.R;
import com.example.skillyouneed.models.Exercise;
import com.example.skillyouneed.models.Routine;
import com.example.skillyouneed.models.Skill;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.security.acl.Owner;
import java.util.ArrayList;

public class SkillListAdapterRecycler extends FirestoreRecyclerAdapter<Skill, SkillListAdapterRecycler.ViewHolder> {

    MaterialAlertDialogBuilder madb;


    public SkillListAdapterRecycler(@NonNull FirestoreRecyclerOptions<Skill> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SkillListAdapterRecycler.ViewHolder holder, int position, @NonNull Skill model) {
        holder.BindHolder(model, position);
    }

    @NonNull
    @Override
    public SkillListAdapterRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.skill_item, parent, false);
        return new SkillListAdapterRecycler.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //Layout
        private ImageView iconSkill;
        private ImageButton infoButton, fauboriteButton,showButton;
        private TextView name;
        private RecyclerView recyclerView;
        private Button examButton;
        //ShowButton
        private Boolean pushShowButton = false;
        private Boolean fistPush = false;
        //initDB();
        private FirebaseFirestore myDB;
        //initRecyclerViewRoutine()
        private RoutineListAdapterRecycler routineListAdapterRecycler;
        //getRoutine;
        private Routine routine;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconSkill = (ImageView) itemView.findViewById(R.id.ImageViewIconSkillItem);
            infoButton = (ImageButton) itemView.findViewById(R.id.imageButtonInfoSkillItem);
            fauboriteButton = (ImageButton) itemView.findViewById(R.id.imageButtonAddFavouriteSkillItem);
            showButton = (ImageButton) itemView.findViewById(R.id.imageButtonShowSkillItem);
            examButton = (Button) itemView.findViewById(R.id.buttonExamSkillItem);
            name = (TextView) itemView.findViewById(R.id.textViewNameSkillItem);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycleViewRoutineSkillItem);
            initDB();
        }
        private void initDB() {
            myDB = FirebaseFirestore.getInstance();
        }

        private void initRecyclerViewRoutine() {
            CollectionReference exerciseReference = myDB.collection("exercise");
            Query query = exerciseReference
                    .orderBy("skillName");

            FirestoreRecyclerOptions<Exercise> myRecyclerOptions = new FirestoreRecyclerOptions
                    .Builder<Exercise>()
                    //.setLifecycleOwner(this)
                    .setQuery(query, Exercise.class)
                    .build();

            getRoutine();
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            routineListAdapterRecycler = new RoutineListAdapterRecycler(myRecyclerOptions, routine);
            routineListAdapterRecycler.notifyDataSetChanged();
            recyclerView.setAdapter(routineListAdapterRecycler);

        }

        public void BindHolder(Skill model, int position) {

            name.setText(model.getSkillName().toString());
            Picasso.get().load(model.getSkillUrlIcon())
                    .resize(60, 60)
                    .centerCrop()
                    .into(iconSkill);

            showButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pushShowButton = !pushShowButton;
                    if (pushShowButton){
                        recyclerView.setVisibility(View.VISIBLE);
                        examButton.setVisibility(View.VISIBLE);
                        if (!fistPush){
                            initRecyclerViewRoutine();
                            fistPush = true;
                        }

                    }else {
                        recyclerView.setVisibility(View.GONE);
                        examButton.setVisibility(View.GONE);
                    }

                }
            });

            iconSkill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    madb = new MaterialAlertDialogBuilder(v.getContext());
                    View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_video, null);
                    TextView descripcion;
                    descripcion = dialogView.findViewById(R.id.textViewDescripcionDialogVideo);
                    YouTubePlayerView video;
                    video = dialogView.findViewById(R.id.youtube_player_viewVideoDialogVideo);

                    video.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onApiChange(YouTubePlayer youTubePlayer) {
                            super.onApiChange(youTubePlayer);

                            youTubePlayer.loadVideo("PL5SMTAVPk6h9g_nyUtl_wrEm7FHP7lCk4", 0); //TODO No coge el codigo del video
                        }
                    });
                    descripcion.setText(model.getSkillDescription());
/*
                    descripcion.setText("Descripcion escrita desde la clase");
                    Log.d("SkillDescription", model.getSkillDescription());
                    Log.d("SkillDescription", "dsdadsaasd");
*/

                    madb.setView(dialogView).setCancelable(true);
                    madb.create().show();
                }
            });

            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    madb = new MaterialAlertDialogBuilder(v.getContext());
                    madb.setTitle("Descripcion:");
                    madb.setMessage(model.getSkillDescription());
                    madb.create().show();

                }
            });

            fauboriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        private void getRoutine() {

            String routineUid = getItem(getAdapterPosition()).getSkillRoutineFK().get(0);
            DocumentReference referenceRoutine = myDB.collection("routine").document(routineUid);
            String name = referenceRoutine.get().getResult().toObject(Routine.class).getRoutineName();

/*            Query queryRoutine = myDB.collection("routine").whereEqualTo("routineUid", routineUid);
            queryRoutine.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                routine = snapshot.toObject(Routine.class);
                            }

                        }
                    });
            Log.d("PruebaFinal:", routine.getRoutineName());*/

/*            String routineName = "Name", routineDescription = "Descripcion", routineDifficulty = "Easy";
            ArrayList<Integer> routineRep = new ArrayList<>();
            routineRep.add(2);
            ArrayList<Integer> routineSet = new ArrayList<>();
            routineSet.add(2);
            ArrayList<Integer> routineTime = new ArrayList<>();
            routineTime.add(0);
            ArrayList<String> routineExerciseFK = new ArrayList<>();
            routineExerciseFK.add("J6lEFBD7ClJ154wj2nTg");
            routine = new Routine(null, routineName, routineDescription, routineDifficulty, routineRep, routineSet, routineTime, routineExerciseFK);*/
        }

        @Override
        public void onClick(View v) {

        }
    }
}
