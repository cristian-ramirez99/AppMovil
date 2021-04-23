package com.example.sintesis.info;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.sintesis.R;
import com.example.sintesis.autenticado.Dashboard;
import com.example.sintesis.auth.Login;
import com.example.sintesis.auth.Registrar;

public class Info extends AppCompatActivity {

    private Button btn_volver;
    private VideoView vvVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //Obtener referencia de los widgets
        btn_volver = findViewById(R.id.btnVolverInfo);

        iniciarVideo();

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_activity();
            }
        });
    }

    private void iniciarVideo() {
        vvVideo = findViewById(R.id.vvVideoInfo);

        String path = "android.resource://" + getPackageName() + "/" + R.raw.video_manual;
        Uri uri = Uri.parse(path);
        vvVideo.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        vvVideo.setMediaController(mediaController);
        mediaController.setAnchorView(vvVideo);
    }

    //Inicia activity dashboard
    private void change_activity() {
        String activity = getIntent().getStringExtra("activity");
        Intent intent;

        if (activity.equals("login")) {
            intent = new Intent(this, Login.class);

        } else {
            intent = new Intent(this, Registrar.class);

        }
        startActivity(intent);
    }
}