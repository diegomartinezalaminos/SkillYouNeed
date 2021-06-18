package com.example.skillyouneed.reycles.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillyouneed.R;
import com.example.skillyouneed.models.Exercise;
import com.example.skillyouneed.reycles.listeners.ManageRoutinesExerciseOnClickListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class ManageAddRoutinesExerciseAdapterRecycler extends FirestoreRecyclerAdapter<Exercise, ManageAddRoutinesExerciseAdapterRecycler.ViewHolder> {

    private int checkPosition = -1;
    private ManageRoutinesExerciseOnClickListener manageRoutinesExerciseOnClickListener;

    public ManageAddRoutinesExerciseAdapterRecycler(@NonNull FirestoreRecyclerOptions<Exercise> options, ManageRoutinesExerciseOnClickListener manageRoutinesExerciseOnClickListener) {
        super(options);
        this.manageRoutinesExerciseOnClickListener = manageRoutinesExerciseOnClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ManageAddRoutinesExerciseAdapterRecycler.ViewHolder holder, int position, @NonNull Exercise model) {
        holder.BindHolder(model, position);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.onClick(holder.itemView);
                checkPosition = position;
                notifyDataSetChanged();
            }
        });

        paintItem(holder, model, position);
    }

    //Pinta o marca el elemento selecionado
    private void paintItem(ViewHolder holder, Exercise model, int position) {
        if (checkPosition == position){
            holder.name.setText("Pulsado");
        } else {
            holder.name.setText(model.getExerciseName());
        }
    }

    @NonNull
    @Override
    public ManageAddRoutinesExerciseAdapterRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        return new ManageAddRoutinesExerciseAdapterRecycler.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView name;
        private ImageView icon;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textViewNameExerciseItem);
            icon = (ImageView) itemView.findViewById(R.id.imageViewExerciseIconExerciseItem);
            cardView = (CardView) itemView.findViewById(R.id.cardViewExerciseItem);

            //itemView.setOnClickListener(this); //TODO duda para Antonio por que no me funciona el lisenaer desde aqui
        }

        public void BindHolder(Exercise model, int position) {
            name.setText(model.getExerciseName());
            Picasso.get().load(model.getExerciseUrlIcon())
                    .resize(80,80)
                    .centerCrop()
                    .into(icon);
        }

        @Override
        public void onClick(View v) {
            manageRoutinesExerciseOnClickListener.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }
}
