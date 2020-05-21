package com.labawsrh.aws.trashtec.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.Models.Centro_Acopio;
import com.labawsrh.aws.trashtec.R;
import com.squareup.picasso.Picasso;

public class CustomInfoCentroAcoptioAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mview;
    private Context context;
    private DatabaseReference databaseReference = Firebase_Variables.database_reference;
    public CustomInfoCentroAcoptioAdapter(Context context) {
        this.context = context;
        this.mview = LayoutInflater.from(context).inflate(R.layout.custom_info_centros_item,null);
    }
    @Override
    public View getInfoWindow(Marker m) {
        GetDataMarker(m,mview);
        return mview;
    }

    @Override
    public View getInfoContents(Marker m) {
        GetDataMarker(m,mview);
        return  mview;
    }

    private void GetDataMarker(Marker m, final View v) {
        String id = m.getSnippet();
        if (id != null) {
            final ImageView imagen = v.findViewById(R.id.imagen_maps);
            databaseReference.child("Centros_Acopio").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Centro_Acopio centro_acopio = dataSnapshot.getValue(Centro_Acopio.class);
                    ((TextView) v.findViewById(R.id.empresa_maps)).setText(centro_acopio.Empresa);
                    ((TextView) v.findViewById(R.id.direccion_maps)).setText(centro_acopio.Direccion);
                    Picasso.get().load(centro_acopio.Imagen).into(imagen);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, "No se Pudo Obtener la Informacion", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
