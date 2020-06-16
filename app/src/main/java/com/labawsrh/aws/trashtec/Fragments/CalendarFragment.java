package com.labawsrh.aws.trashtec.Fragments;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Adapters.Centros_Acopio_Adapter;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.Clases_Helper.Fecha;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.Models.Centro_Acopio;
import com.labawsrh.aws.trashtec.Models.Mes;
import com.labawsrh.aws.trashtec.Models.Recojo;
import com.labawsrh.aws.trashtec.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.util.Calendar.*;

public class CalendarFragment extends Fragment {
    private Main_User_Activity activity;
    private CalendarView calendario;
    private DatabaseReference databaseReference = Firebase_Variables.database_reference;
    private String [] meses = Constantes.meses;
    private String [] dias = Constantes.dias;
    private List<EventDay> events = new ArrayList<>();
    private BottomNavigationView navigationView;
    private TextView dias_next;
    private SwipeRefreshLayout refreshLayout;
    private FirebaseAuth auth = Firebase_Variables.firebaseauth;
    Calendar calendar = getInstance();
    Date fecha = calendar.getTime();
    String mes_actual = meses[fecha.getMonth()];
    String dia_actual  = dias[fecha.getDay()];
    private CardView btn_show_more_centros_acopio;
    private boolean encontro_fecha_next = false;
    private List<Centro_Acopio> centro_acopios;
    private Centros_Acopio_Adapter centros_acopio_adapter;
    private ListView lista_acopio;
    private TextView texto_more_centros;
    private ImageView imagen_more_centros;
    int cantidad_mostrado = 0;
    int cantidad_total;
    boolean desplegadototal = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity=(Main_User_Activity) getActivity();
        return inflater.inflate(R.layout.calendar_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InstanaciarViews(view);
        navigationView = activity.findViewById(R.id.bottomNavigationView);
        enableBottomBar(false);
        GetDataMes(mes_actual);
        GetDataAcopio(dia_actual,centro_acopios);
        ClickCalendario();
        RefreshLayout();
        ClickShowMore();
        lista_acopio.setOnItemClickListener((parent, view1, position, id) -> {
            activity.escucha = false;
            navigationView.setOnNavigationItemSelectedListener(null);
            navigationView.setSelectedItemId(R.id.Points);
            Fragment fragment = new DetalleCentroFragment(centro_acopios.get(position).Id,false);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
        });
    }

