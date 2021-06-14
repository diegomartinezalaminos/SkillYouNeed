package com.example.skillyouneed.reycles.adapters;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillyouneed.R;
import com.example.skillyouneed.models.Routine;
import com.example.skillyouneed.models.Skill;
import com.example.skillyouneed.reycles.listeners.ManageDeleteEditRoutineRoutineListOnClickListener;
import com.example.skillyouneed.utilities.Constant;
import com.example.skillyouneed.utilities.SentencesFirestore;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManageDeleteEditRoutineRoutineListAdapter extends FirestoreRecyclerAdapter<Routine, ManageDeleteEditRoutineRoutineListAdapter.ViewHolder> {

    private MaterialAlertDialogBuilder madb;
    private ManageDeleteEditRoutineRoutineListOnClickListener manageDeleteEditRoutineRoutineListOnClickListener;

    public ManageDeleteEditRoutineRoutineListAdapter(@NonNull FirestoreRecyclerOptions<Routine> options, ManageDeleteEditRoutineRoutineListOnClickListener manageDeleteEditRoutineRoutineListOnClickListener) {
        super(options);
        this.manageDeleteEditRoutineRoutineListOnClickListener = manageDeleteEditRoutineRoutineListOnClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ManageDeleteEditRoutineRoutineListAdapter.ViewHolder holder, int position, @NonNull Routine model) {
        holder.BindHolder(model, position);
    }

    @NonNull
    @Override
    public ManageDeleteEditRoutineRoutineListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_routine_manage_delete_edit_routine_item, parent, false);
        return new ManageDeleteEditRoutineRoutineListAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageButton edit, delete, add;
        private TextView name;
        private FirebaseFirestore myDB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            add = (ImageButton) itemView.findViewById(R.id.imageButtonAddRecyclerRoutineManageDeleteEditRoutineItem);
            name = (TextView) itemView.findViewById(R.id.textViewNameRecyclerRoutineManageDeleteEditRoutineItem);
            edit = (ImageButton) itemView.findViewById(R.id.imageButtonEditRecyclerRoutineManageDeleteEditRoutineItem);
            delete = (ImageButton) itemView.findViewById(R.id.imageButtonDeleteRecyclerRoutineManageDeleteEditRoutineItem);
            initDDBB();
        }

        private void initDDBB() {
            myDB = FirebaseFirestore.getInstance();
        }

        public void BindHolder(Routine model, int position) {
            name.setText(model.getRoutineName());

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manageDeleteEditRoutineRoutineListOnClickListener.onIntemClick(getItem(getAdapterPosition()), Constant.ADD_CODE);
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manageDeleteEditRoutineRoutineListOnClickListener.onIntemClick(getItem(getAdapterPosition()), Constant.EDIT_CODE);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    madb = new MaterialAlertDialogBuilder(v.getContext());
                    madb.setTitle("Borrar Type: ")
                            .setMessage("Estas seguro que quieres borrar la rutina '" + model.getRoutineName() + "'")
                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    SentencesFirestore sentence = new SentencesFirestore();
                                    DocumentReference deleteRoutineDocumentReference = myDB.collection("routine").document(model.getRoutineUid());

                                    //Recogemos todos los documentos de skill y comprobamos is tienen la rutina que vamos a borrar en el caso de que la tengan la eliminamos del arrayList del documento skill
                                    Query querySkill = myDB.collection("skill");
                                    querySkill.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                                                ArrayList<String> newSkillRoutineUid = new ArrayList<>();

                                                newSkillRoutineUid = snapshot.toObject(Skill.class).getSkillRoutineFK();

                                                if (newSkillRoutineUid.contains(model.getRoutineUid())){
                                                    int deleteRoutineFKPosition = newSkillRoutineUid.indexOf(model.getRoutineUid());
                                                    if (deleteRoutineFKPosition != -1) {
                                                        newSkillRoutineUid.remove(deleteRoutineFKPosition);
                                                        DocumentReference reference = myDB.collection("skill").document(snapshot.toObject(Skill.class).getSkillUid());
                                                        sentence.updateDocument(reference, "skillRoutineFK", newSkillRoutineUid);
                                                    } else {
                                                        Log.d("Debug: ", "Error una skill no puede tener mas de una rutina de cada tipo (no se permiten rutinas repetidas)");
                                                    }
                                                }

                                            }
                                        }
                                    });
                                    sentence.deleteDocument(deleteRoutineDocumentReference);
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
