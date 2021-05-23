package com.example.sintesis.autenticado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sintesis.environments.Environments;
import com.example.sintesis.RetrofitInterface;
import com.example.sintesis.results.PedidoResult;
import com.example.sintesis.auth.Login;
import com.example.sintesis.R;
import com.example.sintesis.autenticado.fragments.CarritoFragment;
import com.example.sintesis.autenticado.fragments.QRFragment;
import com.example.sintesis.results.RenewResult;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dashboard extends AppCompatActivity {
    //Instanciamos fragments
    Fragment carritoFragment = new CarritoFragment();
    Fragment QRFragment = new QRFragment();

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    public String token;
    public String uid;
    public String idPedido;

    private TextView tvCorreo;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Instanciamos intent para obtener datos de anteriores intents
        Intent intent = getIntent();

        //Obtenemos el x-token del usuario
        token = intent.getStringExtra(Login.TOKEN);

        //Obtenemos el correo con el que se inicio sesion
        String correo = intent.getStringExtra(Login.CORREO);

        //Obtener referencia de los widgets
        tvCorreo = findViewById(R.id.tvCorreoDashboard);

        tvCorreo.setText(correo);


        //Navbar
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //peticion ("renew") para obtener uid del usuario
        renew(token);


        Handler handler = new Handler();

        /*Esperamos 0,5 segundos para que acabe la peticion renew y asi poder obtener el pedido
        temporal con el uid*/
        handler.postDelayed(new Runnable() {
            public void run() {
                getPedidoTemp();
            }
        }, 500);   //0.5 seconds


           /*Esperamos 1 segundo para que acabe la peticion getPedidoTemp. Si pedidoTemp no existe
             creamos nuevo pedido*/
        handler.postDelayed(new Runnable() {
            public void run() {
                if (idPedido == null) {
                    crearPedido();
                }
            }
        }, 1000);   //1 seconds

        handler.postDelayed(new Runnable() {
            public void run() {
                //Por default se carga QRFragment
                loadFragment(QRFragment);

            }
        }, 1500);//1.5 seconds
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        //Segun el item seleccionado en el navbar, inicia un fragment u otro
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.ic_qr:
                    loadFragment(QRFragment);
                    return true;

                case R.id.ic_carrito:
                    loadFragment(carritoFragment);
                    return true;

                case R.id.ic_signout:
                    change_activity_to_login();
                    return true;
            }
            return false;
        }
    };

    /*Hacemos peticion para obtener el carrito actual. En caso de que no existiera se mostraria
     un toast con el error*/
    private void getPedidoTemp() {
        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(Environments.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Crear interface
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //Hace peticion @GET(/pedidos/temp/{uid})
        Call<PedidoResult> call = retrofitInterface.getPedidoTemp(token, uid);

        call.enqueue(new Callback<PedidoResult>() {
            @Override
            public void onResponse(Call<PedidoResult> call, Response<PedidoResult> response) {
                if (response.code() == 200) {
                    try {
                        idPedido = response.body().getPedido().get_id();
                    } catch (Exception e) {
                    }

                }
            }

            @Override
            public void onFailure(Call<PedidoResult> call, Throwable t) {
                Toast.makeText(Dashboard.this, R.string.error_conexion_DB, Toast.LENGTH_SHORT).show();
                change_activity_to_login();
            }
        });
    }

    /*Hacemos peticion http para obtener el uid del usuario, en caso de fallo se mostraria toast
    con el mensaje de error*/
    private void renew(String token) {
        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(Environments.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Crear interface
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //Hace peticion @GET(/renew)
        Call<RenewResult> call = retrofitInterface.renew(token);

        call.enqueue(new Callback<RenewResult>() {
            @Override
            public void onResponse(Call<RenewResult> call, Response<RenewResult> response) {
                if (response.code() == 200) {
                    uid = response.body().getUsuario().getUid();
                }
            }

            @Override
            public void onFailure(Call<RenewResult> call, Throwable t) {
                Toast.makeText(Dashboard.this, R.string.error_conexion_DB, Toast.LENGTH_SHORT).show();
                change_activity_to_login();
            }
        });
    }

    /*Hacemos peticion http para crear pedido. Inicialmente no tendria lineaPedido asociada*/
    private void crearPedido() {
        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(Environments.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Crear interface
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        HashMap<String, String> map = new HashMap<>();
        map.put("estado", "temporal");
        map.put("usuario", uid);

        //Hace peticion @POST(/pedidos)
        Call<PedidoResult> call = retrofitInterface.crearPedido(token, map);

        call.enqueue(new Callback<PedidoResult>() {
            @Override
            public void onResponse(Call<PedidoResult> call, Response<PedidoResult> response) {
                if (response.code() == 200) {
                    idPedido = response.body().getPedido().get_id();
                }
            }

            @Override
            public void onFailure(Call<PedidoResult> call, Throwable t) {
                Toast.makeText(Dashboard.this, R.string.error_conexion_DB, Toast.LENGTH_SHORT).show();
                change_activity_to_login();

            }
        });
    }


    //Carga el fragment pasado por parametro
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Bundle args = new Bundle();
        args.putString("idPedido", idPedido);

        transaction.replace(R.id.frame_container, fragment);

        fragment.setArguments(args);

        transaction.commit();
    }

    //Inicia activity login
    private void change_activity_to_login() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}