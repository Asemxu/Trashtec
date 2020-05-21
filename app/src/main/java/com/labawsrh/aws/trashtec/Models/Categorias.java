package com.labawsrh.aws.trashtec.Models;

public class Categorias {
    public String Nombre;
    public String Contenido;
    public String Ejemplos;
    public String Imagen1;
    public String Imagen2;
    public String Imagen3;
    private Categorias(){

    }
    private Categorias(String nombre,String contenido,String ejemplos,
                       String imagen1,String imagen2,String imagen3){
        this.Nombre  = nombre;
        this.Contenido = contenido;
        this.Ejemplos=ejemplos;
        this.Imagen1=imagen1;
        this.Imagen2=imagen2;
        this.Imagen3=imagen3;
    }
}
