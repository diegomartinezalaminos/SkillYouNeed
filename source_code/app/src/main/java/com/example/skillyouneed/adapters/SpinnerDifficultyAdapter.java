package com.example.skillyouneed.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.skillyouneed.R;
import com.example.skillyouneed.models.Dyfficulty;

import java.util.ArrayList;

public class SpinnerDifficultyAdapter extends ArrayAdapter<Dyfficulty> {

    public SpinnerDifficultyAdapter(@NonNull Context context, @NonNull ArrayList<Dyfficulty> model) {
        super(context, 0, model);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        return view (position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getDropDownView(position, convertView, parent);
        if (position == 0){
            return viewInicial(position, convertView, parent);
        }
        return view (position, convertView, parent);
    }

    private View view(int position, View convertView, ViewGroup parent){
        convertView = LayoutInflater.from(getContext()).inflate(
                R.layout.spinner_difficulty_item, parent, false
        );

        ImageView icon = convertView.findViewById(R.id.imageViewIconSpinnerDifficultyItem);
        TextView name = convertView.findViewById(R.id.textViewNameSpinnerDifficultyItem);

        Dyfficulty difficultyObb = getItem(position);
        icon.setImageResource(difficultyObb.getIcon());
        name.setText(difficultyObb.getName());
        return convertView;
    }

    private View viewInicial(int position, View convertView, ViewGroup parent){
        convertView = LayoutInflater.from(getContext()).inflate(
                R.layout.spinner_difficulty_inicial_item, parent, false
        );

        Dyfficulty difficultyObb = getItem(position);
        TextView name = convertView.findViewById(R.id.textViewNameDifficultySpinnerDifficultyInicialItem);
        name.setText(difficultyObb.getName());
        return  convertView;
    }
}
