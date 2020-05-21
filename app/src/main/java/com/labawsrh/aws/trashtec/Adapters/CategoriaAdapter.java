package com.labawsrh.aws.trashtec.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.Models.Categorias;
import com.labawsrh.aws.trashtec.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.MyViewHolder>  implements YouTubePlayer.OnInitializedListener {
    Context mContext ;
    List<Categorias> categorias ;
    Dialog dialog;
    public CategoriaAdapter(Context mContext, List<Categorias> categorias) {
        this.mContext = mContext;
        this.categorias=categorias;
    }
    @NonNull
    @Override
    public CategoriaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.categoria_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaAdapter.MyViewHolder holder, int position) {
        holder.categoria.setText(categorias.get(position).Nombre);
        holder.imagen.setContentDescription(Constantes.objects[position]);
        SelectImage(holder,position);
        ClickCardCategoria(holder.categoria_card,categorias.get(position));
    }

    private void ClickCardCategoria(CardView categoria_card, final Categorias categoria) {
        categoria_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.categoria_detalle);
                TextView titulo_categoria = dialog.findViewById(R.id.titulo_detalle_categoria);
                TextView contenido_categoria = dialog.findViewById(R.id.contenido_detalle_categoria);
                ImageView btn_close = dialog.findViewById(R.id.btn_close_detalle_categoria);
                TextView ejemplos_categoria = dialog.findViewById(R.id.ejemplos_detalle_categoria);
                ImageView imagen1 = dialog.findViewById(R.id.imagen_1_categoria);
                ImageView imagen2 = dialog.findViewById(R.id.imagen_2_categoria);
                ImageView imagen3 = dialog.findViewById(R.id.imagen_3_categoria);

                titulo_categoria.setText(categoria.Nombre);
                contenido_categoria.setText(categoria.Contenido);
                ejemplos_categoria.setText(categoria.Ejemplos);
                Picasso.get().load(categoria.Imagen1).into(imagen1);
                Picasso.get().load(categoria.Imagen2).into(imagen2);
                Picasso.get().load(categoria.Imagen3).into(imagen3);
                Constantes.CloseVentana(dialog, btn_close);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
    }

    private void SelectImage(MyViewHolder holder,int position) {
        switch (position){
            case Constantes.Grandes:
                holder.imagen.setImageResource(R.drawable.fridge);
                break;
            case Constantes.Medianos:
                holder.imagen.setImageResource(R.drawable.microwave);
                break;
            case Constantes.Iluminacion:
                holder.imagen.setImageResource(R.drawable.lamp);
                break;
            case Constantes.Pantallas:
                holder.imagen.setImageResource(R.drawable.computer);
                break;
            case Constantes.Otros:
                holder.imagen.setImageResource(R.drawable.desktop);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    public class MyViewHolder  extends  RecyclerView.ViewHolder{

        TextView categoria;
        ImageView imagen;
        CardView categoria_card;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoria = itemView.findViewById(R.id.categoria_text);
            imagen = itemView.findViewById(R.id.image_conocimiento);
            categoria_card = itemView.findViewById(R.id.card_categoria);
        }
    }
}
