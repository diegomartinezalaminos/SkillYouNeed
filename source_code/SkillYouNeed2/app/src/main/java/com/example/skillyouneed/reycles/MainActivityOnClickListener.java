package com.example.skillyouneed.reycles;

import com.example.skillyouneed.models.OptionItem;

public interface MainActivityOnClickListener {
    void onIntemClick(OptionItem optionItem, int position);
    //Cambiar mas adelante para coger id con snapshot y quitar campo de la bbdd
    //void onIntemClick(DocumentSnapshot snapshot, int position);// Se le pasa el obb a snapshot para luego poder usar le metodo getID y obtener la id del documento
}