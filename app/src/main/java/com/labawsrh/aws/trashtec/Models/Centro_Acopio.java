package com.labawsrh.aws.trashtec.Models;

import java.util.HashMap;

public class Centro_Acopio {
    public String Direccion;
    public String Empresa;
    public String static_url;
    public String Imagen;
    public HashMap<String,SubCategoria> Categorias;
    public HashMap<String,Horario> Horario;
    public String Id;
    public Centro_Acopio(){ }

}
