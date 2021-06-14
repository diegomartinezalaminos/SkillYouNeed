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
import com.example.skillyouneed.models.Gadget;
import com.example.skillyouneed.models.Skill;
import com.example.skillyouneed.reycles.listeners.ManageDeleteEditGadgetOnClickListener;
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

public class ManageDeleteEditGadgetAdapterRecycler extends FirestoreRecyclerAdapter<Gadget, ManageDeleteEditGadgetAdapterRecycler.ViewHolder> {

    private ManageDeleteEditGadgetOnClickListener manageDeleteEditGadgetOnClickListener;
    private MaterialAlertDialogBuilder madb;

    public ManageDeleteEditGadgetAdapterRecycler(@NonNull FirestoreRecyclerOptions<Gadget> options, ManageDeleteEditGadgetOnClickListener manageDeleteEditGadgetOnClickListener) {
        super(options);
        this.manageDeleteEditGadgetOnClickListener = manageDeleteEditGadgetOnClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ManageDeleteEditGadgetAdapterRecycler.ViewHolder holder, int position, @NonNull Gadget model) {
        holder.BindHolder(model, position);
    }

    @NonNull
    @Override
    public ManageDeleteEditGadgetAdapterRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_gadget_manage_delete_edit_gadget_item, parent, false);
        return new ManageDeleteEditGadgetAdapterRecycler.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private ImageButton edit, delete;
        private TextView name;
        private FirebaseFirestore myDB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.imageViewIconRecyclerGadgetManageDeleteEditGadgetItem);
            name = (TextView) itemView.findViewById(R.id.textViewNameRecyclerGadgetManageDeleteEditGadgetItem);
            edit = (ImageButton) itemView.findViewById(R.id.imageButtonEditRecyclerGadgetManageDeleteEditGadgetItem);
            delete = (ImageButton) itemView.findViewById(R.id.imageButtonDeleteRecyclerGadgetManageDeleteEditGadgetItem);
            initDDBB();
        }

        private void initDDBB() {
            myDB = FirebaseFirestore.getInstance();
        }

        public void BindHolder(Gadget model, int position) {

            name.setText(model.getGadgetName());
            Picasso.get().load(model.getGadgetUrlIcon())
                    .resize(50, 50)
                    .centerCrop()
                    .into(icon);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manageDeleteEditGadgetOnClickListener.onIntemClick(getItem(getAdapterPosition()), Constant.EDIT_CODE);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    madb = new MaterialAlertDialogBuilder(v.getContext());
                    madb.setTitle("Borrar Gadget: ")
                            .setMessage("Estas seguro que quieres borrar el Gadget '" + model.getGadgetName() + "'")
                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    SentencesFirestore sentence = new SentencesFirestore();
                                    DocumentReference deleteDocumentReferenceGadget = myDB.collection("gadget").document(model.getGadgetUid());
                                    Query deleteGroupDocumentsQuerySkill = myDB.collection("skill").whereEqualTo("skillGadgetFK", model.getGadgetUid());

                                    //Se usa para borrar todas las rutinas de la skills que se borraran
                                    Query querySkill = myDB.collection("skill").whereEqualTo("skillGadgetFK", model.getGadgetUid());
                                    querySkill.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                                                Query query = myDB.collection("routine").whereIn("routineUid", snapshot.toObject(Skill.class).getSkillRoutineFK());
                                                sentence.deleteGroupDocuments(query);
                                            }
                                        }
                                    });

                                    sentence.deleteDocument(deleteDocumentReferenceGadget);
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
    }
}
