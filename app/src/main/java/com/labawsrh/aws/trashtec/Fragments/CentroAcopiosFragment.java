package com.labawsrh.aws.trashtec.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Adapters.ItemCategoriaMapsAdapter;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.Models.Centro_Acopio;
import com.labawsrh.aws.trashtec.Models.Coodenadas;
import com.labawsrh.aws.trashtec.Parser.DirectionsParser;
import com.labawsrh.aws.trashtec.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CentroAcopiosFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private Main_User_Activity activity;
    private BottomNavigationView navigationView;
    private int MY_PERMISSIONS_READ_CONTACTS;
    private GoogleMap Map;
    private Marker posicion_actual;
    private MapView mapa_centros;
    private List<Coodenadas> lista_coordernadas;
    private double lat = 0.0;
    private double lon = 0.0;
    private DatabaseReference databaseReference = Firebase_Variables.database_reference;
    private BottomSheetDialog bottomSheetDialog;
    private MaterialButton btn_ver_centros_lista;
    private List<String> lista_categorias_maps;
    private ItemCategoriaMapsAdapter adapter;
    private MaterialToolbar toolbar;
    private ListView lista;
    private LatLng coordenadas_actuales;
    private List<String> centros_acopio_encontrados = new ArrayList<>();
    private List<String> id_centros_acopio_encontrados = new ArrayList<>();
    private boolean location_mostrado;
    private boolean avanzo = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (Main_User_Activity) getActivity();
        return inflater.inflate(R.layout.centro_acopio_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navigationView = activity.findViewById(R.id.bottomNavigationView);
        InstanciarViews(view);
        if (!checkIfLocationOpened())
            Toast.makeText(getContext(), "Prenda su Ubicación", Toast.LENGTH_SHORT).show();
        enableBottomBar(false);
        ClickListar();
        CreateBootomSheet();
        bottomSheetDialog.setDismissWithAnimation(true);
    }

    private boolean checkIfLocationOpened() {
        String provider = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        System.out.println("Provider contains=> " + provider);
        return provider.contains("gps") || provider.contains("network");
    }

    private void ClickListar() {
        btn_ver_centros_lista.setOnClickListener(v -> bottomSheetDialog.show());
    }

    private void RestarListBottom() {
        lista_categorias_maps.clear();
        adapter.Tipo_Categoria = -1;
        adapter.Tipo_Menu = 0;
        lista_categorias_maps.addAll(Arrays.asList(Constantes.categorias));
        adapter.notifyDataSetChanged();
        toolbar.setNavigationIcon(R.drawable.list);
    }

    private void CreateBootomSheet() {
        if (bottomSheetDialog == null) {
            lista_categorias_maps.addAll(Arrays.asList(Constantes.categorias));
            View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_dialog, null);
            lista = view.findViewById(R.id.lista_categorias_centros);
            toolbar = view.findViewById(R.id.topAppBar);
            adapter = new ItemCategoriaMapsAdapter(getContext(), R.layout.item_lista_categorias_maps, lista_categorias_maps, 0, -1);
            lista.setAdapter(adapter);
            bottomSheetDialog = new BottomSheetDialog(getContext());
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.setDismissWithAnimation(true);
            bottomSheetDialog.setCanceledOnTouchOutside(true);
            bottomSheetDialog.setOnDismissListener(null);
            bottomSheetDialog.setOnDismissListener(dialog -> RestarListBottom());
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    avanzo = true;
                    if (avanzo)
                        toolbar.setNavigationIcon(R.drawable.back_arrow);
                    GetSubItem(position);
                }

                private void GetSubItem(int position) {
                    if (adapter.Tipo_Menu == 0) {
                        lista_categorias_maps.clear();
                        adapter.Tipo_Menu++;
                        GetData(position);
                        adapter.Tipo_Categoria = position;
                    } else if (adapter.Tipo_Menu == 1) {
                        lista_categorias_maps.clear();
                        adapter.Tipo_Menu++;
                        toolbar.setTitle("Lisa de Centros de Acopio");
                        GetSubItemCategoria(position);
                        GetDataCentrosAcopio(adapter.SubItem);
                    } else if (adapter.Tipo_Menu == 2) {
                        GoCentroAcopio(position);
                    }
                    adapter.notifyDataSetChanged();

                }

                private void GetSubItemCategoria(int position) {
                    switch (adapter.Tipo_Categoria) {
                        case Constantes.Grandes:
                            switch (position) {
                                case Constantes.PrimerElementoLista:
                                    adapter.SubItem = Constantes.Neveras;
                                    break;
                                case Constantes.SegundoElementoLista:
                                    adapter.SubItem = Constantes.Congeladores;
                                    break;
                                case Constantes.TerceroElementoLista:
                                    adapter.SubItem = Constantes.Refrigeradoras;
                                    break;
                            }
                            break;
                        case Constantes.Medianos:
                            switch (position) {
                                case Constantes.PrimerElementoLista:
                                    adapter.SubItem = Constantes.Microondas;
                                    break;
                                case Constantes.SegundoElementoLista:
                                    adapter.SubItem = Constantes.Lavadoras;
                                    break;
                                case Constantes.TerceroElementoLista:
                                    adapter.SubItem = Constantes.Cafeteras;
                                    break;
                            }
                            break;
                        case Constantes.Iluminacion:
                            switch (position) {
                                case Constantes.PrimerElementoLista:
                                    adapter.SubItem = Constantes.Tubos_Fluorescentes;
                                    break;
                                case Constantes.SegundoElementoLista:
                                    adapter.SubItem = Constantes.Bombillas;
                                    break;
                            }
                            break;
                        case Constantes.Pantallas:
                            switch (position) {
                                case Constantes.PrimerElementoLista:
                                    adapter.SubItem = Constantes.Televisores;
                                    break;
                                case Constantes.SegundoElementoLista:
                                    adapter.SubItem = Constantes.Monitores_TRC;
                                    break;
                                case Constantes.TerceroElementoLista:
                                    adapter.SubItem = Constantes.Monitores_LCD;
                                    break;
                            }
                            break;
                        case Constantes.Otros:
                            switch (position) {
                                case Constantes.PrimerElementoLista:
                                    adapter.SubItem = Constantes.Impresoras;
                                    break;
                                case Constantes.SegundoElementoLista:
                                    adapter.SubItem = Constantes.Telefonos;
                                    break;
                                case Constantes.TerceroElementoLista:
                                    adapter.SubItem = Constantes.Celulares;
                                    break;
                                case Constantes.CuartoElementoLista:
                                    adapter.SubItem = Constantes.Accesorios_Celular;
                                    break;
                                case Constantes.QuintoElementoLista:
                                    adapter.SubItem = Constantes.Routers;
                                    break;
                                case Constantes.SextoElementoLista:
                                    adapter.SubItem = Constantes.Baterias;
                                    break;
                                case Constantes.SeptimoElementoLista:
                                    adapter.SubItem = Constantes.Laptops;
                                    break;
                                case Constantes.OctavoElementoLista:
                                    adapter.SubItem = Constantes.Computadoras;
                                    break;
                                case Constantes.NovenoElementoLista:
                                    adapter.SubItem = Constantes.Teclados;
                                    break;
                                case Constantes.DecimoElementoLista:
                                    adapter.SubItem = Constantes.Mouse;
                                    break;
                                case Constantes.OnceavoElementoLista:
                                    adapter.SubItem = Constantes.Audifonos;
                                    break;
                            }
                            break;
                    }
                }
            });
            toolbar.setNavigationOnClickListener(v -> {
                if (avanzo && adapter.Tipo_Menu == 1) {
                    toolbar.setTitle("Lista de Categorias");
                    adapter.Tipo_Menu = 0;
                    RestarListBottom();
                    avanzo = false;
                } else if (adapter.Tipo_Menu == 2) {
                    adapter.Tipo_Menu--;
                    toolbar.setTitle("Lista de Categorias");
                    GetData(adapter.Tipo_Categoria);
                    adapter.notifyDataSetChanged();
                } else
                    bottomSheetDialog.dismiss();

            });
        }
    }

    private void GoCentroAcopio(int position) {
        Fragment fragment = new DetalleCentroFragment(id_centros_acopio_encontrados.get(position),true);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        bottomSheetDialog.dismiss();
    }

    private void GetDataCentrosAcopio(final int subItem) {
        databaseReference.child("Centros_Acopio").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                id_centros_acopio_encontrados.clear();
                centros_acopio_encontrados.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Centro_Acopio centro_acopio = data.getValue(Centro_Acopio.class);
                    for (String Key : centro_acopio.Categorias.keySet()) {
                        if (subItem == centro_acopio.Categorias.get(Key).subcategoria) {
                            centros_acopio_encontrados.add(centro_acopio.Empresa + " Direccion: " + centro_acopio.Direccion);
                            id_centros_acopio_encontrados.add(centro_acopio.Id);
                            break;
                        }
                    }
                }
                lista_categorias_maps.addAll(centros_acopio_encontrados);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "No se pudo Obtener la Información", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetData(int position) {
        lista_categorias_maps.clear();
        switch (position) {
            case Constantes.Grandes:
                lista_categorias_maps.addAll(Arrays.asList(Constantes.categoria1));
                break;
            case Constantes.Medianos:
                lista_categorias_maps.addAll(Arrays.asList(Constantes.categoria2));
                break;
            case Constantes.Iluminacion:
                lista_categorias_maps.addAll(Arrays.asList(Constantes.categoria3));
                break;
            case Constantes.Pantallas:
                lista_categorias_maps.addAll(Arrays.asList(Constantes.categoria4));
                break;
            case Constantes.Otros:
                lista_categorias_maps.addAll(Arrays.asList(Constantes.categoria5));
                break;
        }
    }


    private void enableBottomBar(boolean enable) {
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setEnabled(enable);
        }

    }

    private void InstanciarViews(View view) {
        mapa_centros = view.findViewById(R.id.mapa_centros);
        btn_ver_centros_lista = view.findViewById(R.id.btn_ver_centros_lista);
        lista_categorias_maps = new ArrayList<>();
        lista_coordernadas = new ArrayList<>();
        if (mapa_centros != null) {
            mapa_centros.onCreate(null);
            mapa_centros.onResume();
            mapa_centros.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(Objects.requireNonNull(getContext()));
        Map = googleMap;
        Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Map.setMyLocationEnabled(true);
        Map.getUiSettings().setMyLocationButtonEnabled(true);
        Map.getUiSettings().setCompassEnabled(true);
        Map.getUiSettings().setZoomGesturesEnabled(true);
        Map.getUiSettings().setScrollGesturesEnabled(true);
        Map.getUiSettings().setTiltGesturesEnabled(true);
        Map.getUiSettings().setMapToolbarEnabled(false);
        Map.getUiSettings().setRotateGesturesEnabled(true);
        Map.setIndoorEnabled(true);
        Map.getUiSettings().setAllGesturesEnabled(true);
        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.google_style);
        Map.setMapStyle(mapStyleOptions);
        MiUbicacion();
        GetDataUbicacionPuntosAcopio();
        Map.setOnInfoWindowClickListener(this);

    }

    private void GetDataUbicacionPuntosAcopio() {
        databaseReference.child("Posicions_Centro_Acopio").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    lista_coordernadas.add(data.getValue(Coodenadas.class));
                }
                AgregarMarquer(lista_coordernadas);
                Handler handle = new Handler();
                handle.postDelayed(() -> enableBottomBar(true), 2000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "No se pudo Obtener la Informacion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AgregarMarquer(List<Coodenadas> lista_coordernadas) {
        //Map.setInfoWindowAdapter(new CustomInfoCentroAcoptioAdapter(getContext()));
        for (Coodenadas coordenada : lista_coordernadas) {
            final LatLng coordenadas = new LatLng(coordenada.Latitud, coordenada.Longitud);
            String Id = coordenada.Id;
            databaseReference.child("Centros_Acopio").child(Id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Centro_Acopio centro_acopio = dataSnapshot.getValue(Centro_Acopio.class);
                    Marker marker;
                    marker = Map.addMarker(new MarkerOptions().position(coordenadas).title("Centro de Acopio de la Empresa " + centro_acopio.Empresa)
                            .snippet("Direccion:" + centro_acopio.Direccion).
                                    icon(bitmapDescriptorFromVector(getContext(), R.drawable.icon_basura_map)));
                    marker.setTag(centro_acopio.Id);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "No se pudo Obtener la Información", Toast.LENGTH_SHORT).show();
                }
            });
            if (checkIfLocationOpened()) {
                location_mostrado = true;
                coordenadas_actuales = new LatLng(lat, lon);
                //Map.addPolyline(new PolylineOptions().add(coordenadas_actuales,coordenadas).width(15).color(R.color.gradient_start_color));
                String url = GetUrl(coordenadas_actuales, coordenadas);
                TaskRutas taskrutas = new TaskRutas();
                taskrutas.execute(url);
            }
        }
    }

    private void AgregatRutas(LatLng coordenadas_actuales,List<Coodenadas> coodenadas_list) {
        for (Coodenadas coordenada : coodenadas_list) {
            LatLng coordenadas = new LatLng(coordenada.Latitud, coordenada.Longitud);
            String url = "";
            if (coordenadas_actuales != null)
                url = GetUrl(coordenadas_actuales, coordenadas);
            else {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
                }
                LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                coordenadas_actuales = new LatLng(location.getLatitude(),location.getLongitude());
                url = GetUrl(coordenadas_actuales, coordenadas);
            }
            TaskRutas taskrutas = new TaskRutas();
            taskrutas.execute(url);
            location_mostrado = true;
        }

    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.icon_basura_map);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() - 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    public void EstablecerPosicionActual(double lat, double longitud) {
        LatLng coordenada = new LatLng(lat, longitud);
        CameraUpdate miubicacion = CameraUpdateFactory.newLatLngZoom(coordenada, 16);
        if (posicion_actual != null) posicion_actual.remove();
        posicion_actual = Map.addMarker(new MarkerOptions().position(coordenada).title("Mi Ubicación"));
        Map.animateCamera(miubicacion);
    }

    public void ActualizaPosicion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            EstablecerPosicionActual(lat, lon);
        }
    }

    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(checkIfLocationOpened()&&!location_mostrado) {
                ActualizaPosicion(location);
                AgregatRutas(coordenadas_actuales,lista_coordernadas);
            }
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }
        @Override
        public void onProviderEnabled(String provider) { }
        @Override
        public void onProviderDisabled(String provider) { }
    };

    private void MiUbicacion() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        ActualizaPosicion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,60000,0,listener);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getSnippet()!=null) {
            DetalleCentroFragment fragment = new DetalleCentroFragment(Objects.requireNonNull(marker.getTag()).toString(),true);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
        }
    }
    public class TaskRutas extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try{
                responseString = RequestDirection(strings[0]);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }
    public class TaskParser extends AsyncTask<String,Void,List<List<HashMap<String,String>>>>{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String,String>>> routes = null;
            try{
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            ArrayList points = null;
            PolylineOptions polylineOptions = null;
            for(List<HashMap<String,String>> path:lists){
                points = new ArrayList();
                polylineOptions = new PolylineOptions();
                for(HashMap<String,String> point:path){
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));
                    points.add(new LatLng(lat,lon));
                }
                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(R.color.gradient_start_color);
                polylineOptions.geodesic(true);
            }
            if(polylineOptions!= null){
                Map.addPolyline(polylineOptions);
            }else
                Toast.makeText(getContext(),"No se pudo obtener la información compruebe su internet y active su GPS",Toast.LENGTH_SHORT).show();
        }
    }
    public static String GetUrl(LatLng origen , LatLng destino){
        return "https://maps.googleapis.com/maps/api/directions/json?origin="+origen.latitude+"%2C"+origen.longitude+"&destination="+destino.latitude+"%2C"+
                destino.longitude+"&sensor=false&mode=driving&key=AIzaSyAZN-Mkq2PZl08m6SZF8Ro_qblqb3i4GUg";
    }
    public String RequestDirection(String requestUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(requestUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get Response
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }
            responseString = stringBuffer.toString();
            bufferedReader.close();
            reader.close();

        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(inputStream != null){
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }
}
