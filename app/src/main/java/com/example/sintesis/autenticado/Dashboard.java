package com.example.sintesis.autenticado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sintesis.RetrofitInterface;
import com.example.sintesis.autenticado.fragments.PedidoResult;
import com.example.sintesis.auth.Login;
import com.example.sintesis.R;
import com.example.sintesis.autenticado.fragments.CarritoFragment;
import com.example.sintesis.autenticado.fragments.QRFragment;
import com.example.sintesis.auth.RenewResult;
import com.example.sintesis.models.LineaPedido;
import com.example.sintesis.models.Pedido;
import com.example.sintesis.models.Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private String BASE_URL = "http://10.0.2.2:3000/api/";

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

        //Por defualt se carga QRFragment
        loadFragment(QRFragment);

        //Navbar
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Peticion GET para obtener uid
        renew(token);

       // getPedidoTemp();
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

    private void getPedidoTemp() {
        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Crear interface
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //Hace peticion @GET(/pedidos/temp/{uid})
        Call<PedidoResult> call = retrofitInterface.getPedidoTemp(token, uid);

        System.out.println("Hola : " + uid);
        call.enqueue(new Callback<PedidoResult>() {
            @Override
            public void onResponse(Call<PedidoResult> call, Response<PedidoResult> response) {
                if (response.code() == 200) {
                    System.out.println(response.body().getPedido().get_id());
                    idPedido = response.body().getPedido().get_id();
                    Toast.makeText(Dashboard.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PedidoResult> call, Throwable t) {
                Toast.makeText(Dashboard.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void renew(String token) {
        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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
                    System.out.println("UID:"+uid);
                    Toast.makeText(Dashboard.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RenewResult> call, Throwable t) {
                Toast.makeText(Dashboard.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    //Carga el fragment pasado por parametro
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    //Inicia activity login
    private void change_activity_to_login() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}