package com.labawsrh.aws.trashtec.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Fragments.DescuentosFragment;
import com.labawsrh.aws.trashtec.Models.EmpresaAsociada;
import com.labawsrh.aws.trashtec.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BodegaDescuentoAdapter extends PagerAdapter {
    Context mContext ;
    List<EmpresaAsociada> empresaAsociadas ;
    private Main_User_Activity activity;
    private Fragment fragment;
    public BodegaDescuentoAdapter(Context mContext, List<EmpresaAsociada> empresaAsociadas, Main_User_Activity activity) {
        this.mContext = mContext;
        this.activity = activity;
        this.empresaAsociadas = empresaAsociadas;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.empresas_descuentos_items,null);
        EmpresaAsociada empresa = empresaAsociadas.get(position);
        ImageView imagen_empresa = layoutScreen.findViewById(R.id.image_empresa_descuento);
        TextView empresa_titulo = layoutScreen.findViewById(R.id.titulo_empresa_descuento);
        TextView direccion = layoutScreen.findViewById(R.id.direccion_empresa_descuento);
        TextView ver_descuentos = layoutScreen.findViewById(R.id.ver_descuentos);
        Picasso.get().load(empresa.Imagen).into(imagen_empresa);
        empresa_titulo.setText(empresa.Nombre);
        direccion.setText(empresa.direccion);
        container.addView(layoutScreen);
        ClickVerDescuentos(empresa.Id,ver_descuentos,empresa.Nombre);
        return layoutScreen;
    }
    private void ClickVerDescuentos(String id,TextView ver_descuentos,String nombre) {
        ver_descuentos.setOnClickListener(v->{
            fragment = new DescuentosFragment(id,nombre);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
        });
    }
    @Override
    public int getCount() {
        return empresaAsociadas.size();
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
