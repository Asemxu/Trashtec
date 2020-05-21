package com.labawsrh.aws.trashtec.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.R;

public class SettingsFragment extends Fragment {
    private Main_User_Activity activity;
    private TextView terminos;
    //private SwitchMaterial switch_notificaciones;
    private FirebaseAuth auth = Firebase_Variables.firebaseauth;
    private Fragment fragment;
    //private boolean acepta_notificaciones;
    private MaterialToolbar navigationView;
    public SettingsFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (Main_User_Activity) getActivity();
        return inflater.inflate(R.layout.configuracion_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InstanciarViews(view);
        verTerminos();
        //EstablecerSwitchNotificaciones();
    }

    private void verTerminos() {
        terminos.setOnClickListener(v->{
            Context context = new ContextThemeWrapper(getContext(),R.style.Theme_MaterialComponents);
            MaterialAlertDialogBuilder dialog_material = new MaterialAlertDialogBuilder(context);
            dialog_material.setTitle("Terminos y Condiciones");
            dialog_material.setMessage("Consentimiento Informado\n\n"+"Yo, certifico que he sido informado sobre el propósito, procedimientots" +
                    "beneficios y manejode la confiabilidad, de la Investigación titulada: "+ getResources().getString(R.string.titulo_tesis) + "\n\n" +
                    "He leído el documento Hoja de Información del Estudio(ubicado en la paginá de la aplicación) y entiendo claramente" +
                    "cada uno de los aspectos antes mencionados\n\n"+"Certifico a su vez que he entendido mis derechos como participante" +
                    "de este estudio y voluntariamente consiento a participar en el mismo.\n"+"Además, entiendo de qué se trata y las " +
                    "razones por las que se esta llevando a cabo.");
            dialog_material.setNegativeButton("Ok",(dialog, which) -> dialog.dismiss());
            dialog_material.show();
        });
    }

    /*private void EstablecerSwitchNotificaciones() {
        switch_notificaciones.setOnClickListener(v->{
            databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("acepta_notificaciones").setValue(switch_notificaciones.isChecked());
        });
    }*/

    private void InstanciarViews(View view) {
        navigationView = activity.findViewById(R.id.toolbar_configuracion);
        terminos = view.findViewById(R.id.terminos);
        //switch_notificaciones = view.findViewById(R.id.establecer_notificaciones);
        //switch_notificaciones.setChecked(acepta_notificaciones);
    }
}
