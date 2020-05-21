package com.labawsrh.aws.trashtec.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.R;

import java.util.List;

public class SettigsAdapter extends ArrayAdapter {
    private List<String> opciones;
    private int resource;
    private Main_User_Activity activity;
    private Context context;
    public SettigsAdapter(@NonNull Context context, int resource, @NonNull List<String>  opciones) {
        super(context, resource, opciones);
        this.context = context;
        this.resource = resource;
        this.opciones = opciones;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent,false);
        ImageView imagen = convertView.findViewById(R.id.imagen_settings);
        TextView opcion = convertView.findViewById(R.id.opcion_settings);
        opcion.setText(opciones.get(position));
        SetImagen(imagen,position);
        return  convertView;
    }

    private void SetImagen(ImageView imagen, int position) {
        switch (position){
            case Constantes.Settings:
                imagen.setImageResource(R.drawable.settings);
                break;
            case Constantes.Help:
                imagen.setImageResource(R.drawable.help);
                break;
            case Constantes.About:
                imagen.setImageResource(R.drawable.recycle_icon);
                break;
            case Constantes.Descvincular:
                imagen.setImageResource(R.drawable.descvincular);
                break;
            case Constantes.Log_Out:
                imagen.setImageResource(R.drawable.log_out);
                break;
        }
    }

    @Override
    public int getCount() {
        return opciones.size();
    }
}

