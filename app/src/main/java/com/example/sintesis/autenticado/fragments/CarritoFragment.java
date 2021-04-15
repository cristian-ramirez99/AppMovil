package com.example.sintesis.autenticado.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sintesis.ListaAdapter;
import com.example.sintesis.R;
import com.example.sintesis.RetrofitInterface;
import com.example.sintesis.autenticado.Dashboard;
import com.example.sintesis.auth.Login;
import com.example.sintesis.models.LineaPedido;
import com.example.sintesis.models.Pedido;
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
    LineaPedido lineaPedidos[] = new LineaPedido[1];
    RecyclerView recyclerProductos;

    private Dialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private Button btnCancelar;
    private Button btnEliminarProducto;
    private TextView tvMensaje;

    private String idPedido;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista;
        llenarLista();

        Bundle args = getArguments();
        idPedido = args.getString("idPedido");

        getLineaPedidos();

        //Si el carrito esta vacio, carga el fragment de carrito_vacio
        if (lineaPedidos.length == 0) {
            vista = inflater.inflate(R.layout.fragment_carrito_vacio, container, false);

            //Si el carrito tiene productos, carga el fragment carrito
        } else {
            // Inflate the layout for this fragment
            vista = inflater.inflate(R.layout.fragment_carrito, container, false);


            //Instanciamos intent para obtener datos de anteriores intents
            Intent intent = getActivity().getIntent();

            //Obtenemos token del usuario
            token = intent.getStringExtra(Login.TOKEN);

            recyclerProductos = (RecyclerView) vista.findViewById(R.id.rcProductosCarrito);
            recyclerProductos.setLayoutManager(new LinearLayoutManager(getContext()));

            ListaAdapter adapter = new ListaAdapter(lineaPedidos, new ListaAdapter.OnIconBasuraClickListener() {
                //OnClick del icono Basura abrimos modal para eliminar producto
                @Override
                public void onClick(LineaPedido lineaPedido) {
                    open_modal_eliminar_producto(lineaPedido);
                }
            });
            recyclerProductos.setAdapter(adapter);
        }

        return vista;
    }

    private void llenarLista() {
        lineaPedidos[0] = new LineaPedido(2, new Producto("nombre", "descripcion", 20, "img", 20, "12"), "113s1", "12");
    }

    private void getLineaPedidos() {
        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Crear interface
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //Hace peticion @Get(/lineaPedidos)
        Call<LineaPedidoResult> call = retrofitInterface.getLineaPedido(token, idPedido);

        call.enqueue(new Callback<LineaPedidoResult>() {
            @Override
            public void onResponse(Call<LineaPedidoResult> call, Response<LineaPedidoResult> response) {
                if (response.code() == 200) {
                    System.out.println("Body: " + response.body());
                    System.out.println("LineaPedidos: " + response.body().getLineaPedidos().toString());
                    System.out.println("LineaPedidos: " + response.body().getLineaPedidos()[0].getProducto().getNombre());
                    System.out.println("LineaPedidos: " + response.body().getLineaPedidos().length);

                    lineaPedidos = response.body().getLineaPedidos();
                }
            }

            @Override
            public void onFailure(Call<LineaPedidoResult> call, Throwable t) {
                //Si no se puede conectar al servidor
                String mensaje = getString(R.string.error_conexion_DB);
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarProducto(String idLineaPedido) {
        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Crear interface
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        System.out.println("Delete: " + idLineaPedido);
        //Hace peticion @Delete(/lineaPedidos)
        Call<Void> call = retrofitInterface.deleteLineaPedido(token, idLineaPedido);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    System.out.println("Producto eliminado");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //Si no se puede conectar al servidor
                System.out.println(t.getMessage());
                String mensaje = getString(R.string.error_conexion_DB);
                //open_modal(mensaje);
            }
        });
    }

    private void open_modal_eliminar_producto(LineaPedido lineaPedido) {
        dialogBuilder = new AlertDialog.Builder(getContext());

        //View del modal
        final View modalView = getLayoutInflater().inflate(R.layout.activity_modal_eliminar_producto, null);

        //View de los widgets del modal
        btnEliminarProducto = modalView.findViewById(R.id.btnEliminarProductoModalEliminarProducto);
        btnCancelar = modalView.findViewById(R.id.btnCancelarModalEliminarProducto);
        tvMensaje = modalView.findViewById(R.id.tvMensajeModalEliminarProducto);

        tvMensaje.append(" " + lineaPedido.getProducto().getNombre() + "?");

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
                eliminarProducto(lineaPedido.get_id());

                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {
                        getLineaPedidos();
                    }
                }, 150);   //0.5 seconds
                handler.postDelayed(new Runnable() {
                    public void run() {
                        actualizarFragment();
                    }
                }, 300);   //0.5 seconds
                dialog.dismiss();
            }
        });

        //Mostrar modal
        dialogBuilder.setView(modalView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void actualizarFragment() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        Fragment newFragment = this;
        this.onDestroy();
        ft.remove(this);
        ft.replace(R.id.frame_container, newFragment);
        //container is the ViewGroup of current fragment
        ft.addToBackStack(null);
        ft.commit();
    }
}