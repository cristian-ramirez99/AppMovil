package com.example.sintesis.autenticado.fragments;

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
import com.example.sintesis.models.Producto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class CarritoFragment extends Fragment {
    private final String BASE_URL = "http://10.0.2.2:3000/api/";
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    List<Producto> productos;
    RecyclerView recyclerProductos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_carrito, container, false);

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
}