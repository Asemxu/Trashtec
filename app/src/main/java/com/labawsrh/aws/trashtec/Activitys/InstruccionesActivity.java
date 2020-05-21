package com.labawsrh.aws.trashtec.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.labawsrh.aws.trashtec.Adapters.InstruccionesAdapter;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.R;


public class InstruccionesActivity extends AppCompatActivity {

    private ViewPager screenPager;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0 ;
    TextView tvSkip;
    private int tipo ;
    private InstruccionesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_instrucciones);
        // hide the action bar

        getSupportActionBar().hide();

        // ini views
        btnNext = findViewById(R.id.btn_siguiente);
        tabIndicator = findViewById(R.id.indicator_instrucciones);
        tvSkip = findViewById(R.id.saltar_instrucciones);
        tipo = getIntent().getExtras().getInt("Tipo");
        screenPager =findViewById(R.id.pager_instrucciones);
        int [] lista = {R.drawable.publicacion_1,R.drawable.publicacion_2,R.drawable.publicacion3,R.drawable.publicacion4,
                R.drawable.publicacion5,R.drawable.publicacion6};
        int [] lista2 ={R.drawable.publicacion_1,R.drawable.publicacion_2,R.drawable.publicacion_2_2,R.drawable.publicacion_2_3,
            R.drawable.publicacion_2_4,R.drawable.publicacion_2_5};
        if(tipo == 0){
            adapter = new InstruccionesAdapter(this,lista, Constantes.Instruccionespublicacion);
        }else{
            adapter = new InstruccionesAdapter(this,lista2,Constantes.InstruccionsDescuento);
        }
        screenPager.setAdapter(adapter);
        Regresar();

        tabIndicator.setupWithViewPager(screenPager);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Regresar();
    }

    private void Regresar() {
        tvSkip.setOnClickListener(v->{
            Intent  main = new Intent(InstruccionesActivity.this,Main_User_Activity.class);
            startActivity(main);
        });
    }

}
