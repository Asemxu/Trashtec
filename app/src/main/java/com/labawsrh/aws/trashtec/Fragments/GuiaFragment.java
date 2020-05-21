package com.labawsrh.aws.trashtec.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Adapters.ItemCategoriaMapsAdapter;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiaFragment extends Fragment {
    private MaterialToolbar toolbar;
    private List<String> categorias;
    private ItemCategoriaMapsAdapter adapter;
    private ListView lista_categorias;
    private Fragment fragment;
    Main_User_Activity activity;
    private ProgressDialog dialog;
    public GuiaFragment(ProgressDialog dialog) {
        this.dialog = dialog;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (Main_User_Activity) getActivity();
        return inflater.inflate(R.layout.guia_fragment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InstanciaViews(view);
        ClickRegresar();
        GetDataCategorias();
        ClickCategorias();
    }

    private void ClickCategorias() {
        lista_categorias.setOnItemClickListener((parent, view, position, id) -> {
            fragment = new DetalleGuiaFragment(position);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
        });
    }

    private void GetDataCategorias() {
        categorias.addAll(Arrays.asList(Constantes.categorias));
        adapter = new ItemCategoriaMapsAdapter(getContext(),R.layout.item_lista_categorias_maps,categorias);
        lista_categorias.setAdapter(adapter);
        dialog.dismiss();
    }

    private void ClickRegresar() {
        toolbar.setNavigationOnClickListener(v->{
            fragment = new OptionsFragment();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
        });
    }

    private void InstanciaViews(View view) {
        toolbar = activity.findViewById(R.id.topAppBar_reciclaje);
        toolbar.setTitle("Pequeña Guía");
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        categorias = new ArrayList<>();
        lista_categorias = view.findViewById(R.id.lista_categorias_guia);
    }
}
