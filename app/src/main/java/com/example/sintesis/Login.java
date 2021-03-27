package com.example.sintesis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private EditText etCorreo;
    private EditText etPassword;
    private Button btnRegistrar;
    private Button btnLogin;

    private Dialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private TextView tvMensaje;
    private Button btnAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Obtener referencia de los widgets
        btnRegistrar = findViewById(R.id.btnRegistrarLogin);
        btnLogin = findViewById(R.id.btnLoginLogin);
        etCorreo = findViewById(R.id.etCorreoLogin);
        etPassword = findViewById(R.id.etPasswordLogin);

        //Hacer login onClick
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //Ir a la activity registrar onClick
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_activity_to_registrar();
            }
        });
    }

    private void login() {
        //Obtener referencia de texto de los widgets
        String correo = etCorreo.getText().toString();
        String password = etPassword.getText().toString();

        //Si hay campos vacios muestro modal
        if (correo.isEmpty() || password.isEmpty()) {
            String mensaje = getString(R.string.error_campos_vacios);
            open_modal(mensaje);
            return;
        }

        //Si hago login correctamente, cambio de activity a dashboard
        change_activity_to_dashboard();
    }

    //activity_login -> acivity_registrar
    private void change_activity_to_registrar() {
        Intent intent = new Intent(this, Registrar.class);
        startActivity(intent);
    }

    //activity_login -> acivity_dashboard
    private void change_activity_to_dashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }

    private void open_modal(String mensaje) {
        dialogBuilder = new AlertDialog.Builder(this);
        final View modalView = getLayoutInflater().inflate(R.layout.activity_error, null);

        //View de los widgets del modal
        btnAceptar = modalView.findViewById(R.id.btnAceptarError);
        tvMensaje = modalView.findViewById(R.id.tvMensajeError);

        //Cambiamos el mensaje
        tvMensaje.setText(mensaje);

        //Cerra modal onClick
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogBuilder.setView(modalView);
        dialog = dialogBuilder.create();
        dialog.show();
    }
}