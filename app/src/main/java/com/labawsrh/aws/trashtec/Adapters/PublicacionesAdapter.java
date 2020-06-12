package com.labawsrh.aws.trashtec.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.Clases_Helper.Fecha;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.Firebase.Database.Firebase;
import com.labawsrh.aws.trashtec.Fragments.EditarPostFragment;
import com.labawsrh.aws.trashtec.Fragments.PublicacionesFragment;
import com.labawsrh.aws.trashtec.Fragments.TiendaFragment;
import com.labawsrh.aws.trashtec.Models.Mes;
import com.labawsrh.aws.trashtec.Models.Publicacion;
import com.labawsrh.aws.trashtec.Models.User;
import com.labawsrh.aws.trashtec.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PublicacionesAdapter extends ArrayAdapter {
    private Context context;
    private FirebaseAuth auth = Firebase_Variables.firebaseauth;
    private Main_User_Activity activity;
    private DatabaseReference databaseReference = Firebase_Variables.database_reference;
    private int resource;
    private Dialog dialog;
    private List<Publicacion> publicacions;
    private Fragment fragment;
    private List<String> imagenes = new ArrayList<>();
    private ImagenesConocimientoAdapter imagenesadapter;
    private String [] meses = Constantes.meses;
    Calendar calendar = Calendar.getInstance();
    Date fecha = calendar.getTime();
    String mes_actual = meses[fecha.getMonth()];
    public PublicacionesAdapter(@NonNull Context context, int resource, @NonNull List<Publicacion> publicaciones
                                ,Main_User_Activity activity) {
        super(context, resource, publicaciones);
        this.context = context;
        this.resource = resource;
        this.publicacions = publicaciones;
        this.activity = activity;
        this.dialog = new Dialog(context);
    }
    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
        convertView=inflater.inflate(resource,parent,false);
        Publicacion publicacion = publicacions.get(position);
            ImageView imagen_publicacion = convertView.findViewById(R.id.imagen_publicacion);
            ImageView delete_post = convertView.findViewById(R.id.btn_delete_post);
            ImageView editar_post = convertView.findViewById(R.id.btn_edit_post);
            LinearLayout container_texto = convertView.findViewById(R.id.container_texto);
            TextView id_publicacion = convertView.findViewById(R.id.id_publicacion);
            TextView descripcion_publicacion = convertView.findViewById(R.id.descripcion);
            TextView estado = convertView.findViewById(R.id.estado_publicacion);
            id_publicacion.setText("Publicación Id: " + publicacion.Id);
            descripcion_publicacion.setText(publicacion.Descripcion);
            if(!publicacion.Estado) {
                estado.setTextColor(context.getResources().getColor(R.color.red));
                estado.setText("Por Aprobar");
            } else {
                estado.setTextColor(context.getResources().getColor(R.color.principal));
                estado.setText("Aprobado");
                editar_post.setVisibility(View.GONE);
            }
            CargarImagen(publicacion.Id,imagen_publicacion);
            DeletePost(delete_post,publicacion,context);
            CLickImagen(imagen_publicacion,publicacion);
            ClickEditarPublicacion(editar_post,publicacion);
            ClickContainerTexto(container_texto,publicacion);
        return convertView;
    }

    private void ClickEditarPublicacion(ImageView editar_post,Publicacion publicacion) {
        editar_post.setOnClickListener(v->{
            fragment = new EditarPostFragment(publicacion);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
        });
    }

    private void ClickContainerTexto(LinearLayout container_texto,Publicacion publicacion) {
        container_texto.setOnClickListener(v->{
            VerDetalle(publicacion);
        });
    }

    private void VerDetalle(Publicacion publicacion) {
        dialog.setContentView(R.layout.publicacion_descripcion_item);
        imagenes.clear();
        for(int i = 0; i<publicacion.Cantidad_fotos;i++)
            imagenes.add("");
        LinearLayoutManager manager1 = new LinearLayoutManager(context);
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerView lista = dialog.findViewById(R.id.list_imagenes_publicacion);
        lista.setLayoutManager(manager1);
        imagenesadapter = new ImagenesConocimientoAdapter(context,imagenes,Constantes.Publicacion,publicacion.Id);
        lista.setAdapter(imagenesadapter);
        TextView id_publicacion = dialog.findViewById(R.id.id_detalle_publicacion);
        TextView categoria = dialog.findViewById(R.id.categoria_detalle);
        TextView sub_categoria = dialog.findViewById(R.id.sub_categoria_detalle);
        TextView descripcion = dialog.findViewById(R.id.descripcion_detalle);
        TextView cantidad = dialog.findViewById(R.id.cantidad_kilogramos_detalle);
        TextView telefono = dialog.findViewById(R.id.numero_detalle);
        TextView direccion = dialog.findViewById(R.id.direccion_detalle);
        ImageView btn_close = dialog.findViewById(R.id.btn_close_detalle_publicacion);
        id_publicacion.setText("Id:"+publicacion.Id);
        categoria.setText("Categoria: "+publicacion.Categoria);
        sub_categoria.setText("Sub-Categoria: "+publicacion.Subcategoria);
        descripcion.setText("Descripción: "+publicacion.Descripcion);
        cantidad.setText("Peso Aproximado : "+publicacion.Kg_basura +" kg");
        telefono.setText("Numero Telefonico: "+publicacion.Telefono);
        direccion.setText("Dirección:"+ publicacion.direccion_publicacion);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Constantes.CloseVentana(dialog,btn_close);
        dialog.show();
    }

    private void CLickImagen(ImageView imagen_publicacion,Publicacion publicacion) {
        imagen_publicacion.setOnClickListener(v->{
            VerDetalle(publicacion);
        });
    }

    private void DeletePost(ImageView delete_post, Publicacion publicacion,Context contexto) {
        delete_post.setOnClickListener(v->{
            Context context = new ContextThemeWrapper(contexto,R.style.Theme_MaterialComponents);
            MaterialAlertDialogBuilder dialog_material = new MaterialAlertDialogBuilder(context);
            dialog_material.setTitle("Aviso");
            dialog_material.setIcon(R.drawable.delete);
            dialog_material.setMessage("Si guarda una publicación no podra borrarla el mismo dia en que se hara el recogo");
            dialog_material.setPositiveButton("Aceptar", (dialog_, which) -> {
                GetDataMes(mes_actual,publicacion.Id);
            }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            dialog_material.show();
        });
    }

    /*private void ActualizarPost(String id,int Cantidad_fotos) {
        ProgressDialog dialog = ProgressDialog.show(context,"Publicacion","Borrando Publicación Esto puede demorar un poco si su internet es lento....",false,false);
        databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Publicaciones").child(id).child("Cantidad_fotos").setValue(0);
        databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Publicaciones").child(id).child("Estado_Vista").setValue(false);
        for(int i = 1;i<=Cantidad_fotos;i++){
            int posicion = i;
            StorageReference path = Firebase_Variables.storage.child("Fotos_Publicaciones").child(auth.getCurrentUser().getUid()).child(id).child("foto_"+i+".png");
            path.delete().addOnCompleteListener(task -> {
                if(posicion==Cantidad_fotos){
                    dialog.dismiss();
                    Toast.makeText(context,"Publicación eliminada con Éxito",Toast.LENGTH_SHORT).show();
                    fragment = new PublicacionesFragment(dialog);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
                }
            });
        }
    }*/

    private void CargarImagen(String id, final ImageView foto_publicacion) {
        StorageReference filePath = Firebase_Variables.GetReferenceImage(id,auth.getCurrentUser().getUid());
        try{
            final File file = File.createTempFile("image","png");
            filePath.getFile(file).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                foto_publicacion.setImageBitmap(bitmap);
            }).addOnFailureListener(e -> Toast.makeText(context,"Su archivo fue demasiado grande porfavor Actualize",Toast.LENGTH_LONG).show());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void GetDataMes(String mes_actual,String id_publicacion) {
        ProgressDialog dialog = ProgressDialog.show(context,"Publicacion","Borrando Publicación Esto puede demorar un poco si su internet es lento....",false,false);
        databaseReference.child("Fechas_Recoleccion").child(mes_actual).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean se_puede_borrar = true;
                if(dataSnapshot.exists()||dataSnapshot.getChildrenCount()!=0) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Mes data_mes = data.getValue(Mes.class);
                        Calendar calendar = Calendar.getInstance();
                        int year = Integer.parseInt(Fecha.GetYear(data_mes.Fecha));
                        int month = Integer.parseInt(Fecha.GetMonth(data_mes.Fecha));
                        int day = Integer.parseInt(Fecha.GetDay(data_mes.Fecha));
                        int Mes =  (calendar.get(Calendar.MONTH)) + 1;
                        String fecha_actual_string = calendar.get(Calendar.YEAR) +"-"+
                                Mes +"-"+ calendar.get(Calendar.DATE);
                        String fecha_obtenida_string = year + "-" + month + "-" + day ;
                        Log.i("Fechas",fecha_actual_string+"\n"+fecha_obtenida_string);
                        if (fecha_actual_string.equals(fecha_obtenida_string)) {
                            se_puede_borrar=false;
                            break;
                        }
                    }
                    if(se_puede_borrar){
                        databaseReference.child("Users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User usuario = dataSnapshot.getValue(User.class);
                                int cantidad_publicaciones = usuario.Cantidad_publicaciones  - 1;
                                databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Cantidad_publicaciones").setValue(cantidad_publicaciones);
                                Publicacion publicacion = usuario.Publicaciones.get(id_publicacion);
                                for(int i = 1;i<=publicacion.Cantidad_fotos;i++){
                                    int posicion = i;
                                    StorageReference path = Firebase_Variables.storage.child("Fotos_Publicaciones").child(auth.getCurrentUser().getUid()).child(id_publicacion).child("foto_"+i+".png");
                                    path.delete().addOnCompleteListener(task -> {
                                        if(posicion==publicacion.Cantidad_fotos){
                                            databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Publicaciones").child(id_publicacion).removeValue();
                                            dialog.dismiss();
                                            Toast.makeText(context,"Publicación eliminada con Éxito",Toast.LENGTH_SHORT).show();
                                            ProgressDialog dialog2_ = ProgressDialog.show(getContext(),"Mis Publicaciones","Cargando...",false,false);
                                            fragment = new PublicacionesFragment(dialog2_);
                                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(context,"No se pudo Borrar Hubo un error intentelo más tarde",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }else{
                        dialog.dismiss();
                        Context contexto = new ContextThemeWrapper(context,R.style.Theme_MaterialComponents);
                        MaterialAlertDialogBuilder dialog_material = new MaterialAlertDialogBuilder(contexto);
                        dialog_material.setTitle("Aviso");
                        dialog_material.setIcon(R.drawable.delete);
                        dialog_material.setMessage("No se Puede Borrar el Articulo el Mismo dia que se Recoge revise su calendario");
                        dialog_material.setNeutralButton("Cerrar", (dialog, which) -> dialog.dismiss());
                        dialog_material.show();
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"No se pudo Obtener la Informacion",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
