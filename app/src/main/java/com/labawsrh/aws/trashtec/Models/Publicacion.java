package com.labawsrh.aws.trashtec.Models;

public class Publicacion {
    public String Id;
    public String Subcategoria;
    public String Categoria;
    public String Fecha_creacion;
    public String Telefono;
    public String direccion_publicacion;
    public boolean Estado;
    public boolean Estado_Vista;
    public int Cantidad_fotos;
    public float Kg_basura;
    public String Descripcion;
    public Publicacion(){}
    public Publicacion(String direccion_publicacion,String id,String subcategoria,String Categoria,String Fecha_creacion,boolean Estado,boolean Estado_Vista
                        , int cantidad_fotos,String Telefono,String Descripcion,String Kg_basura){
        this.direccion_publicacion = direccion_publicacion;
        this.Id = id;
        this.Subcategoria = subcategoria;
        this.Cantidad_fotos = cantidad_fotos;
        this.Fecha_creacion = Fecha_creacion;
        this.Estado = Estado;
        this.Estado_Vista = Estado_Vista;
        this.Categoria = Categoria;
        this.Telefono = Telefono;
        this.Descripcion = Descripcion;
        this.Kg_basura = Float.parseFloat(Kg_basura);
    }
}
