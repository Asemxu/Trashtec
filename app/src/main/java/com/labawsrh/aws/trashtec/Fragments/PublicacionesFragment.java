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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Adapters.PublicacionesAdapter;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.Models.Publicacion;
import com.labawsrh.aws.trashtec.R;

import java.util.ArrayList;
import java.util.List;

public class PublicacionesFragment extends Fragment {
    private Main_User_Activity activity;
    private SwipeRefreshLayout refreshLayout;
    private MaterialToolbar bar;
    private Fragment fragment;
    private List<Publicacion> publicaciones_aprobadas;
    private List<Publicacion> publicacions_por_aprobar;
    private ListView litsa_publicaciones;
    private FloatingActionButton add_post;
    private List<Publicacion> publicaciones;
    private MaterialButton btn_ver_por_aprobar;
    private MaterialButton btn_ver_aprobados;
    private boolean is_por_aprobar = true;
    private boolean isIs_aprobado = false;
    private PublicacionesAdapter adapter;
    private DatabaseReference databaseReference = Firebase_Variables.database_reference;
    private FirebaseAuth auth = Firebase_Variables.firebaseauth;
    private ProgressDialog dialog;
    public PublicacionesFragment(ProgressDialog dialog) {
        this.dialog = dialog;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (Main_User_Activity) getActivity();
        return inflater.inflate(R.layout.publicaciones_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InstancarViews(view);
        Regresar();
        ClickAddPost();
        GetDataPublicaciones();
        ClickVerPublicaciones();
        Refresh();
    }

    private void Refresh() {
        refreshLayout.setOnRefreshListener(() -> {
            publicaciones.clear();
            publicacions_por_aprobar.clear();
            publicaciones_aprobadas.clear();
           GetDataPublicaciones();
            Handler handler = new Handler();
            handler.postDelayed(() -> refreshLayout.setRefreshing(false),3000);
        });

    }

    private void ClickVerPublicaciones() {
        btn_ver_por_aprobar.setOnClickListener(v -> {
            if (!is_por_aprobar) {
                publicaciones.clear();
                publicaciones.addAll(publicacions_por_aprobar);
                adapter.notifyDataSetChanged();
                is_por_aprobar = true;
                isIs_aprobado = false;
            }
        });
        btn_ver_aprobados.setOnClickListener(v->{
            if(!isIs_aprobado){
                publicaciones.clear();
                publicaciones.addAll(publicaciones_aprobadas);
                adapter.notifyDataSetChanged();
                isIs_aprobado = true;
                is_por_aprobar = false;
            }
        });
    }

    private void GetDataPublicaciones() {
        databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Publicaciones").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Publicacion publicacion = data.getValue(Publicacion.class);
                    if(publicacion.Estado) {
                        if(publicacion.Estado_Vista)
                            publicaciones_aprobadas.add(publicacion);
                    }
                    else
                        publicacions_por_aprobar.add(publicacion);
                }
                btn_ver_por_aprobar.setSelected(true);
                publicaciones.addAll(publicacions_por_aprobar);
                adapter = new PublicacionesAdapter(getContext(),R.layout.publicacion_item,publicaciones,activity);
                litsa_publicaciones.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"No se pudo Obtener la informacióm",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ClickAddPost() {
        add_post.setOnClickListener(v->{
            fragment = new AgregarPostFragment();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
        });
    }

    private void Regresar() {
        bar.setNavigationOnClickListener(v -> {
            fragment = new ReciclajeFragment();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
        });
    }
    private void InstancarViews(View view) {
        bar = activity.findViewById(R.id.topAppBar_reciclaje);
        bar.setTitle("Mis Publicaciones");
        bar.setNavigationIcon(R.drawable.back_arrow);
        publicaciones_aprobadas = new ArrayList<>();
        publicacions_por_aprobar = new ArrayList<>();
        litsa_publicaciones = view.findViewById(R.id.lista_publicaciones);
        add_post  = view.findViewById(R.id.btn_agregar_post);
        btn_ver_aprobados = view.findViewById(R.id.btn_ver_aprobadas);
        btn_ver_por_aprobar = view.findViewById(R.id.btn_ver_por_aprobar);
        publicaciones = new ArrayList<>();
        refreshLayout = view.findViewById(R.id.refresh_publicaciones);
    }
}
