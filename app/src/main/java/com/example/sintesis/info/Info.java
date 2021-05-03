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

        prepararVideo();

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_activity();
            }
        });
    }

    /*Pasamos el url del video a reproducir y le ponemos los controles, para poder pausar o
     iniciar el video*/
    private void prepararVideo() {
        //Obtener referencia de los widgets
        vvVideo = findViewById(R.id.vvVideoInfo);

        //Obtenemos el path donde esta el video
        String path = "android.resource://" + getPackageName() + "/" + R.raw.video_manual;

        Uri uri = Uri.parse(path);

        //Le pasamos al widget el video a reproducir
        vvVideo.setVideoURI(uri);

        //Ponemos los controles del video para que el usuario lo gestioner (pausar o iniciar video)
        MediaController mediaController = new MediaController(this);
        vvVideo.setMediaController(mediaController);
        mediaController.setAnchorView(vvVideo);
    }

    //Inicia activity dashboard
    private void change_activity() {
        String activity = getIntent().getStringExtra("activity");
        Intent intent;

        //Si el usuario estaba anteriormente en la activity login
        if (activity.equals("login")) {
            intent = new Intent(this, Login.class);

            //Si el usuario estaba anteriormente en la activity registrar
        } else {
            intent = new Intent(this, Registrar.class);

        }
        startActivity(intent);
    }
}