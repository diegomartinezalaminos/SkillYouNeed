package com.example.skillyouneed.reycles.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillyouneed.R;
import com.example.skillyouneed.models.Routine;
import com.example.skillyouneed.reycles.listeners.ManageSkillRoutineListOnClickListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class ManageSkillRoutineListAdapterRecycler extends FirestoreRecyclerAdapter<Routine, ManageSkillRoutineListAdapterRecycler.ViewHolder>{

    private ArrayList<String> routineUidArrayList = new ArrayList<>();
    private ManageSkillRoutineListOnClickListener manageSkillRoutineListOnClickListener;

    public ManageSkillRoutineListAdapterRecycler(@NonNull FirestoreRecyclerOptions<Routine> options, ManageSkillRoutineListOnClickListener manageSkillRoutineListOnClickListener) {
        super(options);
        this.manageSkillRoutineListOnClickListener = manageSkillRoutineListOnClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ManageSkillRoutineListAdapterRecycler.ViewHolder holder, int position, @NonNull Routine model) {
        holder.BindHolder(model, position);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRoutineUid(model.getRoutineUid(), holder, model);
                holder.onClick(holder.itemView);
            }
        });
    }

    @NonNull
    @Override
    public ManageSkillRoutineListAdapterRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_skill_routine_list_item, parent, false);
        return new ManageSkillRoutineListAdapterRecycler.ViewHolder(view);
    }

    private void addRoutineUid(String selectRoutineUid, ManageSkillRoutineListAdapterRecycler.ViewHolder holder, Routine model) {

        Log.d("DebugManageSkill: ", "He entrado en la funcoin");

        if (routineUidArrayList.isEmpty()){
            routineUidArrayList.add(selectRoutineUid);
            Log.d("DebugManageSkill: ", "Array bacio añadimos " + selectRoutineUid);
            holder.name.setText("seleccionado");

        }else {

            boolean repeatUid = false;
            int positionRepeatUid = -1;

            for (int x = 0; x < routineUidArrayList.size(); x++){
                if (routineUidArrayList.get(x).equals(selectRoutineUid)){
                    repeatUid = true;
                    positionRepeatUid = x;
                    Log.d("DebugManageSkill: ", "Se ha encontrado un elemento repetido " + selectRoutineUid);
                }
            }
            if (repeatUid){
                routineUidArrayList.remove(positionRepeatUid);
                holder.name.setText(model.getRoutineName());
                Log.d("DebugManageSkill: ", "Se ha eleiminado el elemento repetido" + selectRoutineUid);
            }else {
                routineUidArrayList.add(selectRoutineUid);
                holder.name.setText("seleccionado");
            }

        }
        for (int u = 0; u < routineUidArrayList.size(); u++){
            Log.d("DebugManageSkill: ", "[" + u + "] --> "  + routineUidArrayList.get(u));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textViewNameManageSkillRoutineListItem);
            cardView = (CardView) itemView.findViewById(R.id.cardViewManageSkillRoutineListItem);
        }

        public void BindHolder(Routine model, int position) {
            name.setText(model.getRoutineName());
        }

        @Override
        public void onClick(View v) {
            manageSkillRoutineListOnClickListener.onItemClick(getItem(getAdapterPosition()), getAdapterPosition(), routineUidArrayList);
        }
    }
}
