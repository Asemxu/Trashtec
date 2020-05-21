package com.labawsrh.aws.trashtec.Adapters;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import com.labawsrh.aws.trashtec.Activitys.VideoActivity;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.Models.Conocimiento;
import com.labawsrh.aws.trashtec.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConocimientoAdapter extends PagerAdapter {

    Context mContext ;
    List<Conocimiento> conocimientos ;
    Dialog dialog;
    ImagenesConocimientoAdapter imagenesadapter;
    List<String> imagenes = new ArrayList<>();
    public ConocimientoAdapter(Context mContext, List<Conocimiento>conocimientos) {
        this.mContext = mContext;
        this.conocimientos=conocimientos;
        dialog = new Dialog(mContext);
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layoutScreen = inflater.inflate(R.layout.conocimiento_item,null);
            ImageView imgSlide = layoutScreen.findViewById(R.id.image_conocimiento);
            TextView title = layoutScreen.findViewById(R.id.titulo_conocimiento);
            TextView description = layoutScreen.findViewById(R.id.contenido_conocimiento);
            TextView ver_detalle = layoutScreen.findViewById(R.id.ver_conocimiento);
            title.setText(conocimientos.get(position).Titulo);
            description.setText(conocimientos.get(position).Contenido);
            imagenes = GetImagenes(conocimientos.get(position));
            Picasso.get().load(conocimientos.get(position).Imagen).into(imgSlide);
            container.addView(layoutScreen);
           // ClickConocimiento(card_conocimiento,conocimientos.get(position));
            ClickDetalle(ver_detalle,conocimientos.get(position),imagenes);
            return layoutScreen;
    }

    private List<String> GetImagenes(Conocimiento conocimiento) {
        List<String> images = new ArrayList<>();
        images.add(conocimiento.Imagen);
        images.add(conocimiento.Imagen2);
        images.add(conocimiento.Imagen3);
        images.add(conocimiento.Imagen4);
        images.add(conocimiento.Imagen5);
        return images;
    }

    private void ClickDetalle(TextView ver_detalle, final Conocimiento conocimiento, final List<String> imagenes) {

        ver_detalle.setOnClickListener(v -> {
            dialog.setContentView(R.layout.conocimiento_detalle);
            LinearLayoutManager manager1 = new LinearLayoutManager(mContext);
            manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
            RecyclerView lista = dialog.findViewById(R.id.list_imagenes_detalle);
            lista.setLayoutManager(manager1);
            imagenesadapter = new ImagenesConocimientoAdapter(mContext,imagenes);
            lista.setAdapter(imagenesadapter);
            TextView titulo_detalle = dialog.findViewById(R.id.titulo_detalle_conocimiento);
            TextView descripciion_detalle = dialog.findViewById(R.id.contenido_detalle_conocimiento);
            TextView link_video = dialog.findViewById(R.id.ver_video_conocimiento);
            ImageView btn_close = dialog.findViewById(R.id.btn_close_detalle);
            titulo_detalle.setText(conocimiento.Titulo);
            descripciion_detalle.setText(conocimiento.Contenido);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Constantes.CloseVentana(dialog,btn_close);
            ClickVideo(link_video,conocimiento.codigo_url_video);
            dialog.show();

        });
    }

    private void ClickVideo(TextView link_video, final String codigo_video) {
        link_video.setOnClickListener(v -> {
            Intent video_intent = new Intent(mContext, VideoActivity.class);
            video_intent.putExtra("Data",codigo_video);
            mContext.startActivity(video_intent);
        });
    }


    @Override
    public int getCount() {
        return conocimientos.size();
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

