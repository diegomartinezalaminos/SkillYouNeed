package com.example.skillyouneed.reycles;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillyouneed.R;
import com.example.skillyouneed.models.Exercise;
import com.example.skillyouneed.models.Routine;
import com.example.skillyouneed.models.Skill;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class SkillListAdapterRecycler extends FirestoreRecyclerAdapter<Skill, SkillListAdapterRecycler.ViewHolder> {

    //Variables
    private SkillListOnClickListener skillListOnClickListener;
    private Routine routine;
    private MaterialAlertDialogBuilder madb ;


    public SkillListAdapterRecycler(@NonNull FirestoreRecyclerOptions<Skill> options, SkillListOnClickListener skillListOnClickListener) {
        super(options);
        this.skillListOnClickListener = skillListOnClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull SkillListAdapterRecycler.ViewHolder holder, int position, @NonNull Skill model) {
        holder.BindHolder(model, position);
    }

    @NonNull
    @Override
    public SkillListAdapterRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.final_skill_item, parent, false);
        return new SkillListAdapterRecycler.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private TextView name;
        private ImageButton iconButton, showButton, addFavouriteButton, infoButton;
        private Button buttonExam;

        private RecyclerView recycleViewRoutine;
        private RoutineListAdapterRecycler myAdapter;
        private FirebaseFirestore myDb;
        private CollectionReference index;
        private Boolean pushShowButton = false;
        private Boolean pushShowFirt = false;

        //Variables layout dialog_video.xml
        private VideoView video;
        private TextView descripcion;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewNameFinalSkillItem);
            iconButton = itemView.findViewById(R.id.imageButtonIconFinalSkillItem);
            showButton = itemView.findViewById(R.id.imageButtonShowFinalSkillItem);
            addFavouriteButton = itemView.findViewById(R.id.imageButtonAddFavouriteFinalSkillItem);
            infoButton = itemView.findViewById(R.id.imageButtonInfoFinalSkillItem);
            buttonExam = itemView.findViewById(R.id.buttonExamFinalSkillItem);
            recycleViewRoutine = itemView.findViewById(R.id.recycleViewRoutineFinalSkillItem);

            initDataBase();
            itemView.setOnClickListener(this);
        }

        public void BindHolder(Skill model, int position) {
            name.setText(model.getSkillName());

            iconButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    madb = new MaterialAlertDialogBuilder(v.getContext());
                    View content = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_video, null);
                    //TODO NO funciona la funcion (Solucionar mas adelante)
                    initContentLayoutDialogVideo(v);

                    madb.setView(content)
                            .setCancelable(true);
                    madb.create().show();

                }

                //TODO ERROR EN LA FUNCION
                private void initContentLayoutDialogVideo(View v) {
                    video = v.findViewById(R.id.videoViewVideoDialogVideo);
                    descripcion = v.findViewById(R.id.textViewDescripcionDialogVideo);

                    video.setVideoPath(model.getSkillUrlVideo());
                    MediaController mediaController = new MediaController(v.getContext());
                    mediaController.setAnchorView(video);
                    video.setMediaController(mediaController);
                    //video.requestFocus();
                    video.start();

                    descripcion.setText(model.getSkillDescription());
                }
            });

            showButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pushShowButton = !pushShowButton;
                    if (pushShowButton){
                        recycleViewRoutine.setVisibility(View.VISIBLE);
                        buttonExam.setVisibility(View.VISIBLE);
                        if (!pushShowFirt){
                            initRecyclerView(model, v.getContext()); //TODO no se si es mejor pasarle com parámetro itemView.getContext() o v.getContext() o es indiferente
                            pushShowFirt = true;
                        }
                    }else {
                        recycleViewRoutine.setVisibility(View.GONE);
                        buttonExam.setVisibility(View.GONE);
                    }
                }
            });

            addFavouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            buttonExam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        private void initDataBase() {
            myDb = FirebaseFirestore.getInstance();
            index = myDb.collection("exercise");
/*            //---------------------------------------------------------------
            //Crear un obb prueba
            ArrayList<String> routineExerciseFK = new ArrayList<>();
            routineExerciseFK.add("HVGQRK7b0bSVGZoSaARw");
            ArrayList<Integer> rep = new ArrayList<>();
            rep.add(12);
            ArrayList<Integer> time = new ArrayList<>();
            time.add(0);
            ArrayList<Integer> set = new ArrayList<>();
            set.add(3);
            routine = new Routine("8TUNIe2CVqszIuc0u0Au", "ObbRutinaPrueba","Esto es una descripcion", "Dificil", rep, set, time, routineExerciseFK);
            //-----------------------------------------------------------------------*/
        }

        private void initRecyclerView(Skill model, Context context) {

            recycleViewRoutine.setLayoutManager(new LinearLayoutManager(context));

            //TODO Esta funcion se encarga de obtener el obb routine de la bbdd la funcion da error
            // esta comentada y en su luegar utilizo el obb de prueba definida en funcion initDDBB() (pendiente de solucion) (Duda Antonio)
            getObbRutina(model, 0); //La posicion la he puesto de forma manual

            Query query = myDb
                    .collection(index.getPath())
                    //.whereIn("exerciseUid", routine.getRoutineExerciseFK())
                    .orderBy("exerciseName");

            FirestoreRecyclerOptions<Exercise> myRecyclerOptions = new FirestoreRecyclerOptions
                    .Builder<Exercise>()
                    //.setLifecycleOwner() //TODO no se si es necesario poner esto si está dentro del cecyclerView
                    .setQuery(query, Exercise.class)
                    .build();

            myAdapter = new RoutineListAdapterRecycler(myRecyclerOptions, routine);
            myAdapter.notifyDataSetChanged();
            recycleViewRoutine.setAdapter(myAdapter);
        }

        //TODO ERROR EN LA FUNCION
        //Cogemos de la base de datos la rutina que se indica detro
        // del array finalSkillRoutine dentro del docuemtno de la colecion finalSkill
        private void getObbRutina(Skill model, int positionArrayRutina) {

            //Referencia del documento routina en la bbdd
            String routineUid = model.getSkillRoutineFK().get(positionArrayRutina);
            DocumentReference routineReference = myDb.collection("routine").document(routineUid);

            //Acceder al documento y pasarlo a obb Routine
            routineReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot snapshot) {
                    //TODO El error se produce en la linea de abajo al convertir el documento a obb
                    // (se cierra la actividad y vuelve a la anterio) (pendiente de solucion) (duda para Antonio)
                    routine = snapshot.toObject(Routine.class);
                    Log.d("ObjetoHecho", routine.getRoutineName());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("ErrorRoutineObb", "Error whit: ", e);
                }
            });
        }

        @Override
        public void onClick(View v) {
            skillListOnClickListener.onIntemClick(getItem(getAdapterPosition()), getAdapterPosition());//Da la posicion
        }
    }
}
