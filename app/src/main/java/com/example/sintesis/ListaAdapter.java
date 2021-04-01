package com.example.sintesis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sintesis.models.Producto;

import java.text.DecimalFormat;
import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolder> {
    private final String SIMBOLO_EURO = "\u20ac";
    private final String BASE_URL = "http://10.0.2.2:3000/api/";

    private List<Producto> productos;
    private ImageView ivBasura;


    final ListaAdapter.OnIconBasuraClickListener listener;

    //Creamos interface OnClick para poder gestionarlo en CarritoFragment
    public interface OnIconBasuraClickListener {
        void onClick(Producto producto);
    }

    public ListaAdapter(List<Producto> itemList, ListaAdapter.OnIconBasuraClickListener listener) {
        this.productos = itemList;

        //Pasamos el listener OnClick
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

            //Obtener referencia de los widgets
            iconImage = itemView.findViewById(R.id.ivProductoListaProductos);
            nombreYCantidad = itemView.findViewById(R.id.tvNombreYCantidadListaProductos);
            precioYPrecioTotal = itemView.findViewById(R.id.tvPrecioListaProductos);
        }

        void bindData(final Producto item) {
            //setImagenProducto
            String url = BASE_URL + "upload/usuarios/56487648-6690-4a8d-b80e-2665c5539578.png";
            Glide.with(itemView.getContext()).load(url).into(iconImage);

            //setNombreYCantidad
            nombreYCantidad.setText(item.getNombre() + " (" + item.cantidad + ")");

            //Calculamos el subtotal por producto
            Double precioTotalProducto = calcularPrecioTotalProducto(item.getPrecio(), item.cantidad);

            //Formato con dos decimales
            DecimalFormat format = new DecimalFormat("#.00");// el numero de ceros despues del entero
            String strPrecio = format.format(item.getPrecio());
            String strPrecioTotalProducto = format.format(precioTotalProducto);

            //setPrecioYPrecioTotal
            precioYPrecioTotal.setText(strPrecio + SIMBOLO_EURO + " - Total: " + strPrecioTotalProducto + SIMBOLO_EURO);

            //OnClick del ImageView que tiene icono de basura
            ivBasura.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(item);
                }
            });
        }
    }

    //Devuelve el precio * cantidad de un producto
    private double calcularPrecioTotalProducto(Double precio, int canitdad) {
        return precio * canitdad;
    }
}

