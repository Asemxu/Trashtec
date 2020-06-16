package com.labawsrh.aws.trashtec.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.Models.Centro_Acopio;
import com.labawsrh.aws.trashtec.R;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Centros_Acopio_Adapter extends ArrayAdapter implements GoogleMap.InfoWindowAdapter {
    List<Centro_Acopio> centro_acopios;
    int resource;
    Context context;
    String dia_actual;
    String tipo;
    LinearLayout acopio;
    public Centros_Acopio_Adapter(@NonNull Context context,String dia_actual, int resource, @NonNull List<Centro_Acopio> centro_acopios,String tipo) {
        super(context, resource, centro_acopios);
        this.centro_acopios=centro_acopios;
        this.resource=resource;
        this.context=context;
        this.tipo=tipo;
        this.dia_actual=dia_actual;
    }

    @Override
    public int getCount() {
        return centro_acopios.size();
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
        convertView=inflater.inflate(resource,parent,false);
        InstanciarViews(convertView);
        Centro_Acopio centro_acopio = centro_acopios.get(position);
        if(tipo.equals(Constantes.Calendar)) {
            ViewAcopiosCalendar(convertView, centro_acopio);
        }
        return convertView;
    }

    private void InstanciarViews(View convertView) {
        acopio = convertView.findViewById(R.id.acopio);
    }

    @SuppressLint("SetTextI18n")
    private void ViewAcopiosCalendar(View convertView, Centro_Acopio centro_acopio) {
        ImageView imagen = convertView.findViewById(R.id.imagen_empresa_calendar);
        TextView nombre_empresa = convertView.findViewById(R.id.empresa_calendar);
        TextView hora_apertua = convertView.findViewById(R.id.hora_apertura_calendar);
        TextView estado_acopio = convertView.findViewById(R.id.estado_centro_calendar);
        Picasso.get().load(centro_acopio.Imagen).into(imagen);
        nombre_empresa.setText(centro_acopio.Empresa);
        hora_apertua.setText("Disponible a partir de las   "+ Objects.requireNonNull(centro_acopio.Horario.get(dia_actual)).Inicio + " am");
        ValidarOpenorClosed(estado_acopio, Objects.requireNonNull(centro_acopio.Horario.get(dia_actual)).Inicio,
                            Objects.requireNonNull(centro_acopio.Horario.get(dia_actual)).Final);
    }

    @SuppressLint("SetTextI18n")
    private void ValidarOpenorClosed(TextView estado, int inicio, int aFinal) {
        Date fecha_actual = new Date();
        int hora_actual = fecha_actual.getHours();
        if(hora_actual<inicio||hora_actual>=aFinal){
            estado.setText("Cerrado");
            estado.setTextColor(context.getResources().getColor(R.color.red));
        }else {
            estado.setTextColor(context.getResources().getColor(R.color.green));
            estado.setText("Abierto");
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
