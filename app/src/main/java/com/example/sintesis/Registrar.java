package com.example.sintesis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
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


    private Dialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private TextView tvMensaje;
    private Button btnAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        //Obtener referencia de los widgets
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
        //Obtener referencia de texto de los widgets
        String nombre = etNombre.getText().toString();
        String correo = etCorreo.getText().toString();
        String password = etPassword.getText().toString();
        String repetirPassword = etRepetirPassword.getText().toString();

        //Obtner si terminos checked
        boolean terminosAceptados = cbTerminos.isChecked();

        //Si campos vacios
        if (nombre.isEmpty() || correo.isEmpty() || password.isEmpty() || repetirPassword.isEmpty()) {
            String mensaje = getString(R.string.error_campos_vacios);
            open_modal(mensaje);
            return;
        }
        //Si las passwords no coinciden
        if (!password.equals(repetirPassword)) {
            String mensaje = getString(R.string.error_passwords_diferentes);
            open_modal(mensaje);
            return;
        }

        //Si no se han aceptado los terminos
        if (!terminosAceptados) {
            String mensaje = getString(R.string.error_terminos_sin_aceptar);
            open_modal(mensaje);
            return;
        }

        //Si se registra correctamente, cambio de activity a login
        change_activity_to_login();
    }

    private void change_activity_to_login() {
        Intent intent = new Intent(this, Login.class);
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