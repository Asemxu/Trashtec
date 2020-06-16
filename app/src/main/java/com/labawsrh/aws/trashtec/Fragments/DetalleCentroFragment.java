package com.labawsrh.aws.trashtec.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Adapters.HorarioAdapter;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.Models.Centro_Acopio;
import com.labawsrh.aws.trashtec.Models.Coodenadas;
import com.labawsrh.aws.trashtec.Models.Horario;
import com.labawsrh.aws.trashtec.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


public class DetalleCentroFragment extends Fragment {
    private Main_User_Activity activity;
    private ImageView imagen_centro;
    private MaterialToolbar regresar;
    private String id_centro_acopio;
    private DatabaseReference databaseReference = Firebase_Variables.database_reference;
    private TextView empresa;
    private Fragment fragment;
    private TextView Direccion;
    private MaterialButton btn_ver_centro_google;
    private ListView lista_horario;
    private HorarioAdapter adapter;
    private ImageView imagen_empresa;
    private BottomNavigationView navigationView;
    private DatabaseReference  database = Firebase_Variables.database_reference;
    private boolean estado;
    public DetalleCentroFragment(String id,boolean estado) {
        this.id_centro_acopio = id;
        this.estado = estado;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (Main_User_Activity) getActivity();
        return inflater.inflate(R.layout.detalle_centro_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigationView = activity.findViewById(R.id.bottomNavigationView);
        if(!activity.escucha){
            navigationView.setOnNavigationItemSelectedListener(activity.navlistener);
            activity.escucha = true;
        }
        InstanciarViews(view);
        ClickVer();
        ClickRegresar();
    }

    private void ClickRegresar() {
        regresar.setNavigationOnClickListener(v->{
            if(!estado) {
                navigationView.setOnNavigationItemSelectedListener(null);
                navigationView.setSelectedItemId(R.id.calendar);
                fragment = new CalendarFragment();
                navigationView.setOnNavigationItemSelectedListener(activity.navlistener);
            }
            else
                fragment = new CentroAcopiosFragment();

            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
        });
    }

    private void ClickVer() {
        btn_ver_centro_google.setOnClickListener(v -> {
            //geo:41.3825581,2.1704375?z=16&q=41.3825581,2.1704375(Barcelona)
            database.child("Posicions_Centro_Acopio").child(id_centro_acopio).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Coodenadas coordenadas = dataSnapshot.getValue(Coodenadas.class);
                   // String ruta = "geo:"+coordenadas.Latitud+","+coordenadas.Longitud+"?z=16&q="+coordenadas.Latitud+","+coordenadas.Longitud;
                    String ruta = "google.navigation:q="+coordenadas.Latitud+","+coordenadas.Longitud;
                    Uri uri = Uri.parse(ruta);
                    Intent  intent_google = new Intent(Intent.ACTION_VIEW,uri);
                    intent_google.setPackage("com.google.android.apps.maps");
                    activity.startActivity(intent_google);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(),"No se pudo Obtener la Información",Toast.LENGTH_SHORT).show();
                }
            });

        });
    }

    private void InstanciarViews(View view) {
        imagen_centro = view.findViewById(R.id.imagen_centro);
        empresa = view.findViewById(R.id.nombre_empresa_detalle);
        Direccion = view.findViewById(R.id.direccion_empresa_detalle);
        lista_horario = view.findViewById(R.id.lista_horario);
        imagen_empresa = view.findViewById(R.id.detalle_imagen_empresa);
        btn_ver_centro_google = view.findViewById(R.id.btn_ver_centro_google);
        regresar = view.findViewById(R.id.topAppBarDetalle);
        GetMap();
    }


    private void GetMap() {
        databaseReference.child("Centros_Acopio").child(id_centro_acopio).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Horario> horarios;
                Centro_Acopio centro_acopio = dataSnapshot.getValue(Centro_Acopio.class);
                String url_map = centro_acopio.static_url;
                empresa.setText(centro_acopio.Empresa);
                Direccion.setText(centro_acopio.Direccion);
                Picasso.get().load(centro_acopio.Imagen).into(imagen_empresa);
                Picasso.get().load(url_map).into(imagen_centro, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i("Mensaje",e.getMessage());
                    }
                });

               Collection<Horario> horarios_values = centro_acopio.Horario.values();
                horarios = new ArrayList<>(horarios_values);
                adapter = new HorarioAdapter(Objects.requireNonNull(getContext()),R.layout.horario_item,horarios);
                lista_horario.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "No se pudo Obtener la información", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
