package com.example.skillyouneed.reycles.adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillyouneed.R;
import com.example.skillyouneed.models.Exercise;
import com.example.skillyouneed.models.Routine;
import com.example.skillyouneed.reycles.listeners.ManageDeleteEditRoutineExerciseListOnClickListener;
import com.example.skillyouneed.utilities.Constant;
import com.example.skillyouneed.utilities.SentencesFirestore;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManageDeleteEditRoutineExerciseListAdapter extends FirestoreRecyclerAdapter<Exercise, ManageDeleteEditRoutineExerciseListAdapter.ViewHolder> {

    FirestoreRecyclerOptions<Exercise> list;
    private Routine selectRoutine;
    private MaterialAlertDialogBuilder madb;
    private ManageDeleteEditRoutineExerciseListOnClickListener manageDeleteEditRoutineExerciseListOnClickListener;


    public ManageDeleteEditRoutineExerciseListAdapter(@NonNull FirestoreRecyclerOptions<Exercise> options, ManageDeleteEditRoutineExerciseListOnClickListener manageDeleteEditRoutineExerciseListOnClickListener, Routine selectRoutine) {
        super(options);
        this.manageDeleteEditRoutineExerciseListOnClickListener = manageDeleteEditRoutineExerciseListOnClickListener;
        this.selectRoutine = selectRoutine;
        this.list = options;
    }
    @Override
    protected void onBindViewHolder(@NonNull ManageDeleteEditRoutineExerciseListAdapter.ViewHolder holder, int position, @NonNull Exercise model) {
        holder.BindHolder(model, position);
    }

    @NonNull
    @Override
    public ManageDeleteEditRoutineExerciseListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_exercise_manage_delete_edit_routine_item, parent, false);
        return new ManageDeleteEditRoutineExerciseListAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private ImageButton edit, delete;
        private TextView name;
        private FirebaseFirestore myDB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.imageViewIconRecyclerExerciseManageDeleteEditRoutineItem);
            name = (TextView) itemView.findViewById(R.id.textViewNameRecyclerExerciseManageDeleteEditRoutineItem);
            edit = (ImageButton) itemView.findViewById(R.id.imageButtonEditRecyclerExerciseManageDeleteEditRoutineItem);
            delete = (ImageButton) itemView.findViewById(R.id.imageButtonDeleteRecyclerExerciseManageDeleteEditRoutineItem);
            initDDBB();
        }

        private void initDDBB() {
            myDB = FirebaseFirestore.getInstance();
        }

        public void BindHolder(Exercise model, int position) {

            name.setText(model.getExerciseName());
            Picasso.get().load(model.getExerciseUrlIcon())
                    .resize(50, 50)
                    .centerCrop()
                    .into(icon);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manageDeleteEditRoutineExerciseListOnClickListener.onIntemClick(getItem(getAdapterPosition()), Constant.EDIT_CODE, getAdapterPosition());
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    madb = new MaterialAlertDialogBuilder(v.getContext());
                    madb.setTitle("Borrar Exercise: ")
                            .setMessage("Estas seguro que quieres borrar el ejercicio '" + model.getExerciseName() + "'")
                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (list.getSnapshots().size() > 1){
                                        SentencesFirestore sentence = new SentencesFirestore();
                                        DocumentReference reference = myDB.collection("routine").document(selectRoutine.getRoutineUid());

                                        ArrayList<String> newRoutineExerciseFK = selectRoutine.getRoutineExerciseFK();
                                        ArrayList<Integer> newRep = selectRoutine.getRoutineRep();
                                        ArrayList<Integer> newTime = selectRoutine.getRoutineTime();
                                        ArrayList<Integer> newRound = selectRoutine.getRoutineSet();

                                        int posArrayListExercise = newRoutineExerciseFK.indexOf(model.getExerciseUid());
                                        if (posArrayListExercise != -1){
                                            newRoutineExerciseFK.remove(posArrayListExercise);
                                            newRep.remove(posArrayListExercise);
                                            newTime.remove(posArrayListExercise);
                                            newRound.remove(posArrayListExercise);

                                            sentence.updateDocument(reference, "routineExerciseFK", newRoutineExerciseFK);
                                            sentence.updateDocument(reference, "routineRep", newRep);
                                            sentence.updateDocument(reference, "routineTime", newTime);
                                            sentence.updateDocument(reference, "routineSet", newRound);
                                        }
                                        //Actualizamos recycler con el notifyDataSetChanged() en el Actyvity no funciona
                                        manageDeleteEditRoutineExerciseListOnClickListener.onIntemClick(getItem(getAdapterPosition()), Constant.REFRESH_CODE, getAdapterPosition());
                                    }else {
                                        Snackbar.make(name, "La rutina tiene que tener un ejercicio como minimo", Snackbar.LENGTH_SHORT).show();
                                    }

                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setCancelable(false)
                            .create().show();
                }
            });
        }
    }
}
