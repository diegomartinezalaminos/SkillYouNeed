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
import com.example.skillyouneed.models.Gadget;
import com.example.skillyouneed.reycles.listeners.ManageSkillGadgetListOnClickListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class ManageSkillGadgetListAdapterRecycler extends FirestoreRecyclerAdapter<Gadget, ManageSkillGadgetListAdapterRecycler.ViewHolder> {

    private int checkPosition = -1;
    private ManageSkillGadgetListOnClickListener manageSkillGadgetListOnClickListener;

    public ManageSkillGadgetListAdapterRecycler(@NonNull FirestoreRecyclerOptions<Gadget> options, ManageSkillGadgetListOnClickListener manageSkillGadgetListOnClickListener) {
        super(options);
        this.manageSkillGadgetListOnClickListener = manageSkillGadgetListOnClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ManageSkillGadgetListAdapterRecycler.ViewHolder holder, int position, @NonNull Gadget model) {
        holder.BindHolder(model, position);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.onClick(holder.itemView); //TODO llamo al onclick desde aqui porque en la clase ViewHolder no me deja
                checkPosition = position;
                notifyDataSetChanged();
            }
        });

        paintItem(holder, model, position);
    }

    private void paintItem(ManageSkillGadgetListAdapterRecycler.ViewHolder holder, Gadget model, int position) {
        if (checkPosition == position){
            holder.name.setText("select");
        }else {
            holder.name.setText(model.getGadgetName());
        }
    }

    @NonNull
    @Override
    public ManageSkillGadgetListAdapterRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_skill_gadget_list_item, parent, false);
        return new ManageSkillGadgetListAdapterRecycler.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView icon;
        private TextView name;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.imageViewIconManageSkillGadgetListItem);
            name = (TextView) itemView.findViewById(R.id.textViewNameManageSkillGadgetListItem);
            cardView = (CardView) itemView.findViewById(R.id.cardViewManageSkillGadgetList);
            //itemView.setOnClickListener(this);

        }

        public void BindHolder(Gadget model, int position) {
            name.setText(model.getGadgetName());
            Picasso.get().load(model.getGadgetUrlIcon())
                    .resize(80, 80)
                    .centerCrop()
                    .into(icon);
        }

        @Override
        public void onClick(View v) {
            manageSkillGadgetListOnClickListener.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }
}
