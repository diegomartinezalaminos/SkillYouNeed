package com.example.skillyouneed.models;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;

public class Type implements Serializable {
    private String typeUid, typeName, typeUrlIcon;

    public Type() {

    }

    public Type(String typeUid, String typeName, String typeUrlIcon) {
        this.typeUid = typeUid;
        this.typeName = typeName;
        this.typeUrlIcon = typeUrlIcon;
    }

    public String getTypeUid() {
        return typeUid;
    }

    public void setTypeUid(String typeUid) {
        this.typeUid = typeUid;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeUrlIcon() {
        return typeUrlIcon;
    }

    public void setTypeUrlIcon(String typeUrlIcon) {
        this.typeUrlIcon = typeUrlIcon;
    }
}
