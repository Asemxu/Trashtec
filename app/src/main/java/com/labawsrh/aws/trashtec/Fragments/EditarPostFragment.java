package com.labawsrh.aws.trashtec.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.Models.Publicacion;
import com.labawsrh.aws.trashtec.R;
import com.labawsrh.aws.trashtec.Validation.Validation;

public class EditarPostFragment extends Fragment {
    private Main_User_Activity activity;
    private MaterialToolbar toolbar;
    private TextInputEditText descripcion_edit;
    private TextInputEditText cantidad_edit;
    private TextInputEditText telefono_edit;
    private TextInputEditText direcion_edit;
    private AutoCompleteTextView categoria;
    private AutoCompleteTextView sub_categoria;
    private ArrayAdapter<String> categorias;
    private FirebaseAuth auth = Firebase_Variables.firebaseauth;
    private ArrayAdapter<String> subcategorias;
    private ImageButton btn_edit_categoria;
    private ImageButton btn_edit_subcategoria;
    private Validation validation;
    private ImageButton btn_edit_descripcion;
    private ImageButton btn_edit_peso;
    private DatabaseReference databaseReference = Firebase_Variables.database_reference;
    private ImageButton btn_edit_telefono;
    private ImageButton btn_edit_direccion;
    private ProgressDialog dialog;
    private AlertDialog.Builder alertdialog;
    private Fragment fragment;
    private Publicacion publicacion;
    public EditarPostFragment(Publicacion publicacion){
        this.publicacion = publicacion;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (Main_User_Activity) getActivity();
        return inflater.inflate(R.layout.edit_post_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InicializarViews(view);
        ClickCategoria();
        EditarCategoria();
        EditarSubCategoria();
        EditarDescripcion();
        EditarPesoAproximado();
        EditarTelefono();
        EditarDirecccion();
        GetPositionListaCategoria();
        Regresar();
    }

    private void EditarDirecccion() {
        btn_edit_direccion.setOnClickListener(v->{
            if(direcion_edit.getText().toString().equals(publicacion.direccion_publicacion))
                Toast.makeText(getContext(), "No se permite guardar la misma direccion si quiere editar ingrese otra", Toast.LENGTH_SHORT).show();
            else{
                IniciarlizarValidador();
                validation.ValidarDireccion(direcion_edit.getText().toString());
                if(validation.errorManager.cantErrors()==0) {
                    databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Publicaciones").
                            child(publicacion.Id).child("direccion_publicacion").setValue(direcion_edit.getText().toString());
                    publicacion.direccion_publicacion = direcion_edit.getText().toString();
                    Toast.makeText(getContext(), "Actualizado", Toast.LENGTH_SHORT).show();
                }else
                    validation.mostrarErrores();
            }
        });
    }

    private void EditarTelefono() {
        btn_edit_telefono.setOnClickListener(v->{
            if(telefono_edit.getText().toString().equals(publicacion.Telefono))
                Toast.makeText(getContext(), "No se permite guardar el mismo telefono si quiere editar ingrese otro", Toast.LENGTH_SHORT).show();
            else{
                IniciarlizarValidador();
                validation.ValidarTelefono(telefono_edit.getText().toString());
                if(validation.errorManager.cantErrors()==0) {
                    databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Publicaciones").
                            child(publicacion.Id).child("Telefono").setValue(telefono_edit.getText().toString());
                    publicacion.Telefono = telefono_edit.getText().toString();
                    Toast.makeText(getContext(), "Actualizado", Toast.LENGTH_SHORT).show();
                }else
                    validation.mostrarErrores();
            }
        });
    }

    private void EditarPesoAproximado() {
        btn_edit_peso.setOnClickListener(v->{
            if(Float.parseFloat(cantidad_edit.getText().toString())==publicacion.Kg_basura)
                Toast.makeText(getContext(), "No se permite guardar la misma cantidad si quiere editar ingrese otra", Toast.LENGTH_SHORT).show();
            else {
                IniciarlizarValidador();
                validation.ValidarCantidadKilogramos(cantidad_edit.getText().toString());
                if(validation.errorManager.cantErrors()==0) {
                    databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Publicaciones").
                            child(publicacion.Id).child("Kg_basura").setValue(Float.parseFloat(cantidad_edit.getText().toString()));
                    publicacion.Kg_basura = Float.parseFloat(cantidad_edit.getText().toString());
                    Toast.makeText(getContext(), "Actualizado", Toast.LENGTH_SHORT).show();
                }else
                    validation.mostrarErrores();
            }
        });
    }

    private void EditarDescripcion() {
        btn_edit_descripcion.setOnClickListener(v->{
            if(descripcion_edit.getText().toString().equals(publicacion.Descripcion))
                Toast.makeText(getContext(), "No se permite guardar la misma descripción", Toast.LENGTH_SHORT).show();
            else {
                IniciarlizarValidador();
                validation.ValidarDescripcion(descripcion_edit.getText().toString());
                if(validation.errorManager.cantErrors()==0) {
                    databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Publicaciones").
                            child(publicacion.Id).child("Descripcion").setValue(descripcion_edit.getText().toString());
                    publicacion.Descripcion = descripcion_edit.getText().toString();
                    Toast.makeText(getContext(), "Actualizado", Toast.LENGTH_SHORT).show();
                }else
                    validation.mostrarErrores();
            }
        });
    }

    private void GetPositionListaCategoria() {
        int position = 0;
        String[]categorias = Constantes.categorias;
        for(int i = 0;i<categorias.length;i++) {
            if (categorias[i].equals(publicacion.Categoria)){
                position = i;
            break;
            }
        }
        sub_categoria.setEnabled(true);
        SelectSub(position);
    }

    private void EditarSubCategoria() {

        btn_edit_subcategoria.setOnClickListener(v->{
            if(sub_categoria.getText().toString().equals(publicacion.Subcategoria))
                Toast.makeText(getContext(), "No se permite guardar una Subcategoría del mismo tipo", Toast.LENGTH_SHORT).show();
            else {
                IniciarlizarValidador();
                validation.ValidarSubCategoria(sub_categoria.getText().toString());
                if(validation.errorManager.cantErrors()==0) {
                   EditCategoria_SubCategoria();
                }else
                    validation.mostrarErrores();
            }
        });
    }

    private void EditarCategoria() {
        btn_edit_categoria.setOnClickListener(v->{
            if(categoria.getText().toString().equals(publicacion.Categoria))
                Toast.makeText(getContext(), "No se permite una categoría del mismo tipo", Toast.LENGTH_SHORT).show();
            else {
               IniciarlizarValidador();
                validation.ValidarCategoria(categoria.getText().toString());
                if(validation.errorManager.cantErrors()==0) {
                    EditCategoria_SubCategoria();
                }else
                    validation.mostrarErrores();
            }
        });
    }

    private void EditCategoria_SubCategoria() {
        databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Publicaciones").
                child(publicacion.Id).child("Categoria").setValue(categoria.getText().toString());
        databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Publicaciones").
                child(publicacion.Id).child("Subcategoria").setValue(sub_categoria.getText().toString());
        Toast.makeText(getContext(), "Actualizado", Toast.LENGTH_SHORT).show();
        publicacion.Categoria = categoria.getText().toString();
        publicacion.Subcategoria = sub_categoria.getText().toString();
    }

    private void IniciarlizarValidador() {
        alertdialog =new AlertDialog.Builder(getContext());
        validation = new Validation(alertdialog);
        validation.errorManager.Limpiar();
    }

    private void Regresar() {
        toolbar.setNavigationOnClickListener(v->{
            dialog = ProgressDialog.show(getContext(),"Mis Publicaciones","Cargando...",false,false);
            fragment = new PublicacionesFragment(dialog);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_reciclaje,fragment).commit();
        });
    }
    private void ClickCategoria() {
        categoria.setOnItemClickListener((parent, view, position, id) -> {
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
        sub_categoria.setText(subcategorias.getItem(Constantes.PrimerElementoLista));
        sub_categoria.setAdapter(subcategorias);
    }
    private void InicializarViews(View view) {
        categorias = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1, Constantes.categorias);
        toolbar = activity.findViewById(R.id.topAppBar_reciclaje);
        toolbar.setTitle("Editar Publicación");
        descripcion_edit = view.findViewById(R.id.descripcion_edit);
        descripcion_edit.setText(publicacion.Descripcion);
        telefono_edit = view.findViewById(R.id.telefono_edit);
        telefono_edit.setText(publicacion.Telefono);
        direcion_edit = view.findViewById(R.id.direccion_edit);
        direcion_edit.setText(publicacion.direccion_publicacion);
        cantidad_edit = view.findViewById(R.id.edit_peso);
        cantidad_edit.setText(""+publicacion.Kg_basura);
        categoria = view.findViewById(R.id.lista_categorias_edit);
        categoria.setText(publicacion.Categoria);
        categoria.setAdapter(categorias);
        sub_categoria = view.findViewById(R.id.lista_subcategorias_edit);
        sub_categoria.setText(publicacion.Subcategoria);
        btn_edit_categoria = view.findViewById(R.id.btn_edit_categoria);
        btn_edit_subcategoria = view.findViewById(R.id.btn_edit_subcategoria);
        btn_edit_descripcion = view.findViewById(R.id.btn_edit_descripción);
        btn_edit_peso = view.findViewById(R.id.btn_edit_peso);
        btn_edit_telefono = view.findViewById(R.id.btn_edit_telefono);
        btn_edit_direccion = view.findViewById(R.id.btn_edit_direccion);
    }
}
