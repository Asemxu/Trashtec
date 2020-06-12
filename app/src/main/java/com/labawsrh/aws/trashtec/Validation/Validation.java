package com.labawsrh.aws.trashtec.Validation;
import android.app.AlertDialog;
import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.labawsrh.aws.trashtec.Firebase.Database.Firebase;
import com.labawsrh.aws.trashtec.Models.Publicacion;
import com.labawsrh.aws.trashtec.Models.User;


public class Validation {

    private User user;
    public  ErrorManager errorManager;
    private Firebase firebase;
    private Publicacion publicacion;
    public  Validation(FirebaseUser user,Firebase firebase,boolean tipo_cuenta){
        this.firebase=firebase;
        this.user= Firebase.GetUser(user,tipo_cuenta);

    }
    public Validation(AlertDialog.Builder dialog,Publicacion publicacion){
        this.publicacion = publicacion;
        errorManager = new ErrorManager(dialog);

    }
    public Validation(AlertDialog.Builder dialog){
        errorManager = new ErrorManager(dialog);

    }
    public void ValidarTelefono(String telefono) {
        if(isNullorEmpty(telefono)) {
            errorManager.addError("El telefono es obligatorio y no debe contener espacios");
        }else if(telefono.length()<9){
            errorManager.addError("El telefono solo acepta 9 digitos");
        }else if(telefono.length()>10)
            errorManager.addError("El telefono solo acepta 9 digitos");
    }
    public void ValidarDescripcion(String desripcion) {
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
        ValidarCantidadKilogramos(String.valueOf(publicacion.Kg_basura));
        return errorManager.isValid();
    }

    public void ValidarCantidadKilogramos(String kg_basura) {
        if(kg_basura.isEmpty())
            errorManager.addError("La Cantidad es Obligatoria");
        else{
            float cantidad_kilogramos = Float.parseFloat(kg_basura);
            if(cantidad_kilogramos<=0)
                errorManager.addError("La cantidad no puede ser menor o igual a cero");
        }
    }

    public void ValidarDireccion(String direccion_publicacion) {
        if(direccion_publicacion.isEmpty())
            errorManager.addError("La dirección es Obligatoria");
    }


    private void ValidarCantidadFotos(int cantidad_fotos) {
        if(cantidad_fotos<3)
            errorManager.addError("Tiene que ingresar por lo menos tres fotos diferentes");
    }

    public void ValidarSubCategoria(String subcategoria) {
        if(subcategoria.isEmpty())
            errorManager.addError("Escoga una subcategoria");
    }

    public void ValidarCategoria(String categoria) {
        if(categoria.isEmpty())
            errorManager.addError("Escoga una Categoria");
    }

    public void mostrarErrores() { errorManager.showErros(); }
    public  void  EsValidoLogin(){
        firebase.IsRegistrado(user);
    }
}
