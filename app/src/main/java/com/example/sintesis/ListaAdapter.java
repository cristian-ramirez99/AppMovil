package com.example.sintesis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.sintesis.models.Producto;

import java.text.DecimalFormat;
import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolder> {
    private final String SIMBOLO_EURO = "\u20ac";
    private List<Producto> productos;
    private ImageView ivBasura;

    final ListaAdapter.OnIconBasuraClickListener listener;

    public interface OnIconBasuraClickListener {
        void onClick(Producto producto);
    }

    public ListaAdapter(List<Producto> itemList, ListaAdapter.OnIconBasuraClickListener listener) {
        this.productos = itemList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    @Override
    public ListaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_productos, null, false);
        ivBasura = view.findViewById(R.id.ivBasuraListaProductos);

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
        TextView nombreYCantidad, precioYPrecioTotal;

        ViewHolder(View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.ivProductoListaProductos);
            nombreYCantidad = itemView.findViewById(R.id.tvNombreYCantidadListaProductos);
            precioYPrecioTotal = itemView.findViewById(R.id.tvPrecioListaProductos);
        }

        void bindData(final Producto item) {
            nombreYCantidad.setText(item.getNombre() + " (" + item.cantidad + ")");

            Double precioTotalProducto = calcularPrecioTotalProducto(item.getPrecio(), item.cantidad);

            //Formato con dos decimales
            DecimalFormat format = new DecimalFormat("#.00");// el numero de ceros despues del entero
            String strPrecio = format.format(item.getPrecio());
            String strPrecioTotalProducto = format.format(precioTotalProducto);

            precioYPrecioTotal.setText(strPrecio + SIMBOLO_EURO + " - Total: " + strPrecioTotalProducto + SIMBOLO_EURO);

            ivBasura.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(item);
                }
            });
        }
    }

    private double calcularPrecioTotalProducto(Double precio, int canitdad) {
        return precio * canitdad;
    }
}

