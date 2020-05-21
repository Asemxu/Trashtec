package com.labawsrh.aws.trashtec.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Adapters.BodegaDescuentoAdapter;
import com.labawsrh.aws.trashtec.Adapters.DescuentoAdapter;
import com.labawsrh.aws.trashtec.Adapters.EmpresaDescuentoAdapter;
import com.labawsrh.aws.trashtec.Adapters.RestaurantesDescuentoAdapter;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.Models.Descuentos;
import com.labawsrh.aws.trashtec.Models.EmpresaAsociada;
import com.labawsrh.aws.trashtec.R;

import java.util.ArrayList;
import java.util.List;

public class TiendaFragment extends Fragment {
    private  Main_User_Activity activity;
    private MaterialToolbar toolbar;
    private Fragment fragment;
    private DatabaseReference databaseReference = Firebase_Variables.database_reference;
    private ViewPager lista_farmacias_boticas;
    private List<EmpresaAsociada> empresaAsociadafarmacias;
    private EmpresaDescuentoAdapter empresaDescuentoAdapter_farmacias;
    private BodegaDescuentoAdapter empresaDescuentoAdapter_bodegas;
    private RestaurantesDescuentoAdapter empresaDescuentoAdapter_restaurantes;
    private TabLayout indicator_bodegas;
    private TabLayout indicator_restaurantes;
    private TabLayout indicator_farmacia_boticas;
    private BottomNavigationView navigationView;
    private ViewPager pager_bodegas;
    private ViewPager pager_restaurantes;
    private LinearLayout pagers;
    private FirebaseAuth auth = Firebase_Variables.firebaseauth;
    private int Heigt_pagers = 0;
    private List<EmpresaAsociada> empresaAsociadabodegas;
    private ListView lista_mis_descuentos;
    private List<EmpresaAsociada> getEmpresaAsociadarestaurantes;
    private MaterialButton btn_empresas;
    private MaterialButton btn_mis_descuentos;
    private List<Descuentos> mis_descuentos;
    private List<String> id_misd_descuentos;
    private DescuentoAdapter descuentoAdapter;
    private ProgressDialog dialog;
    public TiendaFragment(ProgressDialog dialog) {
        this.dialog = dialog;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (Main_User_Activity) getActivity();
        return inflater.inflate(R.layout.tienda_fragment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InstanciarViews(view);
        GetDataNegocios();
        indicator_farmacia_boticas.setupWithViewPager(lista_farmacias_boticas);
        indicator_bodegas.setupWithViewPager(pager_bodegas);
        indicator_restaurantes.setupWithViewPager(pager_restaurantes);
        Regresar();
        enableBottomBar(false);
        ActivarNvigation();
        Heigt_pagers = pagers.getHeight();
        GetDataDescuentos();
        GetDataMisDescuentos();
    }

    private void GetDataMisDescuentos() {
        databaseReference.child("Descuentos_Usuarios").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        String id_descuentos = data.getValue().toString();
                        id_misd_descuentos.add(id_descuentos);
                    }
                    if (id_misd_descuentos.size() > 0) {
                        databaseReference.child("Descuentos_Empresas").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                        Descuentos descuento = dataSnapshot2.getValue(Descuentos.class);
                                        for (String id_mi_descuento : id_misd_descuentos) {
                                            if (id_mi_descuento.equals(descuento.Id)) {
                                                mis_descuentos.add(descuento);
                                            }
                                        }
                                    }
                                }
                                descuentoAdapter = new DescuentoAdapter("Mis_Descuentos", getContext(), R.layout.item_descuento, mis_descuentos, activity);
                                lista_mis_descuentos.setAdapter(descuentoAdapter);
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getContext(), "No se pudo Obtener la información", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                }else
                    dialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"No se pudo Obtener la información",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void GetDataDescuentos() {
        btn_empresas.setOnClickListener(v -> {
            lista_mis_descuentos.setVisibility(View.GONE);
            pagers.setVisibility(View.VISIBLE);
        });
        btn_mis_descuentos.setOnClickListener(v->{
            lista_mis_descuentos.setVisibility(View.VISIBLE);
            pagers.setVisibility(View.GONE);
        });
    }

    private void enableBottomBar(boolean enable){
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setEnabled(enable);
        }
    }
    private void ActivarNvigation() {
        Handler handler  = new Handler();
        handler.postDelayed(() -> enableBottomBar(true),2000);
    }

    private void GetDataNegocios() {
        databaseReference.child("Centros_Asociadoas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    EmpresaAsociada empresa = data.getValue(EmpresaAsociada.class);
                    if(empresa.tipo_empresa.equals(Constantes.Farmacias_Boticas))
                        empresaAsociadafarmacias.add(empresa);
                    else if(empresa.tipo_empresa.equals(Constantes.Restaurantes))
                        getEmpresaAsociadarestaurantes.add(empresa);
                    else
                        empresaAsociadabodegas.add(empresa);
                }

                empresaDescuentoAdapter_farmacias = new EmpresaDescuentoAdapter(getContext(),empresaAsociadafarmacias,activity);
                empresaDescuentoAdapter_bodegas = new BodegaDescuentoAdapter(getContext(),empresaAsociadabodegas,activity);
                empresaDescuentoAdapter_restaurantes = new RestaurantesDescuentoAdapter(getContext(),getEmpresaAsociadarestaurantes,activity);
                lista_farmacias_boticas.setAdapter(empresaDescuentoAdapter_farmacias);
                pager_bodegas.setAdapter(empresaDescuentoAdapter_bodegas);
                pager_restaurantes.setAdapter(empresaDescuentoAdapter_restaurantes);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"No se pudo Obtener las Farmacias y Boticas",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Regresar() {
        toolbar.setNavigationOnClickListener(v->{
            fragment = new OptionsFragment();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
        });
    }

    private void InstanciarViews(View view) {
        toolbar = activity.findViewById(R.id.topAppBar_reciclaje);
        toolbar.setTitle("Tienda");
        navigationView = activity.findViewById(R.id.bottomNavigationView);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        empresaAsociadafarmacias = new ArrayList<>();
        empresaAsociadabodegas = new ArrayList<>();
        getEmpresaAsociadarestaurantes = new ArrayList<>();
        pager_bodegas = view.findViewById(R.id.lista_bodegas);
        pager_restaurantes = view.findViewById(R.id.lista_restaurantes);
        lista_farmacias_boticas = view.findViewById(R.id.lista_farmacias_boticas);
        indicator_farmacia_boticas = view.findViewById(R.id.tab_indicator_lista_farmacias_boticas);
        indicator_bodegas = view.findViewById(R.id.tab_indicator_lista_bodegas);
        indicator_restaurantes = view.findViewById(R.id.tab_indicator_restaurantes);
        btn_empresas = view.findViewById(R.id.btn_empresas_descuentos);
        btn_mis_descuentos = view.findViewById(R.id.btn_mis_descuentos);
        lista_mis_descuentos = view.findViewById(R.id.lista_mis_descuentos);
        pagers = view.findViewById(R.id.pagers);
        mis_descuentos = new ArrayList<>();
        id_misd_descuentos = new ArrayList<>();
    }
}
