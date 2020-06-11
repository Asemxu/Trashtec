package com.labawsrh.aws.trashtec.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Adapters.DescuentoAdapter;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.Models.Descuentos;
import com.labawsrh.aws.trashtec.R;

import java.util.ArrayList;
import java.util.List;

public class DescuentosFragment extends Fragment {
    Main_User_Activity activity;
    private BottomNavigationView bottomNavigationView;
    private String id_empresa;
    private DatabaseReference databaseReference = Firebase_Variables.database_reference;
    private DescuentoAdapter descuentoAdapter;
    private List<Descuentos> descuentosList;
    private ListView lista_descuentos;
    private Fragment fragment;
    private String nombre_empresa;
    private MaterialToolbar toolbar;
    public DescuentosFragment(String id_empresa,String nombre_empresa){

        this.id_empresa = id_empresa;
        this.nombre_empresa = nombre_empresa;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity=(Main_User_Activity) getActivity();
        return inflater.inflate(R.layout.descuentos_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InicializarViews(view);
        enableBottomBar(false);
        CLickRegresar();
        HabilitarNavigatiom();
        GetDescuentos();
    }

    private void GetDescuentos() {
        databaseReference.child("Descuentos_Empresas").child(id_empresa).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        Descuentos descuento = data.getValue(Descuentos.class);
                        if(descuento.vista)
                            descuentosList.add(descuento);
                    }
                    descuentoAdapter = new DescuentoAdapter("Descuentos",getContext(),R.layout.item_descuento,descuentosList,activity);
                    lista_descuentos.setAdapter(descuentoAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"No se pudieron obtener los descuentos intente mas tarde",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void HabilitarNavigatiom() {
        Handler handler = new Handler();
        handler.postDelayed(() -> enableBottomBar(true),2000);
    }

    private void CLickRegresar() {
        toolbar.setNavigationOnClickListener(v->{
            ProgressDialog dialog = ProgressDialog.show(getContext(),"Tienda","Cargando...",false,false);
            fragment = new TiendaFragment(dialog);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
        });
    }

    private void InicializarViews(View view) {
        bottomNavigationView = activity.findViewById(R.id.bottomNavigationView);
        toolbar = activity.findViewById(R.id.topAppBar_reciclaje);
        toolbar.setTitle("Descuentos "+nombre_empresa);
        lista_descuentos = view.findViewById(R.id.list_view_descuentos);
        descuentosList = new ArrayList<>();
    }
    private void enableBottomBar(boolean enable){
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            bottomNavigationView.getMenu().getItem(i).setEnabled(enable);
        }
    }
}
