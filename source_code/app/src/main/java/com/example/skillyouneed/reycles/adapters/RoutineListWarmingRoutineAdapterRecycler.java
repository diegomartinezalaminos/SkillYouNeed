package com.example.skillyouneed.reycles.adapters;

import android.graphics.drawable.Drawable;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

public class RoutineListWarmingRoutineAdapterRecycler extends FirestoreRecyclerAdapter<Exercise, RoutineListWarmingRoutineAdapterRecycler.ViewHolder> {

    private Routine routine;
    private MaterialAlertDialogBuilder madb;

    public RoutineListWarmingRoutineAdapterRecycler(@NonNull FirestoreRecyclerOptions<Exercise> options, Routine routine) {
        super(options);
        this.routine = routine;
    }

    @Override
    protected void onBindViewHolder(@NonNull RoutineListWarmingRoutineAdapterRecycler.ViewHolder holder, int position, @NonNull Exercise exercise) {
        holder.BindHolder(exercise, position);
    }

    @NonNull
    @Override
    public RoutineListWarmingRoutineAdapterRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.warming_routine_item, parent, false);
        return new RoutineListWarmingRoutineAdapterRecycler.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon, repTime;
        private TextView name, reps, rounds;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.imageViewIconWarmingRoutineItem);
            name = itemView.findViewById(R.id.textViewNameWarmingRoutineItem);
            reps = itemView.findViewById(R.id.textViewRepetitionsWarmingRoutineItem);
            rounds = itemView.findViewById(R.id.textViewRoundsWarmingRoutineItem);
            repTime = itemView.findViewById(R.id.imageViewRepTimeWarmingRoutineItem);
        }

        public void BindHolder(Exercise exercise, int position) {

            Picasso.get().load(exercise.getExerciseUrlIcon())
                    .resize(80, 80)
                    .centerCrop()
                    .into(icon);
            name.setText(exercise.getExerciseName());

            //En los siguinetes condicionales comprobamos si se va ha usar el sistema de repes para el ejercicio o tiempo en segundos
            if (routine.getRoutineRep().get(position) > 0 && routine.getRoutineTime().get(position) == 0){
                reps.setText(routine.getRoutineRep().get(position).toString());
                repTime.setBackgroundResource(R.drawable.ic_rep);
            } else if (routine.getRoutineRep().get(position) == 0 && routine.getRoutineTime().get(position) > 0){

                reps.setText(routine.getRoutineTime().get(position).toString());
                repTime.setBackgroundResource(R.drawable.ic_time);
            } else {
                Log.e("error_rutina", "Los valores time y rep no pueden ser los dos en la misma posición 0 o mayor");
            }
            rounds.setText(routine.getRoutineSet().get(position).toString() + " set");

            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    madb = new MaterialAlertDialogBuilder(v.getContext());
                    View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_video, null);
                    madb.setView(dialogView).setCancelable(true);
                    madb.create().show();
                    TextView descripcion;
                    descripcion = dialogView.findViewById(R.id.textViewDescripcionDialogVideo);
                    YouTubePlayerView video;
                    video = dialogView.findViewById(R.id.youtube_player_viewVideoDialogVideo);
                    descripcion.setText(exercise.getExerciseDescription());

                    video.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            String videoId = exercise.getExerciseUrlVideo();
                            youTubePlayer.loadVideo(videoId, 0);
                        }

                        @Override
                        public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError error) {
                            String videoId = exercise.getExerciseUrlVideo();
                            youTubePlayer.loadVideo(videoId, 0);
                        }
                    });
                }
            });
        }

    }
}
