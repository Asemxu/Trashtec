package com.labawsrh.aws.trashtec.Activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.AccessToken;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.Firebase.Autentificacion.Autentificacion;
import com.labawsrh.aws.trashtec.R;
import com.facebook.FacebookSdk;

import java.util.Arrays;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private CardView btn_login_facebook;
    private CardView btn_login_google;
    private GoogleSignInClient google_sign_client;
    private FirebaseAuth auth = Firebase_Variables.firebaseauth;
    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Autentificacion autentificacion;
    private ProgressDialog dialog;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        FacebookToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(), "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        setContentView(R.layout.activity_main);

        InstanciarViews();
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.default_web_client_id)).
                requestEmail().
                build();
        Press_Google(options);
        PressFacebook();
         authStateListener = firebaseAuth -> {
             FirebaseUser user = firebaseAuth.getCurrentUser();
             if (user != null)
                 autentificacion.UpdateUI(user);
             else
                 autentificacion.UpdateUI(null);
         };
    }

    private void PressFacebook() {
        btn_login_facebook.setOnClickListener(v -> {
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
        });
    }

    private void FacebookToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                FirebaseUser user = auth.getCurrentUser();
                autentificacion.Tipo_cuenta=false;
                autentificacion.UpdateUI(user);
            }
        });
    }


    private void Press_Google(final GoogleSignInOptions options) {
        btn_login_google.setOnClickListener(v -> {
            google_sign_client = GoogleSignIn.getClient(getApplicationContext(),options);
            LoginGoogle();
        });
    }

    private void LoginGoogle() {
        Intent google_intent = google_sign_client.getSignInIntent();
        startActivityForResult(google_intent, Constantes.RC_SIGN_IN);
    }

    private void InstanciarViews() {
        btn_login_facebook = findViewById(R.id.btn_login_facebook);
        btn_login_google =findViewById(R.id.btn_login_google);
        autentificacion = new Autentificacion(this,getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constantes.RC_SIGN_IN){
            Task<GoogleSignInAccount> task =GoogleSignIn.getSignedInAccountFromIntent(data);
            Autentificacion authentication_ = new Autentificacion(this,null,getApplicationContext(), auth, task);
            dialog = ProgressDialog.show(this,"Login","Logueando....",false,false);
            authentication_.Tipo_cuenta=true;
            authentication_.SigInAutentifacion(dialog);
        }
        if(callbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            auth.removeAuthStateListener(authStateListener);
        }
    }
}
