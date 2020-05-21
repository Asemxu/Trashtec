package com.labawsrh.aws.trashtec.Models;
import java.util.HashMap;

public class User {
    public String PhoneNumber;
    public String Name;
    public String email;
    public String UID;
    public int Cantidad_points;
    public int Cantidad_descuentos;
    public int Cantidad_publicaciones ;
    public boolean Tipo_cuenta;
    public boolean acepta_notificaciones;
    public int cantidad_recojos;
    public HashMap<String,Publicacion> Publicaciones;
    public User(){

    }
    public User(int cantidad_descuentos,String name,String phone,String email,String UID,int cantidad_points,boolean tipo_cuenta,int cantidad_publicaciones
                ,boolean acepta_notificaciones, int cantidad_recojos){
        this.PhoneNumber=phone;
        this.Cantidad_descuentos = cantidad_descuentos;
        this.email=email;
        this.UID= UID;
        this.Name=name;
        this.Tipo_cuenta=tipo_cuenta;
        this.Cantidad_points = cantidad_publicaciones;
        this.Cantidad_points=cantidad_points;
        this.acepta_notificaciones = acepta_notificaciones;
        this.cantidad_recojos = cantidad_recojos;
    }
}
