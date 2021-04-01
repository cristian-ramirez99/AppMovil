package com.example.sintesis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.sintesis.auth.Login;

public class MainActivity extends AppCompatActivity {
    private final long TIME_TO_CHANGE_ACTIVITY = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Espera unos segundos para cambiar de activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                change_activity_to_login();
                finish();
            }
        }, TIME_TO_CHANGE_ACTIVITY);
    }

    //Inicia activity login
    private void change_activity_to_login() {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }

}