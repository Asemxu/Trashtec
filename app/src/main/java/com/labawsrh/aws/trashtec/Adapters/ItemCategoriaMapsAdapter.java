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

import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.R;

import java.util.List;

public class ItemCategoriaMapsAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private List<String> categorias;
    public int Tipo_Menu;
    public boolean menu_guia;
    public int Tipo_Categoria;
    public int SubItem = 0;
    public ItemCategoriaMapsAdapter(@NonNull Context context, int resource, @NonNull List<String>categorias,int Tipo_Menu,int tipo_Categoria) {
        super(context, resource, categorias);
        this.context = context;
        this.resource = resource;
        this.categorias = categorias;
        this.Tipo_Menu = Tipo_Menu;
        this.Tipo_Categoria = tipo_Categoria;
    }
    public ItemCategoriaMapsAdapter(@NonNull Context context, int resource, @NonNull List<String>categorias) {
        super(context, resource, categorias);
        this.context = context;
        this.resource = resource;
        this.menu_guia = true;
        this.categorias = categorias;
    }
    @Override
    public int getCount() {
        return categorias.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);
        String categoria = categorias.get(position);
        ImageView imagen = convertView.findViewById(R.id.imagen_categoria_maps);
        TextView texto_categoria_maps = convertView.findViewById(R.id.texto_categoria_maps);
        texto_categoria_maps.setText(categoria);
        if(this.Tipo_Menu<2)
            SetImagen(imagen,position,texto_categoria_maps);
        else{
            imagen.setImageResource(R.drawable.icon_basura);
        }
        return convertView;
    }

    private void SetImagen(ImageView imagen, int position,TextView textView) {
        if(this.menu_guia){
            SetImagenInicial(imagen,position);
        }else {
            switch (this.Tipo_Categoria) {
                case Constantes.Inicial:
                    SetImagenInicial(imagen, position);
                    break;
                case Constantes.Grandes:
                    SetImagenGrandes(imagen, position);
                    break;
                case Constantes.Medianos:
                    SetImagenMedianos(imagen, position);
                    break;
                case Constantes.Iluminacion:
                    SetImagenIluminacion(imagen, position);
                    break;
                case Constantes.Pantallas:
                    SetImagenPantallas(imagen, position);
                    break;
                case Constantes.Otros:
                    SetImagenOtros(imagen, position);
                    break;
            }
        }
    }

    private void SetImagenInicial(ImageView imagen, int position) {
        switch (position){
            case 0:
                imagen.setImageResource(R.drawable.fridge);
                break;
            case 1:
                imagen.setImageResource(R.drawable.microwave);
                break;
            case 2:
                imagen.setImageResource(R.drawable.lamp);
                break;
            case 3:
                imagen.setImageResource(R.drawable.computer);
                break;
            case 4:
                imagen.setImageResource(R.drawable.desktop);
                break;
        }
    }

    private void SetImagenIluminacion(ImageView imagen, int position) {
        switch (position){
            case 0:
            case 1:
                imagen.setImageResource(R.drawable.lamp);
                break;
        }

    }

    private void SetImagenPantallas(ImageView imagen, int position) {
        switch (position){
            case 0:
            case 1:
            case 2:
                imagen.setImageResource(R.drawable.computer);
                break;
        }
    }

    private void SetImagenOtros(ImageView imagen, int position) {
        switch (position){
            case 0:
                imagen.setImageResource(R.drawable.impresora);
                break;
            case 1:
                imagen.setImageResource(R.drawable.telefono);
                break;
            case 2:
                imagen.setImageResource(R.drawable.celular);
                break;
            case 3:
                imagen.setImageResource(R.drawable.accesorios);
                break;
            case 4:
                imagen.setImageResource(R.drawable.router);
                break;
            case 5:
                imagen.setImageResource(R.drawable.bateria);
                break;
        }
    }

    private void SetImagenMedianos(ImageView imagen, int position) {
        switch (position) {
            case 0:
                imagen.setImageResource(R.drawable.microwave);
                break;
            case 1:
                imagen.setImageResource(R.drawable.lavadora);
                break;
            case 2:
                imagen.setImageResource(R.drawable.coffee);
                break;
        }
    }

    private void SetImagenGrandes(ImageView imagen, int position) {
        switch (position){
            case Constantes.Neveras:
            case Constantes.Congeladores:
            case Constantes.Refrigeradoras:
                imagen.setImageResource(R.drawable.fridge);
                break;
        }

    }
}
