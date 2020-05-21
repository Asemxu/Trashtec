package com.labawsrh.aws.trashtec.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.labawsrh.aws.trashtec.Activitys.InstruccionesActivity;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Adapters.ListaAyudaAdapter;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AyudaFragment extends Fragment {
    private Main_User_Activity activity;
    private List<String> ayudas;
    private ListaAyudaAdapter adapter;
    private ListView lista_ayuda;
    private  LinearLayout como_obtengo;
    private MaterialToolbar toolbar;
    private LinearLayout como_recoge;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (Main_User_Activity)getActivity();
        return inflater.inflate(R.layout.ayuda_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InstanciarViews(view);
        GetListaAyuda();
        GetSelectItemList();
        Regresar();
    }

    private void Regresar() {
        toolbar.setNavigationOnClickListener(v->{
            lista_ayuda.setVisibility(View.VISIBLE);
            como_recoge.setVisibility(View.GONE);
            toolbar.setNavigationIcon(R.drawable.list);
            como_obtengo.setVisibility(View.GONE);
        });
    }

    private void GetSelectItemList() {
        lista_ayuda.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent;
            switch (position){
                case Constantes.PrimerElementoLista:
                case Constantes.CuartoElementoLista:
                    intent = new Intent(activity, InstruccionesActivity.class);
                    intent.putExtra("Tipo",position);
                    activity.startActivity(intent);
                    break;
                case Constantes.SegundoElementoLista:
                    toolbar.setNavigationIcon(R.drawable.back_arrow);
                    como_obtengo.setVisibility(View.VISIBLE);
                    lista_ayuda.setVisibility(View.GONE);
                    como_recoge.setVisibility(View.GONE);
                    break;
                case Constantes.TerceroElementoLista:
                    toolbar.setNavigationIcon(R.drawable.back_arrow);
                    como_obtengo.setVisibility(View.GONE);
                    lista_ayuda.setVisibility(View.GONE);
                    como_recoge.setVisibility(View.VISIBLE);
                    break;

            }
        });
    }

    private void GetListaAyuda() {
        ayudas.addAll(Arrays.asList(Constantes.ListaAyuda));
        adapter = new ListaAyudaAdapter(getContext(),R.layout.item_ayuda,ayudas);
        lista_ayuda.setAdapter(adapter);
    }

    private void InstanciarViews(View view) {
        lista_ayuda = view.findViewById(R.id.lista_ayuda);
        ayudas = new ArrayList<>();
        toolbar = view.findViewById(R.id.toolbar_ayuda);
        como_recoge = view.findViewById(R.id.como_se_recoge);
        como_obtengo = view.findViewById(R.id.como_se_consigue_puntos);
    }

}
