package com.labawsrh.aws.trashtec.Firebase.Database;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.labawsrh.aws.trashtec.Activitys.LoginActivity;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.Fragments.PublicacionesFragment;
import com.labawsrh.aws.trashtec.Fragments.TiendaFragment;
import com.labawsrh.aws.trashtec.Models.Descuentos;
import com.labawsrh.aws.trashtec.Models.Publicacion;
import com.labawsrh.aws.trashtec.Models.User;
import com.labawsrh.aws.trashtec.R;

import java.util.List;
import java.util.Objects;

public class Firebase {

    private DatabaseReference databaseReference;
    private Context context;
    private LoginActivity activity;
    private Publicacion publicacion;
    private FirebaseAuth auth = Firebase_Variables.firebaseauth;
    private Main_User_Activity main_user_activity;
    private List<Uri> path_images;
    private Fragment fragment;
    private Descuentos descuento;
    public Firebase(DatabaseReference databaseReference, Context context,LoginActivity activity){
        this.databaseReference=databaseReference;
        this.context=context;
        this.activity=activity;
    }
    public Firebase(Publicacion publicacion,DatabaseReference databaseReference,Context context,List<Uri> images,
                    Main_User_Activity main_user_activity){
        this.databaseReference = databaseReference;
        this.publicacion = publicacion;
        this.context = context;
        this.path_images = images;
        this.main_user_activity = main_user_activity;
    }
    public Firebase(Descuentos descuentos, DatabaseReference databaseReference ,Context context , Main_User_Activity activity){
        this.databaseReference = databaseReference;
        this.context = context;
        this.main_user_activity = activity;
        this.descuento = descuentos;
    }
    private void RegistrarDatos(User usuario){
        Toast.makeText(context,"Logueado",Toast.LENGTH_SHORT).show();
        databaseReference.child("Users").child(usuario.UID).setValue(usuario);
    }
    public static User GetUser(FirebaseUser usuario,boolean tipo_cuenta) {
        return new User(0,usuario.getDisplayName(),usuario.getPhoneNumber(),usuario.getEmail(),usuario.getUid(),0,tipo_cuenta,0,true,0);
    }
    public void IsRegistrado(final User usuario){
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean existe = false;
                if(dataSnapshot.getChildrenCount()>0) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if (Objects.equals(data.getKey(), usuario.UID))
                            existe = true;
                    }
                }
                if(!existe)
                    RegistrarDatos(usuario);
                MainUserActivity(usuario);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"Algo Paso",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void MainUserActivity(User usuario) {
        databaseReference.child("Users").child(usuario.UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Intent main_user = new Intent(context, Main_User_Activity.class);
                activity.startActivity(main_user);
                activity.finish();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"Algo Paso",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void RegistrarPublicacion(ProgressDialog dialog){
        GuardarFotosPublicacion(path_images,publicacion,dialog);
    }

    private void GuardarFotosPublicacion(List<Uri> path_images,Publicacion publicacion, ProgressDialog dialog) {
        int i = 1;
        for(Uri pathuri:path_images){
            int posicion = i;
            databaseReference.child("Users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User usuario = dataSnapshot.getValue(User.class);
                    int cantidad_publicaciones = usuario.Cantidad_publicaciones + 1;
                    StorageReference path = Firebase_Variables.storage.child("Fotos_Publicaciones").child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                            .child("Id_"+cantidad_publicaciones).child("foto_"+posicion+".png");
                    path.putFile(pathuri).addOnSuccessListener(taskSnapshot -> {
                        if(posicion==path_images.size()){
                            GuardarPublicacion(publicacion,dialog);
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context,"No se pudo Obtener la Informacion",Toast.LENGTH_SHORT).show();
                }
            });
            i++;
        }
    }

    private void GuardarPublicacion(Publicacion publicacion,ProgressDialog dialog) {
        databaseReference.child("Users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User usuario = dataSnapshot.getValue(User.class);
                int cantidad_publicaciones = usuario.Cantidad_publicaciones + 1;
                AgregarPost(publicacion,cantidad_publicaciones,dialog);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"No se pudo Obtener la Informacion",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AgregarPost(Publicacion publicacion, int cantidad_publicaciones,ProgressDialog dialog) {
        String id = "Id_"+cantidad_publicaciones;
        publicacion.Id = id;
        databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Publicaciones").child(id).setValue(publicacion);
        ActualizarCantidadPublicaciones(cantidad_publicaciones);
        dialog.dismiss();
        dialog = ProgressDialog.show(context,"Mis Publicaciones","Cargando...",false,false);
        Fragment fragment = new PublicacionesFragment(dialog);
        main_user_activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
        Toast.makeText(context,"Publicado con Exitó",Toast.LENGTH_SHORT).show();
    }

    private void ActualizarCantidadPublicaciones(int cantidad_publicaciones) {
        databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Cantidad_publicaciones").setValue(cantidad_publicaciones);
    }
    public void GuardarDescuento(int puntos_restantes,ProgressDialog dialog){
        databaseReference.child("Descuentos_Usuarios").child(auth.getCurrentUser().getUid()).push().setValue(descuento.Id);
        ActualizarCantidadDescuentos(puntos_restantes,dialog);
    }

    private void ActualizarCantidadDescuentos(int puntos_restantes, ProgressDialog dialog) {
        databaseReference.child("Users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                int cantidad_descuentos = user.Cantidad_descuentos + 1;
                databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Cantidad_descuentos").setValue(cantidad_descuentos);
                databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Cantidad_points").setValue(puntos_restantes);
                dialog.dismiss();
                ProgressDialog new_dialog = ProgressDialog.show(context,"Tienda","Cargando...",false,false);
                fragment = new TiendaFragment(new_dialog);
                main_user_activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"No se pudo Obtener su información",Toast.LENGTH_SHORT).show();
            }
        });
    }

    ;
}
