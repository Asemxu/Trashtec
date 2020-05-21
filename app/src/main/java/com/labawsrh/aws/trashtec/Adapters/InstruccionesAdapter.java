package com.labawsrh.aws.trashtec.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.labawsrh.aws.trashtec.R;
public class InstruccionesAdapter  extends PagerAdapter {
    private Context context;
    private int [] lista;
    private String [] instrucciones;
    public InstruccionesAdapter(Context context,int [] lista, String [] instrucciones) {
        this.lista = lista;
        this.context= context;
        this.instrucciones = instrucciones;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.item_instruccion,null);
        TextView instruccion = layoutScreen.findViewById(R.id.instruccion);
        ImageView imagen = layoutScreen.findViewById(R.id.imagen_instruccion);
        instruccion.setText(instrucciones[position]);
        imagen.setImageResource(lista[position]);
        container.addView(layoutScreen);
        return layoutScreen;
    }




    @Override
    public int getCount() {
        return lista.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
