package com.labawsrh.aws.trashtec.Clases_Helper;
public class Fecha {

    public static String GetYear(String fecha){
        return fecha.substring(0, 4);
    }
    public static String GetMonth(String fecha){
        return fecha.substring(5,7);
    }
    public static String GetDay(String fecha){
        return fecha.substring(fecha.length()-2);
    }
}
