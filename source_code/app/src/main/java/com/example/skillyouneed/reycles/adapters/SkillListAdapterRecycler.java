package com.example.skillyouneed.reycles.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.skillyouneed.models.User;
import com.example.skillyouneed.reycles.listeners.SkillListOnClickListener;
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

    private MaterialAlertDialogBuilder madb;
    private SkillListOnClickListener skillListOnClickListener;


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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.skill_item, parent, false);
        return new SkillListAdapterRecycler.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //Layout
        private ImageView iconSkill;
        private ImageButton infoButton;
        private TextView name;
        //initDB();
        private FirebaseFirestore myDB;
        //initRecyclerViewRoutine()
        private RoutineListAdapterRecycler routineListAdapterRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconSkill = (ImageView) itemView.findViewById(R.id.ImageViewIconSkillItem);
            infoButton = (ImageButton) itemView.findViewById(R.id.imageButtonInfoSkillItem);
            name = (TextView) itemView.findViewById(R.id.textViewNameSkillItem);
            itemView.setOnClickListener(this);
            initDB();
        }
        private void initDB() {
            myDB = FirebaseFirestore.getInstance();
        }

        public void BindHolder(Skill model, int position) {

            name.setText(model.getSkillName());
            Picasso.get().load(model.getSkillUrlIcon())
                    .resize(75, 75)
                    .centerCrop()
                    .into(iconSkill);
            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    madb = new MaterialAlertDialogBuilder(v.getContext());
                    madb.setTitle("Descripcion:");
                    madb.setMessage(model.getSkillDescription());
                    madb.create().show();

                }
            });
        }

        @Override
        public void onClick(View v) {
            skillListOnClickListener.onIntemClick(getItem(getAdapterPosition()), getAdapterPosition());

        }
    }
}
