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
import com.example.skillyouneed.models.Type;
import com.example.skillyouneed.reycles.listeners.ManageSkillTypeListOnClickListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class ManageSkillTypeListAdapterRecycler extends FirestoreRecyclerAdapter<Type, ManageSkillTypeListAdapterRecycler.ViewHolder> {

    private int checkPosition = -1;
    private ManageSkillTypeListOnClickListener manageSkillTypeListOnClickListener;

    public ManageSkillTypeListAdapterRecycler(@NonNull FirestoreRecyclerOptions<Type> options, ManageSkillTypeListOnClickListener manageSkillTypeListOnClickListener) {
        super(options);
        this.manageSkillTypeListOnClickListener = manageSkillTypeListOnClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ManageSkillTypeListAdapterRecycler.ViewHolder holder, int position, @NonNull Type model) {
        holder.BindHolder(model, position);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.onClick(holder.itemView); //TODO llamo al onclick desde aqui porque en la clase ViewHolder no me deja
                checkPosition = position;
                notifyDataSetChanged();
            }
        });
        paintItem(holder, model, position);
    }

    private void paintItem(ManageSkillTypeListAdapterRecycler.ViewHolder holder, Type model, int position) {
        if (checkPosition == position){
            holder.name.setText("select");
        }else {
            holder.name.setText(model.getTypeName());
        }
    }

    @NonNull
    @Override
    public ManageSkillTypeListAdapterRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_skill_type_list_item, parent, false);
        return new ManageSkillTypeListAdapterRecycler.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView icon;
        private TextView name;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.imageViewIconManageSkillTypeListItem);
            name = (TextView) itemView.findViewById(R.id.textViewNameManageSkillTypeListItem);
            cardView = (CardView) itemView.findViewById(R.id.cardViewManageSkillTypeListItem);
            itemView.setOnClickListener(this);
        }

        public void BindHolder(Type model, int position) {

            name.setText(model.getTypeName());
            Picasso.get().load(model.getTypeUrlIcon())
                    .resize(80, 80)
                    .centerCrop()
                    .into(icon);
        }

        @Override
        public void onClick(View v) {
            manageSkillTypeListOnClickListener.OnItemClick(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }
}
