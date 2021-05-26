package com.example.skillyouneed.models;


import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;

//Modelo para clasificar los skills (se usa en las colecciones main y gadget)
public class OptionItem implements Serializable {

    private String name, url, childColection, uid;
    //TODO El valor DocumentReference da error al pasar el obb de una actividad a otra mediante el serializable (pendiente de solucion) (Duda para Antonio)
    //private DocumentReference reference;

    public OptionItem() {
    }

    /*public OptionItem(String name, String url, String childColection, String uid, DocumentReference reference) {
        this.name = name;
        this.url = url;
        this.childColection = childColection;
        this.uid = uid;
        this.reference = reference;
    }

    public DocumentReference getReference() {
        return reference;
    }

    public void setReference(DocumentReference reference) {
        this.reference = reference;
    }*/

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getChildColection() {
        return childColection;
    }

    public void setChildColection(String childColection) {
        this.childColection = childColection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
