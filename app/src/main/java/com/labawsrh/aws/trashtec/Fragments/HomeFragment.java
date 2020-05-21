package com.labawsrh.aws.trashtec.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.labawsrh.aws.trashtec.Adapters.BeneficioAdapter;
import com.labawsrh.aws.trashtec.Adapters.CategoriaAdapter;
import com.labawsrh.aws.trashtec.Adapters.ConocimientoAdapter;
import com.labawsrh.aws.trashtec.Adapters.RazonesAdapter;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.Models.Beneficios;
import com.labawsrh.aws.trashtec.Models.Categorias;
import com.labawsrh.aws.trashtec.Models.Conocimiento;
import com.labawsrh.aws.trashtec.Models.Razones;
import com.labawsrh.aws.trashtec.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    private int MY_PERMISSIONS_READ_CONTACTS;
    private Main_User_Activity activity;
    private TabLayout tab_indicator;
    private List<Conocimiento> conocimientos;
    private ConocimientoAdapter conocimientoAdapter;
    private ViewPager conocimientos_pager;
    private ListView lista_beneficios;
    private BeneficioAdapter beneficioAdapter;
    private List<Beneficios> beneficios;
    private CategoriaAdapter categoriasadapter;
    private List<Categorias> categorias;
    private RecyclerView list_categorias;
    private DatabaseReference reference = Firebase_Variables.database_reference;
    private SwipeRefreshLayout refreshLayout;
    private RazonesAdapter razonesAdapter;
    private List<Razones> razones;
    private BottomNavigationView navigationView;
    private RecyclerView lista_razones;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity=(Main_User_Activity) getActivity();
        return inflater.inflate(R.layout.home_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InsanciarObjects(view);
        PedirPermisos();
        tab_indicator.setupWithViewPager(conocimientos_pager);
        navigationView = activity.findViewById(R.id.bottomNavigationView);
        enableBottomBar(false);
        Timer timer = new Timer();
        timer.schedule(new MyTimerTask(), 5000, 4000);
        RefreshData();
    }
    public void PedirPermisos() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_READ_CONTACTS);
            return;
        }
    }

    private void RefreshData() {
        refreshLayout.setOnRefreshListener(() -> {
            CargarInformacionConocimientos(new ArrayList<>());
            CargarInformacionCategorias(new ArrayList<>());
            CargarInformacionBeneficios(new ArrayList<>());
            CargarInformacionRazones(new ArrayList<>());
            new Handler().postDelayed(() -> refreshLayout.setRefreshing(false),3000);
        });
    }
    private void enableBottomBar(boolean enable){
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setEnabled(enable);
        }
    }
    private void InsanciarObjects(View view) {
        refreshLayout = view.findViewById(R.id.refresh_home);
        conocimientos_pager = view.findViewById(R.id.lista_conocimientos);
        lista_razones = view.findViewById(R.id.lista_razones);
        conocimientos = new ArrayList<>();
        categorias = new ArrayList<>();
        beneficios = new ArrayList<>();
        razones = new ArrayList<>();
        list_categorias = view.findViewById(R.id.lista_categorias);
        lista_beneficios = view.findViewById(R.id.lista_beneficios);
        tab_indicator = view.findViewById(R.id.tab_indicator_conocimientos);
        CargarInformacionConocimientos(conocimientos);
        CargarInformacionCategorias(categorias);
        CargarInformacionBeneficios(beneficios);
        CargarInformacionRazones(razones);
    }

    private void CargarInformacionRazones(final List<Razones> razones) {
        LinearLayoutManager manager1 = new LinearLayoutManager(getContext());
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        lista_razones.setLayoutManager(manager1);
        reference.child("Razones").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()||dataSnapshot.getChildrenCount()!=0){
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        razones.add(data.getValue(Razones.class));
                    }
                    razonesAdapter = new RazonesAdapter(Objects.requireNonNull(getContext()),razones);
                    lista_razones.setAdapter(razonesAdapter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enableBottomBar(true);
                        }
                    },1000);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"No se pudo obtener la informacion",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CargarInformacionBeneficios(final List<Beneficios> beneficios) {
        reference.child("Beneficios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()||dataSnapshot.getChildrenCount()!=0){
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        beneficios.add(data.getValue(Beneficios.class));
                    }
                    beneficioAdapter = new BeneficioAdapter(Objects.requireNonNull(getContext()),R.layout.beneficion_item,beneficios);
                    lista_beneficios.setAdapter(beneficioAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"No se pudo obtener la informacion",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CargarInformacionCategorias(final List<Categorias> categorias) {
        LinearLayoutManager manager1 = new LinearLayoutManager(getContext());
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        list_categorias.setLayoutManager(manager1);

        reference.child("Categorias").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()||dataSnapshot.getChildrenCount()!=0){
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        categorias.add(data.getValue(Categorias.class));
                    }
                    categoriasadapter = new CategoriaAdapter(getContext(),categorias);
                    list_categorias.setAdapter(categoriasadapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"No se pudo obtener la informacion",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CargarInformacionConocimientos(final List<Conocimiento> conocimientos) {
        reference.child("Conocimientos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()||dataSnapshot.getChildrenCount()!=0){
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        conocimientos.add(data.getValue(Conocimiento.class));
                    }
                    conocimientoAdapter = new ConocimientoAdapter(getContext(),conocimientos);
                    conocimientos_pager.setAdapter(conocimientoAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"No se pudo obtener la informacion",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(conocimientos_pager.getCurrentItem()==0){
                        conocimientos_pager.setCurrentItem(1,true);
                        //viewPager.setCurrentItem(1, true);
                    }else if(conocimientos_pager.getCurrentItem()==1){
                        conocimientos_pager.setCurrentItem(2,true);
                        //viewPager.setCurrentItem(2, true);
                    }else if(conocimientos_pager.getCurrentItem()==2){
                        conocimientos_pager.setCurrentItem(3,true);
                        //viewPager.setCurrentItem(0, true);
                    }else
                        conocimientos_pager.setCurrentItem(0,true);
                }
            });
        }
    }
}
