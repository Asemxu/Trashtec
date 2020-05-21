package com.labawsrh.aws.trashtec.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.R;

public class OptionsFragment extends Fragment {
    private Main_User_Activity activity;
    private CardView btn_reciclaje_fragment;
    private CardView btn_publicaciones_fragment;
    private CardView btn_tienda_fragment;
    private Fragment fragment;
    private MaterialToolbar toolbar;
    ProgressDialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (Main_User_Activity) getActivity();
        return inflater.inflate(R.layout.options_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InicializarViews(view);
        ClickPublicaciones();
        ClickGuia();
        ClickTienda();
    }

    private void ClickTienda() {
            btn_tienda_fragment.setOnClickListener(v->{
                btn_tienda_fragment.setEnabled(false);
                dialog = ProgressDialog.show(getContext(),"Tienda","Cargando...",false,false);
                fragment = new TiendaFragment(dialog);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
            });
    }

    private void ClickGuia() {
        btn_reciclaje_fragment.setOnClickListener(v->{
            btn_reciclaje_fragment.setEnabled(false);
            dialog = ProgressDialog.show(getContext(),"Guia","Cargando...",false,false);
            fragment = new GuiaFragment(dialog);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
        });
    }

    private void ClickPublicaciones() {
        btn_publicaciones_fragment.setOnClickListener(v -> {
            dialog = ProgressDialog.show(getContext(),"Mis Publicaciones","Cargando...",false,false);
            fragment = new PublicacionesFragment(dialog);
            btn_publicaciones_fragment.setEnabled(false);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
        });
    }

    private void InicializarViews(View view) {
        btn_publicaciones_fragment = view.findViewById(R.id.btn_publicaciones_fragment);
        btn_reciclaje_fragment = view.findViewById(R.id.btn_guia_fragment);
        btn_tienda_fragment = view.findViewById(R.id.btn_tienda_fragment);
        toolbar = activity.findViewById(R.id.topAppBar_reciclaje);
        toolbar.setTitle("Reciclaje");
        toolbar.setNavigationIcon(R.drawable.list);
    }

}
