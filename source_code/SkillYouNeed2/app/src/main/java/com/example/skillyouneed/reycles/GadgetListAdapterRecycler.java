package com.example.skillyouneed.reycles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillyouneed.R;
import com.example.skillyouneed.models.OptionItem;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class GadgetListAdapterRecycler extends FirestoreRecyclerAdapter<OptionItem, GadgetListAdapterRecycler.ViewHolder> {

    private GadgetListOnClickListener gadgetListOnClickListener;

    public GadgetListAdapterRecycler(@NonNull FirestoreRecyclerOptions<OptionItem> options, GadgetListOnClickListener gadgetListOnClickListener) {
        super(options);
        this.gadgetListOnClickListener = gadgetListOnClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull GadgetListAdapterRecycler.ViewHolder holder, int position, @NonNull OptionItem optionItem) {
        holder.name.setText(optionItem.getName());
        Picasso.get().load(optionItem.getUrl())
                .resize(80, 80)
                .centerCrop()
                .into(holder.foto);
    }

    @NonNull
    @Override
    public GadgetListAdapterRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_item, parent, false);
        return new GadgetListAdapterRecycler.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView name;
        private ImageView foto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.SectionNameTextView);
            foto = itemView.findViewById(R.id.SectionImageView);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            gadgetListOnClickListener.onIntemClick(getItem(getAdapterPosition()), getAdapterPosition());//Da la posicion
        }
    }
}
