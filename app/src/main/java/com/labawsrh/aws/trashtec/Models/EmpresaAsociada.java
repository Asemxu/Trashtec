package com.labawsrh.aws.trashtec.Models;

public class EmpresaAsociada {
    public String direccion;
    public String Id;
    public String Imagen;
    public String Nombre;
    public String tipo_empresa;
    public EmpresaAsociada(){}

    public String get_direccion(){
        return direccion;
    }

    public String get_Id() {
        return Id;
    }

    public void set_Id(String id) {
        Id = id;
    }

    public String get_Imagen() {
        return Imagen;
    }

    public void set_Imagen(String imagen) {
        Imagen = imagen;
    }

    public String get_Nombre() {
        return Nombre;
    }

    public void set_Nombre(String nombre) {
        Nombre = nombre;
    }
}
