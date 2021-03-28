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

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    private EditText etCorreo;
    private EditText etPassword;
    private Button btnRegistrar;
    private Button btnLogin;

    private Dialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private TextView tvMensaje;
    private Button btnAceptar;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000/";

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
        ////////////////////////////////////////////////dasddsadas
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //Btn onClick
        HashMap<String, String> map = new HashMap<>();

        map.put("email", correo);
        map.put("password", password);

        Call<LoginResult> call = retrofitInterface.executeLogin(map);

        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                //Si hago login correctamente, cambio de activity a dashboard
                if (response.code() == 200) {
                    change_activity_to_dashboard();

                    //Correo no es esta registrado
                } else if (response.code() == 404) {
                    String mensaje = getString(R.string.error_correo_no_existe);
                    open_modal(mensaje);

                    //Si correo no coincide con la password
                } else if (response.code() == 400) {
                    String mensaje = getString(R.string.error_password);
                    open_modal(mensaje);
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                String mensaje = getString(R.string.error_conexion_DB);
                open_modal(mensaje);
            }
        });
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