package com.example.sintesis;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import com.example.sintesis.autenticado.Dashboard;
import com.example.sintesis.auth.Login;
import com.example.sintesis.auth.LoginResult;
import com.example.sintesis.models.Producto;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolder> {
    private List<Producto> productos;
    private ImageView ivBasura;

    public ListaAdapter(List<Producto> itemList) {
        this.productos = itemList;
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    @Override
    public ListaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_productos, null, false);

        ivBasura = view.findViewById(R.id.ivBasuraListaProductos);

        ivBasura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Eliminar producto", Toast.LENGTH_SHORT).show();

            }
        });

        return new ListaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListaAdapter.ViewHolder holder, final int position) {
        holder.bindData(productos.get(position));
    }

    public void setItems(List<Producto> items) {
        productos = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImage;
        TextView nombre, precio;

        ViewHolder(View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.ivProductoListaProductos);
            nombre = itemView.findViewById(R.id.tvNombreListaProductos);
            precio = itemView.findViewById(R.id.tvPrecioListaProductos);
        }

        void bindData(final Producto item) {
            nombre.setText(item.getNombre());
            precio.setText(String.valueOf(item.getPrecio() + "\u20ac"));
        }
    }
}

