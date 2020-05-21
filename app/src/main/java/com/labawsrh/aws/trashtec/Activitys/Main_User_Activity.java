package com.labawsrh.aws.trashtec.Activitys;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.labawsrh.aws.trashtec.Adapters.SettigsAdapter;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.Firebase.Autentificacion.Autentificacion;
import com.labawsrh.aws.trashtec.Fragments.AyudaFragment;
import com.labawsrh.aws.trashtec.Fragments.CalendarFragment;
import com.labawsrh.aws.trashtec.Fragments.CentroAcopiosFragment;
import com.labawsrh.aws.trashtec.Fragments.HomeFragment;
import com.labawsrh.aws.trashtec.Fragments.ReciclajeFragment;
import com.labawsrh.aws.trashtec.Fragments.SettingsFragment;
import com.labawsrh.aws.trashtec.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main_User_Activity extends AppCompatActivity {

    private Autentificacion autentificacion;
    private FirebaseAuth auth = Firebase_Variables.firebaseauth;
    private BottomSheetDialog bottomSheetDialog;
    private BottomNavigationView navigationView;
    private CircleImageView imagen_perfil;
    private TextView nombre_apellidos;
    private Fragment fragment;
    private TextView correo;
    private TextView points;
    private DatabaseReference databaseReference = Firebase_Variables.database_reference;
    private int select_id_actual;
    private boolean primera_vez = false;
    private int cantidad_retrocesos = 0;
    private List<String> opciones;
    public boolean escucha = false;
    public ListView lista_opciones;
    public SettigsAdapter setAdapter;
    private MaterialAlertDialogBuilder dialog_material;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main__user_);
        InstanciarViews();
        navigationView.setOnNavigationItemSelectedListener(navlistener);
        select_id_actual=navigationView.getSelectedItemId();
        MenuItem item = navigationView.getMenu().findItem(select_id_actual);
        GetFragment(item);
        CargarPuntos();
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.default_web_client_id)).
                requestEmail().
                build();

        GetDataUser(options);
    }

    private void GetDataUser(GoogleSignInOptions options) {
        databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Tipo_cuenta").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean tipo_cuento =dataSnapshot.getValue(Boolean.class);
                Log.i("Data",""+tipo_cuento);
                if(tipo_cuento){
                    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                    if(account!=null)
                        CreateBootomSheet(account,options,true);
                }else{
                    CreateBootomSheet(null,options,false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"No se pudo Obtener su Informacion de su Cuenta",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ClickSettingsOptions(GoogleSignInOptions options) {
        lista_opciones.setOnItemClickListener((parent, view, position, id) -> {
            GetAccionItem(position,options);
        });
    }

    private void GetAccionItem(int position,GoogleSignInOptions options) {
        switch (position) {
            case Constantes.Settings:
                databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("acepta_notificaciones").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        fragment  = new SettingsFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                        bottomSheetDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"No se pudo Obtener su Información",Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case Constantes.Help:
                fragment = new AyudaFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                break;
            case Constantes.About:
                GetDialog();
                break;
            case Constantes.Descvincular:
                Deslogearse(options,true);
                break;
            case Constantes.Log_Out:
                Deslogearse(options,false);
                break;
        }
        bottomSheetDialog.dismiss();
    }

    private void GetDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_about_app);
        TextView contenido = dialog.findViewById(R.id.texto_about);
        ImageView close_about = dialog.findViewById(R.id.btn_close_about);
        contenido.setText(Constantes.About_Info);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Constantes.CloseVentana(dialog,close_about);
        dialog.show();
    }

    private void Deslogearse(GoogleSignInOptions options,boolean tipo_deslogeo) {
        Context context = new ContextThemeWrapper(this,R.style.Theme_MaterialComponents);
        dialog_material = new MaterialAlertDialogBuilder(context);
        dialog_material.setTitle("Aviso");
        dialog_material.setIcon(R.drawable.warning);
        if(!tipo_deslogeo)
            dialog_material.setMessage("Desea Salir de la Aplicación");
        else
            dialog_material.setMessage("Desea Descvincular la Aplicación de su Cuenta?");
        dialog_material.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        dialog_material.setPositiveButton("Aceptar", (dialog, which) -> {
            autentificacion = new Autentificacion(auth,getApplicationContext(),this);
            databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Tipo_cuenta").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean tipo_cuenta = dataSnapshot.getValue(Boolean.class);
                    if(tipo_cuenta) {
                        if (!tipo_deslogeo)
                            autentificacion.SignOutGoogle(false, options);
                        else
                            autentificacion.SignOutGoogle(true, options);
                    }else{
                        if(!tipo_deslogeo){
                            autentificacion.SignOutFacebook(false);
                        }else
                            autentificacion.SignOutFacebook(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),"No se pudo Obtener el tipo de cuenta",Toast.LENGTH_SHORT).show();
                }
            });
        });
        dialog_material.show();
    }

    private void CreateBootomSheet(GoogleSignInAccount account,GoogleSignInOptions options,boolean tipo_cuenta) {
        if (bottomSheetDialog == null) {
            opciones.addAll(Arrays.asList(Constantes.opciones));
            View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_mas, null);
            lista_opciones = view.findViewById(R.id.lista_opciones);
            imagen_perfil = view.findViewById(R.id.imagen_perfil);
            nombre_apellidos = view.findViewById(R.id.nombres_cuenta);
            correo = view.findViewById(R.id.correo_cuenta);
            if(tipo_cuenta) {
                nombre_apellidos.setText(account.getDisplayName());
                correo.setText(account.getEmail());
                Picasso.get().load(account.getPhotoUrl().toString()).into(imagen_perfil);
            }else{
                FirebaseUser user = auth.getCurrentUser();
                nombre_apellidos.setText(user.getDisplayName());
                correo.setText(user.getEmail());
                Picasso.get().load(user.getPhotoUrl()+"?type=large").into(imagen_perfil);
            }
            setAdapter = new SettigsAdapter(getApplicationContext(), R.layout.item_settings_adapter,opciones);
            lista_opciones.setAdapter(setAdapter);
            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.setDismissWithAnimation(true);
            bottomSheetDialog.setCanceledOnTouchOutside(true);
            bottomSheetDialog.setOnDismissListener(null);
        }
        ClickSettingsOptions(options);
    }
    private void CargarPuntos() {
        databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("Cantidad_points").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int cantidad_puntos = dataSnapshot.getValue(Integer.class);
                    points.setText(""+cantidad_puntos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"No se pudo obtener los Coins",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public BottomNavigationView.OnNavigationItemSelectedListener navlistener = item -> {
        GetFragment(item);
        return true;
    };

    @Override
    public void onBackPressed() {
        if (cantidad_retrocesos == 0) {
            Toast.makeText(getApplicationContext(), "Retroceda una vez más para Salir", Toast.LENGTH_SHORT).show();
            cantidad_retrocesos++;
        } else {
            super.onBackPressed();
        }
        new CountDownTimer(2000,1000){
            @Override
            public void onTick(long millisUntilFinished) { }
            @Override
            public void onFinish() {
                cantidad_retrocesos=0;
            }
        }.start();
    }

    private void GetFragment(MenuItem item) {
        Fragment select_fragment;
        switch (item.getItemId()){
            case R.id.home:
                if(select_id_actual!=item.getItemId() || !primera_vez||escucha){
                    select_fragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, select_fragment).commit();
                    select_id_actual=item.getItemId();
                    primera_vez=true;
                }
                break;
            case R.id.calendar:
                if(select_id_actual!=item.getItemId()|| !primera_vez || escucha){
                    select_fragment = new CalendarFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, select_fragment).commit();
                    select_id_actual=item.getItemId();

                }
                break;
            case R.id.Points:
                if(select_id_actual!=item.getItemId()|| !primera_vez || escucha){
                    select_fragment = new CentroAcopiosFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, select_fragment).commit();
                    select_id_actual=item.getItemId();
                }
                break;
            case R.id.recicla:
                if(select_id_actual!=item.getItemId()|| !primera_vez || escucha){
                    select_fragment = new ReciclajeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, select_fragment).commit();
                    select_id_actual=item.getItemId();
                }
                break;
            case R.id.more:
                bottomSheetDialog.show();
                break;
        }
    }
    public void InstanciarViews(){
        navigationView = findViewById(R.id.bottomNavigationView);
        autentificacion = new Autentificacion(null,this,getApplicationContext(),auth,null);
        points = findViewById(R.id.cantidad_puntos);
        opciones = new ArrayList<>();
    }

}
