package com.example.skillyouneed.reycles.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillyouneed.R;
import com.example.skillyouneed.models.Gadget;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class SkillListGadgetFilterAdapterRecycler extends FirestoreRecyclerAdapter<Gadget, SkillListGadgetFilterAdapterRecycler.ViewHolder> {

    View view;

    public SkillListGadgetFilterAdapterRecycler(@NonNull FirestoreRecyclerOptions<Gadget> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SkillListGadgetFilterAdapterRecycler.ViewHolder holder, int position, @NonNull Gadget model) {
        holder.BindHolder(model, position);
    }

    @NonNull
    @Override
    public SkillListGadgetFilterAdapterRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gadget_item, parent, false);
        return new SkillListGadgetFilterAdapterRecycler.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private Switch selected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.imageViewIconGadgetItem);
            selected = itemView.findViewById(R.id.switchSelectedGadgetItem);
        }

        public void BindHolder(Gadget model, int position) {

            Picasso.get().load(model.getGadgetUrlIcon())
                    .resize(80,60)
                    .centerCrop()
                    .into(icon);

            selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selected.isChecked()){
                        //Activado
                        Log.d("swich: ", "seleccionado");
                    } else {
                        //Desactivado
                        Log.d("swich: ", "no seleccionado");
                    }
                }
            });
        }
    }
}
