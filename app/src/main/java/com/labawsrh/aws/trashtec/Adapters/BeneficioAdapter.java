package com.labawsrh.aws.trashtec.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Models.Beneficios;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class BeneficioAdapter extends ArrayAdapter {

    private List<Beneficios> beneficios;
    private int resource;
    private Main_User_Activity activity;
    private  Context context;
    private CardView cardView;
    public BeneficioAdapter(@NonNull Context context, int resource, @NonNull List<Beneficios> beneficios) {
        super(context, resource, beneficios);
        this.beneficios=beneficios;
        this.resource=resource;
        this.context=context;
    }

    @Override
    public int getCount() {
        return beneficios.size();
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
        convertView=inflater.inflate(resource,parent,false);
        Beneficios beneficio = beneficios.get(position);
        TextView titulo =convertView.findViewById(R.id.titulo_beneficio);
        TextView contenido = convertView.findViewById(R.id.contenido_beneficio);
        ImageView foto_publicacion = convertView.findViewById(R.id.image_beneficio);
        cardView = convertView.findViewById(R.id.card_beneficio);
        titulo.setText(beneficio.Titulo);
        contenido.setText(beneficio.Contenido);
        Picasso.get().load(beneficio.Imagen).into(foto_publicacion);
        ClickCard(beneficio);
        return convertView;
    }

    private void ClickCard(final Beneficios beneficio) {
        cardView.setOnClickListener(v -> {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.beneficio_detalle);
            ImageView imagen_beneficio = dialog.findViewById(R.id.imagen_beneficio_detalle);
            TextView titulo = dialog.findViewById(R.id.titulo_detalle_beneficio);
            TextView contenido = dialog.findViewById(R.id.contenido_detalle_beneficio);
            ImageView close = dialog.findViewById(R.id.btn_close_detalle_beneficio);
            titulo.setText(beneficio.Titulo);
            contenido.setText(beneficio.Contenido);
            Picasso.get().load(beneficio.Imagen).into(imagen_beneficio);
            Constantes.CloseVentana(dialog,close);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
    }


}
