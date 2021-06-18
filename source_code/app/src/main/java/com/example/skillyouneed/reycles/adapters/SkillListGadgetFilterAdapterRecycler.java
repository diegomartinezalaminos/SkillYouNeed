package com.example.skillyouneed.reycles.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillyouneed.R;
import com.example.skillyouneed.models.Gadget;
import com.example.skillyouneed.reycles.listeners.SkillListGadgetFilterOnClickListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SkillListGadgetFilterAdapterRecycler extends FirestoreRecyclerAdapter<Gadget, SkillListGadgetFilterAdapterRecycler.ViewHolder> {

    private View view;
    private ArrayList<String> gadgetUidArrayList = new ArrayList<>();
    private SkillListGadgetFilterOnClickListener skillListGadgetFilterOnClickListener;

    public SkillListGadgetFilterAdapterRecycler(@NonNull FirestoreRecyclerOptions<Gadget> options, SkillListGadgetFilterOnClickListener skillListGadgetFilterOnClickListener) {
        super(options);
        this.skillListGadgetFilterOnClickListener = skillListGadgetFilterOnClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull SkillListGadgetFilterAdapterRecycler.ViewHolder holder, int position, @NonNull Gadget model) {
        holder.BindHolder(model, position);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRoutineUid(model.getGadgetUid(), holder, model);
                holder.onClick(holder.itemView);
            }
        });
    }

    @NonNull
    @Override
    public SkillListGadgetFilterAdapterRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gadget_item, parent, false);
        return new SkillListGadgetFilterAdapterRecycler.ViewHolder(view);
    }

    private void addRoutineUid(String selectGadgetUid, SkillListGadgetFilterAdapterRecycler.ViewHolder holder, Gadget model) {

        Log.d("DebugManageSkill: ", "He entrado en la funcoin");

        if (gadgetUidArrayList.isEmpty()){
            gadgetUidArrayList.add(selectGadgetUid);
            Log.d("DebugManageSkill: ", "Array bacio añadimos " + selectGadgetUid);
            holder.visibility.setBackgroundColor(Color.argb(0, 255,255,255));

        }else {

            boolean repeatUid = false;
            int positionRepeatUid = -1;

            for (int x = 0; x < gadgetUidArrayList.size(); x++){
                if (gadgetUidArrayList.get(x).equals(selectGadgetUid)){
                    repeatUid = true;
                    positionRepeatUid = x;
                    Log.d("DebugManageSkill: ", "Se ha encontrado un elemento repetido " + selectGadgetUid);
                }
            }
            if (repeatUid){
                gadgetUidArrayList.remove(positionRepeatUid);
                holder.visibility.setBackgroundColor(Color.argb(77, 255,255,255));
                Log.d("DebugManageSkill: ", "Se ha eleiminado el elemento repetido" + selectGadgetUid);
            }else {
                gadgetUidArrayList.add(selectGadgetUid);
                holder.visibility.setBackgroundColor(Color.argb(0, 255,255,255));
            }

        }
        for (int u = 0; u < gadgetUidArrayList.size(); u++){
            Log.d("DebugManageSkill: ", "[" + u + "] --> "  + gadgetUidArrayList.get(u));
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView icon, visibility;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.imageViewIconGadgetItem);
            visibility = itemView.findViewById(R.id.imageViewVisibilityGadgetItem);
            cardView = itemView.findViewById(R.id.cardViewGadgetItem);
        }

        public void BindHolder(Gadget model, int position) {

            Picasso.get().load(model.getGadgetUrlIcon())
                    .resize(80,60)
                    .centerCrop()
                    .into(icon);
        }

        @Override
        public void onClick(View v) {
            skillListGadgetFilterOnClickListener.OnItemClick(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }
}
