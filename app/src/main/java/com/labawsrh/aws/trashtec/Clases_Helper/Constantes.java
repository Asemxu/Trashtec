package com.labawsrh.aws.trashtec.Clases_Helper;

import android.app.Dialog;
import android.widget.ImageView;

public class Constantes {
    public final static int RC_SIGN_IN = 1;
    public final static int Inicial = -1;
    public final static int Grandes = 0;
    public final static int Medianos = 1;
    public final static int Iluminacion = 2;
    public final static int Pantallas = 3;
    public final static int Otros = 4;
    public final static String [] objects = {"Refrigeradora","Microondas","Lampara","Pantalla","Computadora"};
    public final static String [] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
    public final static String [] dias = {"Domingo","Lunes","Martes","Miércoles","Jueves","Viernes","Sábado"};
    public final static String [] categorias = {"Aparatos de refrigeracion","Aparatos grandes y medianos",
            "Aparatos de iluminación","Pantallas y monitores ","Otros electrodomésticos"};
    public final static String [] categoria1= {"Neveras","Congeladores","Refrigeradores"};
    public final static  String [] categoria2 = {"Microondas","Lavadoras","Cafeteras"};
    public final static String [] categoria3 = {"Tubos fluorescentes","Bombillas"};
    public final static String [] categoria4 = {"Televisores","Monitores TRC","Monitores LCD"};
    public final static String [] categoria5 = {"Impresoras","Telefonos","Celulares","Accessorios de Celular","Routers o Modems","Baterías","Laptops","Computadoras",
    "Teclados","Mouse","Audífonos"};


    public final static String ApiKeyYoutube = "AIzaSyAZN-Mkq2PZl08m6SZF8Ro_qblqb3i4GUg";
    public final static String Calendar = "Calendario";

    public static void CloseVentana(final Dialog dialog, ImageView close){
        close.setOnClickListener(v -> dialog.dismiss());
    }
    public static int SELECT_PICTURE = 300;
    //Subcategorias
    public  final static int Neveras = 1;
    public  final static int Congeladores = 2;
    public  final static int Refrigeradoras = 3;
    public  final static int Microondas = 4;
    public  final static int Lavadoras = 5;
    public  final static int Cafeteras = 6;
    public  final static int Tubos_Fluorescentes = 7;
    public  final static int Bombillas = 8;
    public  final static int Televisores = 9;
    public  final static int Monitores_TRC = 10;
    public  final static int Monitores_LCD = 11;
    public  final static int Impresoras = 12;
    public  final static int Celulares = 14;
    public  final static int Accesorios_Celular = 15;
    public  final static int Routers = 16;
    public  final static int Baterias = 17;
    public  final static int Telefonos = 13;
    public  final static int Laptops = 18;
    public  final static int Computadoras = 19;
    public  final static int Teclados = 20;
    public final static int Audifonos = 22;
    public  final static int Mouse = 21;


    //Referencia a los elementos de la lista
    public final static int PrimerElementoLista = 0;
    public final static int SegundoElementoLista = 1;
    public final static int TerceroElementoLista = 2;
    public final static int CuartoElementoLista = 3;
    public final static int QuintoElementoLista = 4;
    public final static int SextoElementoLista = 5;
    public final static int SeptimoElementoLista = 6;
    public final static int OctavoElementoLista = 7;
    public final static int NovenoElementoLista = 8;
    public final static int DecimoElementoLista = 9;
    public final static int OnceavoElementoLista = 11;

    public final static String URL_Google_Maps = "https://maps.googleapis.com/maps/api/directions/";

    public final static String Farmacias_Boticas = "Farmacia_Botica";
    public final static String Restaurantes = "Restaurante";



    //Opciones más
    public  final static String  []  opciones = {"Otros","Instrucciones","Acerda de TrasTec App","Desvincular Cuenta", "Salir"};
    public final static int Settings = 0;
    public final static int Help = 1;
    public final static int About = 2;
    public final static int Log_Out = 4;
    public final static int Descvincular = 3;

    public final static String Conocimiento = "Conocimiento";
    public final static String Publicacion = "Publicacion";

    //base_url_retrofit_apigrah_facebook
    public final static String Url = "https://graph.facebook.com/";


    //Permitidos
    public final static String [] Permitidos ={"Neveras, Congeladores y otros Refrigerantes","Microondas, lavadoras y Cafeteras",
    "Tubos Fluorescentes y Bombillas","Televisores, Monitores TRC y Monitores LCD","Televisores, Monitores TRC y Monitores LCD",
    "Impresoras, Telefonos, Celulares , Accesorios de Celular, Routers o Modems y Baterias"};
    //NoPermitidos
    public final static String [] NoPermitidos ={"Lavaplatos, Aparatos Medicos de Radioterapioa, cardiologia"+
            "dialisis, Maquinas expendedoras ,Fotocopiadores etc",
    "Taladros, sierras y Máquinas de Coser, Termostatos , Detectores de Humo"+"o reguladores de calor, Cafeteras, trenes y carros electricos, consolas de videojuego",
    "Luminarias, lámparas de descarga e alta intensidad","Cámaras de Video","Aspiradoras, planchas, secadores de pelo"};

    public final static String Reglas = "Cuida tus aparatos electrónicos " +"\n\n" + "Alarga su vida útil"+"\n\n" + "Recurre al mercado de segunda mano" +"\n\n"+
            "RECICLA" + "\n\n" +"Todo articulo que este en muy mal estado porfavor limpiarlo antes de entregarlo" +"\n\n" +"Use guantes en caso de entregar un equipo con" +
            "resiudos de componentes toxicos como el mercurio, Arsénico, Bario, Cadmio, Mercurio, etc ";

    public final static String  About_Info = "TrashTec es una aplicación para fomentar el " +"reciclaje técnologico"
            +", donde podra conseguir descuentos en cualquiera de las empresas afiliadas a la aplicación, a través de puntos "+
            "que conseguiras al realizar una aportación atraves de tus publicaciones";


    //Lista ayuda
    public final static String [] ListaAyuda = {"Realizar publicación","Como conseguir puntos?","Como se realiza el recojo?","Como obtener descuentos?"};

    public final static String [] Instruccionespublicacion ={"Primero abra la aplicación","Luego presione la opción de reciclaje y presione mis publicaciones","Presiona el botón + para agregar una publicación",
    "Llena el formulario","Luego espere a que su publicacion sea Aprobada","Una vez aprobada espera a que recogamos sus aparatos"};

    public final static String [] InstruccionsDescuento = {Instruccionespublicacion[0],"Luego presione opción Tienda",
    "Seleccione los descuentos de una empresa haciendo click en ver descuentos","Aparecera una lista con todos los descuentos disponibles","Presione el boton donde aparece el costo de cada descuento, ojo cea siempre cuantos puntos necesita",
            "Al final se visualizara su lista de descuentos"};
}
