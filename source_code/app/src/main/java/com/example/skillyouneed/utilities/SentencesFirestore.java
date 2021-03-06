package com.example.skillyouneed.utilities;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.skillyouneed.activities.LoginActivity;
import com.example.skillyouneed.activities.UserProfileActivity;
import com.example.skillyouneed.models.Type;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SentencesFirestore {
    //-----------------------Format url-------------------//
    public String stringUrlFormat(String str){
        if (str.length() > 28){
            int startPositionUrlPc = str.indexOf("https://www.youtube.com/watch?v=") + 32; //Url web pc
            if (startPositionUrlPc != 31){
                int finalPositionUrlPc = startPositionUrlPc + 11; //11 es el numero de caracteres que tienen las id de yt
                String ytUid = str.substring(startPositionUrlPc, finalPositionUrlPc);
                return ytUid;
            }else {
                return "error";
            }
        } else if (str.length() > 17 && str.length() < 29){
            int startPositionUrlMo = str.indexOf("https://youtu.be/") + 17; //Url app yt
            if (startPositionUrlMo != 16){
                int finalPositionUrlMo = startPositionUrlMo + 11; //11 es el numero de caracteres que tienen las id de yt
                String ytUid = str.substring(startPositionUrlMo, finalPositionUrlMo);
                return ytUid;
            } else {
                return "error";
            }

        } else {
            return "error";
        }
    }

    //-----------------------Add user---------------------//

    //----------------------Delete user-------------------//

    public void deleteUser(FirebaseUser currentUser, DocumentReference documentReference){
        currentUser.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d("Debug deleteUser: ", "User has been delete successfuly");
                        }else{
                            Log.w("Warning deleteUser: ", "Error deleting user", task.getException());
                        }
                    }
                });
        documentReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Debug deleteUser", "Document has been delete successfuly");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Warning deleteUser:", "Error deleting document", e);
                    }
                });
    }


    //-----------------------Get document------------------//

    //-----------------------Add document------------------//

    public void addDocument(CollectionReference collectionReference, Object obb, String fileUid){

        collectionReference
                .add(obb)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        if (fileUid != null){
                            documentReference.update(fileUid, documentReference.getId())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Debug addObbToDB: ", "El campo '" + fileUid + "' se ha actualizado correctamente");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Warning addObbToDB: ", "Error al actualizar el campo '" + fileUid + "' typeUid del documento", e);
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Warning addObbToDB: ", "Error a??adiendo el documento", e);
                    }
                });
    }

    //-----------------------Delete document--------------//

    public void deleteDocument(DocumentReference documentReference){

        documentReference
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Debug:", "DocumentsSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Warnign:", "Error deleting documents");
                    }
                });
    }

    //-----------------------Delete group of document--------------//

    public void deleteDocument(ArrayList<DocumentReference> referenceArrayList){

        for (DocumentReference documentReference : referenceArrayList){
            documentReference
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Debug:", "DocumentsSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Warnign:", "Error deleting documents");
                        }
                    });
        }
    }

    public void deleteGroupDocuments(Query query){
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                DocumentReference reference = documentSnapshot.getReference();
                                reference.delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("Debug:", "Documentsnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Warning:", "Error deleting document", e);
                                            }
                                        });
                            }

                        }else {
                            Log.d("Debug:", "Error getting documents --> ", task.getException());
                        }
                    }
                });
    }

    //-----------------------Update document--------------//

    public  void updateDocument(DocumentReference reference, String fieldName, String newValue){
        reference.update(fieldName, newValue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Debug: ", "El campo '" + fieldName + "' se ha actualizado correctamente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Warning: ", "Error al actualizar el campo routineUid del documento", e);
                    }
                });
    }

    public  void updateDocument(DocumentReference reference, String fieldName, Integer newValue){
        reference.update(fieldName, newValue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Debug: ", "El campo '" + fieldName + "' se ha actualizado correctamente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Warning: ", "Error al actualizar el campo routineUid del documento", e);
                    }
                });
    }

    public  void updateDocument(DocumentReference reference, String fieldName, Boolean newValue){
        reference.update(fieldName, newValue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Debug: ", "El campo '" + fieldName + "' se ha actualizado correctamente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Warning: ", "Error al actualizar el campo routineUid del documento", e);
                    }
                });
    }

    public  void updateDocument(DocumentReference reference, String fieldName, ArrayList newValue){
        reference.update(fieldName, newValue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Debug: ", "El campo '" + fieldName + "' se ha actualizado correctamente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Warning: ", "Error al actualizar el campo routineUid del documento", e);
                    }
                });
    }

    public  void updateDocument(DocumentReference reference, String fieldName, String newValue, int o){
        reference.update(fieldName, newValue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Debug: ", "El campo '" + fieldName + "' se ha actualizado correctamente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Warning: ", "Error al actualizar el campo routineUid del documento", e);
                    }
                });
    }

    //-----------------------Update group of document--------------//

    public void updateGroupDocuments(Query query, String fieldName, String newValue){
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                DocumentReference reference = document.getReference();
                                reference.update(fieldName, newValue)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("Debug: ", "El campo '" + fieldName + "' se ha actualizado correctamente");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Warning: ", "Error al actualizar el campo routineUid del documento", e);
                                            }
                                        });
                            }
                        }else {
                            Log.d("Debug:", "Error getting documents --> ", task.getException());
                        }

                    }
                });
    }

    public void updateGroupDocuments(Query query, String fieldName, Integer newValue){
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                DocumentReference reference = document.getReference();
                                reference.update(fieldName, newValue)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("Debug: ", "El campo '" + fieldName + "' se ha actualizado correctamente");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Warning: ", "Error al actualizar el campo routineUid del documento", e);
                                            }
                                        });
                            }
                        }else {
                            Log.d("Debug:", "Error getting documents --> ", task.getException());
                        }

                    }
                });
    }

    public void updateGroupDocuments(Query query, String fieldName, Boolean newValue){
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                DocumentReference reference = document.getReference();
                                reference.update(fieldName, newValue)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("Debug: ", "El campo '" + fieldName + "' se ha actualizado correctamente");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Warning: ", "Error al actualizar el campo routineUid del documento", e);
                                            }
                                        });
                            }
                        }else {
                            Log.d("Debug:", "Error getting documents --> ", task.getException());
                        }

                    }
                });
    }

    public void updateGroupDocuments(Query query, String fieldName, ArrayList newValue){
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                DocumentReference reference = document.getReference();
                                reference.update(fieldName, newValue)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("Debug: ", "El campo '" + fieldName + "' se ha actualizado correctamente");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Warning: ", "Error al actualizar el campo routineUid del documento", e);
                                            }
                                        });
                            }
                        }else {
                            Log.d("Debug:", "Error getting documents --> ", task.getException());
                        }

                    }
                });
    }

}
