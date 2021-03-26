package com.example.sintesis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Registrar extends AppCompatActivity {
    private TextView txtvLogin;
    private EditText etNombre;
    private EditText etCorreo;
    private EditText etPassword;
    private EditText etRepetirPassword;
    private CheckBox cbTerminos;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        txtvLogin = findViewById(R.id.tvLoginRegistrar);
        etNombre = findViewById(R.id.etNombreRegistrar);
        etCorreo = findViewById(R.id.etCorreoRegistrar);
        etPassword = findViewById(R.id.etPasswordRegistrar);
        etRepetirPassword = findViewById(R.id.etRepetirPasswordRegistrar);
        cbTerminos = findViewById(R.id.cbTerminosRegistrar);
        btnRegistrar = findViewById(R.id.btnRegistrarRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });

        txtvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_activity_to_login();
            }
        });
    }

    private void registrar() {
        String nombre = etNombre.getText().toString();
        String correo = etCorreo.getText().toString();
        String password = etPassword.getText().toString();
        String repetirPassword = etRepetirPassword.getText().toString();
        boolean terminosAceptados = cbTerminos.isChecked();

        //Si campos vacios
        if (nombre.isEmpty() || correo.isEmpty() || password.isEmpty() || repetirPassword.isEmpty()) {
            Toast.makeText(this, "Introduce todos los datos", Toast.LENGTH_SHORT).show();
            return;
        }
        //Si las passwords no coinciden
        if (!password.equals(repetirPassword)) {
            Toast.makeText(this, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        //Si no se han aceptado los terminos
        if (!terminosAceptados) {
            Toast.makeText(this, "No has aceptado los terminos", Toast.LENGTH_SHORT).show();
            return;
        }

        //Si se registra correctamente, cambio de activity a login
        change_activity_to_login();
    }

    private void change_activity_to_login() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}