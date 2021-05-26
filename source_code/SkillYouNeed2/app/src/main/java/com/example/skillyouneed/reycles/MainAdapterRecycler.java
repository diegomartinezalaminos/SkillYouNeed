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

public class MainAdapterRecycler extends FirestoreRecyclerAdapter<OptionItem, MainAdapterRecycler.ViewHolder>{
    private MainActivityOnClickListener mainActivityOnClickListener;

    public MainAdapterRecycler(@NonNull FirestoreRecyclerOptions<OptionItem> options, MainActivityOnClickListener mainActivityOnClickListener) {
        super(options);
        this.mainActivityOnClickListener = mainActivityOnClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull OptionItem optionItem) {
        holder.name.setText(optionItem.getName());
        Picasso.get().load(optionItem.getUrl())
                .resize(80, 80)
                .centerCrop()
                .into(holder.foto);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_item, parent, false);
        return new ViewHolder(view);
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
            mainActivityOnClickListener.onIntemClick(getItem(getAdapterPosition()), getAdapterPosition());//Da la posicion
        }
    }
}
