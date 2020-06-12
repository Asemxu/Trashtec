package com.labawsrh.aws.trashtec.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.logging.ConsoleHandler;

public class ImagenesConocimientoAdapter extends RecyclerView.Adapter<ImagenesConocimientoAdapter.MyViewHolder> {

    private Context context;
    private List<String> imagenes;
    private String Tipo;
    private String Id_publicacion;
    private FirebaseAuth auth = Firebase_Variables.firebaseauth;
    public ImagenesConocimientoAdapter(@NonNull Context context, @NonNull List<String> imagenes,String Tipo,String Id_publicacion) {
        this.context=context;
        this.imagenes=imagenes;
        this.Tipo = Tipo;
        this.Id_publicacion = Id_publicacion;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.imagenes_lista_conocimiento,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(this.Tipo.equals(Constantes.Conocimiento))
            Picasso.get().load(imagenes.get(position)).into(holder.imagen_);
        else
            CargarImagenesFirebase(position,holder.imagen_);
    }

    public void CargarImagenesFirebase(int position,ImageView foto_publicacion) {
        StorageReference filePath = Firebase_Variables.GetReferenceImages(Id_publicacion,auth.getCurrentUser().getUid(),position+1);
        try{
            final File file = File.createTempFile("image","png");
            filePath.getFile(file).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                foto_publicacion.setImageBitmap(bitmap);
            }).addOnFailureListener(e -> Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show());
        }catch (Exception ex){
            Log.i("Error", ex.getMessage());
        }
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
