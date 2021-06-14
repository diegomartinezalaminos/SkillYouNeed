package com.example.skillyouneed.reycles.adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillyouneed.R;
import com.example.skillyouneed.models.Skill;
import com.example.skillyouneed.models.Type;
import com.example.skillyouneed.reycles.listeners.ManageDeleteEditTypeOnClickListener;
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
import com.squareup.picasso.Picasso;

public class ManageDeleteEditTypeAdapterRecycler extends FirestoreRecyclerAdapter<Type, ManageDeleteEditTypeAdapterRecycler.ViewHolder> {

    private ManageDeleteEditTypeOnClickListener manageDeleteEditTypeOnClickListener;
    private MaterialAlertDialogBuilder madb;

    public ManageDeleteEditTypeAdapterRecycler(@NonNull FirestoreRecyclerOptions<Type> options, ManageDeleteEditTypeOnClickListener manageDeleteEditTypeOnClickListener) {
        super(options);
        this.manageDeleteEditTypeOnClickListener = manageDeleteEditTypeOnClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ManageDeleteEditTypeAdapterRecycler.ViewHolder holder, int position, @NonNull Type model) {
        holder.BindHolder(model, position);
    }

    @NonNull
    @Override
    public ManageDeleteEditTypeAdapterRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_type_manage_delete_edit_type_item, parent, false);
        return new ManageDeleteEditTypeAdapterRecycler.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private ImageView icon;
        private ImageButton edit, delete;
        private TextView name;
        private FirebaseFirestore myDB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.imageViewIconRecyclerTypeManageDeleteEditTypeItem);
            name = (TextView) itemView.findViewById(R.id.textViewNameRecyclerTypeManageDeleteEditTypeItem);
            edit = (ImageButton) itemView.findViewById(R.id.imageButtonEditRecyclerTypeManageDeleteEditTypeItem);
            delete = (ImageButton) itemView.findViewById(R.id.imageButtonDeleteRecyclerTypeManageDeleteEditTypeItem);
            initDDBB();
        }

        private void initDDBB() {
            myDB = FirebaseFirestore.getInstance();
        }

        public void BindHolder(Type model, int position) {
            name.setText(model.getTypeName());
            Picasso.get().load(model.getTypeUrlIcon())
                    .resize(50, 50)
                    .centerCrop()
                    .into(icon);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manageDeleteEditTypeOnClickListener.onIntemClick(getItem(getAdapterPosition()), Constant.EDIT_CODE);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    madb = new MaterialAlertDialogBuilder(v.getContext());
                    madb.setTitle("Borrar Type: ")
                            .setMessage("Estas seguro que quieres borrar el type '" + model.getTypeName() + "'")
                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    SentencesFirestore sentence = new SentencesFirestore();
                                    DocumentReference deleteDocumentReference = myDB.collection("type").document(model.getTypeUid());
                                    Query deleteGroupDocumentsQuerySkill = myDB.collection("skill").whereEqualTo("skillTypeFK", model.getTypeUid());

                                    //Se usa para borrar todas las rutinas de la skills que se borraran
                                    Query querySkill = myDB.collection("skill").whereEqualTo("skillTypeFK", model.getTypeUid());
                                    querySkill.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                                                Query query = myDB.collection("routine").whereIn("routineUid", snapshot.toObject(Skill.class).getSkillRoutineFK());
                                                sentence.deleteGroupDocuments(query);
                                            }
                                        }
                                    });

                                    sentence.deleteDocument(deleteDocumentReference);
                                    sentence.deleteGroupDocuments(deleteGroupDocumentsQuerySkill);
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

        @Override
        public void onClick(View v) {
        }
    }

}
