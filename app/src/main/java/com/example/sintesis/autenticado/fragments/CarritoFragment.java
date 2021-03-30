package com.example.sintesis.autenticado.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sintesis.ListaAdapter;
import com.example.sintesis.ListaProductos;
import com.example.sintesis.R;
import com.example.sintesis.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class CarritoFragment extends Fragment {
    private final String BASE_URL = "http://10.0.2.2:3000/api/";
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    List<ListaProductos> productos;
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
        // init();

        return vista;
    }
    private void llenarLista(){
        productos = new ArrayList<>();
        productos.add(new ListaProductos("#121212", "Bobo", "Colombia", "Gay"));
        productos.add(new ListaProductos("#121212", "Bobo", "Colombia", "Gay"));
        productos.add(new ListaProductos("#121212", "Bobo", "Colombia", "Gay"));
        productos.add(new ListaProductos("#121212", "Bobo", "Colombia", "Gay"));
        productos.add(new ListaProductos("#121212", "Bobo", "Colombia", "Gay"));
        productos.add(new ListaProductos("#121212", "Bobo", "Colombia", "Gay"));
        productos.add(new ListaProductos("#121212", "Bobo", "Colombia", "Gay"));

    }
   /* public void init() {
        productos = new ArrayList<>();
        productos.add(new ListaProductos("#121212", "Bobo", "Colombia", "Gay"));
        productos.add(new ListaProductos("#121212", "Bobo", "Colombia", "Gay"));
        productos.add(new ListaProductos("#121212", "Bobo", "Colombia", "Gay"));
        productos.add(new ListaProductos("#121212", "Bobo", "Colombia", "Gay"));
        productos.add(new ListaProductos("#121212", "Bobo", "Colombia", "Gay"));
        productos.add(new ListaProductos("#121212", "Bobo", "Colombia", "Gay"));
        productos.add(new ListaProductos("#121212", "Bobo", "Colombia", "Gay"));

        ListaAdapter listaAdapter = new ListaAdapter(productos, getContext());
        RecyclerView recyclerView = getView().findViewById(R.id.rcProductosCarrito);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listaAdapter);
    } */

    private void getProductos() {
    }
}