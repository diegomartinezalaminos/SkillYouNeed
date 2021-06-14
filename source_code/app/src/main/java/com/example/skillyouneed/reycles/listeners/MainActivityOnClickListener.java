package com.example.skillyouneed.reycles.listeners;
import com.example.skillyouneed.models.Type;

public interface MainActivityOnClickListener {
    void onIntemClick(Type model, int position);
    //Cambiar mas adelante para coger id con snapshot y quitar campo de la bbdd
    //void onIntemClick(DocumentSnapshot snapshot, int position);// Se le pasa el obb a snapshot para luego poder usar le metodo getID y obtener la id del documento
}