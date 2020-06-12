package com.labawsrh.aws.trashtec.Clases_Helper;

import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Firebase_Variables {
    public static final FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
    private static final  FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final DatabaseReference database_reference = database.getReference();
    public static final StorageReference storage= FirebaseStorage.getInstance().getReference();
    public static StorageReference GetReferenceImage(String id,String uid){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseOptions opts = FirebaseApp.getInstance().getOptions();
        Log.i("Informacion", "Bucket = " + opts.getStorageBucket());
        return storage.getReferenceFromUrl("gs://trashtec-cb837.appspot.com/Fotos_Publicaciones/"+uid+"/").child(id).child("foto_1.png");
    }
    public static StorageReference GetReferenceImages(String id, String uid,int i){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        return storage.getReferenceFromUrl("gs://trashtec-cb837.appspot.com/Fotos_Publicaciones/"+uid+"/").child(id).child("foto_"+i+".png");
    }
}
