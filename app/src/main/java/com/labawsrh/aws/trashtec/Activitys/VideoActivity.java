package com.labawsrh.aws.trashtec.Activitys;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.labawsrh.aws.trashtec.Clases_Helper.Constantes;
import com.labawsrh.aws.trashtec.R;

public class VideoActivity extends YouTubeBaseActivity {

    YouTubePlayerView video;
    YouTubePlayer.OnInitializedListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video);
        video = findViewById(R.id.video_conocimiento);
        final String codigo_video = getIntent().getStringExtra("Data");
        Toast.makeText(getApplicationContext(),"Presiona la Pantalla",Toast.LENGTH_SHORT).show();
        listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    youTubePlayer.loadVideo(codigo_video);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        video.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                video.initialize(Constantes.ApiKeyYoutube,listener);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        IrMainUserActivity();
    }

    private void IrMainUserActivity() {
        Intent main_user_intent = new Intent(getApplicationContext(),Main_User_Activity.class);
        startActivity(main_user_intent);
        finish();
    }
}
