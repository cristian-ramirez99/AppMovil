package com.example.sintesis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private EditText etCorreo;
    private EditText etPassword;
    private Button btnRegistrar;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Obtener referencia de la parte visual
        btnRegistrar = findViewById(R.id.btnRegistrarLogin);
        btnLogin = findViewById(R.id.btnLoginLogin);
        etCorreo = findViewById(R.id.etCorreoLogin);
        etPassword = findViewById(R.id.etPasswordLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_activity_to_registrar();
            }
        });
    }

    private void login() {
        String correo = etCorreo.getText().toString();
        String password = etPassword.getText().toString();

        if (correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Introduce todos los datos", Toast.LENGTH_SHORT).show();
            return;
        }

        //Si hago login correctamente, cambio de activit a dashboard
        change_activity_to_dashboard();
    }

    private void change_activity_to_registrar() {
        Intent intent = new Intent(this, Registrar.class);
        startActivity(intent);
    }

    private void change_activity_to_dashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }
}