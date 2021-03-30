package com.example.sintesis.autenticado.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sintesis.ListaAdapter;
import com.example.sintesis.R;
import com.example.sintesis.RetrofitInterface;
import com.example.sintesis.auth.Login;
import com.example.sintesis.models.Producto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarritoFragment extends Fragment {
    private final String BASE_URL = "http://10.0.2.2:3000/api/";
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    public String token;
    List<Producto> productos;
    RecyclerView recyclerProductos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_carrito, container, false);


        Intent intent = getActivity().getIntent();
        token = intent.getStringExtra(Login.TOKEN);

        recyclerProductos = (RecyclerView) vista.findViewById(R.id.rcProductosCarrito);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(getContext()));


        llenarLista();

        ListaAdapter adapter = new ListaAdapter(productos);
        recyclerProductos.setAdapter(adapter);

        return vista;
    }

    private void llenarLista() {
        productos = new ArrayList<>();
        productos.add(new Producto("Mouse Gayming", "Muy polivalente", 50.24, "no-image", "1233", 50));
        productos.add(new Producto("Ordenador Gayming", "Muy polivalente", 200, "no-image", "1234", 50));
        productos.add(new Producto("Monitor Gayming", "Muy polivalente", 119.99, "no-image", "1235", 50));
        productos.add(new Producto("Jesus Gayming", "Muy polivalente", 76, "no-image", "1236", 50));
        productos.add(new Producto("Manuel Gayming", "Muy polivalente", 32, "no-image", "1237", 50));
        productos.add(new Producto("Pepe Gayming", "Muy polivalente", 15, "no-image", "1238", 50));

    }

    private void getProductos() {
    }

    private void eliminarProducto() {
        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Crear interface
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //Hace peticion @POST(/login)
        Call<Void> call = retrofitInterface.deleteProducto(token, "12345");

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {


                } else if (response.code() == 404) {
                    String mensaje = getString(R.string.error_correo_no_existe);
                    //open_modal(mensaje);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //Si no se puede conectar al servidor
                String mensaje = getString(R.string.error_conexion_DB);
                //open_modal(mensaje);
            }
        });
    }
}