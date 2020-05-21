package com.labawsrh.aws.trashtec.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.Firebase.Database.Firebase;
import com.labawsrh.aws.trashtec.Models.Descuentos;
import com.labawsrh.aws.trashtec.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DescuentoAdapter extends ArrayAdapter {
    private int resource;
    private int cantidad_puntos_actuales;
    private Main_User_Activity activity;
    private Context context;
    private Firebase firebase;
    private DatabaseReference databaseReference = Firebase_Variables.database_reference;
    private List<Descuentos> descuentos;
    private int cantidad_puntos_restantes ;
    private String vista_descuento;
    public DescuentoAdapter(String vista_descuento,@NonNull Context context, int resource, @NonNull List<Descuentos> descuentos,Main_User_Activity activity) {
        super(context, resource, descuentos);
        this.context = context;
        this.vista_descuento = vista_descuento;
        this.activity = activity;
        this.resource = resource;
        this.descuentos =descuentos;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
        convertView=inflater.inflate(resource,parent,false);
        ImageView imgdescuento = convertView.findViewById(R.id.imagen_descuento);
        TextView title = convertView.findViewById(R.id.titulo_descuento);
        TextView contenido = convertView.findViewById(R.id.contenido_descuento);
        TextView  puntos = convertView.findViewById(R.id.points_descuento);
        TextView cantidad_descuento = convertView.findViewById(R.id.cantidad_descuento);
        RelativeLayout btn_add_descuento =convertView.findViewById(R.id.btn_add_descuento);
        TextView cantidad_puntos = activity.findViewById(R.id.cantidad_puntos);
        cantidad_puntos_actuales = Integer.parseInt(cantidad_puntos.getText().toString());
        Descuentos descuento = descuentos.get(position);
        title.setText(descuento.Titulo);
        contenido.setText(descuento.Contenido);
        Picasso.get().load(descuento.Imagen).into(imgdescuento);
        puntos.setText(""+descuento.puntos);
        cantidad_descuento.setText(""+descuento.descuento+"%");
        CLickAddDescuento(btn_add_descuento,descuento.puntos,descuento);
        if(vista_descuento.equals("Mis_Descuentos")){
            btn_add_descuento.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    private void CLickAddDescuento(RelativeLayout btn_add_descuento,int puntos,Descuentos descuento) {
        btn_add_descuento.setOnClickListener(v->{
            MaterialAlertDialogBuilder dialog_material;
            Context contexto = new ContextThemeWrapper(context,R.style.Theme_MaterialComponents);
            dialog_material = new MaterialAlertDialogBuilder(contexto);
            if(cantidad_puntos_actuales<puntos){
                dialog_material.setTitle("Aviso");
                dialog_material.setIcon(R.drawable.error);
                dialog_material.setMessage("No tiene suficientes puntos");
                dialog_material.setNeutralButton("Aceptar", (dialog, which) -> dialog.dismiss());
                dialog_material.show();
            }else {
                dialog_material.setTitle("Aviso");
                dialog_material.setIcon(R.drawable.warning);
                dialog_material.setMessage("Desea Gastar sus puntos en este descuento ?");
                dialog_material.setNegativeButton("Canncelar", (dialog, which) -> dialog.dismiss());
                dialog_material.setPositiveButton("Aceptar", (dialog, which) -> {
                    ProgressDialog dialog1 = ProgressDialog.show(context, "Descuentos", "Guardando Descuento.....", false, false);
                    cantidad_puntos_restantes = cantidad_puntos_actuales - puntos;
                    firebase = new Firebase(descuento, databaseReference, context, activity);
                    firebase.GuardarDescuento(cantidad_puntos_restantes, dialog1);
                });
                dialog_material.show();
            }
        });
    }

    @Override
    public int getCount() {
        return descuentos.size();
    }
}
