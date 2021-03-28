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

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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


    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000/api/";

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

        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Crear interface
        retrofitInterface = retrofit.create(RetrofitInterface.class);


        HashMap<String, String> map = new HashMap<>();

        //Body
        map.put("nombre", nombre);
        map.put("email", correo);
        map.put("password", password);

        //Hace peticion @POST(/usuarios)
        Call<Void> call = retrofitInterface.executeRegistrar(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //Si registro correctamente, cambio de activity a login
                if (response.code() == 200) {
                    change_activity_to_login();

                    //Correo no es esta registrado
                } else if (response.code() == 400) {
                    String mensaje = getString(R.string.error_correo_existe);
                    open_modal(mensaje);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //Si no se puede conectar al servidor
                String mensaje = getString(R.string.error_conexion_DB);
                open_modal(mensaje);
            }
        });
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