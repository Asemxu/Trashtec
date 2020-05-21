package com.labawsrh.aws.trashtec.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.R;

public class DetalleGuiaFragment extends Fragment {
    private int tipo_categoria;
    private MaterialToolbar toolbar;
    private Main_User_Activity activity;
    private Fragment fragment;
    private TextView contenido_permitido;
    private TextView contenido_no_permitido;
    private TextView reglas_contenido;
    public DetalleGuiaFragment(int tipo_categoria){
        this.tipo_categoria = tipo_categoria;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (Main_User_Activity) getActivity();
        return inflater.inflate(R.layout.detalle_guia_categoria_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InstanciarViews(view);
        ClickRegresar();
        SetDataDetalleView(tipo_categoria);
    }

    private void SetDataDetalleView(int tipo_categoria) {
        contenido_permitido.setText(Constantes.Permitidos[tipo_categoria]);
        contenido_no_permitido.setText(Constantes.NoPermitidos[tipo_categoria]);
        reglas_contenido.setText(Constantes.Reglas);
    }

    private void ClickRegresar() {
        toolbar.setNavigationOnClickListener(v->{
            ProgressDialog dialog = ProgressDialog.show(getContext(),"Guia","Cargando...",false,false);
            fragment = new GuiaFragment(dialog);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
        });
    }

    private void InstanciarViews(View view) {
        toolbar = activity.findViewById(R.id.topAppBar_reciclaje);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setTitle(Constantes.categorias[tipo_categoria]);
        contenido_permitido = view.findViewById(R.id.contenido_permitido);
        contenido_no_permitido = view.findViewById(R.id.contenido_no_permitido);
        reglas_contenido = view.findViewById(R.id.contenido_reglas);
    }


}