    private void GetDataRecojos(List<EventDay>eventos, List<Calendar> calendars) {
        databaseReference.child("Recojos_Usuario").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()||dataSnapshot.getChildrenCount()>0){
                    for(DataSnapshot data:dataSnapshot.getChildren()) {
                        Recojo recojo = data.getValue(Recojo.class);
                        int year = Integer.parseInt(Fecha.GetYear(recojo.Fecha));
                        int month = Integer.parseInt(Fecha.GetMonth(recojo.Fecha));
                        int day = Integer.parseInt(Fecha.GetDay(recojo.Fecha));
                        String fecha_recojo = year+"-"+(month-1)+"-"+day;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month - 1, day);
                        int i =0;
                        for(Calendar calendario:calendars){
                            Date fecha = calendario.getTime();
                            int year_obtenido = fecha.getYear()+1900;
                            int month_obtenido = fecha.getMonth();
                            int day_obtenido = fecha.getDate();
                            String fecha_obtenida = year_obtenido+"-"+month_obtenido+"-"+day_obtenido;
                            if(fecha_obtenida.equals(fecha_recojo)){
                                eventos.remove(i);
                                Calendar calendario_actual = Calendar.getInstance();
                                Date fecha_actual_calendario = calendario_actual.getTime();
                                int year_actual = fecha_actual_calendario.getYear()+1900;
                                int month_actual = fecha_actual_calendario.getMonth();
                                int day_actual = fecha_actual_calendario.getDate();
                                String fecha_actual = year_actual + "-" + month_actual + "-" + day_actual;
                                if(fecha_actual.equals(fecha_recojo)){
                                    eventos.add(i,new EventDay(calendar,R.drawable.reciclo_ahora));
                                }else
                                    eventos.add(i,new EventDay(calendar,R.drawable.reciclo));
                                break;
                            }else{
                                eventos.add(new EventDay(calendar,R.drawable.check));
                            }
                            i++;
                        }
                    }
                }
                calendario.setEvents(eventos);
                calendario.setSelectedDates(calendars);
                Handler handler = new Handler();
                handler.postDelayed(() -> enableBottomBar(true),1000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"No se pudo Obtener la información",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ClickShowMore() {
        btn_show_more_centros_acopio.setOnClickListener(v -> {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lista_acopio.getLayoutParams();
            if (!desplegadototal) {
                if (cantidad_total >= cantidad_mostrado) {
                    lp.height = lp.height * 2 + 20;
                    lista_acopio.setLayoutParams(lp);
                    cantidad_total -= 3;
                    cantidad_mostrado += 3;
                } else if (cantidad_total < 3 && cantidad_total != 0) {
                    switch (cantidad_total) {
                        case 2:
                            lp.height = lp.height + 680;
                            cantidad_total -= 2;
                            cantidad_total += 2;
                            break;
                        case 1:
                            lp.height = lp.height + 350;
                            cantidad_total -= 1;
                            cantidad_total += 1;
                            break;
                    }
                    lista_acopio.setLayoutParams(lp);
                    texto_more_centros.setText("Mostrar menos centros de acopio");
                    imagen_more_centros.setImageDrawable(getResources().getDrawable(R.drawable.less));
                    desplegadototal = true;
                } else if (cantidad_total == 0) {
                    texto_more_centros.setText("Mostrar menos centros de acopio");
                    imagen_more_centros.setImageDrawable(getResources().getDrawable(R.drawable.less));
                    desplegadototal= true;
                }
            } else {
                RefresLista();
                desplegadototal = false;
            }
        });
    }
    private void GetDataAcopio(final String dia_actual,final List<Centro_Acopio> centro_acopios) {
        databaseReference.child("Centros_Acopio").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Centro_Acopio acopio = data.getValue(Centro_Acopio.class);
                    centro_acopios.add(acopio);
                }
                centros_acopio_adapter = new Centros_Acopio_Adapter(Objects.requireNonNull(getContext()),dia_actual,R.layout.centro_acopio_item_calendar,centro_acopios,Constantes.Calendar);
                lista_acopio.setAdapter(centros_acopio_adapter);
                cantidad_total = centro_acopios.size()-3;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"No se Puedo Obtener la informacion",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void RefreshLayout() {
        refreshLayout.setOnRefreshListener(() -> {
            GetDataMes(mes_actual);
            GetDataAcopio(dia_actual, new ArrayList<>());
            RefresLista();
            new Handler().postDelayed(() -> refreshLayout.setRefreshing(false),3000);
        });
    }

    private void RefresLista() {
        cantidad_total = centro_acopios.size()-3;
        cantidad_mostrado = 0;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lista_acopio.getLayoutParams();
        lp.height = 760;
        lista_acopio.setLayoutParams(lp);
        texto_more_centros.setText("Mostrar más centros de acopio");
        imagen_more_centros.setImageDrawable(getResources().getDrawable(R.drawable.more_plus));
    }

    private void GetDataMes(String mes_actual) {
        final List<Calendar> calendars = new ArrayList<>();
        events.clear();
        databaseReference.child("Fechas_Recoleccion").child(mes_actual).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()||dataSnapshot.getChildrenCount()!=0) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Mes data_mes = data.getValue(Mes.class);
                        Calendar calendar = Calendar.getInstance();
                        Date fecha_actual = calendar.getTime();
                        int year = Integer.parseInt(Fecha.GetYear(data_mes.Fecha));
                        int month = Integer.parseInt(Fecha.GetMonth(data_mes.Fecha));
                        int day = Integer.parseInt(Fecha.GetDay(data_mes.Fecha));
                        calendar.set(year, month - 1, day);
                        Date fecha_obtenida = new Date();
                        fecha_obtenida.setYear(year - 1900);
                        fecha_obtenida.setMonth(month - 1);
                        fecha_obtenida.setDate(day);
                        if (fecha_actual.compareTo(fecha_obtenida) > 0)
                            events.add(new EventDay(calendar, R.drawable.icon_basura));
                        else {
                            events.add(new EventDay(calendar, R.drawable.transport));
                            if (!encontro_fecha_next) {
                                int dias_diferencia = (int) ((fecha_obtenida.getTime() - fecha_actual.getTime()) / 86400000);
                                if (dias_diferencia == 1)
                                    dias_next.setText("Mañana");
                                else if (dias_diferencia != 0)
                                    dias_next.setText("En " + dias_diferencia + " dias");
                                else
                                    dias_next.setText("Hoy");
                                encontro_fecha_next = true;
                            }
                        }
                        calendars.add(calendar);
                    }
                    GetDataRecojos(events,calendars);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"No se pudo Obtener la Informacion",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ClickCalendario() {
        calendario.setOnPreviousPageChangeListener(() -> {
            Calendar  calendario_adelante = calendario.getCurrentPageDate();
            Date fecha_page = calendario_adelante.getTime();
            GetDataMes(meses[fecha_page.getMonth()]);            });
        calendario.setOnForwardPageChangeListener(() -> {
            Calendar  calendario_atras = calendario.getCurrentPageDate();
            Date fecha_page = calendario_atras.getTime();
            GetDataMes(meses[fecha_page.getMonth()]);
        });
    }
    private void enableBottomBar(boolean enable){
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setEnabled(enable);
        }
    }
    private void InstanaciarViews(View view) {
        calendario = view.findViewById(R.id.calendario);
        dias_next = view.findViewById(R.id.dia_recojo);
        centro_acopios = new ArrayList<>();
        refreshLayout = view.findViewById(R.id.refresh_calendar);
        lista_acopio = view.findViewById(R.id.lista_centros);
        btn_show_more_centros_acopio = view.findViewById(R.id.btn_show_more_centros_calendar);
        texto_more_centros = view.findViewById(R.id.texto_more_centros);
        imagen_more_centros = view.findViewById(R.id.imagen_more_centros);
    }
}
