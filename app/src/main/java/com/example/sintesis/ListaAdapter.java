package com.example.sintesis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.sintesis.models.Producto;

import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolder> {
    private List<Producto> productos;

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

