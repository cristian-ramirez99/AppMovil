package com.example.sintesis.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sintesis.environments.Environments;
import com.example.sintesis.info.Info;
import com.example.sintesis.R;
import com.example.sintesis.RetrofitInterface;
import com.example.sintesis.autenticado.Dashboard;
import com.example.sintesis.results.LoginResult;

import java.io.IOException;
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
    private TextView tvPasswordOlvidada;
    private ImageView ivInfo;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    public static final String TOKEN = "com.example.sintesis_20.auth.TOKEN";
    public static final String CORREO = "com.example.sintesis_20.auth.CORREO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Obtener referencia de los widgets
        btnRegistrar = findViewById(R.id.btnRegistrarLogin);
        btnLogin = findViewById(R.id.btnLoginLogin);
        etCorreo = findViewById(R.id.etCorreoLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        ivInfo = findViewById(R.id.ivInfoLogin);
        tvPasswordOlvidada = findViewById(R.id.tvPasswordOlvidadaLogin);

        //OnClick en contraseña olvidada, abre el modal de contraseña olvidada
        tvPasswordOlvidada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_modal_password_olvidadada();
            }
        });

        //OnClick en el icono info cambia de activity a la guia del usuario
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_activity_to_info();
            }
        });

        //OnClick hacer login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camposValidos()) {
                    login();
                }
            }
        });

        //OnClick rr a la activity registrar
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_activity_to_registrar();
            }
        });
    }

    /*Comprueba si los campos a relleanar estan vacios. Si estan  vacios devuelve false, si todo ok
    devuelve true */
    private boolean camposValidos() {
        //Obtener referencia de texto de los widgets
        String correo = etCorreo.getText().toString();
        String password = etPassword.getText().toString();


        //Si hay campos vacios muestro modal
        if (correo.isEmpty() || password.isEmpty()) {
            String mensaje = getString(R.string.error_campos_vacios);
            open_modal(mensaje);
            return false;
        }
        return true;
    }

    /*Hace peticion HTTP para hacer login. Si login correcto va a dashboard, en caso contrario
    se muestra modal indicando el error*/
    private void login() {
        //Obtener referencia de texto de los widgets
        String correo = etCorreo.getText().toString();
        String password = etPassword.getText().toString();


        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(Environments.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Crear interface
        retrofitInterface = retrofit.create(RetrofitInterface.class);


        HashMap<String, String> map = new HashMap<>();

        //Body
        map.put("email", correo);
        map.put("password", password);

        //Hace peticion @POST(/login)
        Call<LoginResult> call = retrofitInterface.executeLogin(map);

        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                //Si hago login correctamente, cambio de activity a dashboard
                if (response.code() == 200) {
                    LoginResult loginResult = response.body();
                    change_activity_to_dashboard(loginResult.getToken(), correo);

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
                //Si no se puede conectar al servidor
                String mensaje = getString(R.string.error_conexion_DB);
                open_modal(mensaje);
            }
        });
    }


    //Inicia activity registrar
    private void change_activity_to_registrar() {
        Intent intent = new Intent(this, Registrar.class);
        startActivity(intent);
    }

    //Inicia activity info
    private void change_activity_to_info() {
        Intent intent = new Intent(this, Info.class);

        intent.putExtra("activity", "login");

        startActivity(intent);
    }

    //Inicia activity dashboard
    private void change_activity_to_dashboard(String token, String correo) {
        Intent intent = new Intent(this, Dashboard.class);
        intent.putExtra(TOKEN, token);
        intent.putExtra(CORREO, correo);
        startActivity(intent);
        finish();
    }

    private void open_modal_password_olvidadada() {
        Button btnAceptar;
        Button btnCancelar;
        EditText etCorreo;

        dialogBuilder = new AlertDialog.Builder(this);
        //View del modal
        final View modalView = getLayoutInflater().inflate(R.layout.activity_modal_password_olvidada, null);

        //View de los widgets del modal
        btnAceptar = modalView.findViewById(R.id.btnAceptarModalPasswordOlvidada);
        btnCancelar = modalView.findViewById(R.id.btnCancelarModalPasswordOlvidada);
        etCorreo = modalView.findViewById(R.id.etEmailModalPasswordOlvidada);

        //Cerra modal onClick
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etCorreo.getText().toString();
                try {
                    //Si el campo email no esta vacio
                    if (!email.isEmpty()) {
                        //Hacemos peticion para recuperar password
                        recuperarPassword(email);
                    }
                } catch (IOException e) {
                    Toast.makeText(Login.this, "Error al intentar recuperar la contraseña", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
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

    /*Envia correo con la nueva contraseña, siempre y cuando el correo este ya registrado en
     nuestra BBDD*/
    private void recuperarPassword(String email) throws IOException {
        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(Environments.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Crear interface
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        HashMap<String, String> map = new HashMap<>();

        //Body
        map.put("email", email);

        //Hace peticion para recuperar contraseña
        Call<Void> call = retrofitInterface.recuperarPassword(map);

        Toast.makeText(Login.this, "En su correo electronico tendra su nueva contraseña", Toast.LENGTH_SHORT).show();

        call.execute();
    }

    //Modal que muestra mensaje de error
    private void open_modal(String mensaje) {
        Button btnAceptar;

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