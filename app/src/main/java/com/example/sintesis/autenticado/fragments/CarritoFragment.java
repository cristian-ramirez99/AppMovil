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
import android.os.StrictMode;
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

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarritoFragment extends Fragment {
    private final String BASE_URL = "https://yavadevs.herokuapp.com/api/";
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

    private String idPedido;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Instanciamos intent para obtener datos de anteriores intents
        Intent intent = getActivity().getIntent();

        //Obtenemos token del usuario
        token = intent.getStringExtra(Login.TOKEN);

        Bundle args = getArguments();
        idPedido = args.getString("idPedido");

        try {
            getLineaPedidos();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    private void getLineaPedidos() throws IOException {
        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Crear interface
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //Hace peticion @Get(/lineaPedidos)
        Call<LineaPedidoResult> call = retrofitInterface.getLineaPedido(token, idPedido);

        lineaPedidos = call.execute().body().getLineaPedidos();
    }

    private void eliminarLineaPedido(String idLineaPedido) {
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

    private void actualizarStock(LineaPedido lineaPedido) {
        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Crear interface
        retrofitInterface = retrofit.create(RetrofitInterface.class);


        HashMap<String, Integer> map = new HashMap<>();
        int stock = lineaPedido.getCantidadad() + lineaPedido.getProducto().getStock();
        map.put("stock", stock);

        System.out.println("Stock: "+stock);
        System.out.println("IdProducto"+lineaPedido.getProducto().getId());

        //Hace peticion @PUT(/productos)
        Call<Void> call = retrofitInterface.actualizarStock(token, lineaPedido.getProducto().getId(), map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    System.out.println("Producto actualizado");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //Si no se puede conectar al servidor
                System.out.println(t.getMessage());
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
                //Peticion que elimina lineaPedido
                eliminarLineaPedido(lineaPedido.get_id());

                //Peticion actualizarStock
                actualizarStock(lineaPedido);

                try {
                    getLineaPedidos();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                actualizarFragment();
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
        ft.addToBackStack(null);
        ft.commit();
    }
}