package com.labawsrh.aws.trashtec.Firebase.Autentificacion;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.labawsrh.aws.trashtec.Activitys.LoginActivity;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.Firebase.Database.Firebase;
import com.labawsrh.aws.trashtec.Interfaces.ApiGraphFacebookServices;
import com.labawsrh.aws.trashtec.Validation.Validation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Autentificacion {

    private FirebaseAuth auth;
    private Firebase firebase;
    private Task<GoogleSignInAccount> task;
    private DatabaseReference databaseReference = Firebase_Variables.database_reference;
    private Context context;
    private Validation validation;
    private LoginActivity loginActivity;
    private  Main_User_Activity main_user_activity;
    public boolean Tipo_cuenta;
    public Autentificacion(LoginActivity loginActivity,Context context){
        this.loginActivity=loginActivity;
        this.context = context;
    }
    public Autentificacion(LoginActivity loginActivity,Main_User_Activity main_user_activity,Context context,
                           FirebaseAuth auth, Task<GoogleSignInAccount> task){
        this.context=context;
        this.loginActivity=loginActivity;
        this.main_user_activity=main_user_activity;
        this.auth=auth;
        this.task=task;
    }
    public Autentificacion(FirebaseAuth auth , Context context,Main_User_Activity activity){
        this.auth = auth;
        this.context = context;
        this.main_user_activity = activity;
    }
    public void SigInAutentifacion(ProgressDialog dialog){
        try{
            GoogleSignInAccount account = task.getResult(ApiException.class);
            FirebaseGoogleAuth(account,dialog);
        }catch(ApiException ex){
            FirebaseGoogleAuth(null,dialog);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount account, ProgressDialog dialog) {
        if(account!=null){
            AuthCredential authcredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
            auth.signInWithCredential(authcredential).addOnCompleteListener(loginActivity, task -> {
                if(task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    UpdateUI(user);
                    dialog.dismiss();
                }else{
                    Toast.makeText(context,"Login Cancelado "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    UpdateUI(null);
                    dialog.dismiss();
                }
            });
        }else{
            dialog.dismiss();
            Toast.makeText(context,"Login Cancelado",Toast.LENGTH_SHORT).show();
        }

    }

    public void UpdateUI(FirebaseUser user) {
        if(user!=null) {
            firebase = new Firebase(databaseReference,context,loginActivity);
            validation = new Validation(user,firebase,Tipo_cuenta);
            validation.EsValidoLogin();
        }
    }

    public void SignOutFacebook(boolean Tipo_desloge) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        IrLoginActivity();
        /*
        if (!Tipo_desloge) {
            IrLoginActivity();
        }else{
            AccessToken token = AccessToken.getCurrentAccessToken();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Constantes.Url+token.getUserId()+"/").
                    addConverterFactory(GsonConverterFactory.create()).build();
            ApiGraphFacebookServices services = retrofit.create(ApiGraphFacebookServices.class);
            Call<Boolean> call = services.RevocarAccesoFacebook(token.getToken());
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Log.i("Informacion",""+response.body());
                    IrLoginActivity();
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.i("Informacion","Fallo");
                    IrLoginActivity();
                }
            });*
            /*Call<Data>  call = services.GetPermisos(token.getToken());
            call.enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call, Response<Data> response) {
                    Data data = response.body();
                    for(Permission permission:data.data){
                        Log.i("Data ","Data: "+permission.permission+ " " +permission.status);

                    }
                }
                @Override
                public void onFailure(Call<Data> call, Throwable t) {
                    Log.i("Data","Fallo");
                }
            });*/
        //RevokeAccessGoogle(options);
    }
    public void SignOutGoogle(boolean Tipo_deslogeo,GoogleSignInOptions options){
        FirebaseAuth.getInstance().signOut();
        if(!Tipo_deslogeo){
            IrLoginActivity();
        }else{
            RevokeAccessGoogle(options);
        }
    }

    private void IrLoginActivity() {
        Intent ir_login = new Intent(main_user_activity,LoginActivity.class);
        main_user_activity.startActivity(ir_login);
        Toast.makeText(context,"Ádios..........",Toast.LENGTH_SHORT).show();
    }

    private void RevokeAccessGoogle(GoogleSignInOptions options) {
        GoogleSignInClient cliente = GoogleSignIn.getClient(context, options);
        cliente.revokeAccess().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                IrLoginActivity();
            }else{
                Toast.makeText(context,"No se pudo Descvincular su Cuenta",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
