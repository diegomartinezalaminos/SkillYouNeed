package com.example.skillyouneed.reycles;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillyouneed.R;
import com.example.skillyouneed.models.Exercise;
import com.example.skillyouneed.models.FinalSkill;
import com.example.skillyouneed.models.Routine;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class FinalSkillListAdapterRecycler extends FirestoreRecyclerAdapter<FinalSkill, FinalSkillListAdapterRecycler.ViewHolder> {

    //Variables
    private FinalSkillListOnClickListener finalSkillListOnClickListener;
    private Routine routine;

    public FinalSkillListAdapterRecycler(@NonNull FirestoreRecyclerOptions<FinalSkill> options, FinalSkillListOnClickListener finalSkillListOnClickListener) {
        super(options);
        this.finalSkillListOnClickListener = finalSkillListOnClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull FinalSkillListAdapterRecycler.ViewHolder holder, int position, @NonNull FinalSkill finalSkill) {
        holder.BindHolder(finalSkill, position);
    }

    @NonNull
    @Override
    public FinalSkillListAdapterRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.final_skill_item, parent, false);
        return new FinalSkillListAdapterRecycler.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private TextView name;
        private ImageButton iconButton, showButton, addFavouriteButton, infoButton;
        private Button buttonExam;

        private RecyclerView recycleViewRoutine;
        private Exercise exercise;
        private RoutineListAdapterRecycler myAdapter;
        private FirebaseFirestore myDb;
        private CollectionReference index;
        private Boolean pushShowButton = false;
        private Boolean pushShowFirt = false;


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

        public void BindHolder(FinalSkill finalSkill, int position) {
            name.setText(finalSkill.getFinalSkillName());

            iconButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
                            initRecyclerView(finalSkill, position, itemView.getContext()); //TODO no se si es mejor pasarle com parámetro itemView.getContext() o v.getContext() o es indiferente
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

            //TODO no funciona la funcion que se encarga de coger el oobb rutina por lo tanto para seguir trabajado con la app he hecho una de prueba (Duda para Antonio)
            //---------------------------------------------------------------
            //Crear un obb prueba
            DocumentReference documentReference = index.document("HVGQRK7b0bSVGZoSaARw");
            DocumentReference documentReferenceRutina = myDb.collection("routine").document("8TUNIe2CVqszIuc0u0Az");
            ArrayList<DocumentReference> arrayListDocuments = new ArrayList<>();
            arrayListDocuments.add(documentReference);
            ArrayList<Number> rep = new ArrayList<>();
            rep.add(12);
            ArrayList<Number> time = new ArrayList<>();
            time.add(22);
            ArrayList<Number> set = new ArrayList<>();
            set.add(3);

            routine = new Routine("sad",
                    "dsda",
                    "NameObject",
                    arrayListDocuments,
                    rep,
                    set,
                    time,
                    documentReferenceRutina);
            //-----------------------------------------------------------------------
        }

        private void initRecyclerView(FinalSkill finalSkill, int position, Context context) {

            recycleViewRoutine.setLayoutManager(new LinearLayoutManager(context));

            //TODO Esta funcion se encarga de obtener el obb routine de la bbdd la funcion da error
            // esta comentada y en su luegar utilizo el obb de prueba definida en funcion initDDBB() (pendiente de solucion) (Duda Antonio)
            //getObbRutina(finalSkill, 0);

            Query query = myDb
                    .collection(index.getPath())
                    .whereIn("reference", routine.getRoutineExercise())
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

        //Cogemos de la base de datos la rutina que se indica detro
        // del array finalSkillRoutine dentro del docuemtno de la colecion finalSkill
        private void getObbRutina(FinalSkill finalSkill, int positionArrayRutina) {

            Log.d("RoutineObb", "funcion getObbRutina");
            //Referencia del documento routina en la bbdd
            DocumentReference reference = (DocumentReference) finalSkill.getFinalSkillRoutine().get(positionArrayRutina);

            //Acceder al documento y pasarlo a obb Routine
            reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot snapshot) {
                    //TODO El error se produce en la linea de abajo al convertir el documento a obb
                    // (se cierra la actividad y vuelve a la anterio) (pendiente de solucion) (duda para Antonio)
                    routine = snapshot.toObject(Routine.class);
                    Log.d("RoutineObb", routine.getRoutineName());
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
            finalSkillListOnClickListener.onIntemClick(getItem(getAdapterPosition()), getAdapterPosition());//Da la posicion

        }
    }
}
