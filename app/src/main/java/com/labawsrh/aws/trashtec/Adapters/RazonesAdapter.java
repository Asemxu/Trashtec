package com.labawsrh.aws.trashtec.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.labawsrh.aws.trashtec.Models.Razones;
import com.labawsrh.aws.trashtec.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RazonesAdapter extends RecyclerView.Adapter<RazonesAdapter.MyViewHolder> {
    Context mContext ;
    List<Razones> razones  ;
    Dialog dialog;
    public RazonesAdapter(Context mContext, List<Razones> razones) {
        this.mContext = mContext;
        this.razones=razones;
    }
    @NonNull
    @Override
    public RazonesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.razon_item,parent,false);
        return new RazonesAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.razon.setText(razones.get(position).Titulo);
        String contador_ = ""+(position+1);
        holder.contador.setText(contador_);
        Picasso.get().load(razones.get(position).Imagen).into(holder.imagen);
        ClickCardRazon(holder.card_razon,razones.get(position));
    }

    private void ClickCardRazon(CardView card_razon,final Razones razon) {
        card_razon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,razon.Titulo,Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return razones.size();
    }

    public class MyViewHolder  extends  RecyclerView.ViewHolder{

        TextView contador;
        TextView razon;
        ImageView imagen;
        CardView card_razon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            contador = itemView.findViewById(R.id.numero);
            razon = itemView.findViewById(R.id.razon_text);
            imagen = itemView.findViewById(R.id.image_razon);
            card_razon = itemView.findViewById(R.id.card_razon);
        }
    }
}

