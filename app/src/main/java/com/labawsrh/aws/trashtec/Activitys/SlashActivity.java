package com.labawsrh.aws.trashtec.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.labawsrh.aws.trashtec.R;

public class SlashActivity extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_slash);


        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                IntroActivity();
            }
        },1000);
    }
    private void IntroActivity(){
        Intent intro_intent = new Intent(getApplicationContext(),IntroActivity.class);
        startActivity(intro_intent);
        finish();
    }
}
