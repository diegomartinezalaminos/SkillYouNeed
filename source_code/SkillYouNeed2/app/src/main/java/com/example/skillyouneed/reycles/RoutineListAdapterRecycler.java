package com.example.skillyouneed.reycles;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillyouneed.R;
import com.example.skillyouneed.models.Exercise;
import com.example.skillyouneed.models.Routine;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class RoutineListAdapterRecycler extends FirestoreRecyclerAdapter<Exercise, RoutineListAdapterRecycler.ViewHolder> {

    private Routine routine;

    public RoutineListAdapterRecycler(@NonNull FirestoreRecyclerOptions<Exercise> options, Routine routine) {
        super(options);
        this.routine = routine; //TODO El obb de prueba que se le pasa le llega bien con todos los datos.
        Log.d("Routine", routine.getRoutineName());
    }

    @Override
    protected void onBindViewHolder(@NonNull RoutineListAdapterRecycler.ViewHolder holder, int position, @NonNull Exercise exercise) {
        holder.BindHolder(exercise, position);
    }

    @NonNull
    @Override
    public RoutineListAdapterRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.routine_item, parent, false);
        return new RoutineListAdapterRecycler.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView name, reps, rounds;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.imageViewIconRoutineItem);
            name = itemView.findViewById(R.id.textViewNameRoutineItem);
            reps = itemView.findViewById(R.id.textViewRepetitionsRoutineItem);
            rounds = itemView.findViewById(R.id.textViewRoundsRoutineItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO mostrar video del ejercicio junto con descripción
                }
            });
        }

        public void BindHolder(Exercise exercise, int position) {

            Picasso.get().load(exercise.getExerciseUrlIcon())
                    .resize(80, 80)
                    .centerCrop()
                    .into(icon);
            name.setText(exercise.getExerciseName());
            reps.setText(routine.getRoutineRep().get(position).toString());
            rounds.setText(routine.getRoutineSet().get(position).toString());

        }
    }
}
