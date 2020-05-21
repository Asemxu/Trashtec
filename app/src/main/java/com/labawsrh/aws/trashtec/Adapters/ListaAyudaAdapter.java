package com.labawsrh.aws.trashtec.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.labawsrh.aws.trashtec.R;

import java.util.List;

public class ListaAyudaAdapter extends ArrayAdapter {
    private int resource;
    private List<String> ayudas;
    private Context context;
    public ListaAyudaAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.ayudas = objects;
        this.context = context;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
        convertView=inflater.inflate(resource,parent,false);
        TextView item_ayuda = convertView.findViewById(R.id.item_ayuda);
        item_ayuda.setText(ayudas.get(position));
        return convertView;
    }

    @Override
    public int getCount() {
        return ayudas.size();
    }
}
