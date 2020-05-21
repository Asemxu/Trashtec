package com.labawsrh.aws.trashtec.Validation;
import android.app.AlertDialog;
import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.labawsrh.aws.trashtec.Firebase.Database.Firebase;
;
import com.labawsrh.aws.trashtec.Models.Publicacion;
import com.labawsrh.aws.trashtec.Models.User;


public class Validation {

    private User user;
    public  ErrorManager errorManager;
    private Firebase firebase;
    private Context context;
    private Publicacion publicacion;
    public  Validation(FirebaseUser user,Firebase firebase,boolean tipo_cuenta){
        this.firebase=firebase;
        this.user= Firebase.GetUser(user,tipo_cuenta);

    }
    public Validation(AlertDialog.Builder dialog, Context context,Publicacion publicacion){
        this.context = context;
        this.publicacion = publicacion;
        errorManager = new ErrorManager(dialog);

    }
    private void ValidarTelefono(String telefono) {
        if(isNullorEmpty(telefono)) {
            errorManager.addError("El telefono es obligatorio y no debe contener espacios");
        }else if(telefono.length()<9){
            errorManager.addError("El telefono solo acepta 9 digitos");
        }
    }
    private void ValidarDescripcion(String desripcion) {
        if(desripcion.isEmpty())
            errorManager.addError("La descripcion es obligatoria");
        else if(desripcion.length()>=50)
            errorManager.addError("Solo se permite 50 caracteres como maximo");
    }
    private boolean isNullorEmpty(String str) {
        if(str == null || str == " "
                || str.length() == 0 || isWhiteSpaceorEmpty(str))
            return  true;
        return  false;
    }
    private boolean isWhiteSpaceorEmpty(String str){
        if(str == null || str == " "
                || str.length() == 0 ||
                str.startsWith("  ") || str.endsWith("  "))
            return  true;
        return  false;
    }
    public boolean esValidoPublicacion(){
        errorManager.Limpiar();
        ValidarTelefono(publicacion.Telefono);
        ValidarDescripcion(publicacion.Descripcion);
        ValidarCategoria(publicacion.Categoria);
        ValidarSubCategoria(publicacion.Subcategoria);
        ValidarCantidadFotos(publicacion.Cantidad_fotos);
        ValidarDireccion(publicacion.direccion_publicacion);
        return errorManager.isValid();
    }

    private void ValidarDireccion(String direccion_publicacion) {
        if(direccion_publicacion.isEmpty())
            errorManager.addError("La dirección es Obligatoria");
    }


    private void ValidarCantidadFotos(int cantidad_fotos) {
        if(cantidad_fotos<3)
            errorManager.addError("Tiene que ingresar por lo menos tres fotos diferentes");
    }

    private void ValidarSubCategoria(String subcategoria) {
        if(subcategoria.isEmpty())
            errorManager.addError("Escoga una subcategoria");
    }

    private void ValidarCategoria(String categoria) {
        if(categoria.isEmpty())
            errorManager.addError("Escoga una Categoria");
    }

    public void mostrarErrores() { errorManager.showErros(); }
    public  void  EsValidoLogin(){
        firebase.IsRegistrado(user);
    }
}
