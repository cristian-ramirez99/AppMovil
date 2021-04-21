package com.example.sintesis.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sintesis.Info;
import com.example.sintesis.R;
import com.example.sintesis.RetrofitInterface;

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
    private ImageView ivInfo;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://yavadevs.herokuapp.com/api/";

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
        ivInfo = findViewById(R.id.ivInfoRegistrar);

        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_activity_to_info();
            }
        });

        //Registrar onClick
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });

        //Iniciar activity login onClick
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

    //Iniciar activity login
    private void change_activity_to_login() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    //Inicia activity info
    private void change_activity_to_info() {
        Intent intent = new Intent(this, Info.class);
        intent.putExtra("activity","registrar");

        startActivity(intent);
    }

    //Modal que muestra mensaje de error
    private void open_modal(String mensaje) {
        dialogBuilder = new AlertDialog.Builder(this);

        //View del modal
        final View modalView = getLayoutInflater().inflate(R.layout.activity_modal_error, null);

        //View de los widgets del modal
        btnAceptar = modalView.findViewById(R.id.btnAceptarModalError);
        tvMensaje = modalView.findViewById(R.id.tvMensajeModalError);

        //Cambiamos el mensaje
        tvMensaje.setText(mensaje);

        //Cerra modal onClick
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Mostrar el modal
        dialogBuilder.setView(modalView);
        dialog = dialogBuilder.create();
        dialog.show();
    }
}