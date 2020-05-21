package com.labawsrh.aws.trashtec.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.labawsrh.aws.trashtec.Models.Horario;
import com.labawsrh.aws.trashtec.R;
import java.util.List;

public class HorarioAdapter extends ArrayAdapter {
    private int resource;
    private Context context;
    private List<Horario> horarios;
    private TextView hora;
    private TextView dia;
    public HorarioAdapter(@NonNull Context context, int resource, @NonNull List<Horario> horarios) {
        super(context, resource, horarios);
        this.resource = resource;
        this.context = context;
        this.horarios = horarios;
    }

    @Override
    public int getCount() {
        return horarios.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
        convertView=inflater.inflate(resource,parent,false);
        InstanciarViews(convertView);
        Horario  horario = horarios.get(position);
        hora.setText(horario.Inicio +" am - "+horario.Final+" pm");
        dia.setText(horario.dia);
        return convertView;
    }

    private void InstanciarViews(View convertView) {
        hora = convertView.findViewById(R.id.hora_centro);
        dia = convertView.findViewById(R.id.dia);
    }
}
