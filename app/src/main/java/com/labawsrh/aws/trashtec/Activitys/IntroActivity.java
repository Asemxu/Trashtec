package com.labawsrh.aws.trashtec.Activitys;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.labawsrh.aws.trashtec.Adapters.IntroViewPagerAdapter;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.R;
import com.labawsrh.aws.trashtec.Models.ScreenItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    private IntroViewPagerAdapter introViewPagerAdapter ;
    private TabLayout tabIndicator;
    private Button btnNext;
    private int position = 0 ;
    private Button btnGetStarted;
    private DatabaseReference reference = Firebase_Variables.database_reference;
    private Animation btnAnim ;
    private TextView tvSkip;
    ProgressDialog progress;
    //private CheckBox no_mostar;
    private  List<ScreenItem> mList;
    private FirebaseAuth  auth = Firebase_Variables.firebaseauth;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // when this activity is about to be launch we need to check if its openened before or not
       /*if (restorePrefData()) {
            Intent mainActivity = new Intent(getApplicationContext(), LoginActivity.class );
            startActivity(mainActivity);
            finish();
        }*/
        setContentView(R.layout.activity_intro);
        // hide the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        // ini views
        InicializarObjecs();
        Timer timer = new Timer();
        timer.schedule(new IntroActivity.MyTimerTask(), 8000, 8000);
        // fill list screen
        screenPager =findViewById(R.id.screen_viewpager);
        GetDataIntro();
        // setup tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager);
        // next button click Listner
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if (position < mList.size()) {
                    position++;
                    screenPager.setCurrentItem(position);
                    loadScreen();
                }
                if (position == mList.size()-1) { // when we rech to the last screen
                    loaddLastScreen();
                    btnGetStarted.setAnimation(btnAnim);
                }
            }
        });
        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size()-1) {
                    loaddLastScreen();
                    btnGetStarted.setAnimation(btnAnim);
                }
                else
                    loadScreen();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        // Get Started button click listener
        btnGetStarted.setOnClickListener(v -> {
            //open main activity
           ValidarAuth();
        });
        // skip button click listener
        tvSkip.setOnClickListener(v -> screenPager.setCurrentItem(mList.size()));

    }

    private void GetDataIntro() {
        progress = ProgressDialog.show(IntroActivity.this,"TrashTec","Cargando......",false,false);
        reference.child("ScreenItems").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot_first) {
                final List<ScreenItem> screenItems = new ArrayList<>();
                int posicion = 1;
                 int cantidad = (int) dataSnapshot_first.getChildrenCount();
                 int cantidad_repeticiones = 0;
                for(DataSnapshot data:dataSnapshot_first.getChildren()) {
                    ScreenItem item = data.getValue(ScreenItem.class);
                    assert item != null;
                    if(item.posicion_actual>=cantidad)
                        item.posicion_actual=0;
                    if(posicion >= item.posicion_actual&&cantidad_repeticiones<3){
                        screenItems.add(data.getValue(ScreenItem.class));
                        cantidad_repeticiones++;
                    }
                    if(cantidad_repeticiones==3)
                        break;
                    posicion++;
                    for(int i=1;i<=cantidad;i++){
                        reference.child("ScreenItems").child("Id_"+i).child("posicion_actual").setValue(posicion);
                    }
                }
                screenItems.addAll(GetList(new ArrayList<ScreenItem>()));
                introViewPagerAdapter = new IntroViewPagerAdapter(getApplicationContext(),screenItems);
                screenPager.setAdapter(introViewPagerAdapter);
                mList=screenItems;
                progress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Algo paso",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ValidarAuth() {
        if(auth.getCurrentUser()!=null){
           MainUserActivity(auth.getCurrentUser());
        }else{
            LoginActivity();
        }
        finish();
    }

    private void MainUserActivity(FirebaseUser user) {
        reference.child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Intent mainuserActivity = new Intent(getApplicationContext(),Main_User_Activity.class);
                startActivity(mainuserActivity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Algo Paso",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void LoginActivity(){
        Intent mainActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(mainActivity);
    }

    private List<ScreenItem> GetList(List<ScreenItem> mList) {
        mList.add(new ScreenItem("Centros de Acopio","Ve a los centros de acopio en diferentes puntos especificados cerca tuyo","https://cdn.icon-icons.com/icons2/1551/PNG/512/if-traveling-icon-flat-outline09-3405110_107375.png"));
        mList.add(new ScreenItem("Acumula Puntos","Recicla tus aparatos para que los recojamos y asi gana puntos para canjearlos por descuentos en la tienda","https://cdn.icon-icons.com/icons2/2069/PNG/512/coins_copy_finance_icon_125514.png"));
        mList.add(new ScreenItem("Trash Tec Facil de Usar","Esta Aplicacion te ayudara a desarrollar y fomentar buenos habitos de reciclaje tecnologico","https://cdn.icon-icons.com/icons2/2102/PNG/512/recycling_recycle_bin_sorting_waste_eco_icon_129013.png"));
        return mList;
    }

    private void InicializarObjecs() {
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        tvSkip = findViewById(R.id.tv_skip);
       // no_mostar= findViewById(R.id.select_no_mostrar);
        mList = new ArrayList<>();
        //no_mostar.setVisibility(View.INVISIBLE);
    }

    private void loadScreen() {
        btnNext.setVisibility(View.VISIBLE);
        btnGetStarted.setVisibility(View.INVISIBLE);
        tvSkip.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.VISIBLE);
        // setup animation
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        return pref.getBoolean("isIntroOpnend",false);
    }
    /*private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.apply();
    }*/
    // show the GETSTARTED Button and hide the indicator and the next button
    private void loaddLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        // TODO : ADD an animation the getstarted button
        // setup animation
    }
    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            IntroActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(screenPager.getCurrentItem()==0){
                        screenPager.setCurrentItem(1,true);
                        //viewPager.setCurrentItem(1, true);
                    }else if(screenPager.getCurrentItem()==1){
                        screenPager.setCurrentItem(2,true);
                        //viewPager.setCurrentItem(2, true);
                    }else if(screenPager.getCurrentItem()==2){
                        screenPager.setCurrentItem(3,true);
                        //viewPager.setCurrentItem(0, true);
                    }else if(screenPager.getCurrentItem()==3){
                        if(mList.size()==5)
                            screenPager.setCurrentItem(0,true);
                        else
                            screenPager.setCurrentItem(4,true);
                    }else if(mList.size()==6){
                        if(screenPager.getCurrentItem()==4)
                            screenPager.setCurrentItem(0,true);
                    }

                }
            });
        }
    }
}
