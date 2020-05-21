package com.labawsrh.aws.trashtec.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.labawsrh.aws.trashtec.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagenesConocimientoAdapter extends RecyclerView.Adapter<ImagenesConocimientoAdapter.MyViewHolder> {

    private Context context;
    private List<String> imagenes;
    public ImagenesConocimientoAdapter(@NonNull Context context, @NonNull List<String> imagenes) {
        this.context=context;
        this.imagenes=imagenes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.imagenes_lista_conocimiento,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(imagenes.get(position)).into(holder.imagen_);
    }

    @Override
    public int getItemCount() {
        return imagenes.size();

    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        ImageView imagen_;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen_ = itemView.findViewById(R.id.imagen_conocimiento_detalles);
        }
    }
}
