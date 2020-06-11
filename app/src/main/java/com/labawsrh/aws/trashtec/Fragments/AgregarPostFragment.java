package com.labawsrh.aws.trashtec.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.Firebase.Database.Firebase;
import com.labawsrh.aws.trashtec.Models.Publicacion;
import com.labawsrh.aws.trashtec.R;
import com.labawsrh.aws.trashtec.Validation.Validation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgregarPostFragment extends Fragment {
    private Main_User_Activity activity;
    private MaterialToolbar bar;
    private Fragment fragment;
    private ArrayAdapter<String> categorias;
    private ArrayAdapter<String> subcategorias;
    private AlertDialog.Builder dialog;
    private AutoCompleteTextView categorias_lista;
    private AutoCompleteTextView subcategorias_lista;
    private String categoria="";
    private String subcategoria="";
    private Button btn_guardar_post;
    private ProgressDialog progressDialog;
    private TextInputEditText telefono;
    private TextInputEditText descripcion;
    private TextInputEditText direccion;
    private TextInputEditText cantidad_kilogramos;
    private Firebase firebase;
    private DatabaseReference databaseReference = Firebase_Variables.database_reference;
    private ImageView imagen1;
    private ImageView imagen2;
    private ImageView imagen3;
    private ImageView imagen4;
    private ImageView imagen5;
    private ImageView imagen6;
    private MaterialAlertDialogBuilder dialog_material;
    private int imagen_seleccionada = 1;
    private boolean hizo_click;
    private List<Uri> path_images;
    private int hizodobleclickimagen1 = 0;
    private int hizodobleclickimagen2 = 0;
    private int hizodobleclickimagen3 = 0;
    private int hizodobleclickimagen4 = 0;
    private int hizodobleclickimagen5 = 0;
    private int hizodobleclickimagen6 = 0;    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (Main_User_Activity) getActivity();
        return inflater.inflate(R.layout.add_publicacion_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InstanciarViews(view);
        ClickBar();
        ClickCategoria();
        CLickGuardarPost();
        ClicImagenes();
    }

    private void ClicImagenes() {
        imagen1.setOnClickListener(v->{
            imagen_seleccionada = 1;
            if(!hizo_click) {
                hizo_click = true;
                Cargar();
            }
        });
        imagen2.setOnClickListener(v->{
            imagen_seleccionada = 2;
            if(!hizo_click) {
                hizo_click = true;
                Cargar();
            }
        });
        imagen3.setOnClickListener(v->{
            imagen_seleccionada = 3;
            if(!hizo_click) {
                hizo_click = true;
                Cargar();
            }
        });
        imagen4.setOnClickListener(v->{
            imagen_seleccionada = 4;
            if(!hizo_click) {
                hizo_click = true;
                Cargar();
            }
        });
        imagen5.setOnClickListener(v->{
            imagen_seleccionada = 5;
            if(!hizo_click) {
                hizo_click = true;
                Cargar();
            }
        });
        imagen6.setOnClickListener(v->{
            imagen_seleccionada = 6;
            if(!hizo_click) {
                hizo_click = true;
                Cargar();
            }
        });
    }

    private void Cargar() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione la Aplicacion"), Constantes.SELECT_PICTURE);
    }


    private void CLickGuardarPost() {

        btn_guardar_post.setOnClickListener(v->{
            dialog = new AlertDialog.Builder(getContext());
            Context context = new ContextThemeWrapper(getContext(),R.style.Theme_MaterialComponents);
            dialog_material = new MaterialAlertDialogBuilder(context);
            dialog_material.setTitle("Aviso");
            dialog_material.setIcon(R.drawable.warning);
            dialog_material.setMessage("Si guarda una publicación no podra borrarla el mismo dia en que se hara el recogo");
            dialog_material.setPositiveButton("Aceptar", (dialog_, which) -> {
                progressDialog = ProgressDialog.show(getContext(),"Registrando Publicación","Espere mientras se guarda la publicación.......",false,false);
                Validation validation = new Validation(dialog,getContext(),GetPublicacion());
                if(validation.esValidoPublicacion()){
                    firebase = new Firebase(GetPublicacion(),databaseReference,getContext(),path_images,activity);
                    firebase.RegistrarPublicacion(progressDialog);
                }else{
                    validation.mostrarErrores();
                    progressDialog.dismiss();
                }
            }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            dialog_material.show();
        });
    }

    private Publicacion GetPublicacion() {
        String fecha_creacion = new Date().toString();
        subcategoria = subcategorias_lista.getText().toString();
        categoria = categorias_lista.getText().toString();
        return new Publicacion(direccion.getText().toString(),null,subcategoria,categoria,fecha_creacion,false,true,path_images.size(),telefono.getText().toString(),
                descripcion.getText().toString(),cantidad_kilogramos.getText().toString());
    }

    private void ClickCategoria() {
        categorias_lista.setOnItemClickListener((parent, view, position, id) -> {
            subcategorias_lista.setEnabled(true);
            SelectSub(position);
        });
    }

    private void SelectSub(int position) {
        switch (position){
            case Constantes.Grandes:
                subcategorias = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1, Constantes.categoria1);
                break;
            case Constantes.Medianos:
                subcategorias = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1, Constantes.categoria2);
                break;
            case Constantes.Iluminacion:
                subcategorias = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1, Constantes.categoria3);
                break;
            case Constantes.Pantallas:
                subcategorias = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1, Constantes.categoria4);
                break;
            case Constantes.Otros:
                subcategorias = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1, Constantes.categoria5);
                break;
        }
        subcategorias_lista.setText(subcategorias.getItem(Constantes.PrimerElementoLista));
        subcategorias_lista.setAdapter(subcategorias);
    }

    private void ClickBar() {
            bar.setNavigationOnClickListener(v->{
                ProgressDialog dialog = ProgressDialog.show(getContext(),"Mis Publicaciones","Cargando...",false,false);
                fragment = new PublicacionesFragment(dialog);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
            });
    }

    private void InstanciarViews(View view) {
        bar = activity.findViewById(R.id.topAppBar_reciclaje);
        bar.setTitle("Agregar Publicación");
        categorias = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1, Constantes.categorias);
        categorias_lista = view.findViewById(R.id.lista_categorias_add);
        categorias_lista.setAdapter(categorias);
        subcategorias_lista = view.findViewById(R.id.sub_categoria);
        btn_guardar_post = view.findViewById(R.id.btn_guardar_post);
        telefono = view.findViewById(R.id.add_telefono);
        direccion = view.findViewById(R.id.direccion);
        descripcion = view.findViewById(R.id.descripcion);
        cantidad_kilogramos = view.findViewById(R.id.cantidad_kilogramos);
        path_images = new ArrayList<>(6);
        imagen1 = view.findViewById(R.id.imagen_1);
        imagen2 = view.findViewById(R.id.imagen_2);
        imagen3 = view.findViewById(R.id.imagen_3);
        imagen4 = view.findViewById(R.id.imagen_4);
        imagen5 = view.findViewById(R.id.imagen_5);
        imagen6 = view.findViewById(R.id.imagen_6);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Request","Req"+resultCode);
        if(resultCode ==Activity.RESULT_OK)
            SelectImage(imagen_seleccionada,data.getData());
        else
            hizo_click = false;
    }

    private void SelectImage(int imagen_seleccionada,Uri uri) {
        switch (imagen_seleccionada){
            case 1:
                hizodobleclickimagen1++;
                imagen1.setImageURI(uri);
                ValidarDobleClick(hizodobleclickimagen1,uri,0);
                break;
            case 2:
                hizodobleclickimagen2++;
                imagen2.setImageURI(uri);
                ValidarDobleClick(hizodobleclickimagen2,uri,1);
                break;
            case 3:
                hizodobleclickimagen3++;
                imagen3.setImageURI(uri);
                ValidarDobleClick(hizodobleclickimagen3,uri,2);
                break;
            case 4:
                hizodobleclickimagen4++;
                imagen4.setImageURI(uri);
                ValidarDobleClick(hizodobleclickimagen4,uri,3);
                break;
            case 5:
                hizodobleclickimagen5++;
                imagen5.setImageURI(uri);
                ValidarDobleClick(hizodobleclickimagen5,uri,4);
                break;
            case 6:
                hizodobleclickimagen6++;
                imagen6.setImageURI(uri);
                ValidarDobleClick(hizodobleclickimagen6,uri,5);
                break;
        }
        hizo_click = false;
    }

    private void ValidarDobleClick(int cantidad_cliks, Uri uri,int posicion) {
        if(cantidad_cliks<2) {
            path_images.add(uri);
        }else{
            path_images.remove(posicion);
            path_images.add(posicion,uri);
        }
    }


}
