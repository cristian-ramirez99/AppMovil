package com.example.sintesis.autenticado.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sintesis.ListaAdapter;
import com.example.sintesis.R;
import com.example.sintesis.RetrofitInterface;
import com.example.sintesis.auth.Login;
import com.example.sintesis.models.LineaPedido;
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
    LineaPedido lineaPedidos[];
    RecyclerView recyclerProductos;

    private Dialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private Button btnCancelar;
    private Button btnEliminarProducto;
    private TextView tvMensaje;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista;

        llenarLista();

        //Si el carrito esta vacio, carga el fragment de carrito_vacio
        if (lineaPedidos.length == 0) {
            vista = inflater.inflate(R.layout.fragment_carrito_vacio, container, false);

            //Si el carrito tiene productos, carga el fragment carrito
        } else {
            // Inflate the layout for this fragment
            vista = inflater.inflate(R.layout.fragment_carrito, container, false);

            recyclerProductos = (RecyclerView) vista.findViewById(R.id.rcProductosCarrito);
            recyclerProductos.setLayoutManager(new LinearLayoutManager(getContext()));

            ListaAdapter adapter = new ListaAdapter(lineaPedidos, new ListaAdapter.OnIconBasuraClickListener() {
                //OnClick del icono Basura abrimos modal para eliminar producto
                @Override
                public void onClick(LineaPedido lineaPedido) {
                    open_modal_eliminar_producto(lineaPedido.getProducto().getNombre());
                }
            });
            recyclerProductos.setAdapter(adapter);
        }

        //Instanciamos intent para obtener datos de anteriores intents
        Intent intent = getActivity().getIntent();

        //Obtenemos token del usuario
        token = intent.getStringExtra(Login.TOKEN);

        return vista;
    }

    private void llenarLista() {

    }

    private void getProductos() {
        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Crear interface
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //Hace peticion @Get(/productos)
        Call<LineaPedido[]> call = retrofitInterface.getLineaPedido(token, "12");

        call.enqueue(new Callback<LineaPedido[]>() {
            @Override
            public void onResponse(Call<LineaPedido[]> call, Response<LineaPedido[]> response) {
                if (response.code() == 200) {
                    lineaPedidos = response.body();

                } else if (response.code() == 404) {
                    String mensaje = getString(R.string.error_correo_no_existe);
                    Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LineaPedido[]> call, Throwable t) {
                //Si no se puede conectar al servidor
                String mensaje = getString(R.string.error_conexion_DB);
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarProducto() {
        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Crear interface
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //Hace peticion @Delete(/productos)
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

    private void open_modal_eliminar_producto(String nombreProducto) {
        dialogBuilder = new AlertDialog.Builder(getContext());

        //View del modal
        final View modalView = getLayoutInflater().inflate(R.layout.activity_modal_eliminar_producto, null);

        //View de los widgets del modal
        btnEliminarProducto = modalView.findViewById(R.id.btnEliminarProductoModalEliminarProducto);
        btnCancelar = modalView.findViewById(R.id.btnCancelarModalEliminarProducto);
        tvMensaje = modalView.findViewById(R.id.tvMensajeModalEliminarProducto);

        tvMensaje.append(" " + nombreProducto + "?");

        //Cerrar modal onClick
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Eliminamos el producto y cerramos modal
        btnEliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Producto eliminado", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        //Mostrar modal
        dialogBuilder.setView(modalView);
        dialog = dialogBuilder.create();
        dialog.show();
    }
}